package it.db.retriever.utils;

/**
 * Classe di parametri standard
 * 
 * @author D.Bertini
 *
 */
public class StandardParameter {
	/**
	 * Directory dove dovranno essere presenti i file di configurazione dei datasource "./datasources/"
	 */
	public static final String DATASOURCE_PATH = "./datasources/";
	
	/**
	 * Directory dove dovranno essere presenti i file di configurazione dei report "./reports/"
	 */
	public static final String REPORTS_PATH = "./reports/";
	
	/**
	 * Numero di thread attivi sullo scheduler per l'eventuale
	 * lancio in parallelo di più task.
	 */
	public static final String THREAD_COUNT = "10";
	
	/**
	 * Nome della variabile per il recupero dei dettagli del report dal JobExecutionContext
	 */
	public static final String REPORT = "REPORT";
	
	/**
	 * Nome standard del gruppo al quale sono associati tutti i report
	 */
	public static final String REPORT_GROUP = "group1";
	
	/**
	 * Nome standard del gruppo al quale sono associati tutti i report di sistema
	 */
	public static final String SYSTEM_GROUP = "systemgroup";
	
	/**
	 * Username di servizio per l'invio delle email tramite Exchange
	 */
	public static final String EXCHANGE_USERNAME = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	/**
	 * Password di servizio per l'invio delle email tramite Exchange
	 */
	public static final String EXCHANGE_PASSWORD = "XXXXXXXXXXXXXXXXXXX";
	
	/**
	 * Directory dove verranno creati temporaneamente i reort da inviare
	 */
	public static final String TEMP_EXPORT_PATH = "./temp/";
	/**
	 * Formato standard con il quale vengono formattati i TimeStamp
	 */
	public static final String TIMESTAMP_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
	
	/**
	 * Formato standard con il quale vengono formattati i Date
	 */
	public static final String DATA_FORMAT = "dd/MM/yyyy";
	
	/**
	 * Cront per la schedualazione del job di sistema di controllo dei
	 * DataSource: '0 0,15,30,45 * ? * *'
	 */
	public static final String CRON_CHECK_DATASOURCE = "0 0,15,30,45 * ? * *";
	
	/**
	 * Cront per la schedualazione del job di sistema di controllo dei
	 * Template: '0 2,17,32,47 * ? * *'
	 */
	public static final String CRON_CHECK_TEMPLATE = "0 2,17,32,47 * ? * *";

	/**
	 * Cront per la schedualazione del job di sistema di controllo dei
	 * Report: '0 4,19,34,49 * ? * *'
	 */
	public static final String CRON_CHECK_REPORT = "0 4,19,34,49 * ? * *";

	/**
	 * Directory dove sono presenti gli XSD per le validazioni degli XML usati in applicazione "./schemas/"
	 */
	public static final String SCHEMA_VALIDATOR_PATH = "./schemas/";
	
	/**
	 * Nome del file XSD per la validazione dei file XML dei DataSource
	 */
	public static final String DATASOURCES_SCHEMA = "DataSourceSchema.xsd";
	/**
	 * Nome del file XSD per la validazione dei file XML dei Report
	 */
	public static final String REPORT_SCHEMA = "ReportSchema.xsd";
	
	/**
	 * Nome del file XSD per la validazione dei file XML dei Report
	 */
	public static final String TEMPLATE_SCHEMA = "TemplateSchema.xsd";
	
	/**
	 * Parametro di limite oltre il quale un file viene compresso
	 * prima di essere inviato tramite email (4,5Mb)
	 */
	public static final long LIMIT_FILE_SIZE = 4718592;
	
	/**
	 * Variabile per l'xml SCHMEA
	 */
	public static final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
	
	/**
	 * Nome del file di riepilogo dei report e datasuorce attivi.
	 */
	public static final String REPORT_FILE = "./running_job.xlsx";

	/**
	 * Enumeration degli stati utilizzabili per il log nel datastore relativo 
	 * alle esecuzioni dei report
	 * 
	 * @author D.Bertini
	 *
	 */
	public enum ReportStatus {
		
		INIT("INIT    "),
		FINISHED("FINISHED"),
		ERROR("ERROR   ");
		
		private final String text;
		
		private ReportStatus(final String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	/**
	 * File di salvataggio del datastore delle esecuzioni dei report
	 */
	public static final String STORIC_DATASTORE_FILE = "./datastore/storicdatastore.dat";

	/**
	 * Directory dove dovranno essere presenti i file di configurazione dei report "./reports/"
	 */
	public static final String TEMPLATE_PATH = "./templates/";
}
