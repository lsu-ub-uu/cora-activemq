package se.uu.ub.cora.activemq.spy;

import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import se.uu.ub.cora.activemq.mcr.MethodCallRecorder;

public class MessageProducerSpy implements MessageProducer {

	public MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public void setDisableMessageID(boolean value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getDisableMessageID() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDisableMessageTimestamp(boolean value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getDisableMessageTimestamp() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDeliveryMode(int deliveryMode) throws JMSException {
		MCR.addCall("deliveryMode", deliveryMode);
	}

	@Override
	public int getDeliveryMode() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPriority(int defaultPriority) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPriority() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTimeToLive(long timeToLive) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getTimeToLive() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDeliveryDelay(long deliveryDelay) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getDeliveryDelay() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Destination getDestination() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Message message) throws JMSException {
		MCR.addCall("message", message);
	}

	@Override
	public void send(Message message, int deliveryMode, int priority, long timeToLive)
			throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Destination destination, Message message) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Destination destination, Message message, int deliveryMode, int priority,
			long timeToLive) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Message message, CompletionListener completionListener) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Message message, int deliveryMode, int priority, long timeToLive,
			CompletionListener completionListener) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Destination destination, Message message,
			CompletionListener completionListener) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Destination destination, Message message, int deliveryMode, int priority,
			long timeToLive, CompletionListener completionListener) throws JMSException {
		// TODO Auto-generated method stub

	}

}
