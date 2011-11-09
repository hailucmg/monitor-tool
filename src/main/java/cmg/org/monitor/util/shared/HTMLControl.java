package cmg.org.monitor.util.shared;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLControl {
	public static final String HTML_DASHBOARD_NAME = "DashBoard.html";
	public static final String HTML_SYSTEM_DETAIL_NAME = "SystemDetail.html";
	public static final String HTML_ADD_NEW_SYSTEM_NAME = "AddnewSystem.html";
	
	public static final int VIEW_DETAILS = 0x001;
	public static final int VIEW_STATISTIC = 0x002;
	
	public static final int GREEN_MESSAGE = 0x001;
	public static final int BLUE_MESSAGE = 0x002;
	public static final int RED_MESSAGE = 0x003;
	public static final int YELLOW_MESSAGE = 0x004;
	
	public static final String HTML_ACTIVE_IMAGE
		= "<img src=\"images/icon/active.gif\" width=\"42\" height=\"20\" " 
				   + " style=\"display: block; margin-left: auto; margin-right: auto\"/>";
	
	public static final String HTML_ARROW_IMAGE
		= "<img src=\"images/icon/right_arrow.png\" />";
	public HTMLControl() {		
	}
	
	public static HTML getSystemInfo(SystemMonitor sys) {
		String temp = "";
		temp += "<h3>SID: " + sys.getCode() + "</h3>";
		temp += "<h3>Name: " + sys.getName() + "</h3>";
		temp += "<h3>IP: " + sys.getIp() + "</h3>";
		temp += "<h3>Health Status: ";
		temp += "<img src=\"images/icon/"
				+ sys.getHealthStatus()
				+ "_status_icon.png\" width=\"24\" height=\"24\" /></h3>";
		return new HTML(temp);
	}
	
	public static String getColor(int type) {
		String color = "";
		switch (type) {		
		case HTMLControl.BLUE_MESSAGE:
			color = "blue";
			break;
		case HTMLControl.RED_MESSAGE:
			color = "red";
			break;
		case HTMLControl.YELLOW_MESSAGE :
			color = "yellow";
			break;
		case HTMLControl.GREEN_MESSAGE:			
		default:
			color = "green";
			break;
		}	
		return color;
	}
	
	public static HTML getColorTitle(String title, boolean isOpen) {
		
		return new HTML(
			"<div id='message-blue'>"
				 + "<table border='0' width='100%' cellpadding='0' cellspacing='0'>"
				 + "<tr><td class='blue-left'>"
				 + title
				  + "</td>"
				 + "<td class='blue-right'>" 
				 + "<a class='close-blue'>" 
				 +"<img src='images/icon/icon_" 
				 + (isOpen ? "close" : "open")
				 + "_blue.gif' /></a></td></tr></table></div>");
	}
	
	public static String getHTMLStatusImage(String healthStatus) {
		return "<img src=\"images/icon/"
				+ healthStatus
				+ "_status_icon.png\" width=\"24\" height=\"24\" "
				+ "style=\"display: block; margin-left: auto; margin-right: auto\" />";
	}
	
	public static String getHTMLStatusImage(boolean b) {
		return "<img src=\"images/icon/"
				+ Boolean.toString(b)
				+ "_icon.png\" width=\"24\" height=\"24\" "
				+ "style=\"display: block; margin-left: auto; margin-right: auto\" />";

	}
	
	public static String getHTMLActiveImage(boolean b) {
		return "<img src=\"images/icon/p_" + 
					(b ? "online" : "offline")+ ".gif\" "
				   + " style=\"display: block; margin-left: auto; margin-right: auto\"/>";
	}
	
	public static String getLinkSystemDetail(String id, String code) {
		return "<a href=\"" + HTML_SYSTEM_DETAIL_NAME + "?sid=" 
				+ id
				+"\">"
				+ code +"</a>";
		
	}
	
	public static String getStringTime(int secsIn) {
		int hours = secsIn / 3600,
		remainder = secsIn % 3600,
		minutes = remainder / 60,
		seconds = remainder % 60;

		String time = ( (hours < 10 ? "0" : "") + hours
				+ ":" + (minutes < 10 ? "0" : "") + minutes
				+ ":" + (seconds< 10 ? "0" : "") + seconds );
		return time;
	}
	
	public static HTML getPageHeading(int view) {
		String temp = "";
		temp += "<h1><a href=\"" + HTML_DASHBOARD_NAME + 
				"\">Dashboard</a> ";
		temp += HTML_ARROW_IMAGE;
		temp +=	" <a href=\"#details\">Details</a> ";
		if (view == VIEW_STATISTIC) {
			temp += HTML_ARROW_IMAGE;
			temp += " <a href=\"#statistic\">Statistic</a>";
		}
		temp += "</h1>";
		return new HTML(temp);
	}
	
	public static HTML getStepHolder(int view) {
		String temp = "";
		temp += "<div class=\"step-no" +
				((view == VIEW_DETAILS) ? "" : "-off")+"\">1</div>";
		temp += "<div class=\"step-" +
				((view == VIEW_DETAILS) ? "dark" : "light") +"-left\">";
		temp += "<a href=\"#details\">System Infomation</a>";
		temp += "</div>";
		temp += "<div class=\"step-" +
				((view == VIEW_DETAILS) ? "dark" : "light") + "-right\">&nbsp;</div>";
		temp += "<div class=\"step-no" +
				((view == VIEW_STATISTIC) ? "" : "-off") + "\">2</div>";
		temp += "<div class=\"step-" +
				((view == VIEW_STATISTIC) ? "dark" : "light") + "-left\">";
		temp += "<a href=\"#statistic\">Statistic System</a>";
		temp += "</div>";
		temp += "<div class=\"step-" +
				((view == VIEW_STATISTIC) ? "dark" : "light") +"-round\">&nbsp;</div>";
		temp += "<div class=\"clear\"></div>";
		return new HTML(temp);
	}
	
	public static String trimHashPart(String url) {
		String temp = url;
		if (url.contains("#")) {
			temp = temp.substring(0, url.indexOf("#"));
		}
		return temp;
	}
	
	public static HTML getHtmlForm(int view) {
		String temp = "";
		if (view == VIEW_STATISTIC) {
			temp += "<div id=\"test1\"></div>"; 
		} else {
			temp += "<div id=\"test2\"></div>";
		}
		return new HTML(temp);
	}
	
	public static String convertMemoryToString(double value) {
		String temp = null;
		if (value >= 1024 * 1024) {
			temp = NumberFormat.getFormat("#.0").format(value / (1024 * 1024)) + " GB";
		} else if (value >= 1024) {
			temp = NumberFormat.getFormat("#.0").format(value / 1024) + " MB";
		} 
		return temp;
	}
	
	public static String format(final String format, final Object... args) {
		StringBuilder sb = new StringBuilder();
	    int cur = 0;
	    int len = format.length();
	    while (cur < len) {
	        int fi = format.indexOf('{', cur);
	        if (fi != -1) {
	            sb.append(format.substring(cur, fi));
	            int si = format.indexOf('}', fi);
	            if (si != -1) {
	                String nStr = format.substring(fi + 1, si);
	                int i = Integer.parseInt(nStr);
	                sb.append(args[i]);
	                cur = si + 1;
	            } else {
	                sb.append(format.substring(fi));
	                break;
	            }
	        } else {
	            sb.append(format.substring(cur, len));
	            break;
	        }
	    }
	    return sb.toString();
		}
}
