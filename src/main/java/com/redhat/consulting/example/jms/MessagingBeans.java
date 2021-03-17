package com.redhat.consulting.example.jms;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import static com.ibm.msg.client.jms.JmsConstants.*;
import static com.ibm.msg.client.wmq.common.CommonConstants.*;

/**
 * This class is used to produce the JmsComponent and ConnectionFactory needed for Camel
 * to connect to a JMS compliant message queue (MQ Series)
 */
@ApplicationScoped
public class MessagingBeans {

	private static final Logger LOG = Logger.getLogger(MessagingBeans.class);

	@ConfigProperty(name = "mq.host", defaultValue = "localhost")
	String mqServer;

	@ConfigProperty(name = "mq.channel", defaultValue = "DEV.APP.SVRCONN")
	String mqChannel;

	@ConfigProperty(name = "mq.port", defaultValue = "1414")
	Integer mqPort;

	@ConfigProperty(name = "mq.manager", defaultValue = "QM1")
	String mqManager;

	@ConfigProperty(name = "mq.user", defaultValue = "app")
	String mqUser;

	@ConfigProperty(name = "mq.pass", defaultValue = "password")
	String mqPass;

	/**
	 * Produces a Named Bean in the CDI object graph which can be used for Camel routes
	 * @return A {@link JmsComponent} configured for MQ Series using a {@link MQConnectionFactory}
	 */
	@Named
	public JmsComponent mq() {
		JmsComponent jmsComponent = new JmsComponent();
		jmsComponent.setConnectionFactory(this.createMqConnectionFactory());
		return jmsComponent;
	}

	/**
	 * Produces a Named Bean in the CDI object graph which can be used connecting to MQ Series
	 * @return A {@link MQConnectionFactory} configured using properties
	 */
	@Named
	public MQConnectionFactory createMqConnectionFactory() {
		MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		mqQueueConnectionFactory.setHostName(mqServer);
		try {
			mqQueueConnectionFactory.setTransportType(WMQ_CM_CLIENT);
			mqQueueConnectionFactory.setChannel(mqChannel);
			mqQueueConnectionFactory.setPort(mqPort);
			mqQueueConnectionFactory.setQueueManager(mqManager);
			mqQueueConnectionFactory.setStringProperty(USERID, mqUser);
			mqQueueConnectionFactory.setStringProperty(PASSWORD, mqPass);
			mqQueueConnectionFactory.setBooleanProperty(USER_AUTHENTICATION_MQCSP, true);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return mqQueueConnectionFactory;
	}

}
