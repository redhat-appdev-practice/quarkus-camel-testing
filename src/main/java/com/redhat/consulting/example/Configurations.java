package com.redhat.consulting.example;

import org.apache.camel.component.log.LogComponent;
import org.apache.camel.support.processor.DefaultExchangeFormatter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class Configurations {

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
