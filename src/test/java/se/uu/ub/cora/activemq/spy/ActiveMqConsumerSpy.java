package se.uu.ub.cora.activemq.spy;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class ActiveMqConsumerSpy implements MessageConsumer {

	public boolean receiveIsCalled = false;
	public TextMessageSpy messageReceived;

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
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("pid", "diva2:666498");
		headers.put("methodName", "modifyDatastreamByValue");
		receiveIsCalled = true;
		messageReceived = new TextMessageSpy();
		messageReceived.properties = headers;
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
