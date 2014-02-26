/**
 * 
 */
package com.tibco.emea.ems;

import java.util.Calendar;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.tibco.emea.bo.Calculator;

/**
 * @author Marcin Rzedzicki
 * 
 */
public class EmsClient {
	private String serverUrl = "tcp://localhost:7222";
	private String userName = "admin";
	private String password = "";
	private Context jndiContext = null;
	private ConnectionFactory cFactory = null;
	private Connection conn = null;
	private Session session = null;
	private MessageProducer myProd = null;
	private Destination myDest, myDestReply = null;
	private MessageConsumer myCons = null;

	private String queueName = "queue.sample";

	public EmsClient() {

		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.tibco.tibjms.naming.TibjmsInitialContextFactory");
			env.put(Context.PROVIDER_URL, serverUrl);
			// env.put(Context.SECURITY_PRINCIPAL, "mrzedzic");
			// env.put(Context.SECURITY_CREDENTIALS, "alamakota");
			jndiContext = new InitialContext(env);
			cFactory = (ConnectionFactory) jndiContext
					.lookup("GenericConnectionFactory");

			conn = cFactory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (NamingException e) {
			System.out.println("Failed JNDI InitialContext with " + serverUrl
					+ ". Error = " + e.toString());
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String sendQueueMessage(Calculator calc) {
		return sendMessage(calc.getX(), calc.getY(),
				String.valueOf(calc.getResult()));
	}

	public String sendMessage(int element1, int element2, String body) {
		String correlationId = String.valueOf(Calendar.getInstance().getTimeInMillis())	+ "Test";
		try {
			myDest = (Destination) jndiContext.lookup(queueName);
			myProd = session.createProducer(myDest);
			TextMessage msg1 = session.createTextMessage();
			msg1.setText(body);
			msg1.setIntProperty("X", element1);
			msg1.setIntProperty("Y", element2);
			msg1.setJMSCorrelationID(correlationId);

			// Send message
//			System.out.println("Sending " + msg1);
			myProd.send(msg1);
			myProd.close();
		} catch (NamingException e) {
			System.out.println("Could not create or send message: " + e);
		} catch (JMSException e) {
			System.out.println("Exception in send message: " + e);
		}
		return correlationId;
	}

	public Message receiveMessage(String correlationId, int waitTime) {
		Message message = null;
		correlationId = "JMSCorrelationID='"+correlationId+"'";
		try {
			myDestReply = (Destination) jndiContext.lookup(queueName);
			myCons = session.createConsumer(myDestReply, correlationId);

			/* read queue messages */
			conn.start();
			message = myCons.receive(waitTime * 1000);
			message.acknowledge();
			conn.stop();
		} catch (JMSException e) {
			System.out.println("Exception in receive message: " + e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
