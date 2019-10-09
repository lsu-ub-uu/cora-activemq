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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageListener;

public class ActiveMqTopicListenerTest {

	private ActiveMQConnectionFactorySpy connectionFactory;
	private JmsMessageRoutingInfo routingInfo;
	private ActiveMqMTopicListener listener;

	@BeforeMethod
	public void beforeMethod() {

		String hostname = "dev-diva-drafts";
		String port = "61617";
		String routingKey = "alvin.updates.#";
		String username = "admin";
		String password = "admin";

		routingInfo = new JmsMessageRoutingInfo(hostname, port, routingKey, username, password);
		connectionFactory = new ActiveMQConnectionFactorySpy();
		listener = ActiveMqMTopicListener
				.usingActiveMQConnectionFactoryAndRoutingInfo(connectionFactory, routingInfo);
	}

	@Test
	public void testInit() throws Exception {
		assertTrue(listener instanceof MessageListener);
		assertSame(listener.getConnectionFactory(), connectionFactory);
		assertSame(listener.getRoutingInfo(), routingInfo);
	}

	// @Test
	// public void testListnerCreatesConnection() throws Exception {
	// assertEquals(connectionFactory.createdConnections.size(), 0);
	// listener.listen(null);
	// }

	@Test
	public void testSetupConnectionFactoryOnListen() throws Exception {
		listener.listen(null);

		assertEquals(connectionFactory.brokerURL,
				"tcp://" + routingInfo.hostname + ":" + routingInfo.port);
		assertEquals(connectionFactory.userName, routingInfo.username);
		assertEquals(connectionFactory.password, routingInfo.password);

	}
}
