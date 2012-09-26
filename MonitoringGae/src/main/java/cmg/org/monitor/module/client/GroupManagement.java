package cmg.org.monitor.module.client;

import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.visualizations.Table;

public class GroupManagement extends AncestorEntryPoint{
	FlexTable flexTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_GROUP_MANAGEMENT) {
			monitorGwtSv.getDefaultContent(new AsyncCallback<String>() {

				@Override
				public void onSuccess(String result) {
					if (result != null) {
						addWidget(HTMLControl.ID_BODY_CONTENT, new HTML(result));
						setVisibleLoadingImage(false);
						setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
						
					} else {
						showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
								"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
						setVisibleLoadingImage(false);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
							"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
					setVisibleLoadingImage(false);
				}
			});

		}
	}

}