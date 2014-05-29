package com.voetsjoeba.imdb.renamer.gui.panel.rename;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.renamer.domain.InPlaceSeriesRenamer;
import com.voetsjoeba.imdb.renamer.domain.exception.RenamingException;
import com.voetsjoeba.imdb.renamer.domain.interpolation.InterpolationResults;
import com.voetsjoeba.imdb.renamer.domain.interpolation.StringMatchCoordinates;
import com.voetsjoeba.imdb.renamer.domain.interpolation.VariableMatch;
import com.voetsjoeba.imdb.renamer.gui.action.generic.EnabledStatusAction;

/**
 * Generic format string entry panel.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public abstract class GenericRenameFormatPanel extends JPanel implements DocumentListener {
	
	// TODO: decide whether or not to apply safeFilename to the sample
	
	protected JScrollPane editorScrollPane;
	protected JEditorPane editorPane;
	
	protected static final InPlaceSeriesRenamer dummyRenamer = new InPlaceSeriesRenamer(); // TODO: use current renamer
	// from application, but the
	// problem is that it
	// doesn't allow for rename
	// formats to be provided
	// (and it shouldn't either)
	
	protected static final Color sampleNormalColor = Color.decode("0x000000");
	protected static final Color sampleErrorColor = Color.decode("0xCC0000");
	protected static final Color editorHighlightColor = Color.decode("0xe2eeff");
	
	protected static final HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(editorHighlightColor);
	
	protected JPanel samplePanel;
	protected JPanel subPanel;
	
	protected JButton resetButton;
	protected ResetButtonAction resetAction;
	
	/**
	 * Label with "Sample:" text.
	 */
	protected JLabel sampleLabelLiteral;
	
	/**
	 * Label holding actual interpolation sample.
	 */
	protected JLabel sampleLabel;
	
	public GenericRenameFormatPanel() {
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents() {
		
		subPanel = new JPanel();
		
		resetAction = new ResetButtonAction();
		resetButton = new JButton(resetAction);
		
		samplePanel = new JPanel();
		sampleLabelLiteral = new JLabel();
		sampleLabel = new JLabel();
		
		sampleLabelLiteral.setText("Sample: ");
		sampleLabelLiteral.setFont(sampleLabelLiteral.getFont().deriveFont(Font.BOLD));
		
		editorPane = new JEditorPane();
		editorPane.setFont(new Font("monospaced", Font.PLAIN, 12));
		
		editorScrollPane = new JScrollPane();
		
	}
	
	private void initLayout() {
		
		samplePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		samplePanel.add(sampleLabelLiteral);
		samplePanel.add(sampleLabel);
		
		subPanel.setLayout(new BorderLayout());
		subPanel.add(samplePanel, BorderLayout.WEST);
		subPanel.add(resetButton, BorderLayout.EAST);
		
		editorScrollPane.setViewportView(editorPane);
		
	}
	
	private void initActions() {
		editorPane.getDocument().addDocumentListener(this);
	}
	
	public String getText() {
		return editorPane.getText().trim();
	}
	
	public void setText(String text) {
		editorPane.setText(text == null ? null : text.trim());
	}
	
	/**
	 * Returns the abstract file path to be used for the sample renaming.
	 */
	protected abstract File getDummyFile();
	
	/**
	 * Returns the {@link Series} object to be used for the sample renaming.
	 */
	protected abstract Series getDummySeries();
	
	/**
	 * Returns the sample rename format to be used for the sample renaming.
	 */
	protected abstract String getSampleRenameFormat();
	
	/**
	 * Returns the sample default title rename format to be used for the sample renaming.
	 */
	protected abstract String getSampleDefaultTitleRenameFormat();
	
	/**
	 * Returns the default value for the text to be entered in the editor pane (used for resetting to default).
	 */
	protected abstract String getDefaultText();
	
	protected void updateSample() {
		
		String currentFormatString = getText();
		
		// don't bother doing the whole renaming thing
		if(currentFormatString.equals("")) {
			sampleLabel.setText("");
			return;
		}
		
		Highlighter highlighter = editorPane.getHighlighter();
		highlighter.removeAllHighlights();
		
		try {
			
			// String renamedFilename = Application.getInstance().getRenamer().getRenamedFilename(dummyFile,
			// dummySeries);
			InterpolationResults renamedFilenameResults = dummyRenamer.getRenamedFilename(getDummyFile(), getDummySeries(), getSampleRenameFormat(),
					getSampleDefaultTitleRenameFormat());
			
			sampleLabel.setForeground(sampleNormalColor);
			sampleLabel.setText(renamedFilenameResults.getOutput());
			
			// renameFormatEditorPane.getHighlighter().addHighlight(, p1, p);
			for (VariableMatch variableMatch : renamedFilenameResults.getVariableMatches()) {
				
				// Highlighter highlighter = renameFormatEditorPane.getHighlighter();
				
				StringMatchCoordinates inputCoords = variableMatch.getInputCoordinates();
				
				try {
					highlighter.addHighlight(inputCoords.getStart(), inputCoords.getEnd(), highlightPainter);
				}
				catch(BadLocationException e) {
					
				}
				
			}
			
		}
		catch(RenamingException e) {
			
			// this shouldn't happen
			sampleLabel.setForeground(sampleErrorColor);
			sampleLabel.setText(e.getMessage());
			
		}
		
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		updateSample();
		resetAction.updateEnabledStatus();
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		updateSample();
		resetAction.updateEnabledStatus();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		updateSample();
		resetAction.updateEnabledStatus();
	}
	
	protected class ResetButtonAction extends EnabledStatusAction {
		
		public ResetButtonAction(){
			super("Reset");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			setText(getDefaultText());
		}
		
		@Override
		public void updateEnabledStatus() {
			setEnabled(!getText().equals(getDefaultText()));
		}
		
	}
	
}
