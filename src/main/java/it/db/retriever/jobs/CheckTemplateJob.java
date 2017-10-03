package it.db.retriever.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.ReportsReader;
import it.db.retriever.templates.Template;
import it.db.retriever.utils.RunningUtils;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

/**
 * Job che controlla la presenza di nuovi template o l'eliminazione di template
 * obsoleti per i quali è stato rimosso il file dalla cartella di default
 * "./templates/"
 * 
 * @author D.Bertini
 *
 */
public class CheckTemplateJob implements Job {

	private List<Template> newTemplates;

	private FileInputStream schema;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// lettura dei file nella directory
		try {
			LogManager.getLogger(CheckTemplateJob.class)
					.info("---------------------------------------------------------");
			LogManager.getLogger(CheckTemplateJob.class).info("Inizio esecuzione job di controllo dei Template");
			this.newTemplates = new ArrayList<>();
			
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
//			
//			Files.newDirectoryStream(Paths.get(StandardParameter.TEMPLATE_PATH),
//					path -> path.toString().endsWith(".xml")).forEach(a -> readFile(a.toFile()));

			// dopo aver recuperato la lista dei template presenti
			// e funzionanti nella directory si controlla
			// se ci sono nuovi datasource da aggiungere alla lista di quelli del context
			this.newTemplates.forEach(tp -> {
				if (!ApplicationContext.INSTANCE.isTemplatePresent(tp.getName()))
					ApplicationContext.INSTANCE.setTemplate(tp);
			});

			// dopo aver aggiunto gli eventuali nuovi template
			// passo alla rimozione dei template obsoleti
			List<Template> tmpTP = new ArrayList<>();

			ApplicationContext.INSTANCE.getTemplates().forEach(tpOld -> {
				this.newTemplates.forEach(tpNew -> {
					if (tpNew.getName().trim().equalsIgnoreCase(tpOld.getName()))
						tmpTP.add(tpOld);
				});
			});
			// Cambio i template presenti nel contesto
			// utilizzati per l'esecuzione dei report
			ApplicationContext.INSTANCE.setTemplates(tmpTP);

			// si controlla la presenza di eventuali report
			// che fanno riferimento a temaplte non più
			// presenti e si scrive il warning
			ApplicationContext.INSTANCE.getReports().forEach(rpt -> {
				if (rpt.getTemplate() != null && !rpt.getTemplate().trim().equalsIgnoreCase("")) {
					if (!ApplicationContext.INSTANCE.isReportPresent(rpt.getTemplate()))
						LogManager.getLogger(CheckTemplateJob.class)
								.warn("Il report " + rpt.getName() + " fa' riferimento al template " + rpt.getTemplate()
										+ " non più presente tra quelli definiti, si utilizzerà quello di default.");
				}
			});

			LogManager.getLogger(CheckTemplateJob.class).info("Template presenti dopo il controllo: ");
			ApplicationContext.INSTANCE.getTemplates()
					.forEach(item -> LogManager.getLogger(CheckTemplateJob.class).info("- " + item.getName()));

		} catch (IOException e) {
			LogManager.getLogger(CheckTemplateJob.class).fatal(e);
		} // per ogni file xml trovato lancio il metodo di lettura

		LogManager.getLogger(CheckTemplateJob.class).info("Job di controllo dei Template eseguito correttamente");
		LogManager.getLogger(CheckTemplateJob.class).info("---------------------------------------------------------");

		// ri-scrivo il report con report e datasource attivi
		RunningUtils.writeRunningReport();
	}

	/**
	 * Metodo di lettura delle configurazioni del datasource
	 * 
	 * @param aFile
	 *            nome del file contenente le configurazioni
	 */
	private void readFile(File aFile) {
		try {
			LogManager.getLogger(CheckTemplateJob.class).info("---------------------------------------");

			// inizializzo lo stream per la validazione degli XML
			this.schema = new FileInputStream(
					StandardParameter.SCHEMA_VALIDATOR_PATH + StandardParameter.TEMPLATE_SCHEMA);
			// controllo sulla validità dell'XML del report
			FileInputStream theFile = new FileInputStream(aFile);
			if (!XmlUtils.validateXml(theFile, this.schema)) {
				LogManager.getLogger(CheckTemplateJob.class).error("Attenzione il file " + aFile.getName()
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
			Template tp = (Template) jaxbUnmarshaller.unmarshal(aFile);

			// controllo se il datasource sia già stato definito in altri file per errore
			if (ApplicationContext.INSTANCE.isTemplatePresent(tp.getName()))
				LogManager.getLogger(CheckTemplateJob.class)
						.info("Template " + tp.getName() + " gia' definito");
			else
				LogManager.getLogger(CheckTemplateJob.class)
						.info("Template " + tp.getName() + " presente nel file " + aFile.getName() + " nuovo!");

			this.newTemplates.add(tp);

		} catch (Exception e) {
			LogManager.getLogger(CheckTemplateJob.class).fatal(e);
		}
	}
}
