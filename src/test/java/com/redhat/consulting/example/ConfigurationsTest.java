package com.redhat.consulting.example;

import org.apache.camel.component.log.LogComponent;
import org.apache.camel.support.processor.DefaultExchangeFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationsTest {

	@Test
	public void confirmLoggingConfiguration() {
		var underTest = new Configurations();
		LogComponent logComponent = underTest.log();

		assertNotNull(logComponent.getExchangeFormatter());
		assertTrue(logComponent.getExchangeFormatter() instanceof DefaultExchangeFormatter);
		DefaultExchangeFormatter formatter = (DefaultExchangeFormatter) logComponent.getExchangeFormatter();
		assertFalse(formatter.isShowExchangePattern());
		assertFalse(formatter.isShowBodyType());
	}
}