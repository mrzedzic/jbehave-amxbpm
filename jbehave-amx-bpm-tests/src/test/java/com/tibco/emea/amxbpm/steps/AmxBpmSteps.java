package com.tibco.emea.amxbpm.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.stereotype.Component;

import com.tibco.emea.amxbpm.AmxBpmClient;
import com.tibco.emea.amxbpm.AmxBpmProcess;
import com.tibco.emea.amxbpm.BusinessProcessNames;
import com.tibco.emea.amxbpm.LoginInfo;
import com.tibco.n2.wp.api.WorkResponseDocument.WorkResponse;
import com.tibco.n2.wp.api.base.BaseWorkRequest;

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
	
//	@Given("rozpoczety proces BzWbkTTY przez: $table")
//	public void rozpoczetyProces(ExamplesTable table) {
//		
////		for (int i = 0; i < table.getRowCount(); i++) {
//			String user = table.getRowAsParameters(0, true).valueAs("processOwner", String.class);
//		    ldapUser = amxBpmClient.getUserDetails(user);
//		    assertNotNull("User: "+ user+" not found in LDAP", ldapUser.getGuid());
//		    
//		    process = amxBpmClient.startProcess(ldapUser, BusinessProcessNames.BzWbkTTY.toString());
//		    assertNotNull("Process: "+ BusinessProcessNames.BzWbkTTY.toString()+" has not been started", process.getProcessId());
//		    processList.add(process);
////		}		
//	}

	@Given("rozpoczety proces BzWbkTTY przez <processOwner>")
	public void rozpoczetyProces(@Named("processOwner") String processOwner) {
	    ldapUser = amxBpmClient.getUserDetails(processOwner);
	    assertNotNull("User: "+ processOwner+" not found in LDAP", ldapUser.getGuid());
	    
	    process = amxBpmClient.startProcess(ldapUser, BusinessProcessNames.BzWbkTTY.toString());
	    assertNotNull("Process: "+ BusinessProcessNames.BzWbkTTY.toString()+" has not been started", process.getProcessId());
	}

	
	@When("$processOwner rozpocznie proces $processName")
	public void startBpmProcess(String processOwner, String processName) {
		LoginInfo owner = new LoginInfo(processOwner, null);
		process = amxBpmClient.startProcess(owner, processName);
		assertNotNull("Process: "+ processName+" has not been started", process.getProcessId());
	}
	
	@When("system zaoferuje zadanie <taskName1> na <taskOwner1>")
	public void checkIfTaskAllocated(@Named("taskName1") String taskName, @Named("taskOwner1") String taskOwner) {
		try {
			System.out.println("--------czekam 5 sec dla " + process.getProcessId());
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LoginInfo user = amxBpmClient.getUserDetails(taskOwner);
				
		int count = amxBpmClient.getUserWorkItemsCount(process, user, taskName);
		assertEquals("Nie znaleziono przypisanego tasku \"" + taskName+"\" do " + taskOwner, 1, count );	
		
		process.setCurrentTaskName(taskName);
	}	
	
	@Then("otworzy <taskOwner1> formularz i uzupelni go danymi <formName1> oraz zatwierdzi")
	public void openUpdateAndSaveTask(@Named("taskOwner1") String taskOwner, @Named("formName1") String formName) {
		try {
			LoginInfo user = amxBpmClient.getUserDetails(taskOwner);
			BaseWorkRequest work = amxBpmClient.getWorkItemFromProcess(process, user);
			WorkResponse openWorkItem= amxBpmClient.openWorkItem(user, work.getWorkItem().getId(), work.getWorkItem().getVersion());
			amxBpmClient.saveOpenWorkItem(user, formName, work.getWorkItem().getId(), work.getWorkItem().getVersion(), openWorkItem.getWorkTypeDetail().getUid(), openWorkItem.getWorkTypeDetail().getVersion(), process.getProcessId());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@When("system zaoferuje zadanie <taskName2> na <taskOwner2>")
	public void checkIfTaskAllocated2(@Named("taskName2") String taskName, @Named("taskOwner2") String taskOwner) {
		checkIfTaskAllocated(taskName, taskOwner);
	}
	
	@When("system zaoferuje zadanie <taskName3> na <taskOwner3>")
	public void checkIfTaskAllocated3(@Named("taskName3") String taskName, @Named("taskOwner3") String taskOwner) {
		checkIfTaskAllocated(taskName, taskOwner);
	}
	
	@Then("otworzy <taskOwner2> formularz i uzupelni go danymi <formName2> oraz zatwierdzi")
	public void openUpdateAndSaveTask2(@Named("taskOwner2") String taskOwner, @Named("formName2") String formName) {
		openUpdateAndSaveTask(taskOwner, formName);
	}
	
	@Then("otworzy <taskOwner3> formularz i uzupelni go danymi <formName3> oraz zatwierdzi")
	public void openUpdateAndSaveTask3(@Named("taskOwner3") String taskOwner, @Named("formName3") String formName) {
		openUpdateAndSaveTask(taskOwner, formName);
	}
}
