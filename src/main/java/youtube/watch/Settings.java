package youtube.watch;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.Cookie;

public class Settings {

	private static final String FILENAME = "settings.conf";
	
	private String videoId;
	private int time;
	private Set<Cookie> cookies;
	
	public Settings() {
		this.videoId = "";
		this.time = -1;
		this.cookies = new HashSet<>();
	}
	
	public String getUrl() {
		if (time > 0) {
			return String.format("https://youtu.be/%s?t=%d", this.videoId, this.time);			
		}
		else {
			return String.format("https://youtu.be/%s", this.videoId);
		}
	}
	
	public String getVideoId() {
		return this.videoId;
	}
	
	public Set<Cookie> getCookies() {
		return this.cookies;
	}
	
	public void setVideoId(String value) {
		if (!this.videoId.equals(value)) {
			this.time = -1;			
		}
		this.videoId = value;
	}
	
	public void setTime(int value) {
		this.time = value;
	}
	
	public void setCookies(Set<Cookie> cookies) {
		this.cookies = cookies;
	}
	
	public void load() {
		this.cookies = new HashSet<>();
		
		try(FileInputStream file = new FileInputStream(FILENAME))
		{
			Properties props = new Properties();
			props.load(file);
			this.videoId = props.getProperty("VIDEO_ID");
			this.time = Integer.parseInt(props.getProperty("VIDEO_TIME", "0"));
			
			int count = Integer.parseInt(props.getProperty("COOKIES.COUNT", "0"));
			for(int i = 0; i < count; i++)
			{
				int id = i + 1;
				String name = props.getProperty(String.format("COOKIE.%d.NAME", id));
				String value = props.getProperty(String.format("COOKIE.%d.VALUE", id));
				String path = props.getProperty(String.format("COOKIE.%d.PATH", id));
				String domain = props.getProperty(String.format("COOKIE.%d.DOMAIN", id));
				
				this.cookies.add(new Cookie(name, value, domain, path, null));
			}
		}
		catch (Exception ex)
		{
			this.videoId = "";
			this.time = -1;
			this.cookies = new HashSet<>();
		}
	}
	
	public void save() {
		Properties props = new Properties();
		props.setProperty("VIDEO_ID", this.videoId);
		props.setProperty("VIDEO_TIME", String.format("%d", this.time));
		
		int count = 0;
		for(Cookie cookie: cookies) {
			count++;
			props.setProperty(String.format("COOKIE.%d.NAME", count), cookie.getName());
			props.setProperty(String.format("COOKIE.%d.VALUE", count), cookie.getValue());
			props.setProperty(String.format("COOKIE.%d.PATH", count), cookie.getPath());
			props.setProperty(String.format("COOKIE.%d.DOMAIN", count), cookie.getDomain());
			
		}
		props.setProperty("COOKIES.COUNT", String.format("%d", count));
		
		try(FileOutputStream file =  new FileOutputStream(FILENAME)) {
			props.store(file, "video settings");
		}
		catch (Exception ex) {
			// swallow
		}		
	}
	
}
