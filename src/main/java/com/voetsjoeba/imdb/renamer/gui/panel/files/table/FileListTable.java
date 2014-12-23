package com.voetsjoeba.imdb.renamer.gui.panel.files.table;

import java.awt.Graphics;
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
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.FileRenamer;
import com.voetsjoeba.imdb.renamer.domain.exception.NoEpisodeMappingException;
import com.voetsjoeba.imdb.renamer.domain.exception.NoSuchEpisodeException;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.AssignSeasonEpisodeNumberAction;
import com.voetsjoeba.imdb.renamer.gui.action.PerformRenameAction;
import com.voetsjoeba.imdb.renamer.gui.action.RemoveFilesAction;
import com.voetsjoeba.imdb.renamer.gui.action.RemoveSeasonEpisodeNumberAction;
import com.voetsjoeba.imdb.renamer.model.FilesModel;

@SuppressWarnings("serial")
public class FileListTable extends JTable implements KeyListener, DropTargetListener, DragSourceListener, DragGestureListener, MouseListener, FileListTableConstants {
	
	protected FilesModel filesModel;
	protected FileRenamer fileRenamer;
	
	protected FileListTableModel tableModel;
	protected FileListTableColumnModel tableColumnModel;
	
	protected DropTarget dropTarget = new DropTarget(this, this);
	protected DragSource dragSource = DragSource.getDefaultDragSource();
	
	/**
	 * Table cell renderer for the original file column.
	 */
	//protected final OriginalFileCellRenderer originalFileCellRenderer = new OriginalFileCellRenderer();
	
	public FileListTable() {
		
		/**
		 * Making sure to pass a TableColumnModel to the JTable constructor so that it'll use that one. If no table column
		 * model is explicitly specified, JTable will automatically create one based on the return values from the
		 * getColumnCount() and getColumnName() methods in the TableModel. Passing a custom one to the constructor 
		 * prevents all that and lets you roll your own with no confusion over who's actually in control of the columns: 
		 * the TableModel or the TableColumnModel.
		 */
		super(new FileListTableModel(), new FileListTableColumnModel());
		
		init();
	}
	
	private void init() {
		
		filesModel = Application.getInstance().getFilesModel();
		fileRenamer = Application.getInstance().getRenamer();
		
		tableModel = (FileListTableModel) getModel();
		tableColumnModel = (FileListTableColumnModel) getColumnModel();
		
		setModel(tableModel);
		setColumnModel(tableColumnModel);
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		addMouseListener(this);
		addKeyListener(this);
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		
		setRowHeight(getPreferredRowHeight(0));
		
	}
	
