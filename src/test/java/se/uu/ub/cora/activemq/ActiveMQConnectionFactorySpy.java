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

import java.util.List;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;

//public class ActiveMQConnectionFactorySpy implements ConnectionFactory {
public class ActiveMQConnectionFactorySpy extends ActiveMQConnectionFactory {

	public List<Connection> createdConnections;
	String brokerURL = null;
	String userName = null;
	String password = null;

	@Override
	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
}
