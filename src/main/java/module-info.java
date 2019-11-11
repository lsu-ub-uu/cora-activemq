module se.uu.ub.cora.activemq {
	requires se.uu.ub.cora.messaging;
	requires activemq.client;
	requires javax.jms.api;
	requires java.naming;

	provides se.uu.ub.cora.messaging.MessagingFactory
			with se.uu.ub.cora.activemq.ActiveMqMessagingFactory;
}