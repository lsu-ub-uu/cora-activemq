package se.uu.ub.cora.activemq.spy;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ActiveMqConsumerSpy implements MessageConsumer {

	public boolean receiveIsCalled = false;
	public TextMessage messageReceived;

	@Override
	public String getMessageSelector() throws JMSException {
		// TODO Auto-generated method stub
		return null;
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
	public Message receive() throws JMSException {
		receiveIsCalled = true;
		messageReceived = new TextMessageSpy();
		return messageReceived;
	}

	@Override
	public Message receive(long timeout) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message receiveNoWait() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

}
