package it.retriever.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

public class ReportTest {

	@Test
	public void validationTest() {
		try {
			boolean valid = XmlUtils.validateXml(
					new FileInputStream(StandardParameter.REPORTS_PATH + "report2.xml"),
					new FileInputStream(StandardParameter.SCHEMA_VALIDATOR_PATH +  StandardParameter.REPORT_SCHEMA));
			assert(valid);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assert(false);
		}
	}
}
