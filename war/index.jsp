<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="cmg.org.monitor.services.google.Util" %>
<%@ page import="cmg.org.monitor.memcache.*" %>
<%@ page import="cmg.org.monitor.util.shared.MonitorConstant" %>
<%@ page import="com.google.api.client.auth.oauth2.Credential"%>

<%@ page import="com.google.api.services.plus.Plus"%>
<%@ page import="com.google.api.services.plus.model.Person"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%


boolean isAuth = false;
String imageURL = "";
String displayName = "";
String redirectURL = request.getRequestURL().toString();

Object obj = MonitorMemcache.get(Key.create(Key.PROJECT_HOST_NAME));
if (obj == null) {	
	String projectHostName = redirectURL;
	if (projectHostName.lastIndexOf("/") > projectHostName.indexOf("//") + 1) {
		projectHostName = projectHostName.substring(0, projectHostName.lastIndexOf("/"));
	}
	System.out.println("projectHostName: " + projectHostName);
	MonitorMemcache.put(Key.create(Key.PROJECT_HOST_NAME), projectHostName);
}

if (!MonitorConstant.DEBUG) {
	UserService userService = UserServiceFactory.getUserService();
	if (!userService.isUserLoggedIn()) {
		response.sendRedirect(userService.createLoginURL(redirectURL));	
	}
	if (Util.getFlow().loadCredential(session.getId()) == null) {
		response.sendRedirect("/connect");
	}	
	
	try {	
		Credential credential = Util.getFlow().loadCredential(session.getId());
		Plus.Builder builder = new Plus.Builder(Util.TRANSPORT, Util.JSON_FACTORY, credential);	
		Plus plus = builder.build();
		Person profile;
		profile = plus.people().get("me").execute();
		imageURL = profile.getImage().getUrl();
		if (imageURL.contains("?sz")) {
			imageURL = imageURL.substring(0, imageURL.indexOf("?sz")); 
			imageURL+= "?sz=100";
		}
		displayName = profile.getDisplayName();	
		isAuth = true;	
	} catch (Exception e) {
		System.out.println("Fetch user profile error. Message: " + e.getMessage());
	}
}
String state = request.getParameter("state");
if (state == null) {
	state = "";
}
boolean isMobile = false;
if (state != "") {
	if (state.equalsIgnoreCase("mobile")) {
		isMobile = true;
	}
} else {
	String ua=request.getHeader("User-Agent").toLowerCase();
	System.out.println(ua);
	if(ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
		isMobile = true;
	}
}
System.out.println("isMobile: " + isMobile);
if (isMobile) {
%>
<html class="ui-mobile-rendering">
<head>
<title>Health Monitoring System</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<!-- The Stylesheets -->
<link rel="stylesheet" href="css/monitor-mobile.min.css" />
<link rel="stylesheet" href="css/jquery.mobile-1.2.0.min.css" />
<link rel="stylesheet" href="css/jqm-icon-pack.css" />
<link href="css/jquery.mobile.iscrollview.css" media="screen" rel="stylesheet" type="text/css" />
<link href="css/jquery.mobile.iscrollview-pull.css" media="screen" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/application.css" />
<!-- The Scripts -->
<script type="text/javascript">
	window._profile = {
		imageURL : "<%=imageURL%>",
		displayName : "<%=displayName%>",
		isAuth : <%=(isAuth ? "true" : "false")%>
	};
</script>
<script src="https://www.google.com/jsapi"></script>
<script src="lib/core.js"></script>
<script src="lib/iscroll.js"></script>
<script src="lib/jquery.mobile.iscrollview.js"></script>
<script src="js/application.js"></script>
</head>
<body>
</body>
</html>
<% } else { %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Health Monitoring System</title>
<link rel="stylesheet" type="text/css"
	href="https://ssl.gstatic.com/sites/p/250eba/system/app/themes/simplywhite/standard-css-simplywhite-ltr-ltr-jfk.css" />

<!--[if IE]>
<link rel="stylesheet" media="all" type="text/css" href="css/pro_dropline_ie.css" />
<![endif]-->
<!--  jquery core -->
<script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>
<!--  checkbox styling script -->
<script src="js/jquery/ui.core.js" type="text/javascript"></script>
<script src="js/jquery/ui.checkbox.js" type="text/javascript"></script>
<script src="js/jquery/jquery.bind.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('input').checkBox();
		$('#toggle-all').click(function() {
			$('#toggle-all').toggleClass('toggle-checked');
			$('#mainform input[type=checkbox]').checkBox('toggle');
			return false;
		});
	});
</script>

<![if !IE 7]>

<!--  styled select box script version 1 -->
<script src="js/jquery/jquery.selectbox-0.5.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.styledselect').selectbox({
			inputClass : "selectbox_styled"
		});
	});
</script>


<![endif]>

<!--  styled select box script version 2 -->
<script src="js/jquery/jquery.selectbox-0.5_style_2.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.styledselect_form_1').selectbox({
			inputClass : "styledselect_form_1"
		});
		$('.styledselect_form_2').selectbox({
			inputClass : "styledselect_form_2"
		});
	});
</script>

<!--  styled select box script version 3 -->
<script src="js/jquery/jquery.selectbox-0.5_style_2.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.styledselect_pages').selectbox({
			inputClass : "styledselect_pages"
		});
	});
