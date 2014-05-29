package com.voetsjoeba.imdb.renamer.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.voetsjoeba.imdb.domain.api.Searchable;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.action.generic.ApplicationAction;

@SuppressWarnings("serial")
public class SearchAction extends ApplicationAction implements DocumentListener {
	
	protected JComboBox searchField;
	
	public SearchAction(JComboBox searchField){
		
		super("Search");
		this.searchField = searchField;
		
		setTooltip("Search for the selected/entered query on IMDb.");
		initActions();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object selectedItem = searchField.getSelectedItem();
		String searchTerm = selectedItem.toString(); 
		
		if(selectedItem instanceof Searchable){
			Searchable searchable = (Searchable) selectedItem;
			searchTerm = searchable.getSearchTerm();
		}
		
		final String finalSearchTerm  = searchTerm;
		
		setEnabled(false);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				
				Application.getInstance().searchAndLoad(finalSearchTerm);
				
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						SearchAction.this.setEnabled(true);
					}
				});
				
			}
		}).start();
		
		
	}
	
	public void initActions(){
		
		Object searchFieldEditor = searchField.getEditor().getEditorComponent();
		if(searchFieldEditor instanceof JTextComponent){
			
			JTextComponent editorTextComponent = (JTextComponent) searchFieldEditor;
			editorTextComponent.getDocument().addDocumentListener(this);
			
		} else {
			throw new IllegalArgumentException("Provided combobox has an editor that is not an instance of JTextComponent");
		}
		
		updateEnabledStatus();
		
	}
	
	public void updateEnabledStatus(){
		
		Object selectedItem = searchField.getSelectedItem();
		Object editingItem = searchField.getEditor().getItem();
		
		boolean newEnabledStatus = (selectedItem != null || (editingItem.toString().length() > 0));
		setEnabled(newEnabledStatus);
		
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		updateEnabledStatus();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		updateEnabledStatus();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}
	
}
