package com.voetsjoeba.imdb.renamer.gui.generic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voetsjoeba.imdb.renamer.gui.action.generic.CloseDialogAction;
import com.voetsjoeba.imdb.renamer.util.EDT;
import com.voetsjoeba.imdb.util.HttpUtils;

/**
 * Displays a dialog to the user containing information about an error that has occurred, and to 
 * submit a problem report.
 * 
 * @author Jeroen
 */
@SuppressWarnings("serial")
public class CrashReportDialog extends ApplicationDialog {
	
	private static final Logger log = LoggerFactory.getLogger(CrashReportDialog.class);
	private static final String crashReportUrl = "http://www.voetsjoeba.com/imdbRenamer/crashReport";
	
	private final JPanel contentPanel = new JPanel();
	
	protected JLabel errorIconLabel;
	protected JPanel mainPanel;
	protected LineWrappedPlaintextLabel infoLabel;
	protected JPanel exceptionInfoPanel;
	protected JScrollPane moreInfoScrollPane;
	protected JTextArea moreInfoTextPane;
	protected JPanel userFeedbackPanel;
	protected JPanel buttonPane;
	protected JButton cancelButton;
	protected JButton sendButton;
	protected LineWrappedPlaintextLabel describeLabel;
	protected LineWrappedPlaintextLabel moreInfoLabel;
	protected JEditorPane userFeedbackEditorPane;
	
	/**
	 * Create the dialog.
	 */
	public CrashReportDialog() {
		initComponents();
		initLayout();
		initActions();
	}
	
	private void initComponents() {
		
		errorIconLabel = new JLabel();
		mainPanel = new JPanel();
		infoLabel = new LineWrappedPlaintextLabel("An unexpected exception has occurred in this application, and it must now be shut down. We've prepared an error report containing details of the crash, so that the problem can be investigated and fixed in a future version.");
		describeLabel = new LineWrappedPlaintextLabel();
		moreInfoLabel = new LineWrappedPlaintextLabel();
		
		exceptionInfoPanel = new JPanel();
		moreInfoScrollPane = new JScrollPane();
		moreInfoTextPane = new JTextArea();
		userFeedbackPanel = new JPanel();
		userFeedbackEditorPane = new JEditorPane();
		buttonPane = new JPanel();
		cancelButton = new JButton(new CloseDialogAction("Cancel", ApplicationDialogConstants.EXIT_CANCEL, this));
		
		sendButton = new JButton("Send Report");
		
	}
	

