package com.voetsjoeba.imdb.renamer.gui.panel;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.voetsjoeba.imdb.renamer.gui.action.ClearLogPanelAction;

/**
 * Logging panel; can display log messages.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class LogPanel extends JPanel implements MouseListener {
	
	protected String logPanelName; // used to match LogPanels to LogPanelAppenders
	
	protected JScrollPane logScrollPanel;
	protected JTextPane logTextPane;
	
	public LogPanel(String logPanelName) {
		
		if(logPanelName == null) throw new IllegalArgumentException("Log panel name must be non-null");
		this.logPanelName = logPanelName;
		
		initComponents();
		initLayout();
		initActions();
		
	}
	
	private void initComponents() {
		
		logScrollPanel = new JScrollPane();
		logTextPane = new JTextPane();
		
		logTextPane.setEditable(false);
		// TODO: enable cut/copy/paste right-click actions on text pane
		
	}
	
	private void initLayout() {
		
		logScrollPanel.setViewportView(logTextPane);
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		add(logScrollPanel, BorderLayout.CENTER);
		
	}
	
	@SuppressWarnings("rawtypes")
	private void initActions() {
		
		logTextPane.addMouseListener(this);
		
		// since there is no way to get a list of configured appenders from log4j directly
		// (appenders are assigned to loggers separately, no global list is maintained),
		// we need to loop over all current loggers and find if there are any that have a
		// LogPanelAppender assigned to them.
		
		Map<String, LogPanelAppender> logPanelAppenders = new HashMap<String, LogPanelAppender>();
		
		List<Logger> loggers = new ArrayList<Logger>();
		loggers.add(LogManager.getRootLogger());
		
		Enumeration loggersEnumeration = LogManager.getLoggerRepository().getCurrentLoggers();
		while(loggersEnumeration.hasMoreElements()) loggers.add((Logger) loggersEnumeration.nextElement());
		
		
		for(Logger logger : loggers){
			
			Enumeration appenders = logger.getAllAppenders();
			
			while(appenders.hasMoreElements()){
				
				Appender appender = (Appender) appenders.nextElement();
				if(appender instanceof LogPanelAppender){
					logPanelAppenders.put(appender.getName(), (LogPanelAppender) appender);
				}
				
			}
			
		}
		
		for(LogPanelAppender logPanelAppender : logPanelAppenders.values()){
			
			String appenderLogPanelName = logPanelAppender.getLogPanelName();
			if(logPanelName.equals(appenderLogPanelName)){
				
				// register this log panel to the appender
				logPanelAppender.setLogPanel(this);
				
			}
			
		}
		
	}
	
	public void logMessage(final String message) {
		
		// run in GUI thread
		if(!SwingUtilities.isEventDispatchThread()){
			
			try {
				
				/*
				 * Here invokeAndWait must be used rather than invokeLater. JTextArea.append is thread-safe by means of taking
				 * a lock on the underlying Document instance. At initialization time (ie. main thread executing public static 
				 * void main(String args)), log calls may be made prior to a call to JFrame.setVisible(true). During this 
				 * setVisible call, the JTextArea is queried for its preferred size; to answer the query, it will read from its
				 * contents. And, since reading and writing to a JTextArea is thread-safe, it will try to acquire a read lock on
				 * the underlying document which will conflict with the event thread trying to write to the JTextArea, causing a
				 * deadlock. In order to prevent this from happening, we must execute the writing to the JTextArea synchronously,
				 * so that all writing to the JTextArea is necessarily completed by the time setVisible is executed.
				 */
				
				SwingUtilities.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						logMessage(message);
					}
				});
				
			}
			catch(InterruptedException e) {
				throw new Error(e);
			}
			catch(InvocationTargetException e) {
				throw new Error(e);
			}
			
			return;
			
		}
		
		logTextPane.setText(logTextPane.getText() + message);
		
		// not getText().length() - setCaretPosition used the document length to bound its argument instead of 
		// the text length, and the document length and text length can differ for god-knows-why. Probably multibyte
		// characters or automatic \r\n'ing of \n's or something, I don't know.
		logTextPane.setCaretPosition(logTextPane.getDocument().getLength() - 1);
		
	}
	
	public void clear(){
		
		if(!SwingUtilities.isEventDispatchThread()){
			
			try {
				
				SwingUtilities.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						clear();
					}
				});
				
			}
			catch(InterruptedException e) {
				throw new Error(e);
			}
			catch(InvocationTargetException e) {
				throw new Error(e);
			}
			
		}
		
		logTextPane.setText("");
		
	}
	
	public String getLogPanelName() {
		return logPanelName;
	}
	
	public void setLogPanelName(String logPanelName) {
		this.logPanelName = logPanelName;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(SwingUtilities.isRightMouseButton(e) || e.isPopupTrigger()){
			//if(e.isPopupTrigger()){
			
			JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(new ClearLogPanelAction(this));
			popupMenu.show(logTextPane, e.getX(), e.getY());
			
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
}
