package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

public class GroupMonitor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	
	private String name;
	
	@Override
	public String toString() {
		return name;
	}
	
	public void parseName() {
		if (id != null && !id.equals("")) {
			name = id.substring(id.lastIndexOf("/") + 1, id.lastIndexOf("%"));
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GroupMonitor() {

	}

	public GroupMonitor(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
