package com.voetsjoeba.imdb.renamer.gui.generic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.voetsjoeba.imdb.renamer.gui.action.CancelCloseDialogAction;

@SuppressWarnings("serial")
public class OkCancelDialog extends OkDialog {
	
	private JButton cancelButton;
	
	public OkCancelDialog() {
		
		super();
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents() {
		cancelButton = new JButton(new CancelCloseDialogAction(this));
	}
	
	private void initLayout() {
		//buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		//buttonBar.add(cancelButton);
		
		JPanel buttonBar = getButtonBar();
		JButton okButton = getOkButton();
		
		buttonBar.setLayout(new GridBagLayout());
		((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80, 80};
		((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};
		
		//---- okButton ----
		
		buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
	}
	
	private void initActions() {
		
	}
	
	public JButton getCancelButton(){
		return cancelButton;
	}
	
}
