package com.voetsjoeba.imdb.renamer.gui.generic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.voetsjoeba.imdb.renamer.gui.action.OkCloseDialogAction;

/**
 * Generic dialog with an OK button to close.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class OkDialog extends ApplicationDialog {
	
	private JPanel buttonBar;
	private JButton okButton;
	
	protected boolean performPack = true;
	
	/**
	 * Holds custom dialog components (i.e. other than the ok button and everything).
	 */
	protected JPanel childContentPanel;
	
	public OkDialog(){
		
		super();
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents() {
		
		//rootPane = new JPanel();
		
		buttonBar = new JPanel();
		okButton = new JButton(new OkCloseDialogAction(this));
		
		childContentPanel = new JPanel();
		
	}
	
	private void initLayout() {
		
		Container contentPane = super.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		{
			//childContentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
			//contentPane.add(childContentPanel, BorderLayout.CENTER);
			
			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				//buttonBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};
				
				//---- okButton ----
				
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				//buttonBar.add(okButton);
				
				
			}
			contentPane.add(buttonBar, BorderLayout.SOUTH);
			
		}
		
		contentPane.add(childContentPanel, BorderLayout.CENTER);
		getRootPane().setDefaultButton(okButton);
		
	}
	
	private void initActions() {
		
	}
	
	public JPanel getChildContentPanel(){
		return childContentPanel;
	}
	
	public JPanel getButtonBar(){
		return buttonBar;
	}
	
	public JButton getOkButton(){
		return okButton;
	}
	
	@Override
	public void setVisible(boolean visible){
		
		if(visible){
			if(performPack) pack();
			setLocationRelativeTo(getOwner());
		}
		
		super.setVisible(visible);
		
	}
	
}
