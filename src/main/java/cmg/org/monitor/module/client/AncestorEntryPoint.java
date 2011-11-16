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

	protected Timer timerMess;
	protected Timer timerReload;

	protected String hash;

	protected boolean isLogin = false;

	protected int currentPage = HTMLControl.PAGE_DASHBOARD;
	protected static final MonitorGwtServiceAsync monitorGwtSv = GWT
			.create(MonitorGwtService.class);

	protected String currentUrl;

	protected int role = MonitorConstant.ROLE_GUEST;

	protected boolean isOnload = true;

	static boolean isReadyDelete = true;

	@Override
	public void onModuleLoad() {
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
		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
		if (currentPage != HTMLControl.PAGE_DASHBOARD
				&& currentPage != HTMLControl.PAGE_SYSTEM_STATISTIC) {
			setVisibleWidget(HTMLControl.ID_MESSAGE_YELLOW, false);
		}
		if (currentPage != HTMLControl.PAGE_SYSTEM_MANAGEMENT) {
			setVisibleWidget(HTMLControl.ID_MESSAGE_RED, false);
			setVisibleWidget(HTMLControl.ID_MESSAGE_BLUE, false);
		}
		
	}

	protected void visibleMessage() {

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

	protected void setVisibleWidget(String id, boolean b) {
		RootPanel.get(id).setVisible(b);
	}

	private void initHash(String hash) {
		currentUrl = HTMLControl.trimHashPart(Window.Location.getHref());
		if (HTMLControl.validIndex(hash)) {
			clear();
			currentPage = HTMLControl.getPageIndex(hash);

			addWidget(HTMLControl.ID_PAGE_HEADING,
					HTMLControl.getPageHeading(currentPage));
			if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL
					|| currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC
					|| currentPage == HTMLControl.PAGE_ADD_SYSTEM
					|| currentPage == HTMLControl.PAGE_SYSTEM_MANAGEMENT) {

				addWidget(
						HTMLControl.ID_STEP_HOLDER,
						HTMLControl.getStepHolder(currentPage,
								HTMLControl.getSystemId(hash)));
				setVisibleWidget(HTMLControl.ID_STEP_HOLDER, true);
			}
			if (isLogin) {
				clearTimer();
				setVisibleLoadingImage(true);
				if (currentPage == HTMLControl.PAGE_DELETE_SYSTEM
						&& isReadyDelete) {
					isReadyDelete = false;
					String sid = HTMLControl.getSystemId(History.getToken());
					Window.Location.replace(HTMLControl
							.trimHashPart(Window.Location
									.getHref())
							+ HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME);
					monitorGwtSv.deleteSystem(sid,
							new AsyncCallback<Boolean>() {
								@Override
								public void onSuccess(Boolean result) {
									isReadyDelete = true;
									initForm();
								}

								@Override
								public void onFailure(Throwable caught) {
									isReadyDelete = true;	
									initForm();
								}
							});
				} else {
					if (isReadyDelete) {
						initForm();
					}
				}
			} else {
				doLogin();
			}

		} else {
			Window.Location.replace(currentUrl
					+ HTMLControl.HTML_DASHBOARD_NAME);
		}
	}

	protected void initForm() {
		changeMenu(currentPage, role);
		visibleMessage();
		init();
	}

	protected void doLogin() {
		monitorGwtSv.getUserLogin(new AsyncCallback<UserLoginDto>() {
			@Override
			public void onFailure(Throwable caught) {
				initMessage("Server error. ", HTMLControl.HTML_DASHBOARD_NAME,
						"Try again. ", HTMLControl.RED_MESSAGE);
				setVisibleMessage(true, HTMLControl.RED_MESSAGE);
			}

			@Override
			public void onSuccess(UserLoginDto result) {
				if (result != null) {
					if (result.isLogin()) {
						addWidget(HTMLControl.ID_MENU, HTMLControl.getMenuHTML(
								HTMLControl.PAGE_DASHBOARD, result.getRole()));
						addWidget(HTMLControl.ID_LOGIN_FORM, HTMLControl
								.getLogoutHTML(result.getLogoutUrl(),
										result.getEmail()));
						if (result.getRole() == MonitorConstant.ROLE_GUEST) {
							initMessage(
									"Hello "
											+ result.getNickName()
											+ ". You might not have permission to use Monitor System. ",
									result.getLogoutUrl(),
									"Login with another account.",
									HTMLControl.YELLOW_MESSAGE);
							setVisibleMessage(true, HTMLControl.YELLOW_MESSAGE);
						} else {
							initMessage(
									"Wellcome to Health Monitoring System. If have any question ",
									HTMLControl.HTML_ABOUT_NAME, "Contact Us.",
									HTMLControl.GREEN_MESSAGE);
							setVisibleMessage(true, HTMLControl.GREEN_MESSAGE);
							isLogin = true;
							role = result.getRole();
							changeMenu(currentPage, role);
							init();
						}
					} else {
						addWidget(HTMLControl.ID_LOGIN_FORM, HTMLControl
								.getLoginHTML(result.getLoginUrl()));
						initMessage("Must login to use Monitor System. ",
								result.getLoginUrl(), "Login. ",
								HTMLControl.RED_MESSAGE);
						setVisibleMessage(true, HTMLControl.RED_MESSAGE);
					}
				} else {
					initMessage("Server error. ",
							HTMLControl.HTML_DASHBOARD_NAME, "Try again. ",
							HTMLControl.RED_MESSAGE);
					setVisibleMessage(true, HTMLControl.RED_MESSAGE);
				}
			}
		});
	}

	protected void showErrorMessage(int type, String desPage, String title) {
		String mes = "";
		switch (type) {
		case HTMLControl.ERROR_SYSTEM_ID:
			mes = "Invalid system ID. ";
			break;
		case HTMLControl.ERROR_NORMAL:
		default:
			mes = "Oops! Error. ";
			break;
		}
		initMessage(mes, desPage, title, HTMLControl.RED_MESSAGE);
		setVisibleMessage(true, HTMLControl.RED_MESSAGE);
	}

	protected abstract void init();

	public static native void redirect(String url)/*-{
		$wnd.location = url;
	}-*/;

	void showRedirectCountMessage(final String mes, final String url,
			final String titleUrl, final int typeMessage) {
		count = MonitorConstant.REDIRECT_WAIT_TIME / 1000;
		if (timerMess != null) {
			setVisibleMessage(false, typeMessage);
			timerMess.cancel();
		}

		timerMess = new Timer() {
			@Override
			public void run() {
				initMessage(mes + " in " + HTMLControl.getStringTime(--count),
						url, titleUrl, typeMessage);
				if (count <= 0) {
					setVisibleMessage(false, typeMessage);
					Window.Location.replace(currentUrl + url);
					this.cancel();
				}
			}
		};
		timerMess.run();
		setVisibleMessage(true, typeMessage);
		timerMess.scheduleRepeating(1000);
	}

	protected void showReloadCountMessage(final int typeMessage) {
		count = MonitorConstant.REFRESH_RATE / 1000;
		if (timerMess != null) {
			setVisibleMessage(false, typeMessage);
			timerMess.cancel();
		}
		initMessage(
				"Latest status of systems. Update in "
						+ HTMLControl.getStringTime(count),
				"#dashboard/reload", "Reload now.", typeMessage);
		setVisibleMessage(true, typeMessage);
		timerMess = new Timer() {
			@Override
			public void run() {
				initMessage("Latest status of systems. Update in "
						+ HTMLControl.getStringTime(--count),
						"#dashboard/reload", "Reload now.", typeMessage);
				if (count <= 0) {
					setVisibleMessage(false, typeMessage);
					Window.Location.replace(Window.Location.getHref()
							+ HTMLControl.PAGE_DASHBOARD);
					this.cancel();
				}
			}
		};
		timerMess.run();
		timerMess.scheduleRepeating(1000);
	}

	protected void addWidget(String id, Widget w) {
		RootPanel.get(id).clear();
		RootPanel.get(id).add(w);
	}

	protected void setVisibleLoadingImage(boolean b) {
		isOnload = b;
		RootPanel.get("img-loading").setVisible(b);
	}

	protected void setVisibleMessage(boolean b, int type) {
		RootPanel.get("message-" + HTMLControl.getColor(type)).setVisible(b);
	}

	protected void initMessage(String message, String url, String titleUrl,
			int type) {
		RootPanel.get("content-" + HTMLControl.getColor(type)).clear();
		RootPanel.get("content-" + HTMLControl.getColor(type)).add(
				new HTML(message
						+ ((url.trim().length() == 0) ? "" : ("  <a href=\""
								+ url + "\">" + titleUrl + "</a>")), true));
	}

	protected void clear(Element parent) {
		Element firstChild;
		while ((firstChild = DOM.getFirstChild(parent)) != null) {
			DOM.removeChild(parent, firstChild);
		}
	}
}
