package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

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
	
	public UserLoginDto() {
		
	} 
	
	public UserLoginDto(boolean isLogin) {
		this.isLogin = isLogin;
	} 
	
	public UserLoginDto(String authDomain, String email, String nickName,
						String userId) {
		this.authDomain = authDomain;
		this.email = email;
		this.nickName = nickName;
		this.userId = userId;
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
}
