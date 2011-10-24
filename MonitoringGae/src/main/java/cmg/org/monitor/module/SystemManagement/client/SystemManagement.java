package cmg.org.monitor.module.SystemManagement.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class SystemManagement implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		Button bttDelete = new Button();
		bttDelete.addStyleName("form-delete");
		RootPanel.get("delete").add(bttDelete);
		
		Button bttEdit = new Button();
		bttEdit.addStyleName("form-edit");
		RootPanel.get("edit").add(bttEdit);
	}

}
