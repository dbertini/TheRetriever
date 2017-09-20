package it.retriever.test;

import org.junit.Test;

import it.db.retriever.core.ConfigurationReader;

public class ConfigurationTest {

	@Test
	public void configurationTest() {
		
		try {
			ConfigurationReader.loadConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
			assert(false);
		}
		assert(true);
	}
}
