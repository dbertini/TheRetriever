package it.retriever.test;

import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import it.db.retriever.templates.Template;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

public class TemplateTest {
	@Test
	public void validationTest() {
		try {
			boolean valid = XmlUtils.validateXml(
					new FileInputStream(StandardParameter.TEMPLATE_PATH + "template.xml"),
					new FileInputStream(StandardParameter.SCHEMA_VALIDATOR_PATH +  StandardParameter.TEMPLATE_SCHEMA));
			
			
			
			// instazio il marshaller con il tipo di oggetto della classe
			JAXBContext jaxbContext = JAXBContext.newInstance(Template.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			Template template = (Template) jaxbUnmarshaller.unmarshal(new FileInputStream(StandardParameter.TEMPLATE_PATH + "template.xml"));
			
			
			
			assert(valid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
