package com.voetsjoeba.imdb.renamer.gui.panel.rename;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.voetsjoeba.imdb.domain.StandardEpisode;
import com.voetsjoeba.imdb.domain.StandardSeason;
import com.voetsjoeba.imdb.domain.StandardSeries;
import com.voetsjoeba.imdb.domain.api.Episode;
import com.voetsjoeba.imdb.domain.api.Season;
import com.voetsjoeba.imdb.domain.api.Series;
import com.voetsjoeba.imdb.renamer.Application;

/**
 * Holds an editor for the default title rename format. Used by {@link RenameFormatDialog}.
 * 
 * @see RenameFormatDialog
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class DefaultTitleRenameFormatPanel extends GenericRenameFormatPanel {
	
	private JLabel whatIsThisLabel;
	
	protected static final Series dummySeries;
	protected static final Episode dummyEpisode;
	protected static final Season dummySeason;
	protected static final File dummyFile = new File("S04E12");
	
	static {
		
		dummySeries = new StandardSeries("tt0773262", "Dexter");
		dummySeason = new StandardSeason(4);
		dummyEpisode = new StandardEpisode(dummySeason, 12, "Episode #4.12");
		
		dummySeason.setEpisodes(Arrays.asList(dummyEpisode));
		dummySeries.setSeasons(Arrays.asList(dummySeason));
		
	}
	
	public DefaultTitleRenameFormatPanel() {
		
		super();
		initComponents();
		initLayout();
		initActions();
		
		updateSample();
		
	}
	
	protected void initComponents() {
		
		whatIsThisLabel = new JLabel();
		
		whatIsThisLabel.setText("<html><body>" +
				"<b>What is this?</b>" +
				"<br>" +
				"Some series do not name their episodes. Instead, they are assigned default names by IMDb such as \"Episode #3.2\". " +
				"This format applies to such episodes. If not set, the standard rename format above is used." +
				"</body></html>");
		whatIsThisLabel.setBorder(new EmptyBorder(5,10,10,10));
		
	}
	
	private void initLayout(){
		
		setBorder(new TitledBorder("Default Title Rename Format"));
		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};
		
		//---- whatIsThisLabel ----
		add(whatIsThisLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		add(editorScrollPane, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		add(subPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
	}
	
	private void initActions(){
		
	}
	
	@Override
	protected File getDummyFile() {
		return dummyFile;
	}
	
	@Override
	protected Series getDummySeries() {
		return dummySeries;
	}
	
	@Override
	protected String getSampleDefaultTitleRenameFormat() {
		return getText();
	}
	
	@Override
	protected String getSampleRenameFormat() {
		return getText();
	}
	
	@Override
	protected String getDefaultText() {
		return Application.getInstance().getRenameFormatModel().getDefaultDefaultTitleRenameFormat();
	}
	
}