package com.voetsjoeba.imdb.renamer.gui.panel.search;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.LimitedTitle;
import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.event.LimitedTitleRegistryListener;
import com.voetsjoeba.imdb.renamer.event.PrefetchListener;
import com.voetsjoeba.imdb.renamer.event.PrefetchProgressEvent;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleAddedEvent;
import com.voetsjoeba.imdb.renamer.event.RegistryTitleRemovedEvent;
import com.voetsjoeba.imdb.renamer.gui.action.SearchAction;
import com.voetsjoeba.imdb.renamer.gui.generic.HyperlinkLabel;
import com.voetsjoeba.imdb.renamer.registry.LimitedTitleRegistry;

/**
 * Top GUI row component; holds the search bar/button and recently used links.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel implements LimitedTitleRegistryListener, PrefetchListener {
	
	private static final Logger log = LoggerFactory.getLogger(SearchPanel.class);
	
	private JButton searchButton;
	private JLabel findLabel;
	private SearchField searchField;
	
	protected SearchAction searchAction;
	private JPanel subBar;
	private JLabel labelRecentlyUsed;
	private JPanel recentlyUsedPanel;
	
	private int maxRecentlyUsed = 8;
	
	public SearchPanel(){
		initComponents();
		initLayout();
		initActions();
	}
	
	protected void initComponents() {
		
		searchField = new SearchField();
		findLabel = new JLabel();
		subBar = new JPanel();
		recentlyUsedPanel = new JPanel();
		labelRecentlyUsed = new JLabel("Recently used: ");
		
		searchAction = new SearchAction(searchField);
		searchButton = new JButton(searchAction);
		
		// TODO: make it so hitting enter on the combobox fires the default button instead of "confirming" the typed combobox text
		
	}
	
	protected void initLayout() {
		
		//======== searchPanel ========
		{
			setBorder(new CompoundBorder(
					new TitledBorder("Search"),
					new EmptyBorder(5, 5, 5, 5)));
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWeights = new double[]{0.0, 1.0};
			setLayout(gridBagLayout);
			((GridBagLayout) getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout) getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout) getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
			((GridBagLayout) getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
			
			//---- findLabel ----
			findLabel.setText("Find:");
			
			add(findLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
			
			add(searchField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
			
			
			GridBagConstraints gbc_subBar = new GridBagConstraints();
			gbc_subBar.fill = GridBagConstraints.BOTH;
			gbc_subBar.gridx = 0;
			gbc_subBar.gridy = 1;
			gbc_subBar.gridwidth = 2;
			add(subBar, gbc_subBar);
			GridBagLayout gbl_subBar = new GridBagLayout();
			gbl_subBar.columnWidths = new int[]{251, 0, 0};
			gbl_subBar.rowHeights = new int[]{23, 0};
			gbl_subBar.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_subBar.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			subBar.setLayout(gbl_subBar);
			
			
			GridBagConstraints gbc_recentlyUsedPanel = new GridBagConstraints();
			gbc_recentlyUsedPanel.fill = GridBagConstraints.BOTH;
			gbc_recentlyUsedPanel.insets = new Insets(0, 0, 0, 5);
			gbc_recentlyUsedPanel.gridx = 0;
			gbc_recentlyUsedPanel.gridy = 0;
			subBar.add(recentlyUsedPanel, gbc_recentlyUsedPanel);
			recentlyUsedPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
			
			
			recentlyUsedPanel.add(labelRecentlyUsed);
			GridBagConstraints gbc_searchButton = new GridBagConstraints();
			gbc_searchButton.fill = GridBagConstraints.BOTH;
			gbc_searchButton.gridx = 1;
			gbc_searchButton.gridy = 0;
			subBar.add(searchButton, gbc_searchButton);
			
			//---- searchButton ----
			searchButton.setText("Search");
			searchButton.setMnemonic('S');
		}
		
	}
	
	protected void initActions() {
		
		LimitedTitleRegistry registry = Application.getInstance().getTitleRegistry();
		registry.addLimitedTitleRegistryListener(this);
		Application.getInstance().addPrefetchListener(this);
		
		// perform initial update
		updateRecentlyUsedTitles();
		
	}
	
	@Override
	public void prefetchProgress(PrefetchProgressEvent e) {
		int progress = e.getSource().getPrefetchCompleted();
		int total = e.getSource().getPrefetchTotal();
		log.debug("Prefetching progress: {} of {}", progress, total);
	}
	
	/**
	 * Returns the button used to perform the search. This is only here so the main window can set it as its default button.
	 */
	public JButton getSearchButton() {
		return searchButton;
	}
	
	
	@Override
	public void registryTitleAdded(RegistryTitleAddedEvent e) {
		updateRecentlyUsedTitles();
	}
	
	@Override
	public void registryTitleRemoved(RegistryTitleRemovedEvent e) {
		updateRecentlyUsedTitles();
	}
	
	protected void updateRecentlyUsedTitles(){
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				
				recentlyUsedPanel.removeAll();
				recentlyUsedPanel.add(labelRecentlyUsed);
				
				LimitedTitleRegistry registry = Application.getInstance().getTitleRegistry();
				final int numEntries = Math.min(maxRecentlyUsed, registry.size());
				
				int i = 0;
				for(final LimitedTitle title : registry){
					
					HyperlinkLabel linkLabel = new HyperlinkLabel(
						title.getTitle(),
						title.getUrl(),
						new ActionListener(){
							@Override public void actionPerformed(ActionEvent e) {
								
								// do a lookup for the limited title's ID (in a separate thread)
								// TODO: abort any previous lookup (eg. if the user misclicked and the right one is already
								// loaded in cache, then the wrong one is gonna jump in at the last minute, maybe right before
								// the user clicks the "Perform Rename" button)
								new Thread(new Runnable(){
									@Override public void run() {
										Application.getInstance().searchAndLoad(title);
									}
								}).start();
								
							}
						}
					);
					linkLabel.setToolTipText("<html><body><b>"+title.getTitle() + " (" + title.getYear() + ")</b><br>" + title.getUrl()+"</body></html>");
					
					recentlyUsedPanel.add(linkLabel);
					if(i < numEntries - 1) recentlyUsedPanel.add(new JLabel(", "));
					
					i++;
					if(i >= numEntries) break;
					
				}
				
				repaint();
				
			}
		});
		
	}
	
}
