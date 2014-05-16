/**
 * 
 */
package com.tibco.emea.amxbpm;

/**
 * @author mrzedzic
 *
 */
public class AmxBpmProcess {
	private String processId;
	private LoginInfo owner;
	private AmxBpmProcessState state;
	private String currentTaskName;
	
	public AmxBpmProcess(){
		
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public LoginInfo getOwner() {
		return owner;
	}

	public void setOwner(LoginInfo owner) {
		this.owner = owner;
	}

	public AmxBpmProcessState getState() {
		return state;
	}

	public void setState(AmxBpmProcessState state) {
		this.state = state;
	}
	public String getCurrentTaskName() {
		return currentTaskName;
	}

	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}

}
