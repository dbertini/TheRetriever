package it.db.retriever.exporter;

import java.util.Date;
import java.util.Random;

import it.db.retriever.core.configuration.entity.QueryResponse;
import it.db.retriever.core.configuration.entity.Report;

/**
 * Interfaccia che deve essere implementata per poter 
 * effettuare un export dei dati
 * @author D.Bertini
 *
 */
public interface ExportInterface {

	/**
	 * Metodo che dato un {@link QueryResponse} contenente i dati
	 * esegue l'export scelto
	 * 
	 * @param aQueryResponse oggetto contenente i dati
	 * @param aReport oggetto {@link Report} contenente le configurazioni del report
	 * 
	 * @return nome del file appena creato
	 * @throws Exception Eccezione generica
	 */
	public abstract String export(QueryResponse aQueryResponse, Report aReport) throws Exception;
	
	/**
	 * Metodo di default dell'interfaccia per la generazione di un nome univoco del report
	 * 
	 * @return nome del report esempio: "report_000000000000000000000"
	 * 
	 */
	public default String generateFileName() {
		Date data = new Date();
		Random rdm = new Random();
		String reportName = "report_" + data.getTime() + rdm.nextInt();
		return reportName;
	}

}
