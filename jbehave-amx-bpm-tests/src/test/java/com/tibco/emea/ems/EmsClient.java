/**
 * 
 */
package com.tibco.emea.ems;

import java.util.Calendar;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.tibco.emea.bo.Calculator;

/**
 * @author Marcin Rzedzicki
 * 
 */

public class EmsClient {
	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;

	@Resource(name = "bwUnitTestResultsQueue")
	private Destination resultsQueue;

	public EmsClient() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-ems-tests.xml");
		jmsTemplate = context.getBean(JmsTemplate.class);
		resultsQueue = context.getBean("bwUnitTestResultsQueue", Destination.class);

	}

	public TextMessage receiveMessage(String correlationId, int waitTime) {

		correlationId = "JMSCorrelationID='" + correlationId + "'";
		TextMessage results = (TextMessage) jmsTemplate.receiveSelected(resultsQueue, correlationId);
		return results;
	}

	public String sendMessage(final int element1, final int element2,
			final String body) {
		final String correlationId = String.valueOf(Calendar.getInstance()
				.getTimeInMillis()) + "Test";

		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(final javax.jms.Session session)
					throws JMSException {
				TextMessage message = session.createTextMessage(body);
				message.setIntProperty("X", element1);
				message.setIntProperty("Y", element2);
				message.setJMSCorrelationID(correlationId);
				return message;
			}
		});
		return correlationId;
	}

	public String sendQueueMessage(Calculator calc) {
		return sendMessage(calc.getX(), calc.getY(),
				String.valueOf(calc.getResult()));
	}
}
