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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class TextMessageSpy implements TextMessage {

	public Map<String, Object> properties;
	public MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public String getJMSMessageID() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJMSMessageID(String id) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getJMSTimestamp() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setJMSTimestamp(long timestamp) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSCorrelationID(String correlationID) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getJMSCorrelationID() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Destination getJMSReplyTo() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJMSReplyTo(Destination replyTo) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Destination getJMSDestination() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJMSDestination(Destination destination) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getJMSDeliveryMode() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setJMSDeliveryMode(int deliveryMode) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getJMSRedelivered() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setJMSRedelivered(boolean redelivered) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getJMSType() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJMSType(String type) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getJMSExpiration() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setJMSExpiration(long expiration) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getJMSDeliveryTime() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setJMSDeliveryTime(long deliveryTime) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getJMSPriority() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setJMSPriority(int priority) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearProperties() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean propertyExists(String name) throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBooleanProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte getByteProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShortProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIntProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLongProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloatProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDoubleProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStringProperty(String name) throws JMSException {
		return (String) properties.get(name);
	}

	@Override
	public Object getObjectProperty(String name) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getPropertyNames() throws JMSException {
		return Collections.enumeration(properties.keySet());
	}

	@Override
	public void setBooleanProperty(String name, boolean value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setByteProperty(String name, byte value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setShortProperty(String name, short value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntProperty(String name, int value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLongProperty(String name, long value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFloatProperty(String name, float value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDoubleProperty(String name, double value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStringProperty(String name, String value) throws JMSException {
		MCR.addCall("name", name, "value", value);
	}

	@Override
	public void setObjectProperty(String name, Object value) throws JMSException {
		MCR.addCall("name", name, "value", value);
	}

	@Override
	public void acknowledge() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBody() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T getBody(Class<T> c) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBodyAssignableTo(Class c) throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setText(String string) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getText() throws JMSException {
		return "Text from TextMessageSpy";
	}

}
