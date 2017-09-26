package it.db.retriever.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.exporter.ExportType;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

/**
 * Classe per la lettura delle configurazioni dei DataSources presenti nella
 * directory "datasource"
 * 
 * @author D.Bertini
 *
 */
public class ReportsReader {

	private FileInputStream schema;

	/**
	 * Metodo che lancia la lettura dei file di configurazione dei report da
	 * eseguire presenti nella directory standard
	 */
	public void initReports() {
		try {

			LogManager.getLogger(ReportsReader.class).info("Inizio lettura file reports");
			// lettura dei file nella directory
			Files.newDirectoryStream(Paths.get(StandardParameter.REPORTS_PATH),
					path -> path.toString().endsWith(".xml")).forEach(a -> readFile(a.toFile())); // per ogni file xml
																									// trovato lancio il
																									// metodo di lettura
			LogManager.getLogger(ReportsReader.class).info("Fine lettura file reports");
		} catch (IOException e) {
			LogManager.getLogger(ReportsReader.class).fatal(e);
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
			LogManager.getLogger(ReportsReader.class).info("---------------------------------------");
			// inizializzo lo stream per la validazione degli XML
			this.schema = new FileInputStream(
					StandardParameter.SCHEMA_VALIDATOR_PATH + StandardParameter.REPORT_SCHEMA);

			// controllo sulla validità dell'XML del report
			if (!XmlUtils.validateXml(new FileInputStream(aFile), this.schema)) {
				LogManager.getLogger(ReportsReader.class).error("Attenzione il file " + aFile.getName()
						+ " contiene un XML non valido rispettivamente alla struttura dei report.");
				throw new Exception("Attenzione il file " + aFile.getName()
						+ " contiene un XML non valido rispettivamente alla struttura dei report.");
			}

			this.schema.close();

			// instazio il marshaller con il tipo di oggetto della classe
			JAXBContext jaxbContext = JAXBContext.newInstance(Report.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// converzione da XML ad oggetto DataSource
			Report report = (Report) jaxbUnmarshaller.unmarshal(aFile);
			report.setFilname(aFile.getName());
			// controllo se il report sia già stato definito in altri file per errore
			if (ApplicationContext.INSTANCE.isReportPresent(report.getName())) {
				LogManager.getLogger(ReportsReader.class).error("Attenzione il report con nome " + report.getName()
						+ " presente nel file " + aFile.getName() + " e' gia' stato definito in precedenza!");
			} else {
				// controllo che il datasource definito per il report sia
				// stato configurato correttamente
				if (ApplicationContext.INSTANCE.isDataSourcePresent(report.getDatasource())) {

					ApplicationContext.INSTANCE.setReport(report);

					// si aggiungono dei controlli sulla presenza o meno del template del report
					if (ExportType.EXCEL.toString().equalsIgnoreCase(report.getExport().trim())) {
						// se si è scelto il tipo di export EXCEL si controlla il fatto che
						// sia stato inserito un template
						if (report.getTemplate() != null && !report.getTemplate().trim().equalsIgnoreCase("")) {
							// Se è stato inserito un template controllo che sia tra quelli disponibili
							// altrimenti si utilizza quello di default
							if (report.getTemplate() != null && !report.getTemplate().trim().equalsIgnoreCase("")) {
								boolean found[] = { false };
								ApplicationContext.INSTANCE.getTemplates().forEach(tp -> {
									if (tp.getName().equalsIgnoreCase(report.getTemplate()))
										found[0] = true;
								});

								if (!found[0])
									LogManager.getLogger(ReportsReader.class).warn("Il report " + report.getName()
											+ " ha associato un template non definito, si utilizzerà quello di default");
							}
						} else {
							// Non è stato definito nessun template, quindi si utilizzerà quello di default
							LogManager.getLogger(ReportsReader.class).warn("Il report " + report.getName()
									+ " non ha associato un template, si utilizzerà quello di default.");
						}

					}

					LogManager.getLogger(ReportsReader.class)
							.info("Report " + report.getName() + " letto correttamente.");

				} else {
					LogManager.getLogger(ReportsReader.class)
							.error("Attenzione il report con nome " + report.getName() + " presente nel file "
									+ aFile.getName() + " ha associato il datasource " + report.getDatasource()
									+ " che non è presente nella lista di quelli configurati.");
				}
			}
		} catch (Exception e) {
			LogManager.getLogger(ReportsReader.class).fatal(e);
		}
	}
}
