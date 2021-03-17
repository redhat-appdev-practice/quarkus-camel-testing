package com.redhat.consulting.example;

import com.ibm.mq.jms.MQConnectionFactory;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import javax.jms.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class XmlToJsonViaMqIT {


	@Inject
	MQConnectionFactory connectionFactory;

	@Test
	public void sendXmlAndExpectJson() throws JMSException {
		Connection conn = connectionFactory.createConnection();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination xmlInput = session.createQueue("XML.QUEUE.1");
		Destination jsonOutput = session.createQueue("JSON.QUEUE.2");
		MessageProducer producer = session.createProducer(xmlInput);
		MessageConsumer consumer = session.createConsumer(jsonOutput);

		Message xml = session.createTextMessage("<html><head><title>My Title</title></head><body>Value</body></html>");
		producer.send(xml);

		Message json = consumer.receive(10000);
		assertEquals("{\n  \"head\" : {\n    \"title\" : \"My Title\"\n  },\n  \"body\" : \"Value\"\n}", json.getBody(String.class));
	}

}
