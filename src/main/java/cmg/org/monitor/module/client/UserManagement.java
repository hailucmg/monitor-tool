package cmg.org.monitor.module.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class UserManagement implements EntryPoint {
	UserManagementServiceAsync userService = GWT
			.create(UserManagementService.class);
	static private Table myTable;

	@Override
	public void onModuleLoad() {
		userService.getUserLogin(new AsyncCallback<UserLoginDto>() {
			@Override
			public void onFailure(Throwable caught) {
				setVisibleLoadingImage(false);
				initMessage("Server error. ", HTMLControl.HTML_USER_MANAGEMENT_NAME,
						"Try again. ", HTMLControl.RED_MESSAGE);
				setVisibleMessage(true, HTMLControl.RED_MESSAGE);
			}
			@Override
			public void onSuccess(UserLoginDto result) {
				setVisibleLoadingImage(false);
				if (result != null) {
					if (result.isLogin()) {
						RootPanel.get("menuContent").add(
								HTMLControl.getMenuHTML(
										HTMLControl.USER_MANAGEMENT_PAGE,
										result.getRole()));
						RootPanel.get("nav-right")
								.add(HTMLControl.getLogoutHTML(result
										.getLogoutUrl(),result.getEmail()));
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
									"Wellcome to Monitor System, "
											+ result.getNickName()
											+ ". If have any question. ",
									HTMLControl.HTML_ABOUT_NAME, "Contact Us.",
									HTMLControl.GREEN_MESSAGE);
							setVisibleMessage(true, HTMLControl.GREEN_MESSAGE);
							init();
						}
					} else {
						initMessage("Must login to use Monitor System. ", result.getLoginUrl(),
								"Login. ", HTMLControl.RED_MESSAGE);
						setVisibleMessage(true, HTMLControl.RED_MESSAGE);
					}
				} else {
					initMessage("Server error. ", HTMLControl.HTML_USER_MANAGEMENT_NAME,
							"Try again. ", HTMLControl.RED_MESSAGE);
					setVisibleMessage(true, HTMLControl.RED_MESSAGE);
				}
			}
		});
	}

	void init() {
		Runnable onLoadCallback = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				myTable = new Table();
				RootPanel.get("dataTableUser").add(myTable);
				initContent();
			}
		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
	}

	void setVisibleLoadingImage(boolean b) {
		RootPanel.get("img-loading").setVisible(b);
	}

	void setVisibleMessage(boolean b, int type) {
		RootPanel.get("message-" + HTMLControl.getColor(type)).setVisible(b);
	}

	/*
	 * Show message with content
	 */
	void initMessage(String message, String url, String titleUrl, int type) {
		RootPanel.get("content-" + HTMLControl.getColor(type)).clear();
		RootPanel.get("content-" + HTMLControl.getColor(type)).add(
				new HTML(message
						+ ((url.trim().length() == 0) ? "" : ("  <a href=\""
								+ url + "\">" + titleUrl + "</a>")), true));
	}

	private void initContent() {
		userService.listUser(new AsyncCallback<Map<String, UserDto>>() {

			@Override
			public void onSuccess(Map<String, UserDto> result) {
				// TODO Auto-generated method stub
				drawTable(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.toString());
			}
		});
	}

	@SuppressWarnings({ "rawtypes" })
	private AbstractDataTable createData(Map<String, UserDto> listUser) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Username");
		data.addColumn(ColumnType.STRING, "Email");
		data.addColumn(ColumnType.STRING, "Group");
		data.addColumn(ColumnType.STRING, "Permission");
		data.addRows(listUser.size());
		Set set = listUser.entrySet();
		Iterator iter = set.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			UserDto u = (UserDto) entry.getValue();
			String permission = "N/A";
			if (u.getGroup().contains("admin")) {
				permission = "Admin";
			} else if (u.getGroup().startsWith("monitor")) {
				permission = "Normal user";
			}
			data.setValue(i, 0, u.getUsername());
			data.setValue(i, 1, u.getEmail());
			data.setValue(i, 2, u.getGroup());
			data.setValue(i, 3, permission);
			i++;
		}
		return data;
	}

	private Options option() {
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(true);
		return option;

	}

	private void drawTable(Map<String, UserDto> listUser) {
		myTable.draw(createData(listUser), option());

	}
}
