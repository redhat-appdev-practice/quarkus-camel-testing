package com.redhat.consulting.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class JMSRouteBuilderTest extends CamelTestSupport {

	@Override
	public String isMockEndpoints() {
		return "*";
	}

	@Test
	public void testJMSRoute() throws InterruptedException {
		MockEndpoint jmsJsonOutput = getMockEndpoint("mock:dest");
		jmsJsonOutput.setAssertPeriod(2000);
		jmsJsonOutput.expectedBodiesReceived("{\n  \"head\" : {\n    \"title\" : \"My Title\"\n  },\n  \"body\" : \"Value\"\n}");
		jmsJsonOutput.expectedMessageCount(1);
		template.sendBody("direct:start", "<html><head><title>My Title</title></head><body>Value</body></html>");

		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		JMSRouteBuilder jmsRouteBuilder = new JMSRouteBuilder();

		// Set the in/out queue URIs for the RouteBuilder so that we can inject mocks and assert results
		jmsRouteBuilder.jmsQueueInUri = "direct:start";
		jmsRouteBuilder.jmsQueueOutUri = "mock:dest";

		return jmsRouteBuilder;
	}
}