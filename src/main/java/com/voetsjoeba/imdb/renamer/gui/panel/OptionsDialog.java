package com.voetsjoeba.imdb.renamer.gui.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationDialog;

@SuppressWarnings("serial")
public class OptionsDialog extends ApplicationDialog {
	
	private JPanel topPanel;
	private JPanel navigationPanel;
	private JPanel buttonBar;
	private JPanel mainPanel;
	private JPanel titlePanel;
	private JPanel contentPanel;
	
	private JSeparator titleSeparator;
	private JSeparator buttonSeparator;
	private JSeparator navSeparator;
	
	private JScrollPane scrollPane;
	private JTree tree;
	private JLabel title;
	private JButton okButton;
	private JButton cancelButton;
	private JButton applyButton;
	
	public OptionsDialog() {
		
		super(new JFrame());
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents(){
		
		topPanel = new JPanel();
		navigationPanel = new JPanel();
		contentPanel = new JPanel();
		mainPanel = new JPanel();
		titlePanel = new JPanel();
		
		tree = new JTree();
		scrollPane = new JScrollPane();
		
		navSeparator = new JSeparator();
		titleSeparator = new JSeparator();
		buttonSeparator = new JSeparator();
		
		title = new JLabel();
		
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		applyButton = new JButton();
		
	}
	
	private void initLayout() {
		
		setTitle("Options");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		try {
			setIcon(Images.getOptionsIcon());
		}
		catch(Exception e){
			
		}
		
		// LEFT PANEL
		//======== tree ========
		
		tree.setBorder(null);
		tree.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("Options") {
					{
						/*for(String panel : dialogModel.getPanelIdentifiers()){
							DefaultMutableTreeNode node = new DefaultMutableTreeNode(panel);
							node.setUserObject(panel);
							add(node);
						}*/
						DefaultMutableTreeNode node = new DefaultMutableTreeNode();
						node.setUserObject("General");
					}
				}));
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			@Override
			public void valueChanged(TreeSelectionEvent e){
				/*DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(node == null) return;
				String panelIdent = (String) node.getUserObject();
				dialogModel.setPanel(panelIdent);*/
			}
		});
		
		DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
		treeCellRenderer.setLeafIcon(Images.getTreeNoneIcon());
		treeCellRenderer.setOpenIcon(null);
		treeCellRenderer.setClosedIcon(null);
		
		tree.setCellRenderer(treeCellRenderer);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		
		//======== scrollPane ========
		scrollPane.setBorder(null);
		scrollPane.setViewportView(tree);
		
		//======== navigationPanel ========
		GridBagLayout navigationPanelLayout = new GridBagLayout();
		navigationPanelLayout.columnWidths = new int[] {132, 0};
		navigationPanelLayout.columnWeights = new double[] {1.0, 0.0};
		navigationPanelLayout.rowHeights = new int[] {0, 0};
		navigationPanelLayout.rowWeights = new double[] {1.0, 0.0};
		
		navigationPanel.setLayout(navigationPanelLayout);
		navigationPanel.setBackground(Color.white);
		navigationPanel.setBorder(new EmptyBorder(3, 0, 0, 0));
		navigationPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		// TOP PANEL
		//======== title ========
		//title.setText(dialogModel.getCurrentPanelIdentifier());
		title.setText("General");
		title.setFont(title.getFont().deriveFont(Font.BOLD, 14.0f));
		
		//======== titlePanel ========
		titlePanel.setBorder(new EmptyBorder(7, 9, 7, 7));
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
		titlePanel.add(title);
		
		//======== contentPanel ========
		CardLayout cardLayout = new CardLayout();
		contentPanel.setLayout(cardLayout);
		
		/*Iterator<Map.Entry<String, OptionsDialogPanel>> iterator = dialogModel.getPanels().entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, OptionsDialogPanel> entry = iterator.next();
			contentPanel.add(entry.getValue(), entry.getKey());
		}*/
		
		
		//======== mainPanel ========
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.columnWidths = new int[]{0, 0};
		mainPanelLayout.columnWeights = new double[]{1.0, 0.0};
		mainPanelLayout.rowHeights = new int[]{0, 0, 0, 0};
		mainPanelLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0};
		
		mainPanel.setLayout(mainPanelLayout);
		mainPanel.add(titlePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(titleSeparator, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(contentPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		//======== navSeparator ========
		navSeparator.setOrientation(SwingConstants.VERTICAL);
		
		//======== topPanel ========
		GridBagLayout topPanelLayout = new GridBagLayout();
		topPanelLayout.columnWidths = new int[]{108,0,0,0};
		topPanelLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
		topPanelLayout.rowHeights = new int[]{0,0};
		topPanelLayout.rowWeights = new double[]{1.0, 0.0};
		
		topPanel.setLayout(topPanelLayout);
		topPanel.add(navigationPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		topPanel.add(navSeparator, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		topPanel.add(mainPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
		// BUTTON PANEL
		//======== okButton ========
		okButton.setText("OK");
		
		//======== cancelButton ========
		cancelButton.setText("Cancel");
		
		//======== applyButton ========
		applyButton.setText("Apply");
		
		//======== buttonBar ========
		GridBagLayout buttonBarLayout = new GridBagLayout();
		buttonBarLayout.columnWidths = new int[]{0, 85, 85, 80};
		buttonBarLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0};
		
		buttonBar.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonBar.setLayout(buttonBarLayout);
		buttonBar.add(applyButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
		buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
		buttonBar.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
		//======== this ========
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{0,0};
		layout.columnWeights = new double[]{1.0, 0.0};
		layout.rowHeights = new int[]{0,0,0,0};
		layout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0};
		setLayout(layout);
		
		Container contentPane = getContentPane();
		contentPane.add(buttonBar, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		contentPane.add(topPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		contentPane.add(buttonSeparator, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		setSize(685, 500);
		setLocationRelativeTo(Application.getInstance().getMainWindow());
		
	}
	
	private void initActions(){
		
		//addWindowListener(this);
		
		okButton.addActionListener(new ActionListener(){@Override
		public void actionPerformed(ActionEvent e){ok();}});
		cancelButton.addActionListener(new ActionListener(){@Override
		public void actionPerformed(ActionEvent e){cancel();}});
		//applyButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){apply();}});
		
	}
	
	public void ok(){
		//if(applyButton.isEnabled()) apply();
		close();
	}
	
	public void cancel(){
		close();
	}
	
	/*public void setVisible(boolean visible){
		super.setVisible(visible);
	}*/
	
	
}
