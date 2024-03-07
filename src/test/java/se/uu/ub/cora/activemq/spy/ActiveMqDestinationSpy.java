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
package se.uu.ub.cora.activemq.spy;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Topic;

import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class ActiveMqDestinationSpy implements Destination, Topic {

	private String topicName;

	MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public String getTopicName() throws JMSException {
		MCR.addCall();

		MCR.addReturned(topicName);
		return topicName;
	}

	void setTopicName(String topicName) {
		MCR.addCall("topicName", topicName);

		this.topicName = topicName;
	}

}
