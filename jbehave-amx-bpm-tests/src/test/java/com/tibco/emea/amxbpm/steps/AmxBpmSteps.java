package com.tibco.emea.amxbpm.steps;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.stereotype.Component;

import com.tibco.emea.amxbpm.AmxBpmClient;
import com.tibco.emea.amxbpm.AmxBpmProcess;
import com.tibco.emea.amxbpm.LoginInfo;

/**
 * @author Marcin Rzedzicki
 * 
 */
@Component
public class AmxBpmSteps {

	AmxBpmClient amxBpmClient;
	AmxBpmProcess process = null;
	long workItemId = -1;
	long workItemVersion = -1;
	String uid, uidVersion;
	String tempNameUser = "";
	LoginInfo ldapUser = null;

	@BeforeStories
	public void initialize() {
		amxBpmClient = new AmxBpmClient();
	}
	
	@Given("zarejestrowanego <user>")
	public void givenZarejestrowanegouser(@Named("user") String user) {
		ldapUser = amxBpmClient.getUserDetails(user);
		assertNotNull("User: "+ user+" not found in LDAP", ldapUser.getGuid());
	}
	
	@Then("system sprawdzi czy jest on na pozycji <position>")
	public void validatePosition(@Named("position") String position) {
	  assertEquals(position, ldapUser.getPosition());
	}

	@Given("zalogowanego uzytkownika <processOwner>")
	public void login(@Named("ldapName") String ldapName) {
		LoginInfo user = amxBpmClient.getUserDetails(ldapName);
		assertNotNull("User: "+ ldapName+" not found in LDAP", user.getGuid());
	}

	@When("$processOwner rozpocznie proces $processName")
	public void startBpmProcess(String processOwner, String processName) {
		LoginInfo owner = new LoginInfo(processOwner, null);
		process = amxBpmClient.startProcess(owner, processName);
		assertNotNull("Process: "+ processName+" has not been started", process.getProcessId());
	}
	
	@When("to system alokuje zadanie <taskName> na <taskOwner>")
	public void checkIfTaskAllocated(@Named("taskName") String taskName, @Named("taskOwner") String taskOwner) {
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LoginInfo user = amxBpmClient.getUserDetails(taskOwner);
		int count = amxBpmClient.getUserWorkItemsCount(process, user, taskName);
		assertEquals("Nie znaleziono przypisanego tasku \"" + taskName+"\" do " + taskOwner, 1, count );	
	}
	
	@Then("otworzy formularz wyszukania Klienta w CIS i wyszuka <PKP Cargo>")
	public void openTask() {
//		try {
//			amxBpmClient.openWorkItem(processOwner, workItemId, workItemVersion);
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
