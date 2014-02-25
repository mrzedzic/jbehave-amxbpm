/**
 * 
 */
package com.tibco.emea.bpm.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;

import com.tibco.emea.ems.EmsClient;

/**
 * @author mr91988
 * 
 */
public class CalculatorSteps {
	EmsClient calculatorMessageRequest = null;
	String correlationId = String.valueOf(hashCode());

	@BeforeStories
	private void inicialise() {
		calculatorMessageRequest = new EmsClient();
		System.out.println("wykonano");
	}

	@Given("send request via ems message with <value1> and <value2>")
	public void add(@Named("value1") int value1, @Named("value2") int value2) {
		calculatorMessageRequest = new EmsClient();
		calculatorMessageRequest.sendMessage(value1, value2, correlationId);

	}

	@Then("the calculator return <result> in less than <maxWait> seconds")
	public void receiveResults(@Named("result") int result,	@Named("maxWait") int maxTime) {
		//calculatorMessageRequest = new EmsClient();
		Message responseMessage = calculatorMessageRequest.receiveMessage(correlationId, maxTime);
		
		if (responseMessage == null) {
			fail("response message was missing");
		}
		String responseBody = null;
		TextMessage textMessage = (TextMessage) responseMessage;
		try {
			responseBody = textMessage.getText();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(result, Integer.parseInt(responseBody));

	}
}
