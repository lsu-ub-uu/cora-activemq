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

import org.apache.activemq.ActiveMQConnectionFactory;

import se.uu.ub.cora.messaging.JmsMessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageListener;
import se.uu.ub.cora.messaging.MessageRoutingInfo;
import se.uu.ub.cora.messaging.MessageSender;
import se.uu.ub.cora.messaging.MessagingFactory;

/**
 * Implementation of {@link MessagingFactory} for ActiveMq.
 */

public class ActiveMqMessagingFactory implements MessagingFactory {

	@Override
	public MessageSender factorTopicMessageSender(MessageRoutingInfo messagingRoutingInfo) {
		return ActiveMqTopicSender.usingActiveMQConnectionFactoryAndRoutingInfo(
				new ActiveMQConnectionFactory(), (JmsMessageRoutingInfo) messagingRoutingInfo);
	}

	@Override
	public MessageListener factorTopicMessageListener(MessageRoutingInfo messagingRoutingInfo) {
		return ActiveMqTopicListener.usingActiveMQConnectionFactoryAndRoutingInfo(
				new ActiveMQConnectionFactory(), (JmsMessageRoutingInfo) messagingRoutingInfo);
	}

}
