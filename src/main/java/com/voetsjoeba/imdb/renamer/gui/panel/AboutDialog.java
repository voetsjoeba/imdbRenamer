package com.voetsjoeba.imdb.renamer.gui.panel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.voetsjoeba.imdb.renamer.Application;
import com.voetsjoeba.imdb.renamer.gui.Images;
import com.voetsjoeba.imdb.renamer.gui.action.OkCloseDialogAction;
import com.voetsjoeba.imdb.renamer.gui.generic.ApplicationDialog;

/**
 * The About dialog.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class AboutDialog extends ApplicationDialog implements MouseListener {
	
	private JPanel imagePanel;
	private JLabel imageLabel;
	private JPanel rightPanel;
	private JPanel contentPanel;
	private JPanel titlePanel;
	private JLabel titleLabel;
	private JPanel textPanel;
	private JLabel textLabel;
	private JPanel buttonBar;
	private JButton okButton;
	
	public AboutDialog(){
		init();
	}
	
	protected void init(){
		initComponents();
		initLayout();
		initActions();
	}
	
	private void initComponents() {
		
		imagePanel = new JPanel();
		imageLabel = new JLabel();
		rightPanel = new JPanel();
		contentPanel = new JPanel();
		titlePanel = new JPanel();
		titleLabel = new JLabel();
		textPanel = new JPanel();
		textLabel = new JLabel();
		
		buttonBar = new JPanel();
		okButton = new JButton(new OkCloseDialogAction(this));
		
		//======== this ========
		
		setResizable(false);
		setTitle("About " + Application.getInstance().getName());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		//titleLabel.setText(Application.getInstance().getName());
		titleLabel.setIcon(Images.getNameIcon());
		textLabel.setText("<html>\n"+Application.getInstance().getName()+" is a small tool to automatically rename movie/series video files using information fetched from IMDb. It is intended to be used by people who have many videos of various series and/or movies laying around and wish to assign them consistent filenames. <br><br>Icons courtesy of <a href=\"http://www.famfamfam.com\">FamFamFam</a>.<br><br>&copy; 2010 Jeroen De Ridder<br><a href=\"\">http://www.voetsjoeba.com</a></html>");
		
	}
	
	private void initLayout(){
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		
		//======== imagePanel ========
		{
			imagePanel.setLayout(new GridBagLayout());
			((GridBagLayout)imagePanel.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)imagePanel.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)imagePanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)imagePanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
			
			
			//---- imageLabel ----
			imageLabel.setIcon(Images.getAboutDialogIcon());
			imagePanel.add(imageLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 0, 0));
			
		}
		contentPane.add(imagePanel, BorderLayout.WEST);
		
		//======== rightPanel ========
		{
			rightPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
			rightPanel.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};
				
				//======== titlePanel ========
				{
					titlePanel.setBorder(new EmptyBorder(3, 3, 3, 3));
					titlePanel.setLayout(new GridBagLayout());
					((GridBagLayout)titlePanel.getLayout()).columnWidths = new int[] {0, 0};
					((GridBagLayout)titlePanel.getLayout()).rowHeights = new int[] {0, 0};
					((GridBagLayout)titlePanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
					((GridBagLayout)titlePanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
					
					//---- titleLabel ----
					titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
					
					titlePanel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(0, 0, 0, 0), 0, 0));
					
				}
				contentPanel.add(titlePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				
				//======== textPanel ========
				{
					textPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
					textPanel.setLayout(new GridLayout(1, 1));
					textPanel.setMaximumSize(new Dimension(300,0));
					
					//---- textLabel ----
					textLabel.setVerticalAlignment(SwingConstants.TOP);
					textPanel.add(textLabel);
					
				}
				contentPanel.add(textPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 0, 0, 0), 0, 0));
			}
			rightPanel.add(contentPanel, BorderLayout.CENTER);
			
			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(5, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};
				
				//---- okButton ----
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
			}
			rightPanel.add(buttonBar, BorderLayout.SOUTH);
		}
		
		contentPane.add(rightPanel, BorderLayout.CENTER);
		
		Dimension preferredSize = this.getPreferredSize();
		Dimension fixedSize = new Dimension(600, (int) preferredSize.getHeight());
		setSize(fixedSize);
		
		//pack();
		setLocationRelativeTo(getOwner());
		
	}
	
	private void initActions(){
		addMouseListener(this);
	}
	
	@Override public void mouseClicked(MouseEvent e) {
		//close(); // TODO: not sure if this will also close the dialog when clicking the links
	}
	
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
	
	
}
