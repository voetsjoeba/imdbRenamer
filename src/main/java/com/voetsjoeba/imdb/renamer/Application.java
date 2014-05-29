package com.voetsjoeba.imdb.renamer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;

import net.jcip.annotations.GuardedBy;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.ImdbSearchResults;
import com.voetsjoeba.imdb.ImdbSearcher;
import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.domain.SeasonEpisodeNumber;
import com.voetsjoeba.imdb.domain.StringSearchable;
import com.voetsjoeba.imdb.domain.api.Searchable;
import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.domain.FileRenamer;
import com.voetsjoeba.imdb.renamer.domain.InPlaceSeriesRenamer;
import com.voetsjoeba.imdb.renamer.domain.analysis.FilenameInfo;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.event.PrefetchListener;
import com.voetsjoeba.imdb.renamer.event.PrefetchProgressEvent;
import com.voetsjoeba.imdb.renamer.gui.FileChooserManager;
import com.voetsjoeba.imdb.renamer.gui.MainWindow;
import com.voetsjoeba.imdb.renamer.gui.SwingFileChooserManager;
import com.voetsjoeba.imdb.renamer.model.FilesModel;
import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;
import com.voetsjoeba.imdb.renamer.model.SearchResultsModel;
import com.voetsjoeba.imdb.renamer.model.TitleModel;
import com.voetsjoeba.imdb.renamer.registry.DefaultLimitedTitleRegistry;
import com.voetsjoeba.imdb.renamer.registry.LimitedTitleRegistry;
import com.voetsjoeba.imdb.renamer.util.CrashReportDialogExceptionHandler;
import com.voetsjoeba.imdb.util.ListenerUtils;

/**
 * Main class of the IMDb Renamer application. Renames files according to a specified {@link Series}.
 * 
 * @author Jeroen De Ridder
 */
public class Application {
	
	// TODO: find subtitles and rename those as well?
	// TODO: provide a way to save logs?
	// TODO: allow for Unknown Seasons (eg. "something something something dark side" for family guy)
	// TODO: meerdere episodes toekennen aan 1 file (bv voor avatar last airbender, cfr Karel)
	
	protected static final Logger log = LoggerFactory.getLogger(Application.class);	
	protected static final Options options;
	protected static Application instance;
	
	static {
		options = new Options();
		options.addOption(null, "no-prefetch", false, "Disables prefetching of recently used titles.");
	}
	
	protected TitleModel titleModel;
	protected FilesModel filesModel;
	protected SearchResultsModel searchResultsModel;
	protected RenameFormatModel renameFormatModel;
	
	protected MainWindow mainWindow;
	protected FileChooserManager fileChooserManager;
	
	protected Map<String, Title> resultCache;
	protected LimitedTitleRegistry ltdTitleRegistry;
	protected File ltdTitleRegistryFile;
	
	protected FileRenamer renamer;
	protected ImdbSearcher imdbSearcher;
	protected EventListenerList listeners;
	
	protected int prefetchTotal = 0;
	protected volatile int prefetchCompleted = 0;
	
	private Application() {
		// no outside instantiation
		resultCache = new HashMap<String, Title>();
		imdbSearcher = new ImdbSearcher();
		listeners = new EventListenerList();
		
		ltdTitleRegistryFile = new File("./titleRegistry.dat");
		
	}
	
	private void initialize(CommandLine cmdLine){
		
		// do not call in constructor! model constructors may fetch application instances from Application.getInstance() 
		// causing an infinite constructor loop
		
		// cmdLine is usually never null, but WindowBuilder passes it null in design view which causes an NPE otherwise
		loadLimitedTitleRegistry(cmdLine == null ? true : !cmdLine.hasOption("no-prefetch"));
		
		renamer = new InPlaceSeriesRenamer();
		
		renameFormatModel = new RenameFormatModel();
		titleModel = new TitleModel();
		filesModel = new FilesModel();
		searchResultsModel = new SearchResultsModel();
		
		log.debug("Constructing GUI");
		mainWindow = new MainWindow();
		fileChooserManager = new SwingFileChooserManager();
		
	}
	
