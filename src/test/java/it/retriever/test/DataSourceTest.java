package it.retriever.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

public class DataSourceTest {
	@Test
	public void validationTest() {
		try {
			boolean valid = XmlUtils.validateXml(
					new FileInputStream(StandardParameter.DATASOURCE_PATH + "monet_test.xml"),
					new FileInputStream(StandardParameter.SCHEMA_VALIDATOR_PATH +  StandardParameter.DATASOURCES_SCHEMA));
			assert(valid);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