</script>

<!--  styled file upload script -->
<script src="js/jquery/jquery.filestyle.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
	$(function() {
		$("input.file_1").filestyle({
			image : "images/forms/choose-file.gif",
			imageheight : 21,
			imagewidth : 78,
			width : 310
		});
	});
</script>

<!-- Custom jquery scripts -->
<script src="js/jquery/custom_jquery.js" type="text/javascript"></script>

<!-- Tooltips -->
<script src="js/jquery/jquery.tooltip.js" type="text/javascript"></script>
<script src="js/jquery/jquery.dimensions.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('a.info-tooltip ').tooltip({
			track : true,
			delay : 0,
			fixPNG : true,
			showURL : false,
			showBody : " - ",
			top : -35,
			left : 5
		});
	});
</script>
<script src="index/index.nocache.js"></script>
<script src="js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	function downloadFullLog(admin, id) {
		window.location = "/download/log?id=" + encodeURI(admin);

	}
</script>

<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
</head>
<body>
	<iframe id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
	<!-- Start: page-top-outer -->
	<div id="page-top-outer">
		<!-- Start: page-top -->
		<div id="page-top">
			<!-- start logo -->
			<div id="logo">
				<a href=""><img src="images/logo/c-mg_logo.png" width="210" height="80" alt="" /></a>
			</div>
			<div id="version"></div>
			<div class="clear"></div>
		</div>

		<!-- End: page-top -->
	</div>
	<!-- End: page-top-outer -->
	<div class="clear">&nbsp;</div>
	<!--  start nav-outer-repeat................................................................................................. START -->
	<div class="nav-outer-repeat">
		<!--  start nav-outer -->
		<div class="nav-outer">

			<!-- start nav-right -->
			<div id="nav-right"></div>
			<!-- end nav-right -->
			<!--  start nav -->
			<div class="nav">
				<div class="table" id="menuContent"></div>
				<div class="clear"></div>
			</div>
			<!--  start nav -->
		</div>
		<div class="clear"></div>
		<!--  start nav-outer -->
	</div>
	<!--  start nav-outer-repeat................................................... END -->

	<div class="clear"></div>
	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">

		<!-- start content -->
		<div id="content">
			<!--  start message-green -->
			<div id="message-green" style="display: none;">
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="green-left"><div id="content-green"></div></td>
						<td class="green-right"><a class="close-green"><img src="images/table/icon_close_green.gif" alt="" /></a></td>
					</tr>
				</table>
			</div>
			<!--  end message-green -->
			<!--  start page-heading -->
			<div id="page-heading"></div>
			<!-- end page-heading -->
			<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
				<tr>
					<th rowspan="3" class="sized"></th>
					<th class="topleft"></th>
					<td id="tbl-border-top">&nbsp;</td>
					<th class="topright"></th>
					<th rowspan="3" class="sized"></th>
				</tr>
				<tr>
					<td id="tbl-border-left"></td>
					<td>
						<!--  start content-table-inner ...................................................................... START -->
						<div id="content-table-inner">
							<!--  start table-content  -->
							<div id="table-content">
								<div id="step-holder" style="display: none;"></div>
								<div id="statusMes" style="display: none;"></div>
								<div id="message-zone">
									<!--  start message-blue -->
									<div id="message-blue" style="display: none;">
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td class="blue-left"><div id="content-blue"></div></td>
												<td class="blue-right"><a class="close-blue"><img src="images/table/icon_close_blue.gif" alt="" /></a></td>
											</tr>
										</table>
									</div>
									<!--  start message-red -->
									<div id="message-red" style="display: none;">
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td class="red-left"><div id="content-red"></div></td>
												<td class="red-right"><a class="close-red"><img src="images/table/icon_close_red.gif" alt="" /></a></td>
											</tr>
										</table>
									</div>
									<!--  end message-red -->
									<!--  start message-yellow -->
									<div id="message-yellow" style="display: none;">
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td class="yellow-left"><div id="content-yellow"></div></td>
												<td class="yellow-right"><a class="close-yellow"><img src="images/table/icon_close_yellow.gif"
														alt="" /></a></td>
											</tr>
										</table>
									</div>
									<!--  end message-yellow -->

								</div>
								<div id="body-content"></div>
							</div>
							<!--  end table-content  -->

							<div class="clear"></div>
						</div> <!--  end content-table-inner ............................................END  -->
					</td>
					<td id="tbl-border-right"></td>
				</tr>
				<tr>
					<th class="sized bottomleft"></th>
					<td id="tbl-border-bottom">&nbsp;</td>
					<th class="sized bottomright"></th>
				</tr>
			</table>
			<div class="clear">&nbsp;</div>
		</div>
		<!--  end content -->
		<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer........................................................END -->

	<div class="clear">&nbsp;</div>
	<!-- start footer -->
	<div id="footer">
		<!-- <div id="footer-pad">&nbsp;</div> -->
		<!--  start footer-left -->
		<div id="footer-left">Health Monitoring System &copy; Copyright Claybourne McGregor Consulting Ltd.
			www.c-mg.com. All rights reserved.</div>
		<!--  end footer-left -->
		<div class="clear">&nbsp;</div>
	</div>
	<div id="img-loading">
		<span id="loading-icon"></span>
	</div>
	<!-- end footer -->

</body>
</html>
<%}%>