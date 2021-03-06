package cmg.org.monitor.util.shared;

import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLControl {

	public static final String NOTIFY_OPTION = "Notify Options";

	public static final String LOGIN_SERVLET_NAME = "login";

	public static final String HTML_INDEX_NAME = "";

	public static final String HTML_DASHBOARD_NAME = "#dashboard";
	public static final String HTML_SYSTEM_DETAIL_NAME = "#dashboard/system/detail";
	public static final String HTML_SYSTEM_STATISTIC_NAME = "#dashboard/system/statistic";
	public static final String HTML_ADD_NEW_SYSTEM_NAME = "#management/system/add";
	public static final String HTML_SYSTEM_MANAGEMENT_NAME = "#management/system";
	public static final String HTML_USER_MANAGEMENT_NAME = "#management/user";
	public static final String HTML_USER_ROLE_NAME = "#management/user/role";
	public static final String HTML_USER_INVITE = "#management/user/invite";
	public static final String HTML_GOOGLE_MANAGEMENT_NAME = "#management/user/google";
	public static final String HTML_ABOUT_NAME = "#about";
	public static final String HTML_HELP_NAME = "#help";
	public static final String HTML_EDIT_NAME = "#management/system/edit";
	public static final String HTML_DELETE_SYSTEM_NAME = "#management/system/delete";
	public static final String HTML_SYSTEM_CHANGELOG = "#management/system/changelog";
	public static final String HTML_GROUP_MANAGEMENT_NAME = "#management/group";
	public static final String HTML_ADD_NEW_GROUP_NAME = "#management/group/add";
	public static final String HTML_EDIT_GROUP_NAME = "#management/group/edit";
	public static final String HTML_REVISION_NAME = "#revision";

	public static final String ID_STEP_HOLDER = "step-holder";
	public static final String ID_PAGE_HEADING = "page-heading";
	public static final String ID_BODY_CONTENT = "body-content";
	public static final String ID_STATUS_MESSAGE = "statusMes";
	public static final String ID_LOGIN_FORM = "nav-right";
	public static final String ID_MENU = "menuContent";
	public static final String ID_MESSAGE_RED = "message-red";
	public static final String ID_MESSAGE_BLUE = "message-blue";
	public static final String ID_MESSAGE_GREEN = "message-green";
	public static final String ID_MESSAGE_YELLOW = "message-yellow";
	public static final String ID_VERSION = "version";

	public static final int VIEW_DETAILS = 0x001;
	public static final int VIEW_STATISTIC = 0x002;

	public static final int GREEN_MESSAGE = 0x001;
	public static final int BLUE_MESSAGE = 0x002;
	public static final int RED_MESSAGE = 0x003;
	public static final int YELLOW_MESSAGE = 0x004;

	public static final int PAGE_DASHBOARD = 0x001;
	public static final int PAGE_SYSTEM_MANAGEMENT = 0x002;
	public static final int PAGE_USER_MANAGEMENT = 0x003;
	public static final int PAGE_HELP = 0x004;
	public static final int PAGE_ABOUT = 0x005;
	public static final int PAGE_SYSTEM_STATISTIC = 0x006;
	public static final int PAGE_SYSTEM_DETAIL = 0x007;
	public static final int PAGE_ADD_SYSTEM = 0x008;
	public static final int PAGE_DELETE_SYSTEM = 0x009;
	public static final int PAGE_EDIT_SYSTEM = 0x010;
	public static final int PAGE_SYSTEM_CHANGE_LOG = 0x011;
	public static final int PAGE_USER_ROLE = 0x012;
	public static final int PAGE_GROUP_MANAGEMENT = 0x013;
	public static final int PAGE_ADD_GROUP = 0x014;
	public static final int PAGE_GOOGLE_MANAGEMENT = 0x015;
	public static final int PAGE_EDIT_GROUP = 0x016;
	public static final int PAGE_REVISION = 0x017;
	public static final int PAGE_INVITE = 0x018;

	public static final int ERROR_NORMAL = 0x000;
	public static final int ERROR_SYSTEM_ID = 0x001;

	public static final String HTML_ACTIVE_IMAGE = "<img src=\"images/icon/active.gif\" width=\"42\" height=\"20\" " + " style=\"display: block; margin-left: auto; margin-right: auto\"/>";

	public static final String HTML_ARROW_IMAGE = "<img src=\"images/icon/right_arrow.png\" />";

	public static String getButtonHtml(String sid, boolean type) {
		StringBuffer tmp = new StringBuffer();
		tmp.append("<a href=\"" + (type ? HTML_SYSTEM_DETAIL_NAME : HTML_SYSTEM_STATISTIC_NAME) + "/" + sid + "\">");
		tmp.append("<input type=\"button\" class=\"" + (type ? "form-details" : "form-statistic") + "\">");
		tmp.append("</a>");
		return tmp.toString();
	}

	public static String getAboutContent() {
		StringBuffer tmp = new StringBuffer();
		// tmp.append("<img src=\"images/logo/c-mg_logo.png\" width=\"210\" height=\"80\" style='margin:10px'>");
		tmp.append("<h3 style='font-size:24px'>" + MonitorConstant.PROJECT_NAME + "</h3>");
		tmp.append("<h3>Version " + MonitorConstant.VERSION + "</h3>");
		tmp.append("<h3>Released on: " + MonitorConstant.RELEASED_ON + "</h3>");
		tmp.append("<h3>Support contact: ");
		tmp.append("<a href='mailto:monitor@c-mg.com'>monitor@c-mg.com</a>");
		tmp.append(" / <a href='mailto:monitor@c-mg.vn'>monitor@c-mg.vn</a></h3>");
		tmp.append("<h3>Find out more about us: <a href=\"http://www.c-mg.com\">www.c-mg.com</a></h3>");
		return tmp.toString();
	}

	public static String getHelpContent() {
		StringBuffer tmp = new StringBuffer();
		tmp.append("<h3>Help-page is in progress</h3>");
		return tmp.toString();
	}

	public static String getSystemId(String hash) {
		return hash.substring(hash.lastIndexOf("/") + 1, hash.length());
	}

	public static int getPageIndex(String hash) {
		hash = "#" + hash.toLowerCase();
		int index = PAGE_DASHBOARD;
		if (hash.equalsIgnoreCase(HTML_DASHBOARD_NAME)) {
			index = PAGE_DASHBOARD;
		} else if (hash.equalsIgnoreCase(HTML_ABOUT_NAME)) {
			index = PAGE_ABOUT;
		} else if (hash.equalsIgnoreCase(HTML_HELP_NAME)) {
			index = PAGE_HELP;
		} else if (hash.equalsIgnoreCase(HTML_ADD_NEW_SYSTEM_NAME)) {
			index = PAGE_ADD_SYSTEM;
		} else if (hash.startsWith(HTML_SYSTEM_DETAIL_NAME)) {
			index = PAGE_SYSTEM_DETAIL;
		} else if (hash.equalsIgnoreCase(HTML_SYSTEM_MANAGEMENT_NAME)) {
			index = PAGE_SYSTEM_MANAGEMENT;
		} else if (hash.equalsIgnoreCase(HTML_USER_MANAGEMENT_NAME)) {
			index = PAGE_USER_MANAGEMENT;
		} else if (hash.startsWith(HTML_SYSTEM_STATISTIC_NAME)) {
			index = PAGE_SYSTEM_STATISTIC;
		} else if (hash.startsWith(HTML_EDIT_NAME)) {
			index = PAGE_EDIT_SYSTEM;
		} else if (hash.startsWith(HTML_DELETE_SYSTEM_NAME)) {
			index = PAGE_DELETE_SYSTEM;
		} else if (hash.equals(HTML_SYSTEM_CHANGELOG)) {
			index = PAGE_SYSTEM_CHANGE_LOG;
		} else if (hash.equalsIgnoreCase(HTML_USER_ROLE_NAME)) {
			index = PAGE_USER_ROLE;
		} else if (hash.equalsIgnoreCase(HTML_GOOGLE_MANAGEMENT_NAME)) {
			index = PAGE_GOOGLE_MANAGEMENT;
		} else if (hash.equalsIgnoreCase(HTML_GROUP_MANAGEMENT_NAME)) {
			index = PAGE_GROUP_MANAGEMENT;
		} else if (hash.equalsIgnoreCase(HTML_ADD_NEW_GROUP_NAME)) {
			index = PAGE_ADD_GROUP;
		} else if (hash.startsWith(HTML_EDIT_GROUP_NAME)) {
			index = PAGE_EDIT_GROUP;
		} else if (hash.equalsIgnoreCase(HTML_REVISION_NAME)) {
			index = PAGE_REVISION;
		} else if (hash.equalsIgnoreCase(HTML_USER_INVITE)) {
			index = PAGE_INVITE;
		}
		return index;
	}

	public static boolean validIndex(String hash) {
		hash = "#" + hash.toLowerCase();
		return (hash.equalsIgnoreCase(HTML_DASHBOARD_NAME) || hash.equalsIgnoreCase(HTML_ABOUT_NAME) || hash.equalsIgnoreCase(HTML_HELP_NAME) || hash.equalsIgnoreCase(HTML_SYSTEM_MANAGEMENT_NAME)
				|| hash.equalsIgnoreCase(HTML_USER_MANAGEMENT_NAME) || hash.startsWith(HTML_SYSTEM_DETAIL_NAME) || hash.startsWith(HTML_SYSTEM_STATISTIC_NAME)
				|| hash.startsWith(HTML_DELETE_SYSTEM_NAME) || hash.equalsIgnoreCase(HTML_ADD_NEW_SYSTEM_NAME) || hash.startsWith(HTML_EDIT_NAME))
				|| hash.equalsIgnoreCase(HTML_SYSTEM_CHANGELOG)
				|| hash.equalsIgnoreCase(HTML_USER_ROLE_NAME)
				|| hash.equalsIgnoreCase(HTML_GOOGLE_MANAGEMENT_NAME)
				|| hash.equalsIgnoreCase(HTML_GROUP_MANAGEMENT_NAME)
				|| hash.equalsIgnoreCase(HTML_ADD_NEW_GROUP_NAME)
				|| hash.startsWith(HTML_EDIT_GROUP_NAME)
				|| hash.equalsIgnoreCase(HTML_REVISION_NAME) || hash.equalsIgnoreCase(HTML_USER_INVITE);
	}

	public static HTML getSystemInfo(SystemMonitor sys) {
		StringBuffer temp = new StringBuffer();
		temp.append("<h3>SID: " + sys.getCode() + "</h3>");
		temp.append("<h3>Name: " + sys.getName() + "</h3>");
		temp.append("<h3>IP: " + sys.getIp() + "</h3>");
		temp.append("<h3>Health Status: ");
		temp.append("<img src=\"images/icon/" + sys.getHealthStatus());
		temp.append("_status_icon.png\" width=\"24\" height=\"24\" /></h3>");
		return new HTML(temp.toString());
	}

	public static HTML getLogoutHTML(String url, String username) {
		StringBuffer temp = new StringBuffer();

		temp.append("<a href='");
		temp.append(url);
		temp.append("' id='logout'><img src='images/shared/nav/nav_logout.gif' width='64' height='14' /></a>");
		temp.append("<div class='showhide-account'><span>" + username + "</span>");
		temp.append("</div>");
		return new HTML(temp.toString());
	}

	public static HTML getLoginHTML(String url) {
		StringBuffer temp = new StringBuffer();
		temp.append("<a href='");
		temp.append(url);
		temp.append("' id='logout'><img src='images/shared/nav/nav_login.gif' width='64' height='14' /></a>");
		return new HTML(temp.toString());
	}

	public static HTML getMenuHTML(int page, int role) {
		StringBuffer temp = new StringBuffer();
		// Dashboard Menu
		temp.append("<ul class='");
		temp.append((page == PAGE_DASHBOARD || page == PAGE_SYSTEM_DETAIL || page == PAGE_SYSTEM_STATISTIC) ? "current" : "select");
		temp.append("'><li><a href='" + HTML_DASHBOARD_NAME + "'><b>");
		temp.append("Dashboard");
		temp.append("</b></a><div class='select_sub");
		temp.append((page == PAGE_DASHBOARD || page == PAGE_SYSTEM_DETAIL || page == PAGE_SYSTEM_STATISTIC) ? " show" : "");
		temp.append("'><ul class='sub'>");
		temp.append("<li></li></ul></div></li></ul>");

		// Administration Menu
		if (role == MonitorConstant.ROLE_ADMIN) {
			temp.append("<div class='nav-divider'>&nbsp;</div>");
			temp.append("<ul class='");
			temp.append((page == PAGE_USER_MANAGEMENT || page == PAGE_SYSTEM_CHANGE_LOG || page == PAGE_SYSTEM_MANAGEMENT || page == PAGE_ADD_SYSTEM || page == PAGE_EDIT_SYSTEM
					|| page == PAGE_USER_ROLE || page == PAGE_GOOGLE_MANAGEMENT || page == PAGE_GROUP_MANAGEMENT || page == PAGE_ADD_GROUP || page == PAGE_EDIT_GROUP || page == PAGE_INVITE) ? "current"
					: "select");
			temp.append("'><li><a href='" + HTML_SYSTEM_MANAGEMENT_NAME + "'><b>");
			temp.append("Administration");
			temp.append("</b></a><div class='select_sub");
			temp.append((page == PAGE_SYSTEM_MANAGEMENT || page == PAGE_SYSTEM_CHANGE_LOG || page == PAGE_USER_MANAGEMENT || page == PAGE_ADD_SYSTEM || page == PAGE_EDIT_SYSTEM
					|| page == PAGE_GOOGLE_MANAGEMENT || page == PAGE_GROUP_MANAGEMENT || page == PAGE_ADD_GROUP || page == PAGE_EDIT_GROUP || page == PAGE_USER_ROLE || page == PAGE_INVITE) ? " show"
					: "");
			temp.append("'><ul class='sub'>");
			temp.append("<li");
			temp.append((page == PAGE_SYSTEM_MANAGEMENT || page == PAGE_ADD_SYSTEM || page == PAGE_EDIT_SYSTEM) ? " class='sub_show'" : "");
			temp.append("><a href='" + HTML_SYSTEM_MANAGEMENT_NAME + "'>System Management</a></li>");
			temp.append("<li");
			temp.append((page == PAGE_SYSTEM_CHANGE_LOG) ? " class='sub_show'" : "");
			temp.append("><a href='" + HTML_SYSTEM_CHANGELOG + "'>System Change Log</a></li>");
			temp.append("<li");
			temp.append((page == PAGE_GROUP_MANAGEMENT || page == PAGE_ADD_GROUP || page == PAGE_EDIT_GROUP) ? " class='sub_show'" : "");
			temp.append("><a href='" + HTML_GROUP_MANAGEMENT_NAME + "'>Group Management</a></li>");

			temp.append("<li");
			temp.append((page == PAGE_USER_MANAGEMENT || page == PAGE_GOOGLE_MANAGEMENT || page == PAGE_USER_ROLE || page == PAGE_INVITE) ? " class='sub_show'" : "");
			temp.append("><a href='" + HTML_USER_MANAGEMENT_NAME + "'>User Management</a></li></ul></div></li></ul>");

		}
		// About Menu
		temp.append("<div class='nav-divider'>&nbsp;</div>");
		temp.append("<ul class='");
		temp.append((page == PAGE_ABOUT) ? "current" : "select");
		temp.append("'><li><a href='" + HTML_ABOUT_NAME + "'><b>");
		temp.append("About");
		temp.append("</b></a><div class='select_sub");
		temp.append((page == PAGE_ABOUT) ? " show" : "");
		temp.append("'><ul class='sub'>");
		temp.append("<li><a href=''></a></li></ul></div></li></ul>");
		// Help Menu
		temp.append("<div class='nav-divider'>&nbsp;</div>");
		temp.append("<ul class='");
		temp.append((page == PAGE_HELP) ? "current" : "select");
		temp.append("'><li><a href='" + HTML_HELP_NAME + "'><b>");
		temp.append("Help");
		temp.append("</b></a><div class='select_sub");
		temp.append((page == PAGE_HELP) ? " show" : "");
		temp.append("'><ul class='sub'>");
		temp.append("<li><a href=''></a></li></ul></div></li></ul>");

		// Revision Menu
		temp.append("<div class='nav-divider'>&nbsp;</div>");
		temp.append("<ul class='");
		temp.append((page == PAGE_REVISION) ? "current" : "select");
		temp.append("'><li><a href='" + HTML_REVISION_NAME + "'><b>");
		temp.append("Revisions");
		temp.append("</b></a><div class='select_sub");
		temp.append((page == PAGE_REVISION) ? " show" : "");
		temp.append("'><ul class='sub'>");
		temp.append("<li><a href=''></a></li></ul></div></li></ul>");
		// Mobile version menu
		temp.append("<div class='nav-divider'>&nbsp;</div>");
		temp.append("<ul class='select'><li><a href='?state=mobile'><b>");
		temp.append("Mobile");
		temp.append("</b></a><div class='select_sub'><ul class='sub'>");
		temp.append("<li></li></ul></div></li></ul>");
		return new HTML(temp.toString());
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
		case HTMLControl.YELLOW_MESSAGE:
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
		return new HTML("<div id='message-blue'>" + "<table border='0' width='100%' cellpadding='0' cellspacing='0'>" + "<tr><td class='blue-left'>" + title + "</td>" + "<td class='blue-right'>"
				+ "<a class='close-blue'>" + "<img src='images/icon/icon_" + (isOpen ? "close" : "open") + "_blue.gif' /></a></td></tr></table></div>");
	}

	public static String getHTMLStatusImage(String sid, String healthStatus) {
		String mes = "";
		if (healthStatus.equals("dead")) {
			mes = "System is not working.\nClick the Icon to see more information!";
		} else if (healthStatus.equals("bored")) {
			mes = "A problem may occur to the system.\nClick the Icon to see more information!";
		} else if (healthStatus.equals("smile")) {
			mes = "The system is working normally.\nClick the Icon to see more information!";
		} else {
			mes = "Click the Icon to see more information!";
		}
		return "<img src=\"images/icon/" + healthStatus + "_status_icon.png\" width=\"24\" height=\"24\" " + "style=\"display: block; margin-left: auto; margin-right: auto\""
				+ " onClick=\"javascript:showStatusDialogBox('" + sid + "','" + healthStatus + "');\"" + " title='" + mes + "'" + " alt='" + mes + "'/>";
	}

	public static String getHTMLStatusImage(boolean b) {
		return "<img src=\"images/icon/" + Boolean.toString(b) + "_icon.png\" width=\"24\" height=\"24\" " + "style=\"display: block; margin-left: auto; margin-right: autso\" />";

	}

	public static String getHTMLActiveImage(boolean b) {
		return "<img src=\"images/icon/p_" + (b ? "online" : "offline") + ".gif\" " + " style=\"display: block; margin-left: auto; margin-right: auto\"/>";
	}

	public static String getLinkSystemDetail(String id, String code) {
		return "<a href=\"" + HTML_SYSTEM_DETAIL_NAME + "/" + id + "\"  class='system-id' ><span>" + code + "</span></a>";

	}

	public static String getLinkSystemStatistic(SystemMonitor sys) {
		return "<a href=\"" + MonitorConstant.PROJECT_HOST_NAME + "/" + HTML_SYSTEM_STATISTIC_NAME + "/" + sys.getId() + "\" ><span>" + sys + "</span></a>";

	}

	public static String getLinkEditSystem(String id, String code) {
		return "<a href=\"" + HTML_EDIT_NAME + "/" + id + "\">" + code + "</a>";

	}

	public static String getLinkEditGroup(String id) {
		String a = "<a href=\"" + HTML_EDIT_GROUP_NAME + "/" + id + "\" title=\"Update Group\" >";
		return a;
	}

	public static String getStringTime(int secsIn) {
		int hours = secsIn / 3600, remainder = secsIn % 3600, minutes = remainder / 60, seconds = remainder % 60;

		String time = ((hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
		return time;
	}

	public static HTML getPageHeading(SystemMonitor sys) {
		StringBuffer temp = new StringBuffer();
		temp.append("<h1>");
		temp.append("<a href=\"" + HTML_DASHBOARD_NAME + "\">Dashboard</a>&nbsp");
		temp.append(HTML_ARROW_IMAGE);
		temp.append("&nbsp<a>" + sys.getCode() + " - " + sys.getName() + "</a>");
		return new HTML(temp.toString());
	}

	public static HTML getPageHeading(int page) {
		StringBuffer temp = new StringBuffer();
		temp.append("<h1>");

		if (page == PAGE_DASHBOARD || page == PAGE_SYSTEM_STATISTIC || page == PAGE_SYSTEM_DETAIL) {
			temp.append("<a href=\"" + HTML_DASHBOARD_NAME + "\">Dashboard</a>&nbsp");
		}
		if (page == PAGE_SYSTEM_STATISTIC || page == PAGE_SYSTEM_DETAIL) {
			temp.append(HTML_ARROW_IMAGE);
			temp.append("&nbsp<a>");
			temp.append(page == PAGE_SYSTEM_STATISTIC ? "Statistic System" : "");
			temp.append(page == PAGE_SYSTEM_DETAIL ? "System Information" : "");
			temp.append("</a>");
		}
		if (page == PAGE_SYSTEM_MANAGEMENT || page == PAGE_ADD_SYSTEM || page == PAGE_EDIT_SYSTEM) {
			temp.append("<a href=\"" + HTML_SYSTEM_MANAGEMENT_NAME + "\">System Management</a>&nbsp");
		}
		if (page == PAGE_ADD_SYSTEM || page == PAGE_EDIT_SYSTEM) {
			temp.append(HTML_ARROW_IMAGE);
			temp.append("&nbsp<a>");
			temp.append(page == PAGE_EDIT_SYSTEM ? "Edit System" : "");
			temp.append(page == PAGE_ADD_SYSTEM ? "Add New System" : "");
			temp.append("</a>");
		}
		if (page == PAGE_SYSTEM_CHANGE_LOG) {
			temp.append("<a href=\"" + HTML_SYSTEM_CHANGELOG + "\">Change Log</a>&nbsp");
		}

		if (page == PAGE_GROUP_MANAGEMENT || page == PAGE_ADD_GROUP || page == PAGE_EDIT_GROUP) {
			temp.append("<a href=\"" + HTML_GROUP_MANAGEMENT_NAME + "\">Group Management</a>&nbsp");
		}
		if (page == PAGE_ADD_GROUP || page == PAGE_EDIT_GROUP) {
			temp.append(HTML_ARROW_IMAGE);
			temp.append("&nbsp<a>");
			temp.append(page == PAGE_EDIT_GROUP ? "Edit Group" : "");
			temp.append(page == PAGE_ADD_GROUP ? "Add New Group" : "");
			temp.append("</a>");
		}

		if (page == PAGE_USER_MANAGEMENT || page == PAGE_USER_ROLE || page == PAGE_GOOGLE_MANAGEMENT || page == PAGE_INVITE) {
			temp.append("<a href=\"" + HTML_USER_MANAGEMENT_NAME + "\">User Management</a>&nbsp");
		}

		if (page == PAGE_USER_ROLE || page == PAGE_GOOGLE_MANAGEMENT || page == PAGE_INVITE) {
			temp.append(HTML_ARROW_IMAGE);
			temp.append("&nbsp<a>");
			temp.append(page == PAGE_USER_ROLE ? "User Role" : "");
			temp.append(page == PAGE_GOOGLE_MANAGEMENT ? "Google Management" : "");
			temp.append(page == PAGE_INVITE ? "Invite User" : "");
			temp.append("</a>");
		}

		if (page == PAGE_ABOUT) {
			temp.append("<a href=\"" + HTML_ABOUT_NAME + "\">About Us</a> ");
		}
		if (page == PAGE_HELP) {
			temp.append("<a href=\"" + HTML_HELP_NAME + "\">Help Content</a> ");
		}
		if (page == PAGE_REVISION) {
			temp.append("<a href=\"" + HTML_REVISION_NAME + "\">Revisions</a> ");
		}
		temp.append("</h1>");
		return new HTML(temp.toString());
	}

	public static String getPercentBar(int percent, int redRangeValue) {
		StringBuffer sb = new StringBuffer();
		String startImg = "<img style=\"padding: 0\" src=\"http://ajax.googleapis.com/ajax/static/modules/gviz/1.0/util/";
		sb.append("<span style=\"padding: 0; float: left; white-space: nowrap;\">");
		sb.append(startImg + "bar_s.png\" height=\"12\" width=\"1\">");
		if (percent > 0) {
			sb.append(startImg + "bar_" + (percent >= redRangeValue ? "r" : "b") + ".png\" height=\"12\" width=\"" + 2 * percent + "\">");
		}
		sb.append(startImg + "bar_w.png\" " + (percent == -1 ? "title=\"Has no data from the system\" alt=\"Has no data from the system\"" : "") + " height=\"12\" width=\"" + 2 * (100 - percent)
				+ "\">");
		sb.append(startImg + "bar_s.png\" height=\"12\" width=\"1\">" + (percent == -1 ? "" : ("&nbsp;" + percent + "%")) + "</span>");
		return sb.toString();
	}

	public static HTML getStepHolder(int page, String sid) {
		StringBuffer temp = new StringBuffer();
		if (page == PAGE_SYSTEM_DETAIL || page == PAGE_SYSTEM_STATISTIC) {
			temp.append("<div class=\"step-no" + ((page == PAGE_SYSTEM_DETAIL) ? "" : "-off") + "\">1</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_SYSTEM_DETAIL) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_SYSTEM_DETAIL_NAME + "/" + sid + "\">System Infomation</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_SYSTEM_DETAIL) ? "dark" : "light") + "-right\">&nbsp;</div>");
			temp.append("<div class=\"step-no" + ((page == PAGE_SYSTEM_STATISTIC) ? "" : "-off") + "\">2</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_SYSTEM_STATISTIC) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_SYSTEM_STATISTIC_NAME + "/" + sid + "\">System Statistic</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_SYSTEM_STATISTIC) ? "dark" : "light") + "-round\">&nbsp;</div>");
			temp.append("<div class=\"clear\"></div>");
		} else if (page == PAGE_SYSTEM_MANAGEMENT || page == PAGE_ADD_SYSTEM) {
			temp.append("<div class=\"step-no" + ((page == PAGE_SYSTEM_MANAGEMENT) ? "" : "-off") + "\">1</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_SYSTEM_MANAGEMENT) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_SYSTEM_MANAGEMENT_NAME + "\">System List</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_SYSTEM_MANAGEMENT) ? "dark" : "light") + "-right\">&nbsp;</div>");
			temp.append("<div class=\"step-no" + ((page == PAGE_ADD_SYSTEM) ? "" : "-off") + "\">2</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_ADD_SYSTEM) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_ADD_NEW_SYSTEM_NAME + "\">Add New System</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_ADD_SYSTEM) ? "dark" : "light") + "-round\">&nbsp;</div>");
			temp.append("<div class=\"clear\"></div>");
		} else if (page == PAGE_GROUP_MANAGEMENT || page == PAGE_ADD_GROUP) {
			temp.append("<div class=\"step-no" + ((page == PAGE_GROUP_MANAGEMENT) ? "" : "-off") + "\">1</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_GROUP_MANAGEMENT) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_GROUP_MANAGEMENT_NAME + "\">Group List</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_GROUP_MANAGEMENT) ? "dark" : "light") + "-right\">&nbsp;</div>");
			temp.append("<div class=\"step-no" + ((page == PAGE_ADD_GROUP) ? "" : "-off") + "\">2</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_ADD_GROUP) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_ADD_NEW_GROUP_NAME + "\">Add New Group</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_ADD_GROUP) ? "dark" : "light") + "-round\">&nbsp;</div>");
			temp.append("<div class=\"clear\"></div>");
		} else if (page == PAGE_USER_MANAGEMENT || page == PAGE_USER_ROLE || page == PAGE_GOOGLE_MANAGEMENT || page == PAGE_INVITE) {
			temp.append("<div class=\"step-no" + ((page == PAGE_USER_MANAGEMENT) ? "" : "-off") + "\">1</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_USER_MANAGEMENT) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_USER_MANAGEMENT_NAME + "\">User Mapping</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_USER_MANAGEMENT) ? "dark" : "light") + "-right\">&nbsp;</div>");

			temp.append("<div class=\"step-no" + ((page == PAGE_USER_ROLE) ? "" : "-off") + "\">2</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_USER_ROLE) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_USER_ROLE_NAME + "\">User Role</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_USER_ROLE) ? "dark" : "light") + "-right\">&nbsp;</div>");

			temp.append("<div class=\"step-no" + ((page == PAGE_INVITE) ? "" : "-off") + "\">3</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_INVITE) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_USER_INVITE + "\">Invite User</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_INVITE) ? "dark" : "light") + "-right\">&nbsp;</div>");

			temp.append("<div class=\"step-no" + ((page == PAGE_GOOGLE_MANAGEMENT) ? "" : "-off") + "\">4</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_GOOGLE_MANAGEMENT) ? "dark" : "light") + "-left\">");
			temp.append("<a href=\"" + HTML_GOOGLE_MANAGEMENT_NAME + "\">Google Management</a>");
			temp.append("</div>");
			temp.append("<div class=\"step-" + ((page == PAGE_GOOGLE_MANAGEMENT) ? "dark" : "light") + "-round\">&nbsp;</div>");
			temp.append("<div class=\"clear\"></div>");
		}

		return new HTML(temp.toString());
	}

	public static String getButtonForActiveUser(InvitedUser u) {
		String id = u.getEmail();
		String typeDelete = "delete";
		String temp;
		String buttonDelete = "<a style=\"margin-left:auto;margin-right:auto;\"  class=\"btnDeleteUser\" title=\"delete\" onClick=\"javascript:showDialogBox('" + id + "','" + typeDelete + "','"
				+ u.getStatus() + "');\" />";
		// String buttonInactive =
		// "<a class=\"btnInActive\" title=\"inactive\" onClick=\"javascript:showDialogBox('"+
		// id + "','" + typeInactive +"','" + u.getStatus() + "');\" />";
		temp = buttonDelete /* + buttonInactive */;

		return temp;
	}

	public static String getButtonForRequestingUser(InvitedUser u) {
		String id = u.getEmail();
		String typeInactive = "active";
		String buttonactive = "<a style=\"margin-left:auto;margin-right:auto;\" class=\"btnActive\" onClick=\"javascript:showDialogBox('" + id + "','" + typeInactive + "','" + u.getStatus()
				+ "');\" title=\"Active\" />";
		return buttonactive;
	}

	public static String getButtonForPendingUser(InvitedUser u) {
		String id = u.getEmail();
		String typeDelete = "delete";
		String buttonDelete = "<a style=\"margin-left:auto;margin-right:auto;\" class=\"btnDeleteUser\" title=\"delete\" onClick=\"javascript:showDialogBox('" + id + "','" + typeDelete + "','"
				+ u.getStatus() + "');\"  / >";
		return buttonDelete;
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
		value = value / 1024;
		String temp = null;
		if (value >= 1024 * 1024) {
			temp = NumberFormat.getFormat("#.0").format(value / (1024 * 1024)) + " GB";
		} else if (value >= 1024) {
			temp = NumberFormat.getFormat("#.0").format(value / 1024) + " MB";
		} else {
			temp = NumberFormat.getFormat("#.0").format(value) + " KB";
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

	public static String getDefaultContent() {
		StringBuffer tmp = new StringBuffer();
		tmp.append("<h3>Page is in progress</h3>");
		return tmp.toString();
	}

}