	protected void loadLimitedTitleRegistry(boolean doPrefetch){
		
		ltdTitleRegistry = new DefaultLimitedTitleRegistry();
		
		if(ltdTitleRegistryFile.exists() && ltdTitleRegistryFile.canRead() && ltdTitleRegistryFile.canWrite()){
			
			try {
				log.debug("Loading titles from limited title registry");
				ltdTitleRegistry.read(ltdTitleRegistryFile);
				
				// load succeeded -- start prefetching the most recently used entries
				if(doPrefetch) prefetchLimitedTitleRegistry();
				
			}
			catch(IOException e) {
				log.error("Could not read cache from " + ltdTitleRegistryFile.getName() + ": " + e.toString());
			}
			
		} else {
			log.debug("Limited title registry file does not exist or is unreadable");
		}
		
	}
	
	/**
	 * Prefetches some titles from the limited title registry for faster access. Employs multiple prefetcher
	 * threads to simultaneously fetch multiple titles' info.
	 */
	protected void prefetchLimitedTitleRegistry() {
		
		// TODO: prefetch only recently used entries and not the entire registry
		
		int numPools = 4; // amount of threads (and hence title pools)
		final List<List<LimitedTitle>> ltdTitlePools = new ArrayList<List<LimitedTitle>>(numPools);
		for(int i=0; i<numPools; i++) ltdTitlePools.add(new ArrayList<LimitedTitle>());
		
		final Collection<LimitedTitle> prefetchCollection = Collections.unmodifiableCollection(ltdTitleRegistry.getTitles());
		prefetchTotal = prefetchCollection.size();
		
		int i = 0;
		for(LimitedTitle title : prefetchCollection){
			
			int poolIndex = i % numPools;
			List<LimitedTitle> pool = ltdTitlePools.get(poolIndex);
			pool.add(title);
			
			i++;
		}
		
		final PrefetchProgressEvent prefetchProgressEvent = new PrefetchProgressEvent(this);
		firePrefetchProgress(prefetchProgressEvent); // initial fire to let listeners know that we're at 0
		
		for(int t=0; t<numPools; t++){
			
			final int poolIndex = t;
			
			Runnable r = new Runnable(){
				@Override public void run() {
					
					for(LimitedTitle title : ltdTitlePools.get(poolIndex)){
						
						log.debug("Prefetching \"{}\" (ID {})", title.getTitle(), title.getId());
						try {
							search(title);
						}
						// we don't really care if this fails, it's just prefetching anyway
						catch(IOException e) {
							log.error(e.getMessage(), e);
						}
						catch(HttpException e) {
							log.error(e.getMessage(), e);
						}
						
						prefetchCompleted++;
						firePrefetchProgress(prefetchProgressEvent); // reuse existing event object, doesn't contain anything useful anyway
						
					}
					
					log.debug("Done prefetching.");
					
				}
			};
			
			log.debug("Starting prefetcher thread {}", t+1);
			new Thread(r, "PF" + t).start();
			
		}
		
	}
	
	public int getPrefetchCompleted(){
		return prefetchCompleted; // volatile, always up to date
	}
	
	public int getPrefetchTotal() {
		return prefetchTotal;
	}

	protected void firePrefetchProgress(PrefetchProgressEvent prefetchProgressEvent) {
		for(PrefetchListener l : listeners.getListeners(PrefetchListener.class)){
			try {
				l.prefetchProgress(prefetchProgressEvent);
			}
			catch(RuntimeException rex){
				ListenerUtils.handleListenerException(rex, l);
			}
		}
	}
	
	public void addPrefetchListener(PrefetchListener l){
		listeners.add(PrefetchListener.class, l);
	}
	
	public void removePrefetchListener(PrefetchListener l){
		listeners.remove(PrefetchListener.class, l);
	}

	public void start(){
		log.debug("Starting GUI");
		mainWindow.setVisible(true);
	}
	
	public SearchResultsModel getSearchResultsModel(){
		return searchResultsModel;
	}
	
	/**
	 * Returns this application's {@link TitleModel} instance.
	 */
	public TitleModel getTitleModel() {
		return titleModel;
	}
	
