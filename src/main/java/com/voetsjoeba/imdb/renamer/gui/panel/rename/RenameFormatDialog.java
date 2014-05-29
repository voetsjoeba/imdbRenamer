package com.voetsjoeba.imdb.renamer.gui.panel.rename;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.generic.OkCancelDialog;
import com.voetsjoeba.imdb.renamer.model.RenameFormatModel;

/**
 * Allows the user to enter the standard rename format and default title rename format.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class RenameFormatDialog extends OkCancelDialog {
	
	private RenameFormatPanel renameFormatPanel;
	private DefaultTitleRenameFormatPanel defaultTitleRenameFormatPanel;
	
	private JPanel availableVariablesPanel;
	private JLabel variablesLabel;
	
	public RenameFormatDialog() {
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents(){
		
		renameFormatPanel = new RenameFormatPanel();
		renameFormatPanel.editorPane.setAlignmentY(Component.TOP_ALIGNMENT);
		renameFormatPanel.editorPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		renameFormatPanel.editorScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		renameFormatPanel.editorScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		defaultTitleRenameFormatPanel = new DefaultTitleRenameFormatPanel();
		defaultTitleRenameFormatPanel.editorPane.setAlignmentY(Component.TOP_ALIGNMENT);
		defaultTitleRenameFormatPanel.editorPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		defaultTitleRenameFormatPanel.editorScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		defaultTitleRenameFormatPanel.editorScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		availableVariablesPanel = new JPanel();
		
		variablesLabel = new JLabel();
		variablesLabel.setText("<html><body>" +
				"<b>${episodeTitle}</b>: Holds the title of the episode<br>" +
				"<b>${episodeNr}</b>: Holds the number of the episode within its season<br>" +
				"<b>${seasonNr}</b>: Holds the number of the season the episode belongs to<br>" +
				"<b>${seriesTitle}</b>: Holds the title of the series the episode belongs to<br>" +
				"<br>" +
				"The numeric variables take an extra length specifier, signified by a colon (:) followed by " +
				"the display format to use for the number. For instance, ${episodeNr:05} will output the " +
				"episode number as a sequence of 5 digits, padded with zeros at the front." + 
				"</body></html>");
		//variablesLabel.setText("<html><body>${episodeTitle}<br>${episodeNr}<br>${seasonNr}<br>${seriesTitle}</body></html>");
		
	}
	
	private void initLayout(){
		
		setTitle("Set Rename Formats");
		
		JPanel contentPanel = getChildContentPanel();
		
		contentPanel.setLayout(new GridBagLayout());
		((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
		((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {1.0, 1.0, 0.0, 1.0E-4};
		contentPanel.add(renameFormatPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		contentPanel.add(defaultTitleRenameFormatPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		//======== availableVariablesPanel ========
		{
			availableVariablesPanel.setBorder(new TitledBorder("Available Variables"));
			availableVariablesPanel.setLayout(new GridBagLayout());
			((GridBagLayout) availableVariablesPanel.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout) availableVariablesPanel.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout) availableVariablesPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout) availableVariablesPanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
			
			//---- variablesLabel ----
			availableVariablesPanel.add(variablesLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPanel.add(availableVariablesPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		performPack = false;
		setSize(475, 550);
		
	}
	
	private void initActions(){
		
		JButton okButton = getOkButton();
		okButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				
				final RenameFormatModel renameFormatModel = Application.getInstance().getRenameFormatModel();
				String newRenameFormat = getRenameFormatString();
				String newDefaultTitleRenameFormat = getDefaultTitleRenameFormatString();
				
				if(newRenameFormat != null) renameFormatModel.setRenameFormat(newRenameFormat); // TODO: turn this into a model so the list cell renderer can listen to changes to it
				if(newDefaultTitleRenameFormat != null) renameFormatModel.setDefaultTitleRenameFormat(newDefaultTitleRenameFormat);
			
			}
		});
		
	}
	
	/**
	 * Returns the rename format string the user picked using this dialog.
	 */
	public String getRenameFormatString(){
		return renameFormatPanel.getText();
	}
	
	/**
	 * Sets the rename format string to be displayed in the editor pane.
	 */
	public void setRenameFormatString(String renameFormatString){
		renameFormatPanel.setText(renameFormatString);
	}
	
	public String getDefaultTitleRenameFormatString(){
		return defaultTitleRenameFormatPanel.getText();
	}
	
	public void setDefaultTitleRenameFormatString(String defaultTitleRenameFormatString){
		defaultTitleRenameFormatPanel.setText(defaultTitleRenameFormatString);
	}
	
}
