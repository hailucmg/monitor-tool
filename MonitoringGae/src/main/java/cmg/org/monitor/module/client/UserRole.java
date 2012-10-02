package cmg.org.monitor.module.client;

import java.util.List;

import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class UserRole extends AncestorEntryPoint {
    static private Table myTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_USER_ROLE) {
		    	UserRole.exportStaticMethod();
			myTable = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT, myTable);
			initContent();
		}
	}

	public static native void exportStaticMethod() /*-{
	$wnd.updateUserRole =
	$entry(@cmg.org.monitor.module.client.UserRole::updateUserRole(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;))
	}-*/;
	
	static void updateUserRole(String email, String role, String b){
	    System.out.println(b);
	    System.out.println(email);
	    System.out.println(role);
	    monitorGwtSv.updateUserRole(email, role, b.equalsIgnoreCase("true"), new AsyncCallback<Boolean>() {

		@Override
		public void onFailure(Throwable caught) {
		    showMessage("Error.", "", "", HTMLControl.RED_MESSAGE, true);
		}

		@Override
		public void onSuccess(Boolean result) {
		    if(!result){
			showMessage("Error.", "", "", HTMLControl.RED_MESSAGE, true);
		    }
		}
		
	    });
	}
	
	private void initContent() {
		monitorGwtSv.listAllSystemUsers(new AsyncCallback<List<SystemUser>>() {

			@Override
			public void onSuccess(List<SystemUser> result) {
				if (result != null) {
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					drawTable(result);
				} else {
					showMessage("Oops! Error.",
							HTMLControl.HTML_DASHBOARD_NAME,
							"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
			}
		});
	}

	private AbstractDataTable createData(List<SystemUser> result) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Domain");
		data.addColumn(ColumnType.STRING, "Username");
		data.addColumn(ColumnType.STRING, "Email");
		data.addColumn(ColumnType.STRING, "Administrator");
		data.addColumn(ColumnType.STRING, "User");
		data.addRows(result.size());

		List<SystemUser> sortUser = sortByname(result);
		for (int j = 0; j < sortUser.size(); j++) {

			data.setValue(j, 0, sortUser.get(j).getDomain());
			data.setValue(j, 1, sortUser.get(j).getUsername());
			data.setValue(j, 2, sortUser.get(j).getEmail());
			if(sortUser.get(j).checkRole(SystemRole.ROLE_ADMINISTRATOR)){
			    data.setValue(j, 3, "<input class='ckUserRole' type='checkbox' name='user_role' role='"+SystemRole.ROLE_ADMINISTRATOR+"' username='"+sortUser.get(j).getEmail()+"' style='display:block;margin-left:auto;margin-right:auto;border-color:green;' checked='checked'>");
			}else{
			    data.setValue(j, 3, "<input class='ckUserRole' type='checkbox' name='user_role' role='"+SystemRole.ROLE_ADMINISTRATOR+"' username='"+sortUser.get(j).getEmail()+"' style='display:block;margin-left:auto;margin-right:auto;border-color:green;'>");
			}
			
			if(sortUser.get(j).checkRole(SystemRole.ROLE_USER)){
			    data.setValue(j, 4, "<input class='ckUserRole' type='checkbox' name='user_role' role='"+SystemRole.ROLE_USER+"' username='"+sortUser.get(j).getEmail()+"' style='display:block;margin-left:auto;margin-right:auto;border-color:green;' checked='checked'>");
			}else{
			    data.setValue(j, 4, "<input class='ckUserRole' type='checkbox' name='user_role' role='"+SystemRole.ROLE_USER+"' username='"+sortUser.get(j).getEmail()+"' style='display:block;margin-left:auto;margin-right:auto;border-color:green;'>");
			}
		}
		return data;
	}

	public List<SystemUser> sortByname(List<SystemUser> users) {
		SystemUser temp = null;
		for (int i = 1; i < users.size(); i++) {
			int j;
			SystemUser val = users.get(i);
			for (j = i - 1; j > -1; j--) {
				temp = users.get(j);
				if (temp.compareByDomain(val) <= 0) {
					break;
				}
				users.set(j+1, temp);
			}
			users.set(j+1, val);
		}
		return users;
	}

	private Options option() {
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(true);
		return option;

	}

	private void drawTable(List<SystemUser> result) {
		myTable.draw(createData(result), option());
	}

}
