package cmg.org.monitor.module.client;

import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class EditGroup extends AncestorEntryPoint {
	FlexTable flexTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_EDIT_GROUP) {
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
