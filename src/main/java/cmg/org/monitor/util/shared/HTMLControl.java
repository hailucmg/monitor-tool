package cmg.org.monitor.util.shared;

import com.google.gwt.user.client.ui.HTML;

public class HTMLControl {
	public static final int GREEN_MESSAGE = 0x001;
	public static final int BLUE_MESSAGE = 0x002;
	public static final int RED_MESSAGE = 0x003;
	public static final int YELLOW_MESSAGE = 0x004;
	
	public static final String HTML_ACTIVE_IMAGE
		= "<img src=\"images/icon/active.gif\" width=\"42\" height=\"20\" " 
				   + " style=\"display: block; margin-left: auto; margin-right: auto\"/>";
	
	public HTMLControl() {		
	}
	
	public static HTML getMessageBox(String message, String url, String urlName, int type) {
		String color = "";
		switch (type) {		
		case BLUE_MESSAGE:
			color = "blue";
			break;
		case RED_MESSAGE:
			color = "red";
		case YELLOW_MESSAGE :
			color = "yellow";
			break;
		case GREEN_MESSAGE:			
		default:
			color = "green";
			break;
		}
		return new HTML(
			"<div id='message-" + color + "'>"
				 + "<table border='0' width='100%' cellpadding='0' cellspacing='0'>"
				 + "<tr><td class='" + color + "-left'>"
				 + message
				 + " <a href='" + url + "'>" 
				 + urlName + "</a></td>"
				 + "<td class='" + color + "-right'>" 
				 + "<a class='close-" + color + "'>" 
				 +"<img src='images/table/icon_close_" + color 
				 + ".gif' /></a></td></tr></table></div>");
	}
	
	public static String getHTMLStatusImage(boolean b) {
		return "<img src=\"images/icon/"
				+ (b ? "true" : "false")
				+ "_icon.png\" width=\"24\" height=\"24\" "
				+ "style=\"display: block; margin-left: auto; margin-right: auto\" />";
	}
	
	public static String getLinkSystemDetail(String id, String code) {
		return "<a href=\"demo_system_details.html?sid="
				+ id
				+"\">"
				+ code +"</a>";
		
	}
}
