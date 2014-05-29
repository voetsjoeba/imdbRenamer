package com.voetsjoeba.imdb.renamer.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ExceptionTestPanel extends JPanel {
	
	private JButton btnEdt;
	private JButton btnWorker;
	private JButton btnEdtStackOverflow;
	public ExceptionTestPanel() {
		
		btnEdt = new JButton("EDT");
		add(btnEdt);
		
		btnWorker = new JButton("Worker (2)");
		add(btnWorker);
		
		btnEdtStackOverflow = new JButton("EDT Stack Overflow");
		add(btnEdtStackOverflow);
		
		btnEdt.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						throw new RuntimeException("EDT Test Exception");
					}
				});
			}
		});
		
		btnWorker.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						throw new RuntimeException("Worker Test Exception 1");
					}
				}).start();
				
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						}
						catch(InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						throw new RuntimeException("Worker Test Exception 2");
					}
				}).start();
			}
		});
		
		btnEdtStackOverflow.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				actionPerformed(e); // stack overflow
			}
		});
		
	}
	
}