	public FilesModel getFilesModel() {
		return filesModel;
	}
	
	public RenameFormatModel getRenameFormatModel(){
		return renameFormatModel;
	}
	
	/**
	 * Returns the application's main window.
	 */
	public JFrame getMainWindow() {
		return mainWindow;
	}
	
	/**
	 * Returns the {@link LimitedTitleRegistry} instance used by this application.
	 */
	public LimitedTitleRegistry getTitleRegistry(){
		return ltdTitleRegistry;
	}
	
	public FileRenamer getRenamer(){
		return renamer;
	}
	
	/* ---------------------------------------------------- */
	
	/**
	 * Returns the name of this application.
	 */
	public String getName(){
		return "IMDb Renamer";
	}
	
	/* ---------------------------------------------------- */
	
	/**
	 * Prompts the user to select one or more files, and adds the selected files to the {@link FilesModel}.
	 */
	public void openFiles(){
		
		File[] selectedFiles = fileChooserManager.selectFiles();
		for(File selectedFile : selectedFiles) filesModel.add(selectedFile);
		
	}
	
	/**
	 * Prompts the user to select a directory, and adds the selected folder to the {@link FilesModel}.
	 */
	public void openFolder(){
		
		File selectedFolder = fileChooserManager.selectFolder();
		if(selectedFolder == null) return;
		
		filesModel.add(selectedFolder);
		
	}
	
	/**
	 * Terminates the application. Performs some shutdown handling before actually exiting.
	 */
	public void exit(){
		
		log.debug("Exiting ...");
		
		try {
			
			if(ltdTitleRegistry.isDirty()){
				log.debug("Writing dirty limited title registry to \"{}\" ...", ltdTitleRegistryFile);
				ltdTitleRegistry.write(ltdTitleRegistryFile);
				log.debug("Limited title registry saved.");
			} else {
				log.debug("Limited title registry is clean, no writeback necessary");
			}
			
			mainWindow.setVisible(false);
			
		}
		catch(IOException e) {
			// shouldn't happen
			log.error("Could not write title registry to " + ltdTitleRegistryFile.getName() + ": " + e.toString());
		}
		finally {
			System.exit(0);
		}
		
	}
	
	public void log(String format, Object... params){
		String message = String.format(format, params);
		log.info(message);
	}
	
