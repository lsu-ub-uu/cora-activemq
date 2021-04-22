/*
 * Copyright 2019, 2021 Uppsala University Library
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

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageListener;
import se.uu.ub.cora.messaging.MessagingFactory;

public class ActiveMQMessagingFactoryTest {

	JmsMessageRoutingInfo routingInfo;
	ActiveMqMessagingFactory factory;

	@BeforeMethod
	public void beforeMethod() {

		String hostname = "dev-diva-drafts";
		String port = "61617";
		String routingKey = "diva.updates.#";
		String username = "admin";
		String password = "admin";

		routingInfo = new JmsMessageRoutingInfo(hostname, port, routingKey, username, password);

		factory = new ActiveMqMessagingFactory();
	}

	@Test
	public void testInit() throws Exception {
		assertTrue(factory instanceof MessagingFactory);
	}

	@Test
	public void testFactorTopicMessageListenerReturnsActiveMqTopicListener() throws Exception {
		ActiveMqTopicListener listener = (ActiveMqTopicListener) factory
				.factorTopicMessageListener(routingInfo);

		assertTrue(listener instanceof MessageListener);
		assertTrue(listener.getConnectionFactory() instanceof ActiveMQConnectionFactory);
		assertSame(listener.getRoutingInfo(), routingInfo);
	}

	@Test
	public void testFactorTopicMessageSenderReturnsActiveMqTopicSender() throws Exception {
		ActiveMqTopicSender sender = (ActiveMqTopicSender) factory
				.factorTopicMessageSender(routingInfo);
		assertTrue(sender.getConnectionFactory() instanceof ActiveMQConnectionFactory);
		assertSame(sender.getRoutingInfo(), routingInfo);
	}
}