	private void initLayout(){
		
		setTitle("Unexpected Problem");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 607, 569);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			
			errorIconLabel.setVerticalAlignment(SwingConstants.TOP);
			errorIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
			GridBagConstraints gbc_errorIconLabel = new GridBagConstraints();
			gbc_errorIconLabel.anchor = GridBagConstraints.NORTH;
			gbc_errorIconLabel.insets = new Insets(5, 0, 0, 10);
			gbc_errorIconLabel.gridx = 0;
			gbc_errorIconLabel.gridy = 0;
			contentPanel.add(errorIconLabel, gbc_errorIconLabel);
		}
		{
			
			GridBagConstraints gbc_mainPanel = new GridBagConstraints();
			gbc_mainPanel.fill = GridBagConstraints.BOTH;
			gbc_mainPanel.gridx = 1;
			gbc_mainPanel.gridy = 0;
			contentPanel.add(mainPanel, gbc_mainPanel);
			GridBagLayout gbl_mainPanel = new GridBagLayout();
			gbl_mainPanel.columnWidths = new int[]{0, 0};
			gbl_mainPanel.rowHeights = new int[]{0, 150, 120, 0};
			gbl_mainPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_mainPanel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			mainPanel.setLayout(gbl_mainPanel);
			{
				
				GridBagConstraints gbc_infoLabel = new GridBagConstraints();
				gbc_infoLabel.fill = GridBagConstraints.HORIZONTAL;
				gbc_infoLabel.anchor = GridBagConstraints.NORTHWEST;
				gbc_infoLabel.insets = new Insets(0, 0, 5, 0);
				gbc_infoLabel.gridx = 0;
				gbc_infoLabel.gridy = 0;
				mainPanel.add(infoLabel, gbc_infoLabel);
			}
			{
				
				GridBagConstraints gbc_exceptionInfoPanel = new GridBagConstraints();
				gbc_exceptionInfoPanel.insets = new Insets(0, 0, 5, 0);
				gbc_exceptionInfoPanel.fill = GridBagConstraints.BOTH;
				gbc_exceptionInfoPanel.gridx = 0;
				gbc_exceptionInfoPanel.gridy = 1;
				mainPanel.add(exceptionInfoPanel, gbc_exceptionInfoPanel);
				exceptionInfoPanel.setLayout(new BorderLayout(0, 5));
				{
					
					moreInfoLabel.setText("Technical information about the problem:");
					exceptionInfoPanel.add(moreInfoLabel, BorderLayout.NORTH);
				}
				{
					
					moreInfoScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
					moreInfoScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
					moreInfoScrollPane.setMaximumSize(new Dimension(32767, 200));
					
					
					exceptionInfoPanel.add(moreInfoScrollPane, BorderLayout.CENTER);
					{
						moreInfoTextPane.setBackground(SystemColor.control);
						moreInfoTextPane.setAlignmentY(Component.TOP_ALIGNMENT);
						moreInfoTextPane.setAlignmentX(Component.LEFT_ALIGNMENT);
						moreInfoTextPane.setText("org.omg.CORBA.MARSHAL: com.ibm.ws.pmi.server.DataDescriptor; IllegalAccessException  minor code: 4942F23E  completed: No \t\r\n         at com.ibm.rmi.io.ValueHandlerImpl.readValue(ValueHandlerImpl.java:199)  \r\n         at com.ibm.rmi.iiop.CDRInputStream.read_value(CDRInputStream.java:1429)  \r\n         at com.ibm.rmi.io.ValueHandlerImpl.read_Array(ValueHandlerImpl.java:625)  \r\n         at com.ibm.rmi.io.ValueHandlerImpl.readValueInternal(ValueHandlerImpl.java:273) \t\r\n         at com.ibm.rmi.io.ValueHandlerImpl.readValue(ValueHandlerImpl.java:189)  \r\n         at com.ibm.rmi.iiop.CDRInputStream.read_value(CDRInputStream.java:1429) \t\r\n         at com.ibm.ejs.sm.beans._EJSRemoteStatelessPmiService_Tie._invoke(_EJSRemoteStatelessPmiService_Tie.java:613)  \r\n         at com.ibm.CORBA.iiop.ExtendedServerDelegate.dispatch(ExtendedServerDelegate.java:515)  \r\n         at com.ibm.CORBA.iiop.ORB.process(ORB.java:2377) \t\r\n         at com.ibm.CORBA.iiop.OrbWorker.run(OrbWorker.java:186) \t\r\n         at com.ibm.ejs.oa.pool.ThreadPool$PooledWorker.run(ThreadPool.java:104)  \r\n         at com.ibm.ws.util.CachedThread.run(ThreadPool.java:137)");
						moreInfoTextPane.setEditable(false);
						moreInfoTextPane.setFont(UIManager.getFont("TextPane.font")); // use a TextPane font instead of huge and ugly monospace 
						moreInfoTextPane.setWrapStyleWord(true);
						moreInfoTextPane.setTabSize(4);
						
						moreInfoScrollPane.setViewportView(moreInfoTextPane);
						
					}
				}
			}
			{
				
				GridBagConstraints gbc_userFeedbackPanel = new GridBagConstraints();
				gbc_userFeedbackPanel.fill = GridBagConstraints.BOTH;
				gbc_userFeedbackPanel.gridx = 0;
				gbc_userFeedbackPanel.gridy = 2;
				mainPanel.add(userFeedbackPanel, gbc_userFeedbackPanel);
				userFeedbackPanel.setLayout(new BorderLayout(0, 5));
				{
					JScrollPane userFeedbackScrollPane = new JScrollPane();
					userFeedbackPanel.add(userFeedbackScrollPane);
					{
						userFeedbackScrollPane.setViewportView(userFeedbackEditorPane);
					}
				}
				{
					describeLabel.setText("What were you doing when the error occurred?");
					userFeedbackPanel.add(describeLabel, BorderLayout.NORTH);
				}
			}
		}
		{
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			GridBagLayout gbl_buttonPane = new GridBagLayout();
			gbl_buttonPane.columnWidths = new int[]{216, 70, 65, 0};
			gbl_buttonPane.rowHeights = new int[]{23, 0};
			gbl_buttonPane.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_buttonPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			buttonPane.setLayout(gbl_buttonPane);
			buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
			
			{
				
				//sendButton.setActionCommand("OK");
				GridBagConstraints gbc_sendButton = new GridBagConstraints();
				gbc_sendButton.anchor = GridBagConstraints.NORTHEAST;
				gbc_sendButton.insets = new Insets(0, 0, 0, 5);
				gbc_sendButton.gridx = 1;
				gbc_sendButton.gridy = 0;
				buttonPane.add(sendButton, gbc_sendButton);
				getRootPane().setDefaultButton(sendButton);
			}
		}
		
		{
			
			//cancelButton.setActionCommand("Cancel");
			GridBagConstraints gbc_cancelButton = new GridBagConstraints();
			gbc_cancelButton.anchor = GridBagConstraints.NORTHWEST;
			gbc_cancelButton.gridx = 2;
			gbc_cancelButton.gridy = 0;
			buttonPane.add(cancelButton, gbc_cancelButton);
		}
		
	}
	
	private void initActions(){
		
		addWindowListener(this);
		
	}
	
	public void setException(final Throwable t) throws InterruptedException, InvocationTargetException{
		
		// TODO: this is gonna get called when a runtimeexception has already occurred -- not sure if this can still 
		// complete normally
		
		if(SwingUtilities.isEventDispatchThread()){
			doSetException(t);
		} else {
			// TODO: for some reason, invokeAndWait works when this handler is called before MainWindow is visible
			// and invokeLater doesn't. Not sure why, but hey, more power to it if it works.
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override public void run() {
					doSetException(t);
				}
			});
		}
	}
	
	@EDT protected void doSetException(final Throwable t){
		
		String stackTrace = ExceptionUtils.getFullStackTrace(t);
		
		moreInfoTextPane.setText(stackTrace);
		//sendReportAction.setThrowable(t); // enables the action
		
		sendButton.setEnabled(true);
		sendButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				
				sendButton.setText("Sending...");
				sendButton.setEnabled(false);
				cancelButton.setEnabled(false);
				
				new Thread(new Runnable(){
					@Override public void run() {
						
						boolean sendSuccess = false;
						try {
							doSendReport(t);
							sendSuccess = true;
						}
						catch(Throwable e){
							// we cannot afford to throw another exception here -- we're in the exception handler
							log.error("Failed to send error report", e);
						}
						
						final String resultText = (sendSuccess ? "Report sent. Exiting..." : "Send failed. Exiting...");
						
						try {
							SwingUtilities.invokeAndWait(new Runnable(){
								@Override public void run() {
									sendButton.setText(resultText);
								}
							});
							Thread.sleep(2500);
						}
						catch(Throwable e2){
							// ignore
						}
						
						System.exit(1);
						
					}
				}).start();
				
			}
		});
		
		repaint();
		
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		// simulate EXIT_ON_CLOSE
		System.exit(1);
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		// the cancel button closes the dialog but apparently doesn't trigger windowClosing, so we need this one here
		// to make sure it gets caught
		System.exit(1);
	}
	
	/**
	 * Resets the dialog for use with a new exception.
	 */
	public void reset(){
		
	}
	
	protected void doSendReport(Throwable t) throws HttpException, ParseException, IOException {
		
		log.info("Sending crash report");
		
		StringBuffer errorReportBuffer = new StringBuffer();
		errorReportBuffer.append(">>>stacktrace\n");
		errorReportBuffer.append(ExceptionUtils.getFullStackTrace(t));
		errorReportBuffer.append(">>>system\n");
		for(Map.Entry<Object, Object> p : System.getProperties().entrySet()){
			errorReportBuffer.append(p.getKey());
			errorReportBuffer.append("\t");
			errorReportBuffer.append(p.getValue());
			errorReportBuffer.append("\n");
		}
		errorReportBuffer.append(">>>user_context\n");
		errorReportBuffer.append(userFeedbackEditorPane.getText());
		
		final String errorReport = errorReportBuffer.toString();
		log.info("Error report:\n{}", errorReport);
		
		// ----------------------------------------------------------------------------------
		
		// create new default http client and disable cookies
		HttpClient httpClient = HttpUtils.getNoCookiesHttpClient();
		
		// build POST request
		HttpPost request = new HttpPost(crashReportUrl);
		HttpEntity postData = new StringEntity(errorReport);
		request.setEntity(postData);
		
		// send request
		HttpResponse response = httpClient.execute(request);
		
		// make sure we got a 200 OK response
		StatusLine responseStatusLine = response.getStatusLine();
		if(responseStatusLine.getStatusCode() != HttpStatus.SC_OK){
			throw new HttpException("Response returned status code that is not 200 OK (returned: " + responseStatusLine.getStatusCode() + " " + responseStatusLine.getReasonPhrase() + ")");
		}
		
		// get the response as a properly character-decoded string
		HttpEntity responseContent = response.getEntity();
		String responseString = EntityUtils.toString(responseContent);
		log.info("Crash report response: {}", responseString);
		
		responseContent.consumeContent();
		
		log.info("Report sent.");
		
	}
}
