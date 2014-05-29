package com.voetsjoeba.imdb.renamer.gui.panel.files.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.domain.FileRenamer;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.model.TitleModel;

/**
 * Component representing a single entry in the file list.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class FileListItem extends JPanel {
	
	protected File file;
	
	private JPanel fileInfoPanel;
	private JLabel filenameLabel;
	private JLabel directoryLabel;
	private JPanel arrowPanel;
	private JLabel arrowLabel;
	private JPanel renamePanel;
	private JLabel renamedTitleLabel;
	private JLabel renamedTitleSubLabel;
	
	private static final Color oddBackgroundColor = Color.decode("0xf0f6ff");
	private static final Color directoryLabelSelection = Color.LIGHT_GRAY;
	private static final Color directoryLabelSelectionForeground = Color.decode("0xDDDDDD");
	private static final Color mappingErrorForeground = Color.decode("0xCC0000");
	
	protected Font regularLabelFont;
	protected Font boldLabelFont;
	
	/**
	 * 
	 * @param file the {@link File} this list item is associated with
	 * @throws IllegalArgumentException if <i>file</i> is null
	 */
	public FileListItem(File file) {
		
		if(file == null) throw new IllegalArgumentException("File must not be null");
		this.file = file;
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	protected void initComponents() {
		
		/*entryControlsPanel = new JPanel();
		removeButton = new JButton();*/
		fileInfoPanel = new JPanel();
		filenameLabel = new JLabel();
		directoryLabel = new JLabel();
		
		arrowPanel = new JPanel();
		arrowLabel = new JLabel();
		renamePanel = new JPanel();
		renamedTitleLabel = new JLabel();
		renamedTitleSubLabel = new JLabel();
		
		arrowLabel.setText("->");
		
		regularLabelFont = renamedTitleLabel.getFont();
		boldLabelFont = regularLabelFont.deriveFont(Font.BOLD);
		
	}
	
	protected void initLayout(){
		
		//======== this ========
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(2, 2, 2, 2));
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
		
		setOpaque(true);
		setBackground(Color.WHITE);
		
		fileInfoPanel.setOpaque(false);
		renamePanel.setOpaque(false);
		arrowPanel.setOpaque(false);
		
		//======== entryControlsPanel ========
		/*{
			entryControlsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

			//---- removeButton ----
			removeButton.setIcon(Icons.getCrossIcon());
			entryControlsPanel.add(removeButton);
		}
		add(entryControlsPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.NONE,
			new Insets(0, 0, 0, 5), 0, 0));*/
		
		//======== fileInfoPanel ========
		{
			fileInfoPanel.setLayout(new GridBagLayout());
			((GridBagLayout)fileInfoPanel.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)fileInfoPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)fileInfoPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)fileInfoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
			
			//---- filenameLabel ----
			fileInfoPanel.add(filenameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			
			//---- directoryLabel ----
			directoryLabel.setForeground(directoryLabelSelection);
			fileInfoPanel.add(directoryLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 2, 0, 0), 0, 0));
		}
		add(fileInfoPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		//======== arrowPanel ========
		{
			arrowPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
			arrowPanel.setLayout(new BorderLayout());
			
			//---- arrowLabel ----
			arrowPanel.add(arrowLabel, BorderLayout.CENTER);
		}
		add(arrowPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));
		
		//======== renamePanel ========
		{
			renamePanel.setLayout(new GridBagLayout());
			((GridBagLayout)renamePanel.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)renamePanel.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)renamePanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)renamePanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
			
			//---- renamedTitleLabel ----
			renamePanel.add(renamedTitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			
			renamePanel.add(renamedTitleSubLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 2, 0, 0), 0, 0));
			
		}
		add(renamePanel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
	}
	
	protected void initActions(){
		
		//Application.getInstance().getTitleModel().addTitleSelectedListener(this);
		
	}
	
	public void updateState(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
		
		if(isSelected) {
			
			setBackground(list.getSelectionBackground());
			//setForeground(list.getSelectionForeground());
			filenameLabel.setForeground(list.getSelectionForeground());
			directoryLabel.setForeground(directoryLabelSelectionForeground);
			
			//renamedTitleLabel.setForeground(list.getSelectionForeground());
			arrowLabel.setForeground(list.getSelectionForeground());
			//renamedTitleLabel.setForeground(list.getSelectionForeground());
			
		} else {
			
			Color backgroundColor = (index % 2 == 0 ? list.getBackground() : oddBackgroundColor);
			
			setBackground(backgroundColor);
			//arrowLabel.setBackground(backgroundColor);
			
			arrowLabel.setForeground(list.getForeground());
			renamedTitleLabel.setBackground(backgroundColor);
			//renamedTitleLabel.setForeground(list.getForeground());
			
			//setForeground(list.getForeground());
			filenameLabel.setForeground(list.getForeground());
			directoryLabel.setForeground(directoryLabelSelection);
			
		}
		
		renamedTitleLabel.setFont(regularLabelFont);
		renamedTitleLabel.setForeground(list.getForeground());
		
		filenameLabel.setText(file.getName());
		directoryLabel.setText(file.getParentFile().getAbsolutePath());
		
		updateTitleSelectedStatus();
		
		
	}
	
	/**
	 * Updates the labels that have to do with the currently selected title.
	 * @see TitleModel
	 */
	protected void updateTitleSelectedStatus(){
		
		TitleModel titleModel = Application.getInstance().getTitleModel();
		boolean isTitleSelected = (titleModel.isTitleSelected());
		
		if(isTitleSelected){
			
			renamedTitleLabel.setVisible(true);
			arrowLabel.setVisible(true);
			
			FileRenamer renamer = Application.getInstance().getRenamer();
			
			File renamedFile = null;
			Exception renamingException = null;
			
			try {
				
				renamedFile = renamer.getRenamedFile(file, titleModel.getTitle());
				//renamedTitleLabel.setFont(boldLabelFont);
				if(!renamedFile.equals(file)){
					
					renamedTitleLabel.setFont(boldLabelFont);
					renamedTitleLabel.setText(renamedFile.getName());
					
				} else {
					
					renamedTitleLabel.setVisible(false);
					arrowLabel.setVisible(false);
					
				}
				
				
			}
			/*catch(UnsupportedOperationException usoex){
				renamingException = usoex;
			}*/
			/*catch(NoSuchEpisodeException nseex){
				renamedTitleLabel.setText(nseex.getMessage());
			}
			catch(NoEpisodeMappingException nemex){
				renamedTitleLabel.setText(nemex.getMessage());
			}*/
			catch(RenamingException e) {
				
				//log.debug(e.getMessage());
				renamingException = e;
			}
			
			if(renamingException != null){
				renamedTitleLabel.setText(renamingException.getMessage());
				renamedTitleLabel.setForeground(mappingErrorForeground);
			}
			
			//arrowLabel.setVisible(true);
			//renamedTitleLabel.setVisible(true);
			
		} else {
			
			//renamedTitleLabel.setText("");
			
			renamedTitleLabel.setVisible(false);
			arrowLabel.setVisible(false);
			
		}
		
	}
	
	/*public void titleSelected(TitleSelectedEvent e) {
		updateTitleSelectedStatus();
		repaint();
	}*/
	
}
