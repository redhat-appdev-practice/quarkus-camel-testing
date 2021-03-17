package com.redhat.consulting.example;

import org.apache.camel.component.log.LogComponent;
import org.apache.camel.support.processor.DefaultExchangeFormatter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class Configurations {

	/**
	 * Produces a named bean in the CDI object graph to configure logging for Camel
	 * @return A {@link LogComponent} configured for Quarkus and JBoss Logging
	 */
	@Named
	LogComponent log() {
		DefaultExchangeFormatter formatter = new DefaultExchangeFormatter();
		formatter.setShowExchangePattern(false);
		formatter.setShowBodyType(false);

		LogComponent component = new LogComponent();
		component.setExchangeFormatter(formatter);

		return component;
	}
}
