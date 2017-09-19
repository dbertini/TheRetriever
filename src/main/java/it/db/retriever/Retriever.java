package it.db.retriever;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.DataSourcesReader;
import it.db.retriever.core.ReportsReader;
import it.db.retriever.core.TemplateReader;
import it.db.retriever.jobs.CheckDataSourcesJob;
import it.db.retriever.jobs.CheckReportJob;
import it.db.retriever.jobs.CheckTemplateJob;
import it.db.retriever.jobs.ReportJob;
import it.db.retriever.utils.RunningUtils;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.Version;
import it.db.retriever.webserver.RetrieverServer;

public class Retriever {

	public static void main(String[] args) {

		try {
			Retriever theRetriever = new Retriever();
			theRetriever.init();
			theRetriever.retrive();
		} catch (Exception e) {
			LogManager.getLogger(Retriever.class).fatal(e);
			System.exit(-99);
		}
	}

	/**
	 * Metodo principale per il lancio dell'esecuzione dei job
	 * 
	 * @throws Exception Eccezione generica
	 */
	public void retrive() throws Exception {
		
		Properties props = new Properties();
		props.setProperty("org.quartz.threadPool.threadCount", StandardParameter.THREAD_COUNT);
		ApplicationContext.INSTANCE.initScheduler(props);
		// inizializzazione di tutti i vari job
		LogManager.getLogger(Retriever.class).info("****************** SCHEDULER ******************");
		initScheduler();
		LogManager.getLogger(Retriever.class).info("***********************************************");
		LogManager.getLogger(Retriever.class).info("****************** SYSTEM SCHEDULER ******************");
		initSystemScheduler();
		LogManager.getLogger(Retriever.class).info("******************************************************");

		// avvio dello scheduler
		LogManager.getLogger(Retriever.class).info("Starting scheduler............");
		ApplicationContext.INSTANCE.getScheduler().start();
		LogManager.getLogger(Retriever.class).info("Scheduler started!");
		//creo il file report con tutti i report ed i datasource attivi
		RunningUtils.writeRunningReport();

		LogManager.getLogger(Retriever.class).info("Report Retriver running up on version " + Version.version + " !");
	}

	private void init() {
		LogManager.getLogger(Retriever.class).info("Retriever is starting.....");

		LogManager.getLogger(Retriever.class).info("***************** DATASOURCES *****************");
		LogManager.getLogger(Retriever.class).info("Reading datasource configuration");
		DataSourcesReader dsr = new DataSourcesReader();
		dsr.initDataSources();
		LogManager.getLogger(Retriever.class).info("Datasource configuration read.");
		LogManager.getLogger(Retriever.class).info("***********************************************");
		
		LogManager.getLogger(Retriever.class).info("****************** TEMPLATES ******************");
		LogManager.getLogger(Retriever.class).info("Reading template configuration");
		TemplateReader tr = new TemplateReader();
		tr.initTemplates();
		LogManager.getLogger(Retriever.class).info("Template configuration read.");
		LogManager.getLogger(Retriever.class).info("***********************************************");
		

		LogManager.getLogger(Retriever.class).info("***************** REPORTS *****************");
		LogManager.getLogger(Retriever.class).info("Reading report configuration");
		ReportsReader rr = new ReportsReader();
		rr.initReports();
		LogManager.getLogger(Retriever.class).info("Report configuration read.");
		LogManager.getLogger(Retriever.class).info("***********************************************");

		LogManager.getLogger(Retriever.class).info("***************** WEBSERVER *****************");
		new Thread()
		{
		    public void run() {
		    	RetrieverServer.startServerRetriver(2222);
		    }
		}.start();
		LogManager.getLogger(Retriever.class).info("*********************************************");

	}

