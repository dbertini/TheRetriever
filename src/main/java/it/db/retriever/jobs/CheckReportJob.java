package it.db.retriever.jobs;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.exporter.ExportType;
import it.db.retriever.utils.RunningUtils;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

/**
 * Job che controlla la cartella dei report per vedere se ci sono state delle
 * modifiche.
 * 
 * @author D.Bertini
 *
 */
public class CheckReportJob implements Job {

	private List<Report> newReports;
	
	private FileInputStream schema;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		this.newReports = new ArrayList<>();
		try {
			LogManager.getLogger(CheckReportJob.class)
					.info("---------------------------------------------------------");
			LogManager.getLogger(CheckReportJob.class).info("Inizio esecuzione del job di controllo dei Report");

			LogManager.getLogger(CheckReportJob.class).info("Inizio lettura dei file report");
			// lettura dei file nella directory
			Files.newDirectoryStream(Paths.get(StandardParameter.REPORTS_PATH),
					path -> path.toString().endsWith(".xml")).forEach(a -> readFile(a.toFile())); // per ogni file xml
																									// trovato lancio il
																									// metodo di lettura
			LogManager.getLogger(CheckReportJob.class).info("File report letti correttamente.");

			// per ogni report trovato tra i file analizzati
			// si controlla il fatto che esista già un report schedulato
			// se non esiste viene creato
			this.newReports.forEach(rpt -> {
				// Per ogni report si controlla se è presente o meno
				if (!ApplicationContext.INSTANCE.isReportPresent(rpt.getName())) {
					// se il report non è presente tra quelli schedulati
					// si passa alla schedualzione del nuovo report
					LogManager.getLogger(CheckReportJob.class)
							.debug("-----------------------------------------------------");
					LogManager.getLogger(CheckReportJob.class)
							.debug("Inizio schedulazione del report " + rpt.getName());

					// Mappa per il passaggio dati al job che eseguirà il report
					JobDataMap map = new JobDataMap();
					// aggiungo alla mappa le configurazioni di esecuzione del report
					map.put(StandardParameter.REPORT, rpt);
					// creazione del JOB
					JobDetail job = newJob(ReportJob.class).withIdentity(rpt.getName(), StandardParameter.REPORT_GROUP)
							.usingJobData(map).build();
					// Creazione del trigger per far scattare il job
					Trigger trigger = newTrigger().withIdentity(rpt.getName(), StandardParameter.REPORT_GROUP)
							.startNow().withSchedule(cronSchedule(rpt.getCron())).build();
					try {
						// associazione del job e del trigger all'interno dello scheduler
						ApplicationContext.INSTANCE.getScheduler().scheduleJob(job, trigger);
					} catch (SchedulerException e) {
						LogManager.getLogger(CheckReportJob.class)
								.error("Errore durante la schedulazione del report " + rpt.getName());
						LogManager.getLogger(CheckReportJob.class).error(e);
					}
					LogManager.getLogger(CheckReportJob.class)
							.info("Schedulazione del report " + rpt.getName() + " andata a buon fine.");
				}
			});

			// per ogni report presente nel context si controlla che sia ancora
			// presente tra quelli appena caricati dai file
			// se non è più presente viene rimosso
			ApplicationContext.INSTANCE.getReports().forEach(oldRpt -> {
				boolean found = false;
				// si scorrono tutti i vecchi report e si confrontano con quelli nuovi
				for (Iterator<Report> iterator = this.newReports.iterator(); iterator.hasNext();) {
					if (iterator.next().getName().trim().equalsIgnoreCase(oldRpt.getName()))
						found = true;
				}
				// se non è stato trovato il report viene rimosso da quelli schedulati...
				if (!found) {
					try {
						LogManager.getLogger(CheckReportJob.class).info("Rimozione del report " + oldRpt.getName()
								+ " perchè non più presente tra i file di configurazione dei report.");
						// rimozione della schedulazione del report
						ApplicationContext.INSTANCE.getScheduler()
								.deleteJob(jobKey(oldRpt.getName(), StandardParameter.REPORT_GROUP));
						LogManager.getLogger(CheckReportJob.class)
								.info("Rimozione del report " + oldRpt.getName() + " andata a buon fine.");
					} catch (SchedulerException e) {
						LogManager.getLogger(CheckReportJob.class).fatal(e);
					}
				}
			});

			// rimpiazzo la vecchia lista con la nuova lista
			ApplicationContext.INSTANCE.setReports(this.newReports);

			// adesso si controlla che i report rimasti attivi
			// abbiano un datasource valido
			List<Report> tmpReportList = new ArrayList<>();

			ApplicationContext.INSTANCE.getReports().forEach(rpt -> {
				if (ApplicationContext.INSTANCE.isDataSourcePresent(rpt.getDatasource())) {
					// il report ha un datasource valido
					tmpReportList.add(rpt);
				} else {
					// il report ha un datasource che non è pià valido
					// quindi passo alla rimozione della schedulazione del report
					try {
						LogManager.getLogger(CheckReportJob.class).info("Rimozione del report " + rpt.getName()
								+ " perchè ha associato un datasource non più valido.");
						ApplicationContext.INSTANCE.getScheduler()
								.deleteJob(jobKey(rpt.getName(), StandardParameter.REPORT_GROUP));
						LogManager.getLogger(CheckReportJob.class)
								.info("Rimozione del report " + rpt.getName() + " andata a buon fine.");

						LogManager.getLogger(CheckReportJob.class)
								.error("Attenzione il report report " + rpt.getName()
										+ " è stato rimosso dall'esecuzione a perchè associato al datasource "
										+ rpt.getDatasource() + " che non risulta essere più valido.");

					} catch (SchedulerException e) {
						LogManager.getLogger(CheckReportJob.class).fatal(e);
					}
				}
				
				// si aggiungono dei controlli sulla presenza o meno del template del report
				if (ExportType.EXCEL.toString().equalsIgnoreCase(rpt.getExport().trim())) {
					// se si è scelto il tipo di export EXCEL si controlla il fatto che
					// sia stato inserito un template
					if (rpt.getTemplate() != null && !rpt.getTemplate().trim().equalsIgnoreCase("")) {
						// Se è stato inserito un template controllo che sia tra quelli disponibili
						// altrimenti si utilizza quello di default
						if (rpt.getTemplate() != null && !rpt.getTemplate().trim().equalsIgnoreCase("")) {
							boolean found[] = { false };
							ApplicationContext.INSTANCE.getTemplates().forEach(tp -> {
								if (tp.getName().equalsIgnoreCase(rpt.getTemplate()))
									found[0] = true;
							});

							if (!found[0])
								LogManager.getLogger(CheckReportJob.class).warn("Il report " + rpt.getName()
										+ " ha associato un template non definito, si utilizzerà quello di default");
						}
					} else {
						// Non è stato definito nessun template, quindi si utilizzerà quello di default
						LogManager.getLogger(CheckReportJob.class).warn("Il report " + rpt.getName()
								+ " non ha associato un template, si utilizzerà quello di default.");
					}

				}
				
			});

			// rimpiazzo la vecchia lista con la nuova lista
			ApplicationContext.INSTANCE.setReports(tmpReportList);
			LogManager.getLogger(CheckReportJob.class).info("Report presenti dopo il controllo: ");
			ApplicationContext.INSTANCE.getReports()
					.forEach(item -> LogManager.getLogger(CheckReportJob.class).info(" - " + item.getName()));

			LogManager.getLogger(CheckReportJob.class).info("Fine esecuzione del job di controllo dei Report");
			LogManager.getLogger(CheckReportJob.class)
					.info("---------------------------------------------------------");
			//ri-scrivo il report con  report e datasource attivi
			RunningUtils.writeRunningReport();

		} catch (IOException e) {
			LogManager.getLogger(CheckReportJob.class).fatal(e);
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
			LogManager.getLogger(CheckReportJob.class).info("---------------------------------------");

			// inizializzo lo stream per la validazione degli XML
			this.schema = new FileInputStream(
					StandardParameter.SCHEMA_VALIDATOR_PATH + StandardParameter.REPORT_SCHEMA);

			// controllo sulla validità dell'XML del report
			if (!XmlUtils.validateXml(new FileInputStream(aFile), this.schema)) {
				LogManager.getLogger(CheckReportJob.class).error("Attenzione il file " + aFile.getName()
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

			// controllo se il report sia già stato definito in altri file per errore
			if (ApplicationContext.INSTANCE.isReportPresent(report.getName())) {
				LogManager.getLogger(CheckReportJob.class).info("Report con nome " + report.getName()
						+ " presente nel file " + aFile.getName() + " e' gia' stato definito in precedenza!");
				this.newReports.add(ApplicationContext.INSTANCE.getReport(report.getName()));
			} else {
				// controllo che il datasource definito per il report sia
				// stato configurato correttamente
				if (ApplicationContext.INSTANCE.isDataSourcePresent(report.getDatasource())) {

					this.newReports.add(report);

					LogManager.getLogger(CheckReportJob.class)
							.info("Report " + report.getName() + " letto correttamente.");

				} else {
					LogManager.getLogger(CheckReportJob.class)
							.error("Attenzione il report con nome " + report.getName() + " presente nel file "
									+ aFile.getName() + " ha associato il datasource " + report.getDatasource()
									+ " che non è presente nella lista di quelli configurati.");
				}
			}
		} catch (Exception e) {
			LogManager.getLogger(CheckReportJob.class).fatal(e);
		}
	}
}
