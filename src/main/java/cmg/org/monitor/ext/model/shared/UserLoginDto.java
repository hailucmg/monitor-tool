package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.List;

public class UserLoginDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String authDomain;
	private String email;
	private String nickName;
	private String userId;
	private int role;
	private boolean isLogin;
	private String logoutUrl;
	private String loginUrl;

	private boolean isAdmin;
	private List<String> groupIds;
	
	private boolean needAddAccount = false;

	public UserLoginDto() {
		this.isAdmin = false;
		this.isLogin = false;
	}

	public UserLoginDto(boolean isLogin) {
		this.isLogin = isLogin;
		this.isAdmin = false;
	}

	public UserLoginDto(String authDomain, String email, String nickName,
			String userId) {
		this.authDomain = authDomain;
		this.email = email;
		this.nickName = nickName;
		this.userId = userId;
	}

	public boolean checkGroup(String groupId) {
		if (groupIds != null && groupIds.size() > 0) {
			for (String gid: groupIds) {
				if (gid.equalsIgnoreCase(groupId)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getAuthDomain() {
		return authDomain;
	}

	public void setAuthDomain(String authDomain) {
		this.authDomain = authDomain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin
	 *            the isAdmin to set
	 */

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the groupIds
	 */
	public List<String> getGroupIds() {
		return groupIds;
	}

	/**
	 * @param groupIds
	 *            the groupIds to set
	 */

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	/** 
	 * @return the needAddAccount 
	 */
	public boolean isNeedAddAccount() {
		return needAddAccount;
	}

	/** 
	 * @param needAddAccount the needAddAccount to set 
	 */
	
	public void setNeedAddAccount(boolean needAddAccount) {
		this.needAddAccount = needAddAccount;
	}
}
