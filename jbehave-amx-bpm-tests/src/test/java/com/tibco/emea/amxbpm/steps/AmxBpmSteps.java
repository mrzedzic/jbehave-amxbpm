package com.tibco.emea.amxbpm.steps;

import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.springframework.stereotype.Component;

import com.tibco.emea.amxbpm.AmxBpmClient;

/**
 * @author mr91988
 * 
 */
@Component
public class AmxBpmSteps {

	AmxBpmClient amxBpmClient = null;
	

	@BeforeStories
	public void inicialise() {
		amxBpmClient = new AmxBpmClient();
		System.out.println("wykonano");
	}

	@Given("As I am PRM logged as tibco-admin")
	public void login() {
		
	}

	@Then("I would like to login to openspace")
	public void login2() {
		
	}
}
