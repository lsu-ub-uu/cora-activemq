package se.uu.ub.cora.activemq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import javax.jms.Connection;
import javax.jms.Session;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.activemq.spy.ActiveMQConnectionFactorySpy;
import se.uu.ub.cora.activemq.spy.ActiveMqConnectionSpy;
import se.uu.ub.cora.activemq.spy.ActiveMqSessionSpy;
import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageSender;

public class ActiveMqTopicSenderTest {

	ActiveMqTopicSender sender;

	private ActiveMQConnectionFactorySpy connectionFactory;
	private JmsMessageRoutingInfo routingInfo;
	// private MethodCallRecorder connectionFactoryMCR;

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
	public void testListnerCreatesConnectionAndStarted() throws Exception {

		assertEquals(connectionFactory.createdConnections.size(), 0);
		sender.sendMessage(null, null);
		assertEquals(connectionFactory.createdConnections.size(), 1);
		assertTrue(connectionFactory.createdConnections.get(0) instanceof Connection);
		assertTrue(connectionFactory.createdConnections.get(0).hasBeenStarted);
	}

	@Test
	public void testListenerCreatesSession() throws Exception {
		sender.sendMessage(null, null);

		ActiveMqConnectionSpy connectionSpy = connectionFactory.createdConnections.get(0);

		connectionSpy.MCR.assertMethodWasCalled("createSession");
		connectionSpy.MCR.assertParameters("createSession", 0, false, Session.AUTO_ACKNOWLEDGE);

		ActiveMqSessionSpy sessionSpy = (ActiveMqSessionSpy) connectionSpy.MCR
				.getReturnValue("createSession", 0);

		sessionSpy.MCR.assertParameters("createTopic", 0, routingInfo.routingKey);
		Object returnValue = sessionSpy.MCR.getReturnValue("createTopic", 0);

		// ActiveMqSessionSpy session = connection.createdSession.get(0);
		//
		// assertTrue(session instanceof Session);

		// assertEquals(session.getTransacted(), false);
		// assertEquals(session.getAcknowledgeMode(), 1);
		// assertTrue(session.destination instanceof Topic);
		// assertEquals(session.destination.getTopicName(), routingInfo.routingKey);
		// assertEquals(session.createdConsumer.size(), 1);
		// assertTrue(session.createdConsumer.get(0) instanceof Topic);
		// assertTrue(session.consumer instanceof MessageConsumer);
	}

	// Spike
	// @Test
	// public void testCallSpike() {
	// ActiveMQConnectionFactory connectionFactory1 = new ActiveMQConnectionFactory();
	// sender.spikeSendMessage(connectionFactory1, routingInfo);
	// }

}
