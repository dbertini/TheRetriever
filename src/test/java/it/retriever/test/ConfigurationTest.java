package it.retriever.test;

import org.junit.Test;

import it.db.retriever.core.ConfigurationReader;

public class ConfigurationTest {

	@Test
	public void configurationTest() {
		
		ConfigurationReader.loadConfiguration();
		assert(true);
	}
}
