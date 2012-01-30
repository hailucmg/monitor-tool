package cmg.org.monitor.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.XhtmlTextConstruct;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.data.sites.ContentFeed;
import com.google.gdata.data.sites.WebPageEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SitesHelper {
	private static final Logger logger = Logger.getLogger(SitesHelper.class
			.getName());

	public static String getSiteEntryContent(String entryID) {
		String temp = "";
		SitesService service = new SitesService(MonitorConstant.SITES_APP_NAME);
		Object obj = MonitorMemcache.get(Key.create(Key.TOKEN_SITES));
		if (obj != null) {
			if (obj instanceof String) {
				logger.log(Level.INFO, "Getting token from memcache");
				String token = (String) obj;
				service.setUserToken(token);
				ContentFeed contentFeed;
				try {
					contentFeed = service.getFeed(new URL(
							MonitorConstant.SITES_CONTENT_FEED_URL),
							ContentFeed.class);
					for (WebPageEntry entry : contentFeed
							.getEntries(WebPageEntry.class)) {
						if (getEntryId(entry).equals(entryID)) {
							temp = getContentBlob(entry);
							break;
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					logger.log(Level.INFO,"getting exception from memcache :" + e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.log(Level.INFO,"getting exception from memcache :" + e.getMessage());
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					logger.log(Level.INFO,"getting exception from memcache :" + e.getMessage());
				}
			}
			} else {
				try {
					service.setUserCredentials(MonitorConstant.SITES_USERNAME,
							MonitorConstant.SITES_PASSWORD);
					ContentFeed contentFeed = service.getFeed(new URL(
							MonitorConstant.SITES_CONTENT_FEED_URL),
							ContentFeed.class);
					for (WebPageEntry entry : contentFeed
							.getEntries(WebPageEntry.class)) {
						if (getEntryId(entry).equals(entryID)) {
							temp = getContentBlob(entry);
							break;
						}
					}
				} catch (AuthenticationException e) {
					logger.log(Level.INFO,"getting exception from Manual :" + e.getMessage());
				} catch (IOException e) {
					logger.log(Level.INFO,"getting exception from Manual :" + e.getMessage());
				} catch (ServiceException e) {
					logger.log(Level.INFO,"getting exception from Manual :" + e.getMessage());
				}
			}
		return temp;
	}
	
	public static String getContentBlob(BaseContentEntry<?> entry) {
		return ((XhtmlTextConstruct) entry.getTextContent().getContent())
				.getXhtml().getBlob();
	}
	
	/**
	 * Returns an entry's numeric ID.
	 */
	private static String getEntryId(BaseContentEntry<?> entry) {
		String selfLink = entry.getSelfLink().getHref();
		return selfLink.substring(selfLink.lastIndexOf("/") + 1);
	}
			
}
