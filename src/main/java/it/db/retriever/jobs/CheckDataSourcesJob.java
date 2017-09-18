package it.db.retriever.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
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
import it.db.retriever.core.ConnectionProvider;
import it.db.retriever.core.configuration.entity.DataSource;
import it.db.retriever.utils.RunningUtils;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.XmlUtils;

/**
 * Job che controlla la presenza di nuovi datasource o l'eliminazione di
 * datasource obsoleti per i quali è stato rimosso il file dalla cartella di
 * default "./datasorces/"
 * 
 * @author D.Bertini
 *
 */
public class CheckDataSourcesJob implements Job {

	private List<DataSource> newDataSources;

	private FileInputStream schema;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// lettura dei file nella directory
		try {
			LogManager.getLogger(CheckDataSourcesJob.class)
					.info("---------------------------------------------------------");
			LogManager.getLogger(CheckDataSourcesJob.class).info("Inizio esecuzione job di controllo dei DataSources");
			this.newDataSources = new ArrayList<>();
			Files.newDirectoryStream(Paths.get(StandardParameter.DATASOURCE_PATH),
					path -> path.toString().endsWith(".xml")).forEach(a -> readFile(a.toFile()));

			// dopo aver recuperato la lista dei datasouces presenti
			// e funzionanti nella directory si controlla
			// se ci sono nuovi datasource da aggiungere alla lista di quelli del context
			this.newDataSources.forEach(ds -> {
				if (!ApplicationContext.INSTANCE.isDataSourcePresent(ds.getName()))
					ApplicationContext.INSTANCE.setDataSource(ds);
			});

			// dopo aver aggiunto gli eventuali nuovi datasource
			// passo alla rimozione dei datasouce obsoleti
			List<DataSource> tmpDS = new ArrayList<>();

			ApplicationContext.INSTANCE.getDataSources().forEach(dsOld -> {
				this.newDataSources.forEach(dsNew -> {
					if (dsNew.getName().trim().equalsIgnoreCase(dsOld.getName()))
						tmpDS.add(dsOld);
				});
			});
			// Cambio i datasource presenti nel contesto
			// utilizzati per l'esecuzione dei report
			ApplicationContext.INSTANCE.setDataSources(tmpDS);

			// si controlla la presenza di eventuali report
			// che fanno riferimento a datasource non più
			// presenti e se ne notifica l'errore
			ApplicationContext.INSTANCE.getReports().forEach(rep -> {
				if (!ApplicationContext.INSTANCE.isDataSourcePresent(rep.getDatasource())) {
					LogManager.getLogger(CheckDataSourcesJob.class)
							.error("Attenzione il report con nome " + rep.getName() + " ha associato il datasource "
									+ rep.getDatasource() + " che non è presente nella lista di quelli configurati.");
				}
			});

			LogManager.getLogger(CheckDataSourcesJob.class).info("Datasource presenti dopo il controllo: ");
			ApplicationContext.INSTANCE.getDataSources()
					.forEach(item -> LogManager.getLogger(CheckDataSourcesJob.class).info("- " + item.getName()));

		} catch (IOException e) {
			LogManager.getLogger(CheckDataSourcesJob.class).fatal(e);
		} // per ogni file xml trovato lancio il metodo di lettura

		LogManager.getLogger(CheckDataSourcesJob.class).info("Job di controllo dei DataSources eseguito correttamente");
		LogManager.getLogger(CheckDataSourcesJob.class)
				.info("---------------------------------------------------------");
		
		//ri-scrivo il report con  report e datasource attivi
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
			LogManager.getLogger(CheckDataSourcesJob.class).info("---------------------------------------");

			// inizializzo lo stream per la validazione degli XML
			this.schema = new FileInputStream(
					StandardParameter.SCHEMA_VALIDATOR_PATH + StandardParameter.DATASOURCES_SCHEMA);
			// controllo sulla validità dell'XML del report
			if (!XmlUtils.validateXml(new FileInputStream(aFile), this.schema)) {
				LogManager.getLogger(CheckDataSourcesJob.class).error("Attenzione il file " + aFile.getName()
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
			if (ApplicationContext.INSTANCE.isDataSourcePresent(ds.getName()))
				LogManager.getLogger(CheckDataSourcesJob.class)
						.info("Datasource " + ds.getName() + " gia' stato definito");
			else
				LogManager.getLogger(CheckDataSourcesJob.class)
						.info("Datasource " + ds.getName() + " presente nel file " + aFile.getName() + " nuovo!");

			// controllo se la connessione definita nel datasource sia
			// funzionante
			if (ConnectionProvider.testConnection(ds)) {
				// aggiungo il datasource alla lista di quelli presenti
				this.newDataSources.add(ds);
			} else {
				LogManager.getLogger(CheckDataSourcesJob.class)
						.error("Attenzione il datasource con nome " + ds.getName() + " presente nel file "
								+ aFile.getName() + " non riesce a connettersi alla base dati definita.");
			}
		} catch (Exception e) {
			LogManager.getLogger(CheckDataSourcesJob.class).fatal(e);
		}
	}
}
