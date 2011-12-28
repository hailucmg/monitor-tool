package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

public class UserDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String username;
	String group;
	String email;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public UserDto() {
	}

	public UserDto(String username, String email, String group,
			String permission) {
		this.username = username;
		this.email = email;
		this.group = group;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return getUsername();

	}

	public int compareByName(UserDto c) {
		int x = username.trim().compareTo(c.getUsername().trim());
		return x;
	}
}
