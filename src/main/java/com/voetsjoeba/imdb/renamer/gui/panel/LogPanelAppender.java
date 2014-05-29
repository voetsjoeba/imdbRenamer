package com.voetsjoeba.imdb.renamer.gui.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;


/**
 * Log4j appender for {@link LogPanel}s.
 * 
 * @author Jeroen De Ridder
 */
public class LogPanelAppender extends AppenderSkeleton {
	
	
	protected LogPanel logPanel;
	
	// name of the log panel to direct output to
	protected String logPanelName;
	
	protected List<LoggingEvent> pendingEvents;
	
	public LogPanelAppender(){
		
	}
	
	public void setLogPanel(LogPanel logPanel){
		
		this.logPanel = logPanel;
		flushBuffer();
		
	}
	
	@Override
	protected void append(final LoggingEvent event) {
		
		if(logPanelName == null){
			errorHandler.error("No output log panel name set for the appender named [" + name + "]; cannot log message.");
			return;
		}
		
		if(logPanel == null){
			
			// store temporarily in buffer until log panel is set
			buffer(event);
			return;
			
		}
		
		logPanel.logMessage(layout.format(event));
		
	}
	
	@Override
	public void close() {
		closed = true;
	}
	
	@Override
	public boolean requiresLayout() {
		return true;
	}
	
	/**
	 * Keeps a LoggingEvent around until an output log panel is assigned.
	 */
	private void buffer(LoggingEvent e){
		if(pendingEvents == null) pendingEvents = new ArrayList<LoggingEvent>();
		pendingEvents.add(e);
	}
	
	/**
	 * Called when a log panel is assigned; processes all pending logging events and clears the buffer.
	 */
	private void flushBuffer(){
		
		if(pendingEvents == null || pendingEvents.size() <= 0) return;
		
		for(LoggingEvent pendingEvent : pendingEvents){
			append(pendingEvent);
		}
		
		pendingEvents.clear();
		
	}
	
	public String getLogPanelName() {
		return logPanelName;
	}
	
	public void setLogPanelName(String logPanelName) {
		this.logPanelName = logPanelName;
	}
	
}
