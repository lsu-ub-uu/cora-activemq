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

import java.util.HashMap;
import java.util.Map;

import se.uu.ub.cora.messaging.MessageReceiver;

public class MessageReceiverSpy implements MessageReceiver {

	public Map<String, String> headers = new HashMap<String, String>();
	public String message;
	public int messagesReceived = 0;

	@Override
	public void receiveMessage(Map<String, String> headers, String message) {
		this.headers = headers;
		this.message = message;
		this.messagesReceived++;
	}

	@Override
	public void topicClosed() {
		// TODO Auto-generated method stub

	}

}
