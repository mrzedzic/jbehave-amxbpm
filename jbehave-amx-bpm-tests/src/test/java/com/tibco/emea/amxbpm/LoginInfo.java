/**
 * 
 */
package com.tibco.emea.amxbpm;

/**
 * @author mrzedzic
 * 
 */
public class LoginInfo {
	/** Login Info object which has logged in user info */
	private String userName;
	private String guid;
	private String position;

	public LoginInfo(String userName, String guid) {
		this.userName = userName;
		this.guid = guid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return "LoginInfo [userName=" + userName + "]";
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}
