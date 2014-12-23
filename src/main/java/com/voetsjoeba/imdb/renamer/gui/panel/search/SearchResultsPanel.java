package com.voetsjoeba.imdb.renamer.gui.panel.search;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.domain.api.BaseTitle;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.SearchResultsListener;
import com.voetsjoeba.imdb.renamer.event.SearchResultsSetEvent;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.generic.EnabledStatusAction;
import com.voetsjoeba.imdb.renamer.model.SearchResultsModel;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Displays the search results.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class SearchResultsPanel extends JPanel implements SearchResultsListener {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SearchResultsPanel.class);
	
	private JScrollPane resultsListScrollPane;
	private JList<LimitedTitle> resultsList;
	private JPanel resultsListButtonsPanel;
	private JButton loadButton;
	
	protected TitleModel titleModel;
	protected SearchResultsModel searchResultsModel;
	protected SearchResultsListModel resultsListModel;
	
	protected LoadSearchResultAction loadAction;
	
	/**
	 * Generic label for displaying any status text.
	 */
	protected JLabel statusLabel;
	protected JPanel statusLinePanel;
	
	//protected BaseTitle selectedTitle;
	
	public SearchResultsPanel() {
		
		searchResultsModel = Application.getInstance().getSearchResultsModel();
		titleModel = Application.getInstance().getTitleModel();
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	protected void initComponents() {
		
		resultsListModel = new SearchResultsListModel();
		
		resultsList = new JList<LimitedTitle>(resultsListModel);
		resultsListScrollPane = new JScrollPane();
		resultsListScrollPane.setPreferredSize(new Dimension(2, 150));
		resultsListScrollPane.setMinimumSize(new Dimension(23, 150));
		resultsListScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		
		resultsListButtonsPanel = new JPanel();
		
		loadAction = new LoadSearchResultAction();
		loadButton = new JButton(loadAction);
		
		statusLinePanel = new JPanel();
		statusLabel = new JLabel();
		
	}
	
	protected void initLayout() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};
		
		//======== chooseListScrollPane ========
		{
			resultsListScrollPane.setViewportView(resultsList);
		}
		add(resultsListScrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
		GridBagLayout gbl_resultsListButtonsPanel = new GridBagLayout();
		gbl_resultsListButtonsPanel.columnWidths = new int[]{33, 0, 0};
		gbl_resultsListButtonsPanel.rowHeights = new int[]{9, 0};
		gbl_resultsListButtonsPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_resultsListButtonsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		resultsListButtonsPanel.setLayout(gbl_resultsListButtonsPanel);
		add(resultsListButtonsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		
		//======== chooseListButtonsPanel ========
		{
			
			//---- acceptButton ----
			/*loadButton.setText("Accept");
			loadButton.setMnemonic('A');*/
			GridBagConstraints gbc_loadButton = new GridBagConstraints();
			gbc_loadButton.insets = new Insets(0, 0, 0, 5);
			gbc_loadButton.anchor = GridBagConstraints.NORTHWEST;
			gbc_loadButton.gridx = 0;
			gbc_loadButton.gridy = 0;
			resultsListButtonsPanel.add(loadButton, gbc_loadButton);
		}
		
		GridBagConstraints gbc_statusLinePanel = new GridBagConstraints();
		gbc_statusLinePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusLinePanel.gridx = 1;
		gbc_statusLinePanel.gridy = 0;
		resultsListButtonsPanel.add(statusLinePanel, gbc_statusLinePanel);
		GridBagLayout gbl_statusLinePanel = new GridBagLayout();
		gbl_statusLinePanel.columnWidths = new int[]{0, 0};
		gbl_statusLinePanel.rowHeights = new int[]{9, 0};
		gbl_statusLinePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_statusLinePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		statusLinePanel.setLayout(gbl_statusLinePanel);
		
		GridBagConstraints gbc_searchResultsStatusLine = new GridBagConstraints();
		gbc_searchResultsStatusLine.anchor = GridBagConstraints.WEST;
		gbc_searchResultsStatusLine.gridx = 0;
		gbc_searchResultsStatusLine.gridy = 0;
		statusLinePanel.add(statusLabel, gbc_searchResultsStatusLine);
		
	}
	
	protected void initActions() {
		
		searchResultsModel.addSearchResultsListener(this);
		
		resultsList.addListSelectionListener(loadAction);
		resultsList.addMouseListener(loadAction);
		resultsListModel.addListDataListener(loadAction);
		
		// initial updates
		updateSearchResults();
		loadAction.updateEnabledStatus();
		
	}
	
	/**
	 * Loads the currently selected search result as the active title into the TitleModel. First, detailed information is fetched
	 * about the selected title, as the search result data contains only limited information which does not include e.g. the episodes
	 * of a series. Once finished, the resulting Title instance is set as the active title.
	 */
	public void loadSelectedResult(){
		
		LimitedTitle selectedValue = resultsList.getSelectedValue();
		if(selectedValue == null) return;
		
		final BaseTitle selectedTitle = (BaseTitle) selectedValue;
		
		Runnable newSearch = new Runnable(){
			@Override
			public void run() {
				
				// if the current title is not the one already selected (if any), select the new one
				// (by means of a search for its exact id)
				Application application = Application.getInstance();
				Title currentTitle = application.getTitleModel().getTitle();
				
				if(selectedTitle.equals(currentTitle)) return;
				
				try {
					
					SwingUtilities.invokeAndWait(new Runnable(){
						@Override
						public void run() {
							
							resultsList.setEnabled(false);
							loadAction.setEnabled(false);
							
							setStatusLoading();
							
						}
					});
					
				}
				catch(InterruptedException e) {
					throw new Error(e);
				}
				catch(InvocationTargetException e) {
					throw new Error(e);
				}
				
				// search by ID (should result in an exact match) and load the result into the title model/search results model
				application.searchAndLoad(selectedTitle.getId());
				
				try {
					
					SwingUtilities.invokeAndWait(new Runnable(){
						@Override
						public void run() {
							
							resultsList.setEnabled(true);
							loadAction.setEnabled(true);
							
							setStatusText("");
							
						}
					});
					
				}
				catch(InterruptedException e) {
					throw new Error(e);
				}
				catch(InvocationTargetException e) {
					throw new Error(e);
				}
				
			}
		};
		(new Thread(newSearch)).start();
		
	}
	
	public void setStatusLoading(){
		
		// TODO: maybe create an @EDT annotation for this, as in http://www.java.net/node/657940 ?
		if(!SwingUtilities.isEventDispatchThread()){
			SwingUtilities.invokeLater(new Runnable() { @Override public void run(){ setStatusLoading(); } });
		}
		
		statusLabel.setIcon(Images.getLoadingAnimationIcon());
		statusLabel.setText("Loading");
		repaint();
		
	}
	
	public void setStatusText(final String message){
		
		if(!SwingUtilities.isEventDispatchThread()){
			SwingUtilities.invokeLater(new Runnable() { @Override public void run(){ setStatusText(message); } });
		}
		
		statusLabel.setIcon(null);
		statusLabel.setText(message);
		repaint();
		
	}
	
	protected void updateSearchResults(){
		
		// must execute on the Swing thread
		SwingUtilities.invokeLater(new Runnable(){
			@Override public void run() {
				
				List<LimitedTitle> results = null;
				
				synchronized(resultsListModel){
					
					resultsListModel.clear();
					
					results = searchResultsModel.getSearchResults();
					if(results == null) return;
					
					if(results.size() > 0){
						
						setStatusText("Found " + results.size() + " result(s).");
						
						for(LimitedTitle limitedTitle : results){
							resultsListModel.addElement(limitedTitle);
						}
						
					} else {
						
						setStatusText("No results.");
						
					}
					
				}
				
				repaint();
				
			}
		});
		
	}
	
	@Override
	public void searchResultsSet(SearchResultsSetEvent e) {
		updateSearchResults();
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		if(resultsListModel.getSize() <= 0){
			
			ImageIcon emptySearchResultsIcon = Images.getEmptySearchResultsIcon();
			
			// TODO: this really should be overriden in the scroll pane instead of here
			int width = resultsListScrollPane.getWidth();
			int height = resultsListScrollPane.getHeight();
			int iconWidth = emptySearchResultsIcon.getIconWidth();
			int iconHeight = resultsListScrollPane.getY() + emptySearchResultsIcon.getIconHeight();
			
			if(iconWidth <= width - 5 && iconHeight <= height - 5){
				g.drawImage(
					emptySearchResultsIcon.getImage(),
					resultsListScrollPane.getX() + (width - iconWidth)/2,
					resultsListScrollPane.getY() + (height - iconHeight)/2,
					null
				);
			}
			
		}
		
	}
	
	/**
	 * Action to load the currently selected search result into the TitleModel.
	 * 
	 * @author Jeroen
	 */
	private class LoadSearchResultAction extends EnabledStatusAction implements ListSelectionListener, ListDataListener, MouseListener {
		
		public LoadSearchResultAction() {
			super("Load");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			loadSelectedResult();
		}
		
		@Override
		public void updateEnabledStatus(){
			synchronized(resultsListModel){
				setEnabled(resultsListModel.getSize() > 0 && resultsList.getSelectedIndex() >= 0);
			}
		}
		
		@Override public void valueChanged(ListSelectionEvent e) { updateEnabledStatus(); }
		@Override public void contentsChanged(ListDataEvent e) { updateEnabledStatus(); }
		
		@Override public void intervalAdded(ListDataEvent e) { updateEnabledStatus(); }
		@Override public void intervalRemoved(ListDataEvent e) { updateEnabledStatus(); }
		
		@Override public void mousePressed(MouseEvent e) { }
		@Override public void mouseReleased(MouseEvent e) { }
		@Override public void mouseEntered(MouseEvent e) { }
		@Override public void mouseExited(MouseEvent e) { }
		@Override public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)){
				actionPerformed(null);
			}
		}
		
	}
	
}
