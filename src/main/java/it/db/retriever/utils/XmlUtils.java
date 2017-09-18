package it.db.retriever.utils;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;

/**
 * Classe di utiliy relativa agli XML
 * 
 * @author D.Bertini
 *
 */
public class XmlUtils {
	
	

	/**
	 * Metodo che valida un XML relativamente ad uno schema XSD
	 * 
	 * @param xml {@link InputStream} relativo al file xml da validare
	 * @param xsd {@link InputStream} relativo allo schema XSD di validazione
	 * 
	 * @return true se viene sorpassata la validazione, altrimento false
	 */
	public static boolean validateXml(InputStream xml, InputStream xsd) {
		try {
			//Tolto a causa dell'utilizzo della libreria STAX per le poi di apache nuove
			//SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			SchemaFactory factory = SchemaFactory.newInstance(StandardParameter.W3C_XML_SCHEMA_NS_URI);

			Schema schema = factory.newSchema(new StreamSource(xsd));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml));
			return true;
		} catch (Exception ex) {
			//si logga solo come warning l'eventuale errore 
			//riscontrato nella validazione
			LogManager.getLogger(XmlUtils.class).warn(ex);
			return false;
		}
	}
}
