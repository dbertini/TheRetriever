package it.db.retriever.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import it.db.retriever.core.configuration.entity.DataSource;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.templates.Template;

/**
 * Enumeration singleton per accedere a tutte le configurazioni dell'applicazione
 * 
 * @author D.Bertini
 *
 */
public enum ApplicationContext {
	
	INSTANCE;
	
	private List<DataSource> dataSources;
	
	private List<Report> reports;
	
	private List<Template> templates;
	
	// oggetto che tiene lo scheduling
	private Scheduler scheduler;
	
	private Properties configuration;

	private ApplicationContext() {
		//inizializzazione delle variabili
		this.dataSources = new ArrayList<DataSource>();
		this.reports = new ArrayList<Report>();
		this.templates = new ArrayList<Template>();
	}

	/**
	 * Metodo per aggiungere una lista di DataSource a quelli presenti
	 * @param aDataSources lista dei datasouce da aggiungere
	 */
	public void setDataSources(List<DataSource> aDataSources) {
		this.dataSources.clear();
		this.dataSources.addAll(aDataSources);
	}

	/**
	 * Metodo per aggiungere un datasource alla lista di quelli presenti
	 * 
	 * @param aDataSource datasource
	 */
	public void setDataSource(DataSource aDataSource) {
		this.dataSources.add(aDataSource);
	}
	/**
	 * Metodo che dato un nome di datasource ne ritorna i dati per la connessione.
	 * 
	 * @param aName nome del datasource
	 * 
	 * @return datasource richiesto
	 */
	public DataSource getDataSource(String aName) {
		return this.dataSources
				.stream()
				.filter(datasource -> datasource.getName().equalsIgnoreCase(aName))
				.findAny().get();
	}
	
	/**
	 * Metodo per controllare se un DataSource è già presente
	 * @param aName nome del datasource da controllare
	 * @return true se è già presente, altrimenti false
	 */
	public boolean isDataSourcePresent(String aName) {
		if( this.dataSources.stream().filter(datasource -> datasource.getName().equalsIgnoreCase(aName)).count() > 0 )
			return true;
		else
			return false;
	}

	/**
	 * Metodo per aggiungere una lista di {@link Report} a quelli presenti
	 * @param aReports lista dei report da aggiungere
	 */
	public void setReports(List<Report> aReports) {
		this.reports.clear();
		this.reports.addAll(aReports);
	}

	/**
	 * Metodo per aggiungere un {@link Report} alla lista di quelli presenti
	 * 
	 * @param aReport report da aggiungere
	 */
	public void setReport(Report aReport) {
		this.reports.add(aReport);
	}
	/**
	 * Metodo che dato un nome di report ne ritorna i dati per l'esecuzione.
	 * 
	 * @param aName nome del report
	 * 
	 * @return {@link Report} richiesto
	 */
	public Report getReport(String aName) {
		return this.reports
				.stream()
				.filter(report -> report.getName().equalsIgnoreCase(aName))
				.findAny().get();
	}

	/**
	 * Metodo per controllare se un {@link Report} è già presente nella lista
	 * @param aName nome del report da controllare
	 * @return true se è già presente, altrimenti false
	 */
	public boolean isReportPresent(String aName) {
		if( this.reports.stream().filter(report -> report.getName().equalsIgnoreCase(aName)).count() > 0 )
			return true;
		else
			return false;
	}
	/**
	 * Metodo che ritorna la lista dei {@link Report} attivi
	 * 
	 * @return Lista di {@link Report}
	 */
	public List<Report> getReports() {
		return this.reports;
	}
	
	/**
	 * Metodo che ritorna la lista dei {@link DataSource} attivi
	 * 
	 * @return Lista di {@link DataSource}
	 */
	public List<DataSource> getDataSources() {
		return this.dataSources;
	}
	
	/**
	 * Metodoo per l'inizializzazione dello scheduler
	 * 
	 * @param aProperties {@link Properties} di inizializzazione dello scheduler
	 * 
	 * @throws SchedulerException eccezione di schedulazione
	 */
	public void initScheduler(Properties aProperties) throws SchedulerException {
		// Factory di inizializzazione dei job
		StdSchedulerFactory sf = new StdSchedulerFactory();
		// inizializzazione della factory con le properties
		sf.initialize(aProperties);
		// creo il nuovo scheduler
		this.scheduler = sf.getScheduler();
	}
	
	/**
	 * Metodo che ritorna lo {@link Scheduler} di contesto
	 * 
	 * @return {@link Scheduler} attivo
	 */
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	/**
	 * Metodo per aggiungere una lista di {@link Template} a quelli presenti
	 * @param aTemplates lista dei datasouce da aggiungere
	 */
	public void setTemplates(List<Template> aTemplates) {
		this.templates.clear();
		this.templates.addAll(aTemplates);
	}

	/**
	 * Metodo per aggiungere un {@link Template} alla lista di quelli presenti
	 * 
	 * @param aTemplate datasource
	 */
	public void setTemplate(Template aTemplate) {
		this.templates.add(aTemplate);
	}
	/**
	 * Metodo che dato un nome di {@link Template} ne ritorna i dati per l'export.
	 * 
	 * @param aName nome del template
	 * 
	 * @return {@link Template} richiesto
	 */
	public Template getTemplate(String aName) {
		return this.templates
				.stream()
				.filter(template -> template.getName().equalsIgnoreCase(aName))
				.findAny().get();
	}
	
	/**
	 * Metodo per controllare se un {@link Template} è già presente
	 * @param aName nome del {@link Template} da controllare
	 * @return true se è già presente, altrimenti false
	 */
	public boolean isTemplatePresent(String aName) {
		if( this.templates.stream().filter(template -> template.getName().equalsIgnoreCase(aName)).count() > 0 )
			return true;
		else
			return false;
	}
	/**
	 * Metodo che ritorna la lista dei {@link Template} attivi
	 * 
	 * @return Lista di {@link Template}
	 */
	public List<Template> getTemplates() {
		return this.templates;
	}
	
	public void setConfiguration(Properties aConfiguration) {
		this.configuration = aConfiguration;
	}
	
	public Properties getConfiguration() {
		return this.configuration;
	}
}
