package com.redhat.consulting.example;

import com.ibm.mq.jms.MQConnectionFactory;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import javax.jms.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class XmlToJsonViaMqIT {

	private static final Logger LOG = Logger.getLogger(XmlToJsonViaMqIT.class);

	@Inject
	MQConnectionFactory connectionFactory;

	@Test
	public void sendXmlAndExpectJson() throws JMSException {
		Connection conn = connectionFactory.createConnection();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue xmlInput = session.createQueue("XML.QUEUE.1");
		Queue jsonOutput = session.createQueue("JSON.QUEUE.2");
		MessageProducer producer = session.createProducer(xmlInput);
		MessageConsumer consumer = session.createConsumer(jsonOutput);

		final CompletableFuture<String> messageFuture = new CompletableFuture<>();

		consumer.setMessageListener(json -> {
			LOG.info("Message received");
			try {
				json.acknowledge();
				if (json instanceof TextMessage) {
					TextMessage jsonTextMsg = (TextMessage)json;
					LOG.info("Message decoded");
					messageFuture.complete(jsonTextMsg.getText());
				} else {
					messageFuture.completeExceptionally(new IllegalArgumentException());
				}
			} catch (Exception e) {
				e.printStackTrace();
				messageFuture.completeExceptionally(e);
			}
		});

		Message xml = session.createTextMessage("<html><head><title>My Title</title></head><body>Value</body></html>");
		producer.send(xml);
		conn.start();

		String result = messageFuture.orTimeout(5, TimeUnit.SECONDS).join();

		assertEquals("{\"head\":{\"title\":\"My Title\"},\"body\":\"Value\"}", result);
		conn.stop();
		producer.close();
		consumer.close();
		conn.close();
	}

}
