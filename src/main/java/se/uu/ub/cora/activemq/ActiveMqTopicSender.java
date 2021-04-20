package se.uu.ub.cora.activemq;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageSender;

public class ActiveMqTopicSender implements MessageSender {

	private ActiveMQConnectionFactory connectionFactory;
	private JmsMessageRoutingInfo routingInfo;

	public static ActiveMqTopicSender usingActiveMQConnectionFactoryAndRoutingInfo(
			ActiveMQConnectionFactory connectionFactory, JmsMessageRoutingInfo routingInfo) {

		return new ActiveMqTopicSender(connectionFactory, routingInfo);

	}

	private ActiveMqTopicSender(ActiveMQConnectionFactory connectionFactory,
			JmsMessageRoutingInfo routingInfo) {

		this.connectionFactory = connectionFactory;
		this.routingInfo = routingInfo;
		setupConnectionFactory();
	}

	private void setupConnectionFactory() {
		connectionFactory.setBrokerURL(buildBrokerURLFromRoutingInfo(routingInfo));
		connectionFactory.setUserName(routingInfo.username);
		connectionFactory.setPassword(routingInfo.password);
	}

	private String buildBrokerURLFromRoutingInfo(JmsMessageRoutingInfo routingInfo) {
		return "tcp://" + routingInfo.hostname + ":" + routingInfo.port;
	}

	@Override
	public void sendMessage(Map<String, Object> headers, String message) {
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			session.createTopic(routingInfo.routingKey);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// SPIKE
	public void spikeSendMessage(ActiveMQConnectionFactory connectionFactory,
			JmsMessageRoutingInfo routingInfo) {

		connectionFactory.setBrokerURL(buildBrokerURLFromRoutingInfo(routingInfo));
		connectionFactory.setUserName(routingInfo.username);
		connectionFactory.setPassword(routingInfo.password);
		try {
			setupConnectionFactory();
			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Destination destination = session.createQueue(routingInfo.routingKey);
			Destination destination = session.createTopic(routingInfo.routingKey);
			System.out.println("Session:" + session.toString());
			System.out.println("destination:" + destination.toString());

			// // Create the destination (Topic or Queue)
			// Destination destination = session.createQueue("TEST.FOO");
			//
			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			System.out.println("producer:" + producer.toString());
			//
			// // Create a messages
			String text = "Hello world! From: " + Thread.currentThread().getName() + " : "
					+ this.hashCode();
			TextMessage message = session.createTextMessage(text);
			//
			// // Tell the producer to send the message
			System.out.println("Sent message: " + message.hashCode() + " : "
					+ Thread.currentThread().getName());
			producer.send(message);

			// Clean up
			session.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	// Only for test
	Object getConnectionFactory() {
		return connectionFactory;
	}

	// Only for test
	Object getRoutingInfo() {
		return routingInfo;
	}

}
