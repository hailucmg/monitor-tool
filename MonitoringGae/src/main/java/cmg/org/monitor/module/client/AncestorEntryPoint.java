package cmg.org.monitor.module.client;

import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.Table;

public abstract class AncestorEntryPoint implements EntryPoint {

	private static int count = 0;

	protected static Timer timerMess;
	protected static Timer timerReload;

	protected static Timer timerJvm = null;
	protected static Timer timerCpu = null;
	protected static Timer timerMem = null;
	protected static Timer timerService = null;
	protected static Timer timerFilesystem = null;
	
	protected static UserLoginDto currentUser = null;

	protected String hash;

	protected boolean isLogin = false;

	protected int currentPage = HTMLControl.PAGE_DASHBOARD;
	protected static final MonitorGwtServiceAsync monitorGwtSv = GWT
			.create(MonitorGwtService.class);

	protected static String currentUrl;

	protected int role = MonitorConstant.ROLE_GUEST;

	protected boolean isOnload = true;

	static boolean isReadyDelete = true;

	protected static DialogBox dialogBox;

	private static DialogBox dialogFix;
	
	protected static DialogBox dialogConfirm;

	@Override
	public void onModuleLoad() {
		initDialog();
		DashBoard.exportRequest();
		dialogBox = new DialogBox();
		dialogFix = new DialogBox();
		dialogFix.setStyleName("");
		addWidget(HTMLControl.ID_VERSION, new HTML("<span>Version "
				+ MonitorConstant.VERSION + "</span>"));
		try {
			Runnable onLoadCallback = new Runnable() {
				@Override
				public void run() {
					hash = History.getToken();
					initHash(hash);
					History.addValueChangeHandler(new ValueChangeHandler<String>() {
						@Override
						public void onValueChange(ValueChangeEvent<String> event) {
							initHash(event.getValue());
						}
					});
				}
			};
			VisualizationUtils.loadVisualizationApi(onLoadCallback,
					Table.PACKAGE, Gauge.PACKAGE, AreaChart.PACKAGE,
					PieChart.PACKAGE, AnnotatedTimeLine.PACKAGE);
		} catch (Exception ex) {
			Window.Location.replace(HTMLControl.trimHashPart(Window.Location
					.getHref()));
			return;
		}

	}

	protected void clearTimer() {
		if (timerMess != null) {
			timerMess.cancel();
			timerMess = null;
		}
		if (timerReload != null) {
			timerReload.cancel();
			timerReload = null;
		}
		if (timerCpu != null) {
			timerCpu.cancel();
			timerCpu = null;
		}
		if (timerFilesystem != null) {
			timerFilesystem.cancel();
			timerFilesystem = null;
		}
		if (timerJvm != null) {
			timerJvm.cancel();
			timerJvm = null;
		}
		if (timerMem != null) {
			timerMem.cancel();
			timerMem = null;
		}
		if (timerService != null) {
			timerService.cancel();
			timerService = null;
		}
		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
		setVisibleWidget(HTMLControl.ID_MESSAGE_YELLOW, false);
		setVisibleWidget(HTMLControl.ID_MESSAGE_RED, false);
		setVisibleWidget(HTMLControl.ID_MESSAGE_BLUE, false);

	}

	protected void visibleMessage() {

	}

	protected static void setOnload(boolean b) {
		if (b) {
			dialogFix.center();
		} else {
			dialogFix.hide();
		}
	}

	protected void clear() {
		setVisibleWidget(HTMLControl.ID_STEP_HOLDER, false);
		setVisibleWidget(HTMLControl.ID_STATUS_MESSAGE, false);
	}

	protected void changeMenu(int page, int role) {
		RootPanel.get(HTMLControl.ID_MENU).clear();
		addWidget(HTMLControl.ID_MENU, HTMLControl.getMenuHTML(page, role));
	}

	protected void setStatusMessage(String mes) {
		addWidget(HTMLControl.ID_STATUS_MESSAGE, new HTML(mes));
		setVisibleWidget(HTMLControl.ID_STATUS_MESSAGE, true);
	}

	protected void setStepHolder(HTML html) {
		addWidget(HTMLControl.ID_STEP_HOLDER, html);
		setVisibleWidget(HTMLControl.ID_STEP_HOLDER, true);
	}

	protected static void setVisibleWidget(String id, boolean b) {
		RootPanel.get(id).setVisible(b);
	}

