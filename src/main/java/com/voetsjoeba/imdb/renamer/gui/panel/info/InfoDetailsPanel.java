package com.voetsjoeba.imdb.renamer.gui.panel.info;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.api.Name;
import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.gui.generic.HyperlinkLabel;
import com.voetsjoeba.imdb.renamer.util.EDT;

/**
 * Displays detailed information about the currently selected title, such as type, name, year of release, genre and plot.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class InfoDetailsPanel extends JPanel {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(InfoDetailsPanel.class);
	
	protected JLabel typeTitleLabel;
	protected JLabel typeLabel;
	protected JLabel titleTitleLabel;
	protected HyperlinkLabel titleLabel;
	protected JLabel starsTitleLabel;
	protected JPanel starsPanel;
	protected JLabel genreTitleLabel;
	protected JLabel genreLabel;
	protected JLabel plotTitleLabel;
	protected JTextPane plotText;
	protected JPanel plotTitleLabelPanel;
	protected JScrollPane plotTextScrollPane;
	
	public InfoDetailsPanel() {
		initComponents();
		initLayout();
		initActions();
	}
	
	private void initComponents() {
		
		typeLabel = new JLabel();
		typeLabel.setText("--");
		titleLabel = new HyperlinkLabel();
		starsPanel = new JPanel();
		genreLabel = new JLabel();
		genreLabel.setText("--");
		
		typeTitleLabel = new JLabel();
		titleTitleLabel = new JLabel();
		starsTitleLabel = new JLabel();
		genreTitleLabel = new JLabel();
		
		typeTitleLabel.setText("Type: ");
		titleTitleLabel.setText("Title: ");
		starsTitleLabel.setText("Stars:");
		genreTitleLabel.setText("Genre:" );
		
		plotTitleLabelPanel = new JPanel();
		plotText = new JTextPane();
		plotText.setMinimumSize(new Dimension(6, 50));
		plotTitleLabel = new JLabel();
		plotTextScrollPane = new JScrollPane();
		plotTextScrollPane.setMinimumSize(new Dimension(23, 80));
		
	}
	
	private void initLayout() {
		
		setBorder(null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		setLayout(gridBagLayout);
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		
		add(typeTitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 5), 0, 0));
		add(typeLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));
		
		//---- titleTitleLabel ----
		add(titleTitleLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 5), 0, 0));
		add(titleLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));
		
		//---- yearTitleLabel ----
		add(starsTitleLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 5), 0, 0));
		add(starsPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));
		
		//---- genreTitleLabel ----
		add(genreTitleLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 5), 0, 0));
		add(genreLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));
		
		
		plotTitleLabelPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
		GridBagConstraints gbc_plotTitleLabelPanel = new GridBagConstraints();
		gbc_plotTitleLabelPanel.fill = GridBagConstraints.BOTH;
		gbc_plotTitleLabelPanel.insets = new Insets(0, 0, 0, 5);
		gbc_plotTitleLabelPanel.gridx = 0;
		gbc_plotTitleLabelPanel.gridy = 4;
		add(plotTitleLabelPanel, gbc_plotTitleLabelPanel);
		plotTitleLabelPanel.setLayout(new BorderLayout(0, 0));
		
		plotTitleLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		plotTitleLabelPanel.add(plotTitleLabel);
		plotTitleLabel.setVerticalAlignment(SwingConstants.TOP);
		plotTitleLabel.setText("Plot:");
		
		plotTextScrollPane.setPreferredSize(new Dimension(2, 80));
		plotTextScrollPane.setMaximumSize(new Dimension(32767, 80));
		plotTextScrollPane.setBorder(null);
		plotTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		plotTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_plotTextScrollPane = new GridBagConstraints();
		gbc_plotTextScrollPane.fill = GridBagConstraints.BOTH;
		gbc_plotTextScrollPane.gridx = 1;
		gbc_plotTextScrollPane.gridy = 4;
		add(plotTextScrollPane, gbc_plotTextScrollPane);
		
		plotTextScrollPane.setViewportView(plotText);
		plotText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		plotText.setBackground(UIManager.getColor("control"));
		plotText.setText("long text is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long is long");
		plotText.setEditable(false);
		
		FlowLayout fl_starsPanel = new FlowLayout(FlowLayout.LEFT);
		fl_starsPanel.setVgap(0);
		fl_starsPanel.setHgap(0);
		starsPanel.setLayout(fl_starsPanel);
		
	}
	
	private void initActions() {
		
	}
	
	@EDT public void setTitleDetails(final Title newTitle){
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override public void run() {
				
				setType(null);
				setTitle(null, null);
				setStars(null);
				
				setGenres(null);
				setPlot(null);
				
				if(newTitle == null) return;
				
				setType(newTitle.getTypeString() + (newTitle.getYear() == null ? "" : " (" + newTitle.getYear() + ")"));
				setTitle(newTitle.getTitle(), newTitle.getUrl());
				
				setStars(newTitle.getStars());
				setGenres(newTitle.getGenres());
				setPlot(newTitle.getPlot());
				
			}
		});
		
	}
	
	@EDT public void setType(String type){
		typeLabel.setText(type);
	}
	
	@EDT public void setTitle(final String title, final String url){
		titleLabel.setText(title, url);
	}
	
	@EDT public void setStars(List<Name> stars){
		
		starsPanel.removeAll();
		if(stars == null) return;
		
		final int numStars = stars.size();
		
		int i = 0;
		for(Name star : stars){
			
			HyperlinkLabel linkLabel = new HyperlinkLabel(star.getName(), star.getUrl());
			
			starsPanel.add(linkLabel);
			if(i < numStars - 1) starsPanel.add(new JLabel(", "));
			
			i++;
		}
		
	}
	
	@EDT public void setGenres(List<String> genres){
		genreLabel.setText(genres == null ? "" : StringUtils.join(genres, ", "));
	}
	
	@EDT public void setPlot(String plot){
		plotText.setText(plot);
		// make sure to scroll the JTextPane all the way back up -- by default it gets positioned all the way down
		plotText.setCaretPosition(0);
	}
	
}
