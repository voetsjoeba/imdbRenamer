package com.voetsjoeba.imdb.renamer.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>Bare Bones Browser Launch for Java</b><br>
 * Utility class to open a web page from a Swing application in the user's default browser.<br>
 * Supports: Mac OS X, GNU/Linux, Unix, Windows XP/Vista<br>
 * Example Usage:<code><br> &nbsp; &nbsp;
 *    String url = "http://www.google.com/";<br> &nbsp; &nbsp;
 *    BareBonesBrowserLaunch.openURL(url);<br></code> Latest Version: <a
 * href="http://www.centerkey.com/java/browser/">www.centerkey.com/java/browser</a><br>
 * Author: Dem Pilafian<br>
 * Public Domain Software -- Free to Use as You Like
 * 
 * @version 2.0, May 26, 2009
 */
public class BrowserLauncher {
	
	private static final Logger log = LoggerFactory.getLogger(BrowserLauncher.class);
	
	static final String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "seamonkey", "galeon", "kazehakase", "mozilla", "netscape" };
	
	/**
	 * Opens the specified web page in a web browser
	 * 
	 * @param url A web address (URL) of a web page (ex: "http://www.google.com/")
	 */
	public static void openURL(String url) throws BrowserLauncherException {
		
		log.debug("Launching URL \"{}\"", url);
		String osName = System.getProperty("os.name");
		
		try {
			
			if(osName.startsWith("Mac OS")) {
				
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
				
			} else if(osName.startsWith("Windows")) {
				
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				
			} else { // assume Unix or Linux
				
				boolean found = false;
				for (String browser : browsers){
					if(!found) {
						found = Runtime.getRuntime().exec(new String[] { "which", browser }).waitFor() == 0;
						if(found) Runtime.getRuntime().exec(new String[] { browser, url });
					}
				}
				if(!found) throw new BrowserLauncherException(Arrays.toString(browsers));
				
			}
		}
		catch(InterruptedException e) {
			throw new BrowserLauncherException(e);
		}
		catch(IOException e) {
			throw new BrowserLauncherException(e);
		}
		catch(ClassNotFoundException e) {
			throw new BrowserLauncherException(e);
		}
		catch(SecurityException e) {
			throw new BrowserLauncherException(e);
		}
		catch(NoSuchMethodException e) {
			throw new BrowserLauncherException(e);
		}
		catch(IllegalArgumentException e) {
			throw new BrowserLauncherException(e);
		}
		catch(IllegalAccessException e) {
			throw new BrowserLauncherException(e);
		}
		catch(InvocationTargetException e) {
			throw new BrowserLauncherException(e);
		}
		
	}
	
}
