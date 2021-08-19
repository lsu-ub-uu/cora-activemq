module se.uu.ub.cora.activemq {
	requires se.uu.ub.cora.messaging;
	// requires activemq.client;
	requires java.naming;
	requires jakarta.jms.api;
	requires artemis.jakarta.client;

	provides se.uu.ub.cora.messaging.MessagingFactory
			with se.uu.ub.cora.activemq.ActiveMqMessagingFactory;
}