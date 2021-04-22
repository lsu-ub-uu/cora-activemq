package se.uu.ub.cora.activemq;

import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageSender;
import se.uu.ub.cora.messaging.MessagingInitializationException;

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
		return formatBrokerURL(routingInfo);
	}

	private String formatBrokerURL(JmsMessageRoutingInfo routingInfo) {
		return "tcp://" + routingInfo.hostname + ":" + routingInfo.port;
	}

	@Override
	public void sendMessage(Map<String, Object> headers, String message) {
		tryTosendMessage(headers, message);
	}

	private void tryTosendMessage(Map<String, Object> headers, String message) {
		try (Connection connection = connectionFactory.createConnection();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);) {

			prepareAndSendMessage(headers, message, session);

		} catch (JMSException e) {
			throw new MessagingInitializationException(e.getMessage(), e);
		}
	}

	private void prepareAndSendMessage(Map<String, Object> headers, String message, Session session)
			throws JMSException {
		TextMessage jmsMessage = buildMessage(headers, message, session);
		createTopicAndPublishMessage(session, jmsMessage);
	}

	private void createTopicAndPublishMessage(Session session, TextMessage jmsMessage)
			throws JMSException {
		Topic topic = session.createTopic(routingInfo.routingKey);

		publishMessage(session, jmsMessage, topic);
	}

	private void publishMessage(Session session, TextMessage jmsMessage, Topic topic)
			throws JMSException {
		MessageProducer messageProducer = session.createProducer(topic);
		messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		messageProducer.send(jmsMessage);
	}

	private TextMessage buildMessage(Map<String, Object> headers, String message, Session session)
			throws JMSException {
		TextMessage textMessage = session.createTextMessage(message);
		addHeadersToMessage(headers, textMessage);
		return textMessage;
	}

	private void addHeadersToMessage(Map<String, Object> headers, TextMessage textMessage)
			throws JMSException {
		for (Entry<String, Object> header : headers.entrySet()) {
			textMessage.setObjectProperty(header.getKey(), header.getValue());
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