	/**
	 * Metodo per schedulare tutti i JOB trovati
	 * 
	 * @throws SchedulerException Eccezione di schedualazione
	 */
	private void initScheduler() throws SchedulerException {

		// ciclo su ogni report configurato
		ApplicationContext.INSTANCE.getReports().parallelStream().forEach(report -> {
			LogManager.getLogger(Retriever.class).info("-----------------------------------------------------");
			LogManager.getLogger(Retriever.class).info("Inizio schedulazione del report " + report.getName());

			// Mappa per il passaggio dati al job che eseguirà il report
			JobDataMap map = new JobDataMap();
			// aggiungo alla mappa le configurazioni di esecuzione del report
			map.put(StandardParameter.REPORT, report);
			// creazione del JOB
			JobDetail job = newJob(ReportJob.class).withIdentity(report.getName(), StandardParameter.REPORT_GROUP)
					.usingJobData(map).build();
			// Creazione del trigger per far scattare il job
			Trigger trigger = newTrigger().withIdentity(report.getName(), StandardParameter.REPORT_GROUP).startNow()
					.withSchedule(cronSchedule(report.getCron())).build();
			try {
				// associazione del job e del trigger all'interno dello scheduler
				ApplicationContext.INSTANCE.getScheduler().scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				LogManager.getLogger(Retriever.class)
						.error("Errore durante la schedulazione del report " + report.getName());
				LogManager.getLogger(Retriever.class).error(e);
			}
			LogManager.getLogger(Retriever.class)
					.info("Schedulazione del report " + report.getName() + " andata a buon fine.");
		});
	}

	/**
	 * Metodo per l'inizializzazione dei job di sistema
	 * @throws SchedulerException Eccezione di schedulazione
	 */
	private void initSystemScheduler() throws SchedulerException {
		LogManager.getLogger(Retriever.class).info("----------------------------------------------------------");
		LogManager.getLogger(Retriever.class).info("Inizio schedulazione job di controllo DataSources");
		JobDetail checkDataSourcesJob = newJob(CheckDataSourcesJob.class)
				.withIdentity("CheckDataSourcesJob", StandardParameter.SYSTEM_GROUP).build();
		// Creazione del trigger per far scattare il job
		Trigger checkDataSourcesTrigger = newTrigger().withIdentity("CheckDataSourcesJob", StandardParameter.SYSTEM_GROUP).startNow()
				.withSchedule(cronSchedule(StandardParameter.CRON_CHECK_DATASOURCE)).build();
		ApplicationContext.INSTANCE.getScheduler().scheduleJob(checkDataSourcesJob, checkDataSourcesTrigger);
		LogManager.getLogger(Retriever.class).info("Schedulazione job di controllo DataSources effettuata correttamente con la definizione: '" + StandardParameter.CRON_CHECK_DATASOURCE + "'");

		LogManager.getLogger(Retriever.class).info("----------------------------------------------------------");
		LogManager.getLogger(Retriever.class).info("Inizio schedulazione job di controllo Template");
		
		JobDetail checkTemplateJob = newJob(CheckTemplateJob.class)
				.withIdentity("CheckTemplateJob", StandardParameter.SYSTEM_GROUP).build();
		// Creazione del trigger per far scattare il job
		Trigger checkTemplateTrigger = newTrigger().withIdentity("CheckTemplateJob", StandardParameter.SYSTEM_GROUP).startNow()
				.withSchedule(cronSchedule(StandardParameter.CRON_CHECK_TEMPLATE)).build();
		ApplicationContext.INSTANCE.getScheduler().scheduleJob(checkTemplateJob, checkTemplateTrigger);
		LogManager.getLogger(Retriever.class).info("Schedulazione job di controllo template effettuata correttamente con la definizione: '" + StandardParameter.CRON_CHECK_TEMPLATE + "'");

		LogManager.getLogger(Retriever.class).info("----------------------------------------------------------");
		LogManager.getLogger(Retriever.class).info("Inizio schedulazione job di controllo Report");
		
		JobDetail checkReportJob = newJob(CheckReportJob.class)
				.withIdentity("CheckReportJob", StandardParameter.SYSTEM_GROUP).build();
		// Creazione del trigger per far scattare il job
		Trigger checkReportTrigger = newTrigger().withIdentity("CheckReportJob", StandardParameter.SYSTEM_GROUP).startNow()
				.withSchedule(cronSchedule(StandardParameter.CRON_CHECK_REPORT)).build();
		ApplicationContext.INSTANCE.getScheduler().scheduleJob(checkReportJob, checkReportTrigger);
		LogManager.getLogger(Retriever.class).info("Schedulazione job di controllo Report effettuata correttamente con la definizione: '" + StandardParameter.CRON_CHECK_REPORT + "'");
		
	}

}
