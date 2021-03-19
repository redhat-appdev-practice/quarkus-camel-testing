package com.redhat.consulting.example;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;

@ApplicationScoped
public class JMSRouteBuilder extends RouteBuilder {

	private static final Logger LOG = Logger.getLogger(JMSRouteBuilder.class);

	private String jmsQueueInUri;

	private String jmsQueueOutUri;

	public JMSRouteBuilder(
			// Source endpoint URI injected using CDI in Quarkus (see: src/main/resources/application.properties)
			@ConfigProperty(name = "jms.example.queue.inbound", defaultValue = "jms:queue:XML.QUEUE.1") String jmsQueueInUri,
			// Destination endpoint URI injected using CDI in Quarkus (see: src/main/resources/application.properties)
			@ConfigProperty(name = "jms.example.queue.outbound", defaultValue = "jms:queue:JSON.QUEUE.2") String jsmQueueOutUri
	) {
		this.jmsQueueInUri = jmsQueueInUri;
		this.jmsQueueOutUri = jsmQueueOutUri;
	}

	@Override
	public void configure() throws Exception {
		from(jmsQueueInUri)
				.unmarshal().jacksonxml(HashMap.class)
				.marshal().json()
				.log("Input Received")
				.to(jmsQueueOutUri+"?jmsMessageType=Text")
				.to("log:info");
	}
}
