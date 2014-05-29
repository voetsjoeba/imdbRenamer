package com.voetsjoeba.imdb.renamer.gui.panel.info;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.voetsjoeba.imdb.domain.api.Title;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.TitleSelectedEvent;
import com.voetsjoeba.imdb.renamer.event.TitleSelectionListener;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel implements TitleSelectionListener {
	
	protected InfoDetailsPanel detailsPanel;
	protected InfoImagePanel imagePanel;
	
	public InfoPanel(){
		initComponents();
		initLayout();
		initActions();
	}
	
	private void initComponents(){
		detailsPanel = new InfoDetailsPanel();
		detailsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		imagePanel = new InfoImagePanel();
	}
	
	private void initLayout(){
		
		setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(0, 0, 0, 0)));
		
		setLayout(new GridBagLayout());
		((GridBagLayout) getLayout()).columnWidths = new int[] {0, 100, 0};
		((GridBagLayout) getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout) getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
		((GridBagLayout) getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
		
		add(detailsPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		
		add(imagePanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0), 0, 0));
		
	}
	
	private void initActions(){
		
		Application application = Application.getInstance();
		application.getTitleModel().addTitleSelectionListener(this);
		
		updateTitleInfo();
		
	}
	
	public void updateTitleInfo(){
		
		Application application = Application.getInstance();
		final Title newTitle = application.getTitleModel().getTitle();
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override public void run() {
				detailsPanel.setTitleDetails(newTitle);
				imagePanel.setTitleThumbnail(newTitle);
			}
		});
		
	}
	
	@Override
	public void titleSelected(TitleSelectedEvent e) {
		updateTitleInfo();
	}
	
}
