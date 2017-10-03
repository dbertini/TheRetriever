package it.db.retriever.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.templates.Template;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

/**
 * Classe per la lettura delle configurazioni dei {@link Template} presente su file XML
 * @author D.Bertini
 *
 */
public class TemplateReader {

	private FileInputStream schema;
	
	/**
	 * Metodo che lancia la lettura dei file di configurazione dei template da
	 * eseguire presenti nella directory standard
	 */
	public void initTemplates() {
		try {

			LogManager.getLogger(TemplateReader.class).info("Inizio lettura file template");
			// lettura dei file nella directory
			DirectoryStream<Path> stream = null;
			try {
				DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
					public boolean accept(Path file) throws IOException {
						return (file.toString().endsWith(".xml"));
					}
				};
				Path dir = Paths.get(StandardParameter.TEMPLATE_PATH);
				stream = Files.newDirectoryStream(dir, filter);
				stream.forEach(a -> readFile(a.toFile()));
			} catch (Exception e) {
				LogManager.getLogger(ReportsReader.class).error(e);
			} finally {
				stream.close();
			}
			
//			
//			Files.newDirectoryStream(Paths.get(StandardParameter.TEMPLATE_PATH),
//					path -> path.toString().endsWith(".xml")).forEach(a -> readFile(a.toFile())); // per ogni file xml
																									// trovato lancio il
																									// metodo di lettura
			LogManager.getLogger(TemplateReader.class).info("Fine lettura file template");
		} catch (IOException e) {
			LogManager.getLogger(TemplateReader.class).fatal(e);
		}
	}

	/**
	 * Metodo di lettura delle configurazioni del template
	 * 
	 * @param aFile
	 *            nome del file contenente le configurazioni
	 */
	private void readFile(File aFile) {
		try {
			LogManager.getLogger(TemplateReader.class).info("---------------------------------------");
			
			// inizializzo lo stream per la validazione degli XML
			this.schema = new FileInputStream(
					StandardParameter.SCHEMA_VALIDATOR_PATH + StandardParameter.TEMPLATE_SCHEMA);

			// controllo sulla validità dell'XML del report
			FileInputStream theFile = new FileInputStream(aFile);
			if (!XmlUtils.validateXml(theFile, this.schema)) {
				LogManager.getLogger(TemplateReader.class).error("Attenzione il file " + aFile.getName()
						+ " contiene un XML non valido rispettivamente alla struttura dei template.");
				throw new Exception("Attenzione il file " + aFile.getName()
						+ " contiene un XML non valido rispettivamente alla struttura dei template.");
			}
			theFile.close();
			this.schema.close();
			
			// instazio il marshaller con il tipo di oggetto della classe
			JAXBContext jaxbContext = JAXBContext.newInstance(Template.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// converzione da XML ad oggetto Template
			Template template = (Template) jaxbUnmarshaller.unmarshal(aFile);

			// controllo se il template sia già stato definito in altri file per errore
			if (ApplicationContext.INSTANCE.isTemplatePresent(template.getName())) {
				LogManager.getLogger(TemplateReader.class).error("Attenzione il template con nome " + template.getName()
						+ " presente nel file " + aFile.getName() + " e' gia' stato definito in precedenza!");
			} else {
				//aggiungo il template alla lista dei templete utilizzabili
				ApplicationContext.INSTANCE.setTemplate(template);
				LogManager.getLogger(TemplateReader.class).info("Template " + template.getName() + " letto correttamente.");
			}
		} catch (Exception e) {
			LogManager.getLogger(TemplateReader.class).fatal(e);
		}
	}
}
