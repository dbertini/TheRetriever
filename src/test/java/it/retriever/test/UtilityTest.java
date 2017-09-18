package it.retriever.test;

import java.io.IOException;
import java.text.MessageFormat;

import org.junit.Test;

import it.db.retriever.utils.FileUtils;
import it.db.retriever.utils.StandardParameter;

public class UtilityTest {

	@Test
	public void fileLengthTest() {
		try {
			assert(FileUtils.checkFileLength(StandardParameter.SCHEMA_VALIDATOR_PATH +  StandardParameter.DATASOURCES_SCHEMA));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testString() {
		try {
			StringBuffer xml = new StringBuffer();
			xml.append("<NOME>{0}</NOME>");
			xml.append("<COGNOME>{1}</COGNOME>");
			Object[] params = new Object[]{"Mario", "Rossi"};
			
			String msg = MessageFormat.format(xml.toString(), params);
			System.out.println(msg);

			assert(true);
		} catch (Exception e) {
			e.printStackTrace();
			assert(false);
		}
	}
}
