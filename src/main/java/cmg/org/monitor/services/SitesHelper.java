package cmg.org.monitor.services;

import java.io.IOException;
import java.net.URL;

import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.XhtmlTextConstruct;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.data.sites.ContentFeed;
import com.google.gdata.data.sites.WebPageEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SitesHelper {

	public static String getSiteEntryContent(String entryID) {
		String temp = "";		
		SitesService service = new SitesService(MonitorConstant.SITES_APP_NAME);		
		try {
			service.setUserCredentials(MonitorConstant.SITES_USERNAME, MonitorConstant.SITES_PASSWORD);
			ContentFeed contentFeed = service.getFeed(new URL(MonitorConstant.SITES_CONTENT_FEED_URL),
					ContentFeed.class);
			for (WebPageEntry entry : contentFeed.getEntries(WebPageEntry.class)) {
				if (getEntryId(entry).equals(entryID)){
					temp = getContentBlob(entry);
					break;
				}
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
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
