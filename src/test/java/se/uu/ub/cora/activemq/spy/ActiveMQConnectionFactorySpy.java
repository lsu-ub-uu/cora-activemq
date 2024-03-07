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

import org.apache.activemq.ActiveMQConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

//public class ActiveMQConnectionFactorySpy implements ConnectionFactory {
public class ActiveMQConnectionFactorySpy extends ActiveMQConnectionFactory {

	public List<ActiveMqConnectionSpy> createdConnections = new ArrayList<>();
	public String brokerURL = null;
	public String userName = null;
	public String password = null;
	public boolean throwError = false;

	public MethodCallRecorder MCR = new MethodCallRecorder();

	public ActiveMQConnectionFactorySpy() {
		ifMCRisNotInitialized();
	}

	@Override
	public void setBrokerURL(String brokerURL) {
		ifMCRisNotInitialized();
		MCR.addCall("brokerURL", brokerURL);
		this.brokerURL = brokerURL;
	}

	private void ifMCRisNotInitialized() {
		if (MCR == null) {
			MCR = new MethodCallRecorder();
		}
	}

	@Override
	public void setUserName(String userName) {
		MCR.addCall("userName", userName);
		this.userName = userName;
	}

	@Override
	public void setPassword(String password) {
		MCR.addCall("password", password);
		this.password = password;
	}

	@Override
	public Connection createConnection() throws JMSException {
		MCR.addCall();

		if (throwError)
			throw new JMSException("Error from ActiveMqTopicListenerSpy on newConnection");
		ActiveMqConnectionSpy activeMqConnectionSpy = new ActiveMqConnectionSpy();
		createdConnections.add(activeMqConnectionSpy);

		MCR.addReturned(activeMqConnectionSpy);
		return activeMqConnectionSpy;
	}

	@Override
	public Connection createConnection(String userName, String password) throws JMSException {
		return null;
	}

}
