package com.voetsjoeba.imdb.renamer.gui.panel.registry;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.domain.api.BaseTitle;
import com.voetsjoeba.imdb.domain.api.Title;

/**
 * Single entry in a {@link TitleRegistryViewerList}.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class TitleRegistryViewerItem extends JPanel implements ListCellRenderer<BaseTitle> {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(TitleRegistryViewerItem.class);
	private static final RenderFailureLabel renderFailureLabel = new RenderFailureLabel("Failed to render entry");
	
	private JPanel iconPanel;
	private JLabel iconLabel;
	
	private JPanel infoPanel;
	private JLabel titleLabel;
	private JLabel typeLabel;
	private JLabel infoLabel;
	
	public TitleRegistryViewerItem(){
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	protected void initComponents() {
		
		iconPanel = new JPanel();
		iconLabel = new JLabel();
		
		infoPanel = new JPanel();
		titleLabel = new JLabel();
		typeLabel = new JLabel();
		infoLabel = new JLabel();
		
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
		
		iconPanel.setOpaque(false);
		infoPanel.setOpaque(false);
		
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		
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
			iconPanel.setLayout(new GridBagLayout());
			((GridBagLayout)iconPanel.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)iconPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)iconPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)iconPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
			
			//---- filenameLabel ----
			iconPanel.add(iconLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			
		}
		add(iconPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		//======== renamePanel ========
		{
			infoPanel.setLayout(new GridBagLayout());
			((GridBagLayout)infoPanel.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)infoPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
			((GridBagLayout)infoPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)infoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
			
			//---- renamedTitleLabel ----
			infoPanel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			
			infoPanel.add(typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 2, 0, 0), 0, 0));
			
			infoPanel.add(infoLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 2, 0, 0), 0, 0));
			
		}
		add(infoPanel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 0, 0), 0, 0));
		
	}
	
	protected void initActions(){
		
		//Application.getInstance().getTitleModel().addTitleSelectedListener(this);
		
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends BaseTitle> list, BaseTitle baseTitle, int index, boolean isSelected, boolean cellHasFocus) {
		
		if(baseTitle == null)
			return renderFailureLabel;
		
		if(isSelected) {
			setBackground(list.getSelectionBackground());
		} else {
			setBackground(list.getBackground());
		}
		
		// set image
		if(baseTitle instanceof Title){
			
			Title title = (Title) baseTitle;
			Image thumbnail = title.getThumbnail();
			
			if(thumbnail != null){
				
				Image resizedThumbnail = thumbnail.getScaledInstance(-1, 100, Image.SCALE_SMOOTH);
				ImageIcon thumbnailImageIcon = new ImageIcon(resizedThumbnail);
				
				iconLabel.setIcon(thumbnailImageIcon);
			}
			
		}
		
		// set info
		titleLabel.setText(baseTitle.getTitle());
		typeLabel.setText(baseTitle.getUrl()); // TODO: fill in some proper info here
		//infoLabel.setText("24 episodes");
		
		return this;
		
	}
	
}
