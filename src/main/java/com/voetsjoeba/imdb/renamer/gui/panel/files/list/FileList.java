package com.voetsjoeba.imdb.renamer.gui.panel.files.list;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.FileRenamer;
import com.voetsjoeba.imdb.renamer.domain.exception.NoEpisodeMappingException;
import com.voetsjoeba.imdb.renamer.domain.exception.NoSuchEpisodeException;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.event.DefaultTitleRenameFormatChangedEvent;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisListener;
import com.voetsjoeba.imdb.renamer.event.FilenameAnalysisUpdatedEvent;
import com.voetsjoeba.imdb.renamer.event.RenameFormatChangedEvent;
import com.voetsjoeba.imdb.renamer.event.RenameFormatListener;
import com.voetsjoeba.imdb.renamer.event.TitleSelectedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectionListener;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.AssignSeasonEpisodeNumberAction;
import com.voetsjoeba.imdb.renamer.gui.action.PerformRenameAction;
import com.voetsjoeba.imdb.renamer.gui.action.RemoveFilesAction;
import com.voetsjoeba.imdb.renamer.gui.action.RemoveSeasonEpisodeNumberAction;
import com.voetsjoeba.imdb.renamer.model.FilesModel;

/**
 * GUI list of loaded files.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class FileList extends JList implements FilenameAnalysisListener, TitleSelectionListener, RenameFormatListener, KeyListener, DropTargetListener, DragSourceListener, DragGestureListener, MouseListener {
	
	private static final Logger log = LoggerFactory.getLogger(FileList.class);
	
	protected FileListModel fileListModel;
	protected FileListCellRenderer fileListCellRenderer;
	
	protected DropTarget dropTarget = new DropTarget(this, this);
	protected DragSource dragSource = DragSource.getDefaultDragSource();
	
	public FileList(){
		init();
	}
	
	protected void init(){
		
		fileListModel = new FileListModel();
		fileListCellRenderer = new FileListCellRenderer();
		
		setModel(fileListModel);
		setCellRenderer(fileListCellRenderer);
		
		final Application app = Application.getInstance();
		//Application.getInstance().getFilesModel().addFileListChangeListener(this);
		app.getFilesModel().addFileListChangeListener(fileListModel);
		app.getFilesModel().addFileListChangeListener(fileListCellRenderer);
		
		app.getTitleModel().addTitleSelectionListener(this);
		app.getRenameFormatModel().addRenameFormatListener(this);
		app.getRenamer().getFilenameAnalyzer().addFilenameAnalysisListener(this);
		
		addMouseListener(this);
		addKeyListener(this);
		
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		//Application.getInstance().getTitleModel().addTitleSelectionListener(this);
		
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized void drop(DropTargetDropEvent dtde) {
		
		try {
			
			Transferable transferable = dtde.getTransferable();
			if(transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
				
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Object transferData = transferable.getTransferData(DataFlavor.javaFileListFlavor);
				
				if(transferData instanceof List<?>){
					List<File> files = (List<File>) transferData;
					Application.getInstance().getFilesModel().add(files);
				}
				
			}
			
		}
		catch(IOException ioex){
			log.debug(ioex.getMessage(), ioex);
		}
		catch(UnsupportedFlavorException e) {
			log.debug(e.getMessage(), e);
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		Title selectedTitle = Application.getInstance().getTitleModel().getTitle();
		
		boolean isLeftMouseButton = SwingUtilities.isLeftMouseButton(e);
		boolean isRightMouseButton = SwingUtilities.isRightMouseButton(e);
		boolean isDoubleClick = (e.getClickCount() >= 2);
		
		int clickedIndex = FileList.this.locationToIndex(e.getPoint()); // index of item where mouse was clicked (may and does return an item that isn't actually directly at the location; the position is rounded (see documentation for locationToIndex))
		Rectangle clickedItemBounds = FileList.this.getCellBounds(clickedIndex, clickedIndex);
		
		if(clickedItemBounds != null && !clickedItemBounds.contains(e.getPoint()) && isLeftMouseButton){
			//FileList.this.setSelectedIndex(-1); // user clicked outside of item, deselect any currently selected
			FileList.this.setSelectedIndex(-1); // TODO: does not work for some reason (test with a single item in the list; remains selected when clicking outside it)
		}
		
		// get currently selected indices
		int[] selectedIndices = FileList.this.getSelectedIndices();
		boolean singleSelection = (selectedIndices.length == 1);
		
		// check if the mouse was clicked at at least one of the currently selected items
		Arrays.sort(selectedIndices);
		boolean clickedSelectedItem = (Arrays.binarySearch(selectedIndices, clickedIndex) >= 0);
		
		
		// rightlick
		if(selectedIndices.length > 0 && isRightMouseButton && clickedSelectedItem){
			
			Object[] selectedObjects = FileList.this.getSelectedValues();
			List<File> selectedFiles = new ArrayList<File>(selectedObjects.length);
			
			for(Object objectToRemove : selectedObjects){
				selectedFiles.add((File) objectToRemove);
			}
			
			FileRenamer renamer = Application.getInstance().getRenamer();
			
			// add remove action for selected files
			Action removeItemsAction = new RemoveFilesAction(selectedFiles);
			
			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(removeItemsAction);
			
			// check if (all) the selected files can be renamed, have mappings, ...
			if(selectedTitle instanceof Series){ // prevent nasty "can only work with series"-exceptions for this next bit
				
				boolean allRenamable = true;
				boolean allHaveValidMappings = true;
				
				// TODO: hide this behind a function somewhere like isUnaffectedRenameCandidate in Application
				
				for(File selectedFile : selectedFiles){
					
					try {
						renamer.getRenamedFile(selectedFile, selectedTitle);
					}
					catch(NoSuchEpisodeException nsex){
						// mapped episode does not exist in title, invalid mapping
						allHaveValidMappings = false;
						allRenamable = false;
						//throw nsex;
					}
					catch(NoEpisodeMappingException nemex){
						// no mapping could be extracted from filename, invalid mapping
						allHaveValidMappings = false;
						allRenamable = false;
						//throw nemex;
					}
					catch(RenamingException rex){
						// can't get renamed file, set allRenamable to false and break
						allRenamable = false;
						//break;
					}
					
					
				}
				
				// if all files can be renamed, add a "perform rename" action to the rightclick menu
				if(allRenamable){
					popupMenu.add(new PerformRenameAction(selectedFiles));
				}
				
				if(allHaveValidMappings){
					popupMenu.add(new RemoveSeasonEpisodeNumberAction(selectedFiles));
				}
				
			}
			
			// this next section may add actions specific to single selections
			if(singleSelection){
				
				Object selectedValue = FileList.this.getSelectedValue();
				File selectedFile = (File) selectedValue;
				
				// add the possibility to assign mappings to files when a series title is loaded
				if((selectedTitle instanceof Series)){
					Action assignSeNumberAction = new AssignSeasonEpisodeNumberAction(selectedFile, (Series) selectedTitle);
					popupMenu.add(assignSeNumberAction);
				}
				
			}
			
			popupMenu.show(FileList.this, e.getX(), e.getY());
			
		}
		
		// leftclick
		if(isLeftMouseButton && isDoubleClick){
			
			// if the selected title is a series object but no mapping exists for this entry, open the mapper dialog
			if(singleSelection){
				
				//SeasonEpisodeMapper mapper = Application.getInstance().getRenamer().getSeasonEpisodeMapper();
				
				Object selectedValue = FileList.this.getSelectedValue();
				File selectedFile = (File) selectedValue;
				
				if((selectedTitle instanceof Series)){
					
					boolean hasValidMapping = Application.getInstance().hasValidMapping(selectedFile, (Series) selectedTitle);
					
					if(!hasValidMapping){
						Action assignSeNumberAction = new AssignSeasonEpisodeNumberAction(selectedFile, (Series) selectedTitle);
						assignSeNumberAction.actionPerformed(null);
					}
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		FilesModel filesModel = Application.getInstance().getFilesModel();
		
		if(filesModel.getSize() <= 0){
			
			ImageIcon dropzoneIcon = Images.getDropzoneIcon();
			
			int width = getWidth();
			int height = getHeight();
			int iconWidth = dropzoneIcon.getIconWidth();
			int iconHeight = dropzoneIcon.getIconHeight();
			
			g.drawImage(dropzoneIcon.getImage(), (width - iconWidth)/2, (height - iconHeight)/2, null);
			
		}
		
	}
	
	@Override public void dragExit(DropTargetEvent dte) {}
	@Override public void dragOver(DropTargetDragEvent dtde) {}
	@Override public void dropActionChanged(DropTargetDragEvent dtde) {}
	@Override public void dragDropEnd(DragSourceDropEvent dsde) {}
	@Override public void dragEnter(DragSourceDragEvent dsde) {}
	@Override public void dragExit(DragSourceEvent dse) {}
	@Override public void dragOver(DragSourceDragEvent dsde) {}
	@Override public void dropActionChanged(DragSourceDragEvent dsde) {}
	@Override public void dragGestureRecognized(DragGestureEvent dge) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	
	protected void forceUiUpdate(){
		revalidate();
		repaint();
	}
	
	@Override
	public void filenameAnalysisUpdated(FilenameAnalysisUpdatedEvent e) {
		forceUiUpdate();
	}
	
	@Override
	public void titleSelected(TitleSelectedEvent e) {
		forceUiUpdate();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_DELETE){
			
			Object[] selectedValues = getSelectedValues();
			
			for(Object selectedValue : selectedValues){
				
				if(selectedValue instanceof File){
					
					File selectedFile = (File) selectedValue;
					
					RemoveFilesAction removeFilesAction = new RemoveFilesAction(Arrays.asList(selectedFile));
					removeFilesAction.actionPerformed(null);
					
				}
				
			}
			
		}
		
	}
	
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}
	
	@Override
	public void defaultTitleRenameFormatChanged(DefaultTitleRenameFormatChangedEvent e) {
		forceUiUpdate();
	}
	
	@Override
	public void renameFormatChanged(RenameFormatChangedEvent e) {
		forceUiUpdate();
	}
	
}
