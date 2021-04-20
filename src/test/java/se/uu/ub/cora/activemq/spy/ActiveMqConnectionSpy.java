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

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import se.uu.ub.cora.activemq.mcr.MethodCallRecorder;

public class ActiveMqConnectionSpy implements Connection {

	public boolean hasBeenStarted = false;
	public List<ActiveMqSessionSpy> createdSession = new ArrayList<>();

	public MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		MCR.addCall("transacted", transacted, "acknowledgeMode", acknowledgeMode);
		ActiveMqSessionSpy activeMqSessionSpy = new ActiveMqSessionSpy();

		activeMqSessionSpy.setTransacted(false);
		activeMqSessionSpy.setAcknowledgeMode(1);
		createdSession.add(activeMqSessionSpy);

		MCR.addReturned(activeMqSessionSpy);
		return activeMqSessionSpy;
	}

	@Override
	public Session createSession(int sessionMode) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session createSession() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientID() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClientID(String clientID) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExceptionListener(ExceptionListener listener) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws JMSException {
		hasBeenStarted = true;
	}

	@Override
	public void stop() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic,
			String subscriptionName, String messageSelector, ServerSessionPool sessionPool,
			int maxMessages) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
