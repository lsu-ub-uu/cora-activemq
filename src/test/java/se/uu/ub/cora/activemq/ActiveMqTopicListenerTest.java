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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Topic;
import se.uu.ub.cora.activemq.spy.ActiveMQConnectionFactorySpy;
import se.uu.ub.cora.activemq.spy.ActiveMqConnectionSpy;
import se.uu.ub.cora.activemq.spy.ActiveMqConsumerSpy;
import se.uu.ub.cora.activemq.spy.ActiveMqSessionSpy;
import se.uu.ub.cora.activemq.spy.MessageReceiverSpy;
import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageListener;
import se.uu.ub.cora.messaging.MessagingInitializationException;

public class ActiveMqTopicListenerTest {

	private ActiveMQConnectionFactorySpy connectionFactory;
	private JmsMessageRoutingInfo routingInfo;
	private ActiveMqTopicListener listener;
	private MessageReceiverSpy messageReceiver;

	@BeforeMethod
	public void beforeMethod() {

		String hostname = "dev-diva-drafts";
		int port = 61617;
		String routingKey = "diva.updates.#";
		String username = "admin";
		String password = "admin";

		routingInfo = new JmsMessageRoutingInfo(hostname, port, routingKey, username, password);
		connectionFactory = new ActiveMQConnectionFactorySpy();
		listener = ActiveMqTopicListener
				.usingActiveMQConnectionFactoryAndRoutingInfo(connectionFactory, routingInfo);
		messageReceiver = new MessageReceiverSpy();
	}

	@Test
	public void testInit() throws Exception {
		assertTrue(listener instanceof MessageListener);
		assertSame(listener.getConnectionFactory(), connectionFactory);
		assertSame(listener.getRoutingInfo(), routingInfo);
	}

	@Test
	public void testSetupConnectionFactoryOnListen() throws Exception {
		tryToListen();

		assertEquals(connectionFactory.brokerURL,
				"tcp://" + routingInfo.hostname + ":" + routingInfo.port);
		assertEquals(connectionFactory.userName, routingInfo.username);
		assertEquals(connectionFactory.password, routingInfo.password);

	}

	@Test
	public void testListnerCreatesConnectionAndStarted() throws Exception {
		assertEquals(connectionFactory.createdConnections.size(), 0);
		tryToListen();
		assertEquals(connectionFactory.createdConnections.size(), 1);
		assertTrue(connectionFactory.createdConnections.get(0) instanceof Connection);
		assertTrue(connectionFactory.createdConnections.get(0).hasBeenStarted);
	}

	@Test
	public void testListenerCreatesSession() throws Exception {
		tryToListen();
		ActiveMqConnectionSpy connection = connectionFactory.createdConnections.get(0);
		assertEquals(connection.createdSession.size(), 1);

		ActiveMqSessionSpy session = connection.createdSession.get(0);

		assertEquals(session.getTransacted(), false);
		assertEquals(session.getAcknowledgeMode(), 1);
		assertTrue(session.destination instanceof Topic);
		assertEquals(session.destination.getTopicName(), routingInfo.routingKey);
		assertEquals(session.createdConsumer.size(), 1);
		assertTrue(session.createdConsumer.get(0) instanceof Topic);
		assertTrue(session.consumer instanceof MessageConsumer);
	}

	@Test
	public void testListenOneMessage() throws Exception {
		tryToListen();
		ActiveMqConnectionSpy connection = connectionFactory.createdConnections.get(0);
		ActiveMqSessionSpy session = connection.createdSession.get(0);
		ActiveMqConsumerSpy consumer = session.consumer;

		assertTrue(consumer.receiveIsCalled);
		// assertTrue(consumer.messageReceived instanceof TextMessage);
		assertNotNull(messageReceiver.message);
		assertEquals(messageReceiver.message, "Text from TextMessageSpy");
	}

	@Test
	public void testListenOneMessageSendsHeaders() throws Exception {
		tryToListen();
		assertEquals(messageReceiver.headers.size(), 2);
		assertEquals(messageReceiver.headers.get("pid"), "diva2:666498");
		assertEquals(messageReceiver.headers.get("methodName"), "modifyDatastreamByValue");
	}

	@Test(expectedExceptions = MessagingInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "Error from ActiveMqTopicListenerSpy on newConnection")
	public void testThrowsExceptionOnListenMessage() throws Exception {
		connectionFactory.throwError = true;
		listener.listen(messageReceiver);
	}

	@Test
	public void testThrowsExceptionOnListenMessageSendsAlongInitialException() throws Exception {
		connectionFactory.throwError = true;
		try {
			listener.listen(messageReceiver);
		} catch (Exception e) {
			assertTrue(e.getCause() instanceof JMSException);
		}
	}

	@Test
	public void testListenTenMessages() throws Exception {
		tryToListen();
		assertEquals(messageReceiver.messagesReceived, 10);

	}

	@Test
	public void testStopListening() throws Exception {
		tryToListen();
		listener.listening = false;
		listener.listen(messageReceiver);
	}

	private void tryToListen() {
		try {
			listener.listen(messageReceiver);
		} catch (Exception e) {
			// Do nothing, in order to be able to stop infinite loop.
		}
	}

}
