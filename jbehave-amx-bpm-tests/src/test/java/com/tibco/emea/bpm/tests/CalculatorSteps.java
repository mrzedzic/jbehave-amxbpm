/**
 * 
 */
package com.tibco.emea.bpm.tests;

import static org.junit.Assert.assertEquals;

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
	String correlationId = "test1";

	@BeforeStories
	private void inicialise() {
		calculatorMessageRequest = new EmsClient();
		System.out.println("wykonano");
	}

	@Given("send request via ems message with <value1> and <value2>")
	public void add(@Named("value1") int value1, @Named("value2") int value2) {
		// account = new Account(balance);
		calculatorMessageRequest = new EmsClient();
		calculatorMessageRequest.sendMessage(value1, value2, correlationId);

	}
	
	@Then("wait for response $wait")
	public void waitFor(@Named("wait") int wait) {
		try {
			Thread.sleep(wait*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Then("the calculator return <result>")
	public void receiveResults(@Named("result") int result) {
		Message responseMessage = calculatorMessageRequest
				.receiveMessage(correlationId);
		String responseBody = null;
		TextMessage textMessage = (TextMessage) responseMessage;
		try {
			responseBody = textMessage.getText();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("dupa");
		}

		assertEquals(Integer.parseInt(responseBody), result);

	}
}
