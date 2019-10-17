/*
 * Copyright 2019 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.activemq;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageListener;
import se.uu.ub.cora.messaging.MessageReceiver;
import se.uu.ub.cora.messaging.MessageRoutingInfo;
import se.uu.ub.cora.messaging.MessagingInitializationException;

/**
 * ActiveMqMTopicListener is an implementation of {@link MessageListener} for ActiveMQ
 *
 */

public class ActiveMqMTopicListener implements MessageListener {

	public static ActiveMqMTopicListener usingActiveMQConnectionFactoryAndRoutingInfo(
			ActiveMQConnectionFactory connectionFactory, JmsMessageRoutingInfo routingInfo) {
		return new ActiveMqMTopicListener(connectionFactory, routingInfo);
	}

	private ActiveMQConnectionFactory connectionFactory;
	private JmsMessageRoutingInfo routingInfo;
	boolean listening = true;

	private ActiveMqMTopicListener(ActiveMQConnectionFactory connectionFactory,
			JmsMessageRoutingInfo routingInfo) {
		this.connectionFactory = connectionFactory;
		this.routingInfo = routingInfo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param messageReceiver
	 *            {@inheritDoc}
	 */

	@Override
	public void listen(MessageReceiver messageReceiver) {
		setUpConnectionFactory();
		tryToListenForMessages(messageReceiver);
	}

	private void tryToListenForMessages(MessageReceiver messageReceiver) {

		try (Connection connection = connectionFactory.createConnection();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);) {
			Destination destination = session.createTopic(routingInfo.routingKey);
			MessageConsumer consumer = session.createConsumer(destination);
			listenForMessages(messageReceiver, connection, consumer);
		} catch (JMSException e) {
			throw new MessagingInitializationException(e.getMessage());
		}
	}

	private void listenForMessages(MessageReceiver messageReceiver, Connection connection,
			MessageConsumer consumer) throws JMSException {
		connection.start();
		while (listening) {
			TextMessage message = (TextMessage) consumer.receive();
			Map<String, Object> headers = addPropertiesAsHeaders(message);
			messageReceiver.receiveMessage(headers, message.getText());
		}
	}

	private void setUpConnectionFactory() {
		connectionFactory.setBrokerURL("tcp://" + routingInfo.hostname + ":" + routingInfo.port);
		connectionFactory.setUserName(routingInfo.username);
		connectionFactory.setPassword(routingInfo.password);
	}

	private Map<String, Object> addPropertiesAsHeaders(TextMessage message) throws JMSException {
		@SuppressWarnings("unchecked")
		List<String> propertiesNames = Collections.list(message.getPropertyNames());
		return getHeaders(message, propertiesNames);
	}

	private Map<String, Object> getHeaders(TextMessage message, List<String> propertiesNames)
			throws JMSException {
		Map<String, Object> headers = new HashMap<>();
		for (String propertyName : propertiesNames) {
			headers.put(propertyName, message.getStringProperty(propertyName));
		}
		return headers;
	}

	ActiveMQConnectionFactory getConnectionFactory() {
		// Only needed for test
		return connectionFactory;
	}

	MessageRoutingInfo getRoutingInfo() {
		// Only needed for test
		return routingInfo;
	}

}
