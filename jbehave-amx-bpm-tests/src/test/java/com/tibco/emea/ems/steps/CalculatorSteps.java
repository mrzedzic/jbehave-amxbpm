/**
 * 
 */
package com.tibco.emea.ems.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.springframework.stereotype.Component;

import com.tibco.emea.bo.Calculator;
import com.tibco.emea.ems.EmsClient;

/**
 * @author mr91988
 * 
 */
@Component
public class CalculatorSteps {

	EmsClient emsClient = null;
	Calculator calc = new Calculator();
	String correlationId = "-1";

	@BeforeStories
	public void inicialise() {
		emsClient = new EmsClient();
		System.out.println("wykonano");
	}

	@Given("I want to add them")
	public void add() {
		calc.setResult(calc.add(calc.getX(), calc.getY()));
		correlationId = emsClient.sendQueueMessage(calc);
	}

	@Given("send request via ems message with <value1> and <value2>")
	public void add(@Named("value1") int value1, @Named("value2") int value2) {
		calc.setX(value1);
		calc.setY(value2);
	}

	@Then("the calculator return <result> in less than <maxWait> seconds")
	public void receiveResults(@Named("result") int result,	@Named("maxWait") int maxTime) {

		TextMessage responseMessage = emsClient.receiveMessage(correlationId, maxTime);

		if (responseMessage == null) {
			fail("response message was missing");
		}
		String responseBody = null;
		try {
			responseBody = responseMessage.getText();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(result, Integer.parseInt(responseBody));

	}

	@Given("I want to substract them")
	public void substract() {
		calc.setResult(calc.subtract(calc.getX(), calc.getY()));
		correlationId = emsClient.sendQueueMessage(calc);
	}
}
