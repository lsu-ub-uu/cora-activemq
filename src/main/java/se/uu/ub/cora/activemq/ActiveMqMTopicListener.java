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

public class ActiveMqMTopicListener implements MessageListener {

	public static ActiveMqMTopicListener usingActiveMQConnectionFactoryAndRoutingInfo(
			ActiveMQConnectionFactory connectionFactory, JmsMessageRoutingInfo routingInfo) {
		return new ActiveMqMTopicListener(connectionFactory, routingInfo);
	}

	private ActiveMQConnectionFactory connectionFactory;
	private JmsMessageRoutingInfo routingInfo;

	private ActiveMqMTopicListener(ActiveMQConnectionFactory connectionFactory,
			JmsMessageRoutingInfo routingInfo) {
		this.connectionFactory = connectionFactory;
		this.routingInfo = routingInfo;
	}

	@Override
	public void listen(MessageReceiver messageReceiver) {

		connectionFactory.setBrokerURL("tcp://" + routingInfo.hostname + ":" + routingInfo.port);
		connectionFactory.setUserName(routingInfo.username);
		connectionFactory.setPassword(routingInfo.password);
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(routingInfo.routingKey);
			MessageConsumer consumer = session.createConsumer(destination);
			TextMessage message = (TextMessage) consumer.receive();
			messageReceiver.receiveMessage(null, message.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