	/**
	 * Displays an error message dialog to the user.
	 */
	public void error(String format, Object... params){
		String message = String.format(format, params);
		log.error(message);
		JOptionPane.showMessageDialog(mainWindow, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Looks up a title on IMDb, and enters results into the limited title registry and the result cache where applicable so that
	 * future lookups of the same title can happen more quickly/conveniently.
	 * 
	 * @return the {@link ImdbSearchResults} instance produced by the lookup.
	 */
	protected ImdbSearchResults search(Searchable searchable) throws IOException, HttpException {
		
		if(searchable == null) return null;
		
		String searchTerm = StringUtils.trimToNull(searchable.getSearchTerm());
		if(searchTerm == null) return null;
		
		// ----------------------------------------------------------------------------------------------
		// the search term could be a proper ID -- look it up in the full result cache to see if we have anything on it
		
		Title resultCacheMatch = null;
		synchronized(resultCache){
			resultCacheMatch = resultCache.get(searchTerm);
		}
		
		if(resultCacheMatch != null){
			//log.debug("Found \"{}\" in result cache, reusing existing Title", searchTerm);
			return new ImdbSearchResults(resultCacheMatch);
		}
		
		// ----------------------------------------------------------------------------------------------
		
		// TODO: prevent multiple threads from loading the same entry multiple times
		log("Searching for \"" + searchTerm + "\" ...");
		ImdbSearchResults results = imdbSearcher.search(searchTerm);
		
		if(results.isExactMatch()){
			
			Title match = (Title) results.getMatches().get(0);
			log("Found exact match: \"" + match.getTitle() + "\"");
			
			synchronized(resultCache){
				log.debug("Entering exact match into result cache");
				resultCache.put(match.getId(), match);
				log.debug("{}", resultCache);
			}
			synchronized(ltdTitleRegistry){
				ltdTitleRegistry.add(new LimitedTitle(match));
			}
			
		} else {
			
			@SuppressWarnings("rawtypes")
			final List limitedTitles = results.getMatches();
			log("Found " + limitedTitles.size() + " (limited) search result(s).");
			
			// if there's only a single search result, do a new search for its id instead to get an exact match
			if(limitedTitles.size() == 1){
				
				LimitedTitle singleResult = (LimitedTitle) limitedTitles.get(0);
				results = search(singleResult); // TODO: add an extra argument to signify that this is a recursive call, and that it should produce a Title result (otherwise we'd have an infinite loop)
			
			} else {
				
				// TODO: now we have a whole bunch of titles and their registered IDs, also the ones that the user didn't want
				// these are a goldmine of data for the limited title registry, but right now the limited title registry is being
				// used as an equivalent of "most recently used" entries, so adding them would clutter that up.
				// eventually though, the limited title registry should end up containing as many title/id pairs as possible, so the
				// MRU list should be kept separately
				
			}
			
		}
		
		return results;
		
	}
	
	/**
	 * Convenience method for {@link #searchAndLoad(Searchable)}. Wraps the argument in a {@link StringSearchable}.
	 */
	public void searchAndLoad(String searchTerm){
		searchAndLoad(new StringSearchable(searchTerm));
	}
	
	/**
	 * <p>Looks up a title on IMDb via this Application's ImdbSearcher object, and applies the result to either the TitleModel
	 * or the SearchResultsModel.</p>
	 * 
	 * <p>Before a request is sent out to IMDb, the result cache is checked first to see if the search term already has a Title
	 * associated with it. If so, it is reused from cache. Otherwise, a new lookup is performed and.</p>
	 * 
	 * <p>If the result of a new lookup is a full Title, the newly fetched Title is saved:
	 *   <ul>
	 *     <li>in the result cache, for future access within this session</li>
	 *     <li>in the limited title registry, to prevent future manual lookups by name and to have quick access to it on future sessions</li>
	 *   </ul>
	 * 
	 * Otherwise, if it is a list of (limited) search results, the list of possible matches is set in the SearchResultsModel.</p>
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void searchAndLoad(Searchable searchable){
		
		try {
			
			ImdbSearchResults searchResults = search(searchable);
			if(searchResults == null){
				log.warn("search() produced no ImdbSearchResults -- unless the user has entered empty search terms, this shouldn't happen");
				return;
			}
			
			if(searchResults.isExactMatch()){
				//log.debug("Loading exact match into TitleModel");
				titleModel.setTitle(searchResults.getExactMatch());
			} else {
				//log.debug("Loading search results into SearchResultsModel");
				//log.debug("{}", searchResults.getMatches());
				searchResultsModel.setSearchResults((List) searchResults.getMatches());
			}
			
		}
		catch(IOException e) {
			log.debug(e.getMessage(), e);
		}
		catch(HttpException e) {
			log.debug(e.getMessage(), e);
		}
		
	}
	
	/* --------------------- RENAMING --------------------- */
	
	/**
	 * Returns true if a valid episode number could be extracted from <i>file</i> according to <i>title</i>, and if
	 * <i>file</i> requires a renaming operation for it to conform (i.e. it does not have the filename or location we require), false otherwise.
	 */
	public boolean isProperRenameCandidate(File file, Title title){
		
		boolean result = false;
		
		try {
			File renamedFile = renamer.getRenamedFile(file, title); // TODO: keep a central mapping of renamed files or something so that we don't have to execute this all the time
			if(!file.equals(renamedFile)) result = true;
		}
		catch(RenamingException rex){
			// result remains false
		}
		
		return result;
		
	}
	
	/**
	 * Returns true if a valid episode number could be extracted from <i>file</i> according to <i>title</i>, but if
	 * <i>file</i> is already at the location it would have otherwise been renamed to.
	 */
	public boolean isUnaffectedRenameCandidate(File file, Title title){
		
		File renamedFile = null;
		
		try {
			renamedFile = renamer.getRenamedFile(file, title);
		}
		catch(RenamingException rex) {
			// renamedFile remains null
		}
		
		/*if(renamedFile != null && renamedFile.equals(file)){
			// <file> is unaffected, remove it
			removeSet.add(file);
		}*/
		
		return (renamedFile != null && renamedFile.equals(file));
		
	}
	
	/**
	 * Returns true if <i>file</i> has a valid mapping against <i>title</i>, i.e. if a season and episode number can be extracted
	 * from <i>file</i> and if the extracted S/E number exists within <i>title</i>.
	 */
	public boolean hasValidMapping(File file, Series title){
		
		/*try {
			renamer.getRenamedFile(selectedFile, selectedTitle);
		}
		catch(NoSuchEpisodeException nseex){
			assignationRequired = true;
		}
		catch(NoEpisodeMappingException nemex){
			assignationRequired = true;
		}
		catch(RenamingException rex) {
			
		}*/
		
		FilenameInfo fileInfo = renamer.getFilenameAnalyzer().getFilenameInfo(file);
		if(fileInfo == null) return false;
		
		SeasonEpisodeNumber seNumber = fileInfo.getSeasonEpisodeNumber();
		if(seNumber == null) return false;
		
		return title.hasSeasonEpisode(seNumber);
		
	}
	
	/**
	 * Renames the provided collection of files.
	 * @param files The collection of files to rename. Any files not present in the file model are ignored. Set to null to indicate all files currently present in the file model.
	 */
	public void performRename(Collection<File> files){
		
		Title selectedTitle = titleModel.getTitle();
		if(selectedTitle == null) return;
		
		// TODO: ew
		if(!(selectedTitle instanceof Series)){
			throw new UnsupportedOperationException("Can only work with objects of type Series");
		}
		
		Set<File> successfullyRenamedFiles = new HashSet<File>();
		
		Iterator<File> filesIterator = filesModel.iterator();
		if(files != null) filesIterator = files.iterator();
		
		//for(File selectedFile : filesModel){
		while(filesIterator.hasNext()){
			
			File selectedFile = filesIterator.next();
			if(!filesModel.contains(selectedFile)){
				log("Skipped renaming of \"%s\", not in files list", selectedFile);
				continue; // process only files present in the file list
			}
			
			try {
				
				File destinationFile = renamer.getRenamedFile(selectedFile, selectedTitle);
				if(destinationFile == null) continue;
				
				//log("Gonna rename " + selectedFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
				try {
					
					selectedFile.renameTo(destinationFile);
					
					log("Renamed %s to %s", selectedFile, destinationFile);
					successfullyRenamedFiles.add(selectedFile);
					
				}
				catch(SecurityException ioex) {
					
				}
				
			}
			catch(UnsupportedOperationException usoex){
				// shouldn't happen, we made sure selectedTitle is an instance of Series
			}
			catch(RenamingException rex){
				// nothing happens, continue
			}
			
		}
		
		for(File successfullyRenamedFile : successfullyRenamedFiles){
			filesModel.remove(successfullyRenamedFile);
		}
		
	}
	
	/* --------------------- STATIC --------------------- */
	
	/**
	 * Returns the current Application instance, or creates one if no instance exists. This is the main interaction point
	 * for other classes to reach {@link Application}'s functionality.
	 */
	public static Application getInstance(){
		return getInstance(null);
	}
	
	/**
	 * <p>Creates a new Application using the given arguments if none exists; returns the existing one otherwise.</p>
	 * 
	 * <p>This method is thread-safe and will block out any other threads from acquiring the instance until it is fully
	 * constructed.</p>
	 */
	@GuardedBy("Application.class")
	public synchronized static Application getInstance(CommandLine cmdLine){
		
		if(instance == null){
			instance = new Application();
			instance.initialize(cmdLine);
		}
		
		return instance;
		
	}
	
	// TODO: credit JDownloader for swing properties
	public static void main(String[] args){
		
		CommandLine cmdLine = null;
		Application application = null;
		
		try {
			
			// -----------------------------------------------------------------------
			// parse command line options (TODO: these should become configuration options in the future)
			
			CommandLineParser cmdLineParser = new PosixParser();
			try {
				cmdLine = cmdLineParser.parse(options, args);
			}
			catch(ParseException pex){
				System.err.println("Error parsing arguments: " + pex.getMessage());
				
				HelpFormatter helpFormatter = new HelpFormatter();
				helpFormatter.printHelp("imdbRenamer", options); // TODO: what to use as the name of the app? :/
				
				System.exit(1);
			}
			
			// -----------------------------------------------------------------------
			// tweak some UI settings
			
			initUiProperties();
			
			// -----------------------------------------------------------------------
			// initialize the application (creates the GUI, sets up models, the whole shebang)
			
			application = Application.getInstance(cmdLine);
			
			// -----------------------------------------------------------------------
			
			/**
			 * Set uncaught exception handlers. These handlers will display an error report dialog to the user if
			 * an uncaught exception occurs in any thread. For this reason though, these handlers can only be used
			 * after the MainWindow has been instantiated because the dialog needs a parent window in order to not 
			 * "pop under" the other windows.
			 */
			
			// set our uncaught exception handler as the default for all new threads
			Thread.setDefaultUncaughtExceptionHandler(new CrashReportDialogExceptionHandler());
			
			// set our uncaught exception handler as the handler for swing EDT exceptions
			System.setProperty("sun.awt.exception.handler", CrashReportDialogExceptionHandler.class.getName());
			
		}
		catch(RuntimeException rex){
			// exception handlers not yet installed, should use a different error reporting method
			JOptionPane.showMessageDialog(
				null,
				"An unexpected problem occurred -- the application could not be started.\nThe problem is: " + rex.getClass().getCanonicalName() + ": " + rex.getMessage(),
				"Unexpected Problem",
				JOptionPane.ERROR_MESSAGE
			);
			log.error(rex.getMessage(), rex);
			System.exit(1);
		}
		
		// -----------------------------------------------------------------------
		// add initial files/directories to the models
		
		@SuppressWarnings("unchecked")
		List<String> filePaths = cmdLine.getArgList();
		FilesModel filesModel = application.getFilesModel();
		
		for(String filePath : filePaths){
			File file = new File(filePath);
			filesModel.add(file);
		} 
		
		
		// -----------------------------------------------------------------------
		
		//if(true) throw new RuntimeException("lol exception XD");
		application.start();
		
	}
	
	/**
     * Sets special Properties for MAC.
     * Lifted from JDownloader
     * TODO: GPL stuff
     */
	private static void initUiProperties() {
		
		// set system L&F instead of fugly default swing
		String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(systemLookAndFeelClassName);
		}
		catch(Exception ex){
			log.warn("Failed to set system look and feel {}", systemLookAndFeelClassName);
		}
		
		
		// enable text anti-aliasing globally (since JDK 1.5)
		System.getProperties().put("swing.aatext", true);

        // in default swing, you first need to select an item before you are
        // able to drag it around; this setting changes that so that you can
        // immediately start dragging unselected items
        System.setProperty("sun.swing.enableImprovedDragGesture", "true");
        
        // only use ipv4, because debian changed default stack to ipv6
        System.setProperty("java.net.preferIPv4Stack", "true");
        
        // Disable the GUI rendering on the graphic card
        System.setProperty("sun.java2d.d3d", "false");
        
        // -------------------------------------------------------------------------------------
        // Mac OS X specific
		
		// set DockIcon (most used in Building)
		/*try {
			com.apple.eawt.Application.getApplication().setDockIconImage(Images.getApplicationIcon().getImage());
		}
		catch(final Throwable e) {
			log.info("Could not set Mac OS dock icon: {}", e.getMessage()); // not every mac has this
		}*/
		
		// Use ScreenMenu in every LAF
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		// native Mac just if User Choose Aqua as Skin
		// Mac Java from 1.3
		System.setProperty("com.apple.macos.useScreenMenuBar", "true");
		// prevent the grow box (resize drag area on the bottom right) from overlapping window components
		System.setProperty("com.apple.mrj.application.growbox.intrudes", "true");
		System.setProperty("com.apple.hwaccel", "true");
		
		// Mac Java from 1.4
		System.setProperty("apple.awt.showGrowBox", "true");
		
		
	}
	
}