	public int getPreferredRowHeight(int margin) {
	   
		/*// Get the current default height for all rows
		int rowIndex = 0;
	    int height = getRowHeight();

	    // Determine highest cell in the row
	    for (int c=0; c<getColumnCount(); c++) {
	        TableCellRenderer renderer = getCellRenderer(rowIndex, c);
	        Component comp = prepareRenderer(renderer, rowIndex, c);
	        int h = comp.getPreferredSize().height + 2*margin;
	        height = Math.max(height, h);
	    }
	    return height;*/
		
		FileInfoCell r = new OriginalFileCellRenderer();
		return r.getPreferredSize().height;
		
	}
	
	
	@Override public void paint(Graphics g) {
		
		super.paint(g);
		
		FilesModel filesModel = Application.getInstance().getFilesModel();
		
		if(filesModel.getSize() <= 0){
			
			ImageIcon dropzoneIcon = Images.getDropzoneIcon();
			
			int width = getWidth();
			int height = getHeight();
			int iconWidth = dropzoneIcon.getIconWidth();
			int iconHeight = dropzoneIcon.getIconHeight();
			
			if(iconWidth <= width - 5 && iconHeight <= height - 5){
				g.drawImage(dropzoneIcon.getImage(), (width - iconWidth)/2, (height - iconHeight)/2, null);
			}
			
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
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized void drop(DropTargetDropEvent dtde)
	{
		try
		{
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
			
		}
		catch(UnsupportedFlavorException e) {
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		Title selectedTitle = Application.getInstance().getTitleModel().getTitle();
		
		boolean isLeftMouseButton = SwingUtilities.isLeftMouseButton(e);
		boolean isRightMouseButton = SwingUtilities.isRightMouseButton(e);
		boolean isDoubleClick = (e.getClickCount() >= 2);
		
		int clickedRowIdx = rowAtPoint(e.getPoint()); // Note: can return -1 if the point is out of range!
		int[] selectedRowIndices = getSelectedRows();
		
		if(selectedRowIndices.length == 0 && clickedRowIdx >= 0){
			// if there's no selection, select the one that just got right-clicked
			getSelectionModel().setSelectionInterval(clickedRowIdx, clickedRowIdx);
			selectedRowIndices = new int[]{clickedRowIdx};
		}
		
		boolean singleSelection = (selectedRowIndices.length == 1);
		
		// check if any of the selected items got clicked
		Arrays.sort(selectedRowIndices);
		boolean clickedSelectedItem = (Arrays.binarySearch(selectedRowIndices, clickedRowIdx) >= 0);
		
		if(selectedRowIndices.length > 0 && isRightMouseButton && clickedSelectedItem)
		{
			List<File> selectedFiles = new ArrayList<File>();
			for(int selectedRowIdx : selectedRowIndices)
				selectedFiles.add(filesModel.getFile(selectedRowIdx));
			
			Action removeItemsAction = new RemoveFilesAction(selectedFiles);
			
			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(removeItemsAction);
			
			// check if (all) the selected files can be renamed, have mappings, ...
			if(selectedTitle instanceof Series) // prevent nasty "can only work with series"-exceptions for this next bit
			{
				boolean allRenamable = true;
				boolean allHaveValidMappings = true;
				
				// TODO: hide this behind a function somewhere like isUnaffectedRenameCandidate in Application
				for(File selectedFile : selectedFiles)
				{
					try
					{
						fileRenamer.getRenamedFile(selectedFile, selectedTitle);
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
				if(allRenamable)
					popupMenu.add(new PerformRenameAction(selectedFiles));
				if(allHaveValidMappings)
					popupMenu.add(new RemoveSeasonEpisodeNumberAction(selectedFiles));
			}
			
			// this next section may add actions specific to single selections
			if(singleSelection)
			{
				//File selectedFile = (File) selectedValue;
				File selectedFile = filesModel.getFile(selectedRowIndices[0]);
				
				// add the possibility to assign mappings to files when a series title is loaded
				if((selectedTitle instanceof Series)){
					Action assignSeNumberAction = new AssignSeasonEpisodeNumberAction(selectedFile, (Series) selectedTitle);
					popupMenu.add(assignSeNumberAction);
				}
			}
			
			popupMenu.show(this, e.getX(), e.getY());
		}
		
		if(isLeftMouseButton && isDoubleClick)
		{
			// if the selected title is a series object but no mapping exists for this entry, open the mapper dialog
			if(singleSelection)
			{
				//SeasonEpisodeMapper mapper = Application.getInstance().getRenamer().getSeasonEpisodeMapper();
				
				/*Object selectedValue = getSelectedValue();
				File selectedFile = (File) selectedValue;*/
				File selectedFile = filesModel.getFile(selectedRowIndices[0]);
				
				if((selectedTitle instanceof Series))
				{
					boolean hasValidMapping = Application.getInstance().hasValidMapping(selectedFile, (Series) selectedTitle);
					
					if(!hasValidMapping){
						Action assignSeNumberAction = new AssignSeasonEpisodeNumberAction(selectedFile, (Series) selectedTitle);
						assignSeNumberAction.actionPerformed(null);
					}
				}
			}
		}
		
		// TODO
		/*
		
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
					popupMenu.add(new RemoveMappingAction(selectedFiles));
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
			
		}*/
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		/*if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			
			Object[] selectedValues = getSelectedValues();
			
			for(Object selectedValue : selectedValues) {
				
				if(selectedValue instanceof File) {
					
					File selectedFile = (File) selectedValue;
					
					RemoveFilesAction removeFilesAction = new RemoveFilesAction(Arrays.asList(selectedFile));
					removeFilesAction.actionPerformed(null);
					
				}
				
			}
			
		}*/
		
	}
	
}