	private void initHash(String hash) {
		currentUrl = HTMLControl.trimHashPart(Window.Location.getHref());		
		if (hash == null || hash.equals("") || hash.equals("dashboard/reload")) {
			Window.Location.replace(currentUrl
					+ HTMLControl.HTML_DASHBOARD_NAME);
			setOnload(false);
			return;
		} else if (HTMLControl.validIndex(hash)) {
			setOnload(true);
			dialogBox.hide();
			clear();
			currentPage = HTMLControl.getPageIndex(hash);
			if (currentUser != null) {
				if (currentUser.isLogin()) {
					if (currentUser.getRole() != MonitorConstant.ROLE_ADMIN
							&& (currentPage == HTMLControl.PAGE_USER_MANAGEMENT
									|| currentPage == HTMLControl.PAGE_ADD_SYSTEM
									|| currentPage == HTMLControl.PAGE_SYSTEM_CHANGE_LOG
									|| currentPage == HTMLControl.PAGE_SYSTEM_MANAGEMENT
									|| currentPage == HTMLControl.PAGE_GOOGLE_MANAGEMENT
									|| currentPage == HTMLControl.PAGE_GROUP_MANAGEMENT
									|| currentPage == HTMLControl.PAGE_USER_MANAGEMENT
									|| currentPage == HTMLControl.PAGE_USER_ROLE
									|| currentPage == HTMLControl.PAGE_EDIT_GROUP
									|| currentPage == HTMLControl.PAGE_ADD_GROUP
									|| currentPage == HTMLControl.PAGE_INVITE)) {
						Window.Location.replace(currentUrl
								+ HTMLControl.HTML_DASHBOARD_NAME);
						setOnload(false);
						return;
					}
					
					if (currentUser.isAdmin() 
							&& currentUser.isNeedAddAccount()
							&& !(currentPage == HTMLControl.PAGE_GOOGLE_MANAGEMENT
							|| currentPage == HTMLControl.PAGE_ABOUT
							|| currentPage == HTMLControl.PAGE_HELP
							|| currentPage == HTMLControl.PAGE_REVISION)) {
						Window.Location.replace(currentUrl
								+ HTMLControl.HTML_GOOGLE_MANAGEMENT_NAME);
						setOnload(false);
						showMessage(
								"There are no user in system. Please create a google account to sync. ",
								"", "",
								HTMLControl.RED_MESSAGE, true);						
						return;
					}
				}
			}
			if (currentPage != HTMLControl.PAGE_SYSTEM_DETAIL
					&& currentPage != HTMLControl.PAGE_SYSTEM_STATISTIC) {
				addWidget(HTMLControl.ID_PAGE_HEADING,
						HTMLControl.getPageHeading(currentPage));
			}
			if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL
					|| currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC
					|| currentPage == HTMLControl.PAGE_ADD_SYSTEM
					|| currentPage == HTMLControl.PAGE_SYSTEM_MANAGEMENT
					|| currentPage == HTMLControl.PAGE_GOOGLE_MANAGEMENT
					|| currentPage == HTMLControl.PAGE_GROUP_MANAGEMENT
					|| currentPage == HTMLControl.PAGE_USER_MANAGEMENT
					|| currentPage == HTMLControl.PAGE_USER_ROLE
					|| currentPage == HTMLControl.PAGE_ADD_GROUP
					|| currentPage == HTMLControl.PAGE_INVITE) {

				addWidget(
						HTMLControl.ID_STEP_HOLDER,
						HTMLControl.getStepHolder(currentPage,
								HTMLControl.getSystemId(hash)));
				setVisibleWidget(HTMLControl.ID_STEP_HOLDER, true);
			}
			if (isLogin) {
				clearTimer();
				setVisibleLoadingImage(true);
				changeMenu(currentPage, role);
				visibleMessage();
				init();
			} else {
				doLogin();
			}

		} /*
		 * else { Window.Location.replace(currentUrl +
		 * HTMLControl.HTML_DASHBOARD_NAME); }
		 */
	}

	protected void doLogin() {
		monitorGwtSv.getUserLogin(new AsyncCallback<UserLoginDto>() {
			@Override
			public void onFailure(Throwable caught) {
				showMessage("Server error. ", HTMLControl.HTML_DASHBOARD_NAME,
						"Try again. ", HTMLControl.RED_MESSAGE, true);
			}

			@Override
			public void onSuccess(UserLoginDto result) {
				if (result != null) {
					if (result.isLogin()) {
						currentUser = result;
						addWidget(HTMLControl.ID_MENU, HTMLControl.getMenuHTML(
								HTMLControl.PAGE_DASHBOARD, result.getRole()));
						addWidget(HTMLControl.ID_LOGIN_FORM, HTMLControl
								.getLogoutHTML(result.getLogoutUrl(),
										result.getEmail()));
						if (result.getRole() == MonitorConstant.ROLE_GUEST) {
							if (currentPage == HTMLControl.PAGE_ABOUT
									|| currentPage == HTMLControl.PAGE_HELP
									|| currentPage == HTMLControl.PAGE_REVISION) {
								setVisibleMessage(false, HTMLControl.YELLOW_MESSAGE);
								setVisibleMessage(false, HTMLControl.RED_MESSAGE);
								role = result.getRole();
								changeMenu(currentPage, role);								
								init();
							} else {
								setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
								setVisibleLoadingImage(false);
								showPermissionWarningMessage(result.getNickName(), result.getLogoutUrl());								
							}
						} else {
							showMessage(
									"Welcome to Health Monitoring System. If have any question ",
									HTMLControl.HTML_ABOUT_NAME, "Contact Us.",
									HTMLControl.GREEN_MESSAGE, true);
							isLogin = true;
							role = result.getRole();
							changeMenu(currentPage, role);
							if (currentUser.getFullname().trim().length() == 0 &&
									!currentUser.isAdmin()) {
								dialogConfirm.center();
							}
							init();
						}
					} else {
						addWidget(HTMLControl.ID_LOGIN_FORM,
								HTMLControl.getLoginHTML(result.getLoginUrl()));
						setVisibleLoadingImage(false);
						showMessage(
								"Must login to use Health Monitoring System. ",
								result.getLoginUrl(), "Login. ",
								HTMLControl.RED_MESSAGE, true);
					}
				} else {
					showMessage("Server error. ",
							HTMLControl.HTML_DASHBOARD_NAME, "Try again. ",
							HTMLControl.RED_MESSAGE, true);
				}
			}
		});
	}

	protected abstract void init();
	
	protected abstract void initDialog();

	protected static void showRedirectCountMessage(final String mes,
			final String url, final String titleUrl, final int typeMessage) {
		count = MonitorConstant.REDIRECT_WAIT_TIME / 1000;
		if (timerMess != null) {
			setVisibleMessage(false, typeMessage);
			timerMess.cancel();
		}

		timerMess = new Timer() {
			@Override
			public void run() {
				showMessage(mes + " in " + HTMLControl.getStringTime(--count),
						url, titleUrl, typeMessage, false);
				if (count <= 0) {
					setVisibleMessage(false, typeMessage);
					Window.Location.replace(currentUrl + url);					
					this.cancel();
					return;
				}
			}
		};
		timerMess.run();
		setVisibleMessage(true, typeMessage);
		timerMess.scheduleRepeating(1000);
	}

	protected static void showReloadCountMessage(final int typeMessage) {
		count = MonitorConstant.REFRESH_RATE / 1000;
		if (timerMess != null) {
			timerMess.cancel();
			timerMess = null;
		}
		showMessage(
				"Latest status of systems. Update in "
						+ HTMLControl.getStringTime(count),
				"#dashboard/reload",
				"<input type=\"button\"  class=\"form-reload\">", typeMessage,
				true);
		timerMess = new Timer() {
			@Override
			public void run() {
				int time = --count;
				if (time < 0) {
					count = MonitorConstant.REFRESH_RATE / 1000;
					time = --count;
				}
				showMessage("Latest status of systems. Update in "
						+ HTMLControl.getStringTime(time),
						"#dashboard/reload",
						"<input type=\"button\"  class=\"form-reload\">",
						typeMessage, false);
				
			}
		};
		timerMess.run();
		timerMess.scheduleRepeating(1000);
	}

	protected static void addWidget(String id, Widget w) {
		RootPanel.get(id).clear();
		RootPanel.get(id).add(w);
	}

	protected static void setVisibleLoadingImage(boolean b) {
		RootPanel.get("img-loading").setVisible(b);
		setOnload(b);
	}

	protected static void setVisibleMessage(boolean b, int type) {
		RootPanel.get("message-" + HTMLControl.getColor(type)).setVisible(b);
	}

	protected static void clear(Element parent) {
		Element firstChild;

		while ((firstChild = DOM.getFirstChild(parent)) != null) {
			DOM.removeChild(parent, firstChild);
		}
	}

	protected static void showMessage(String message, String url,
			String titleUrl, int type, boolean isVisible) {
		clear(DOM.getElementById("content-" + HTMLControl.getColor(type)));
		DOM.getElementById("content-" + HTMLControl.getColor(type))
				.setInnerHTML(
						message
								+ ((url.trim().length() == 0) ? ""
										: ("  <a href=\"" + url + "\">"
												+ titleUrl + "</a>")));
		if (isVisible) {
			setVisibleMessage(isVisible, type);
		}
		setOnload(false);
	}
	
	protected static void showPermissionWarningMessage(String user, String logoutURL) {
		clear(DOM.getElementById("content-" + HTMLControl.getColor(HTMLControl.YELLOW_MESSAGE)));
		DOM.getElementById("content-" + HTMLControl.getColor(HTMLControl.YELLOW_MESSAGE))
				.setInnerHTML("Hello "
						+ user
						+ ". You might not have permission to use Health Monitoring System. "			
				
								+ "  <a href=\"" + logoutURL + "\">Login with another account</a>" + "&nbsp;<span style=\"font-weight: normal;\">or</span>&nbsp;<a href=\"#\" onclick=\"javascript:showRequestForm();return false;\">send request permission</a>");
		
		setVisibleMessage(true, HTMLControl.YELLOW_MESSAGE);		
		setOnload(false);
	}
}
