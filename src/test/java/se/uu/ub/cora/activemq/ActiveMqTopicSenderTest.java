package se.uu.ub.cora.activemq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import se.uu.ub.cora.activemq.spy.ActiveMQConnectionFactorySpy;
import se.uu.ub.cora.activemq.spy.ActiveMqConnectionSpy;
import se.uu.ub.cora.activemq.spy.ActiveMqDestinationSpy;
import se.uu.ub.cora.activemq.spy.ActiveMqSessionSpy;
import se.uu.ub.cora.activemq.spy.MessageProducerSpy;
import se.uu.ub.cora.activemq.spy.TextMessageSpy;
import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageSender;
import se.uu.ub.cora.messaging.MessagingInitializationException;

public class ActiveMqTopicSenderTest {

	ActiveMqTopicSender sender;

	private ActiveMQConnectionFactorySpy connectionFactory;
	private JmsMessageRoutingInfo routingInfo;
	Map<String, Object> emptyHeaders = Collections.emptyMap();

	@BeforeMethod
	public void beforeMethod() {

		String hostname = "diva-docker-fedora";
		String port = "61616";
		String routingKey = "fedora.apim.update";
		String username = "fedoraAdmin";
		String password = "changeit";

		routingInfo = new JmsMessageRoutingInfo(hostname, port, routingKey, username, password);
		connectionFactory = new ActiveMQConnectionFactorySpy();
		sender = ActiveMqTopicSender.usingActiveMQConnectionFactoryAndRoutingInfo(connectionFactory,
				routingInfo);
	}

	@Test
	public void testInit() {
		assertTrue(sender instanceof MessageSender);
		assertSame(sender.getConnectionFactory(), connectionFactory);
		assertTrue(sender.getConnectionFactory() instanceof ActiveMQConnectionFactorySpy);
		assertSame(sender.getRoutingInfo(), routingInfo);
	}

	@Test
	public void testSetConnectionFactoryChannel() throws Exception {
		assertEquals(connectionFactory.brokerURL,
				"tcp://" + routingInfo.hostname + ":" + routingInfo.port);
		assertEquals(connectionFactory.userName, routingInfo.username);
		assertEquals(connectionFactory.password, routingInfo.password);
	}

	@Test
	public void testSenderCreatesConnectionAndStarted() throws Exception {
		assertEquals(connectionFactory.createdConnections.size(), 0);

		sender.sendMessage(emptyHeaders, null);

		assertEquals(connectionFactory.createdConnections.size(), 1);
		assertTrue(connectionFactory.createdConnections.get(0) instanceof Connection);
	}

	@Test
	public void testListenerCreateAndCloseASession() throws Exception {

		sender.sendMessage(emptyHeaders, null);

		ActiveMqConnectionSpy connection = (ActiveMqConnectionSpy) connectionFactory.MCR
				.getReturnValue("createConnection", 0);

		connection.MCR.assertMethodWasCalled("createSession");
		connection.MCR.assertParameters("createSession", 0, false, Session.AUTO_ACKNOWLEDGE);

		ActiveMqSessionSpy session = (ActiveMqSessionSpy) connection.MCR
				.getReturnValue("createSession", 0);

		session.MCR.assertParameters("createTopic", 0, routingInfo.routingKey);
		ActiveMqDestinationSpy destination = (ActiveMqDestinationSpy) session.MCR
				.getReturnValue("createTopic", 0);
		assertTrue(destination instanceof Topic);

		session.MCR.assertParameters("createProducer", 0, destination);
		MessageProducerSpy messageProducer = (MessageProducerSpy) session.MCR
				.getReturnValue("createProducer", 0);
		session.MCR.assertReturn("createProducer", 0, messageProducer);

		messageProducer.MCR.assertParameters("setDeliveryMode", 0, DeliveryMode.NON_PERSISTENT);

		session.MCR.assertMethodWasCalled("close");
		connection.MCR.assertMethodWasCalled("close");

	}

	@Test(expectedExceptions = MessagingInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "Error from ActiveMqTopicListenerSpy on newConnection")
	public void testThrowsExceptionOnListenMessage() throws Exception {
		connectionFactory.throwError = true;
		sender.sendMessage(emptyHeaders, null);
	}

	@Test
	public void testMessageWithText() throws Exception {
		String message = "It is a message";

		sender.sendMessage(emptyHeaders, message);

		ActiveMqSessionSpy session = getSessionFromConnectionFactorySpy();
		session.MCR.assertParameters("createTextMessage", 0, message);
	}

	private ActiveMqSessionSpy getSessionFromConnectionFactorySpy() {
		ActiveMqConnectionSpy connection = (ActiveMqConnectionSpy) connectionFactory.MCR
				.getReturnValue("createConnection", 0);
		ActiveMqSessionSpy session = (ActiveMqSessionSpy) connection.MCR
				.getReturnValue("createSession", 0);
		return session;
	}

	@Test
	public void testMessageWithOneHeader() throws Exception {
		Map<String, Object> header = new HashMap<>();
		header.put("methodName", "addDataStream");

		sender.sendMessage(header, null);

		ActiveMqSessionSpy session = getSessionFromConnectionFactorySpy();
		TextMessageSpy textMessage = (TextMessageSpy) session.MCR
				.getReturnValue("createTextMessage", 0);
		textMessage.MCR.assertParameters("setObjectProperty", 0, "methodName", "addDataStream");

	}

	@Test
	public void testMessageWithOneHeaderFilledWithAnObject() throws Exception {
		Map<String, Object> header = new HashMap<>();
		Object headerObject = new Object();
		header.put("methodName", headerObject);
		sender.sendMessage(header, null);

		ActiveMqSessionSpy session = getSessionFromConnectionFactorySpy();
		TextMessageSpy textMessage = (TextMessageSpy) session.MCR
				.getReturnValue("createTextMessage", 0);
		textMessage.MCR.assertParameters("setObjectProperty", 0, "methodName", headerObject);

	}

	@Test
	public void testMessageSeveralHeaders() throws Exception {
		Map<String, Object> headers = new HashMap<>();
		headers.put("pid", "authority-person:146");
		headers.put("methodName", "addDataStream");
		headers.put("from", "testNG");
		sender.sendMessage(headers, null);

		ActiveMqSessionSpy session = getSessionFromConnectionFactorySpy();
		TextMessageSpy textMessage = (TextMessageSpy) session.MCR
				.getReturnValue("createTextMessage", 0);
		textMessage.MCR.assertNumberOfCallsToMethod("setObjectProperty", 3);
		textMessage.MCR.assertParameters("setObjectProperty", 0, "methodName", "addDataStream");
		textMessage.MCR.assertParameters("setObjectProperty", 1, "pid", "authority-person:146");
		textMessage.MCR.assertParameters("setObjectProperty", 2, "from", "testNG");
	}

	@Test
	public void testPublishMessage() throws Exception {

		sender.sendMessage(emptyHeaders, null);

		ActiveMqSessionSpy session = getSessionFromConnectionFactorySpy();
		MessageProducerSpy messageProducer = (MessageProducerSpy) session.MCR
				.getReturnValue("createProducer", 0);
		TextMessageSpy textMessage = (TextMessageSpy) session.MCR
				.getReturnValue("createTextMessage", 0);

		messageProducer.MCR.assertParameters("send", 0, textMessage);
	}
}
