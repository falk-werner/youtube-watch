/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package youtube.watch;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class App {
	
	private static final int POLL_INTERVAL = 1 * 1000;
	
    public static void main(String[] args)  {
    	Settings settings = new Settings();
    	settings.load();

    	String videoId = (args.length > 0) ? args[0] : "";    	
    	if (videoId.isEmpty()) {
    		videoId = settings.getVideoId();
    	}
    	else {
    		settings.setVideoId(videoId);
    	}
    	
    	if (videoId.isEmpty() || (videoId.equals("-h")) || (videoId.equals("--help"))) {    		
    		printUsage();
    		System.exit(0);
    	}

    	Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    	System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");    	
    	// geckodriver should be in PATH or specified via property
    	// System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");

    	
    	WebDriver driver = new FirefoxDriver();
    	driver.get(settings.getUrl());
    	
    	for (Cookie cookie: settings.getCookies()) {
    		driver.manage().addCookie(cookie);
    	}

    	driver.get(settings.getUrl());
    	
    	boolean isFinished = false;
    	while (!isFinished)
    	{
	    	try {
        		Thread.sleep(POLL_INTERVAL);
            	settings.setTime(getVideoTime(driver));
            	settings.setCookies(driver.manage().getCookies());
        	}
	    	catch (Exception ex) {
	    		isFinished = true;
	    	}
    	}
    	
    	settings.save();
    	closeDriver(driver);
    }
    
    private static void printUsage() {
    	System.out.println("youtube-watch, (c) 2022 by Falk Werner <github.com/falk-werner>");
    	System.out.println("watch YouTube videos");
    	System.out.println();
    	System.out.println("Usage:");
    	System.out.println("    youtube-watch [<video-id>]");
    }
    
	private static void closeDriver(WebDriver driver) {
    	try {
        	driver.close();    		
    	}
    	catch (Exception ex) {
    		// swallow
    	}		
	}
	
	private static int getVideoTime(WebDriver driver) {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	Object result = js.executeScript("var player = document.getElementById('movie_player'); return parseInt(player.getCurrentTime());");
    	return Integer.parseInt(result.toString());
	}
    
}