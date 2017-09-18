package it.db.retriever.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.DataSource;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

/**
 * Classe per la lettura delle configurazioni dei DataSources presenti nella
 * directory "datasource"
 * 
 * @author D.Bertini
 *
 */
public class DataSourcesReader {

	private FileInputStream schema;

	/**
	 * Metodo che lancia la lettura dei file di configurazione dei datasource
	 * presenti nella directory standard
	 */
	public void initDataSources() {
		try {

			LogManager.getLogger(DataSourcesReader.class).info("Inizio lettura file datasource");
			// lettura dei file nella directory
			Files.newDirectoryStream(Paths.get(StandardParameter.DATASOURCE_PATH),
					path -> path.toString().endsWith(".xml")).forEach(a -> readFile(a.toFile())); // per ogni file xml
																									// trovato lancio il
																									// metodo di lettura
			LogManager.getLogger(DataSourcesReader.class).info("Fine lettura file datasource");
		} catch (IOException e) {
			LogManager.getLogger(DataSourcesReader.class).fatal(e);
		}
	}

	/**
	 * Metodo di lettura delle configurazioni del datasource
	 * 
	 * @param aFile
	 *            nome del file contenente le configurazioni
	 */
	private void readFile(File aFile) {
		try {
			LogManager.getLogger(DataSourcesReader.class).info("---------------------------------------");
			// inizializzo lo stream per la validazione degli XML
			this.schema = new FileInputStream(
					StandardParameter.SCHEMA_VALIDATOR_PATH + StandardParameter.DATASOURCES_SCHEMA);
			// controllo sulla validità dell'XML del report
			if (!XmlUtils.validateXml(new FileInputStream(aFile), this.schema)) {
				LogManager.getLogger(DataSourcesReader.class).error("Attenzione il file " + aFile.getName()
						+ " contiene un XML non valido rispettivamente alla struttura dei datasource.");
				throw new Exception("Attenzione il file " + aFile.getName()
						+ " contiene un XML non valido rispettivamente alla struttura dei datasource.");
			}
			this.schema.close();
			// instazio il marshaller con il tipo di oggetto della classe
			JAXBContext jaxbContext = JAXBContext.newInstance(DataSource.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// converzione da XML ad oggetto DataSource
			DataSource ds = (DataSource) jaxbUnmarshaller.unmarshal(aFile);

			// controllo se il datasource sia già stato definito in altri file per errore
			if (ApplicationContext.INSTANCE.isDataSourcePresent(ds.getName())) {
				LogManager.getLogger(DataSourcesReader.class).error("Attenzione il datasource con nome " + ds.getName()
						+ " presente nel file " + aFile.getName() + " e' gia' stato definito in precedenza!");
			} else {
				// controllo se la connessione definita nel datasource sia
				// funzionante
				if (ConnectionProvider.testConnection(ds)) {
					// aggiungo il datasource alla lista di quelli presenti
					ApplicationContext.INSTANCE.setDataSource(ds);
				} else {
					LogManager.getLogger(DataSourcesReader.class)
							.error("Attenzione il datasource con nome " + ds.getName() + " presente nel file "
									+ aFile.getName() + " non riesce a connettersi alla base dati definita.");
				}
			}
		} catch (Exception e) {
			LogManager.getLogger(DataSourcesReader.class).fatal(e);
		}
	}
}
