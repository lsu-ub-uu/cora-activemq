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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import se.uu.ub.cora.activemq.mcr.MethodCallRecorder;

public class ActiveMqSessionSpy implements Session {

	private boolean transacted = true;
	private int acknowledgeMode = 0;
	public ActiveMqDestinationSpy destination = null;
	public List<Object> createdConsumer = new ArrayList<>();
	public ActiveMqConsumerSpy consumer;

	public MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapMessage createMapMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message createMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextMessage createTextMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getTransacted() throws JMSException {
		// TODO Auto-generated method stub
		return transacted;
	}

	@Override
	public int getAcknowledgeMode() throws JMSException {
		return acknowledgeMode;
	}

	@Override
	public void commit() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void recover() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		createdConsumer.add(destination);
		consumer = new ActiveMqConsumerSpy();
		return consumer;
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector,
			boolean noLocal) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName,
			String messageSelector) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Queue createQueue(String queueName) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic createTopic(String topicName) throws JMSException {
		MCR.addCall("topicName", topicName);

		destination = new ActiveMqDestinationSpy();
		destination.setTopicName(topicName);

		MCR.addReturned(destination);
		return destination;
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector,
			boolean noLocal) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector,
			boolean noLocal) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name,
			String messageSelector) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String name) throws JMSException {
		// TODO Auto-generated method stub

	}

	void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	void setAcknowledgeMode(int acknowledgeMode) {
		this.acknowledgeMode = acknowledgeMode;
	}

}
