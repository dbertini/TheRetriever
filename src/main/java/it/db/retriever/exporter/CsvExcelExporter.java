/**
 * 
 */
package it.db.retriever.exporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.QueryResponse;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.utils.StandardParameter;

/**
 * Classe per effettuare l'export in formato CSV formattato per excel
 * @author D.Bertini
 *
 */
public class CsvExcelExporter implements ExportInterface {

	/* (non-Javadoc)
	 * @see it.bcc.sinergia.retriever.exporter.ExportInterface#export(it.bcc.sinergia.retriever.core.configuration.entity.QueryResponse)
	 */
	@Override
	public String export(QueryResponse aQueryResponse, Report aReport) throws Exception {
		//creazione del nome del file nella directory
		String lsFileName = StandardParameter.TEMP_EXPORT_PATH + generateFileName() + ".csv";

		//apertura dello stream per la scrittura del file
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(lsFileName));

		//Tramite la labels trovate durante l'esecuzione della select
		//creo la prima riga, ovvero l'intestazione
		String lsFirstRow = "";
		for (Iterator<String> iterator = aQueryResponse.getLabels().iterator(); iterator.hasNext();) {
			lsFirstRow = lsFirstRow + iterator.next()+";";
		}
		//scrittura della riga
		writer.write(lsFirstRow);
		writer.write("\n");
		writer.flush();
		
		//per ogni riga trovata faccio scrivo la riga
		aQueryResponse.getRows().stream().forEachOrdered(row -> {
			try {
				int rowSize = row.getColoumn().size();
				String rowStr = "";
				for (int i = 0; i < rowSize; i++) {
					try {
						//controllo i tipi delle colonne per effettuare il corretto cast
						if( aQueryResponse.getColoumnType().get(i) == java.sql.Types.NUMERIC) {
							if(row.getColoumn().get(i) != null) {
								BigDecimal bd = (BigDecimal)row.getColoumn().get(i);
								rowStr = rowStr + "=\"" + bd.toString() + "\";";
							} else {
								rowStr = rowStr + "" + ";";
							}
						} else if( aQueryResponse.getColoumnType().get(i) == java.sql.Types.INTEGER) {
							if(row.getColoumn().get(i) != null) {
								BigInteger bd = (BigInteger)row.getColoumn().get(i);
								rowStr = rowStr + "=\"" + bd.toString() + "\";";
							} else {
								rowStr = rowStr + "" + ";";
							}
						} else if( aQueryResponse.getColoumnType().get(i) == java.sql.Types.VARCHAR) {
							
							if(row.getColoumn().get(i) != null) {
								rowStr = rowStr + "=\"" + row.getColoumn().get(i) + "\";";
							} else {
								rowStr = rowStr + "" + ";";
							}
						} else if( aQueryResponse.getColoumnType().get(i) == java.sql.Types.TIMESTAMP) {

							if(row.getColoumn().get(i) != null) {
								SimpleDateFormat fmt = new SimpleDateFormat(StandardParameter.TIMESTAMP_FORMAT);
								rowStr = rowStr + fmt.format(new Date(((Timestamp)row.getColoumn().get(i)).getTime())) + ";";
							} else {
								rowStr = rowStr + "" + ";";
							}
						} else if( aQueryResponse.getColoumnType().get(i) == java.sql.Types.DATE) {
							if(row.getColoumn().get(i) != null) {
								SimpleDateFormat fmt = new SimpleDateFormat(StandardParameter.DATA_FORMAT);
								rowStr = rowStr + fmt.format(new Date(((java.sql.Date)row.getColoumn().get(i)).getTime())) + ";";
							} else {
								rowStr = rowStr + "" + ";";
							}
						} else {
							if(row.getColoumn().get(i) != null) {
								rowStr = rowStr + "=\"" + row.getColoumn().get(i).toString() + "\";";
							} else {
								rowStr = rowStr + "" + ";";
							}
						}
					} catch(Exception ex) {
						rowStr = rowStr + "" + ";";
						LogManager.getLogger(CsvExcelExporter.class).warn(ex);
					}
				}
				//scrittura della riga
				writer.write(rowStr);
				writer.write("\n");
				writer.flush();
				
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		});
		//chiusura del file
		writer.flush();
		writer.close();

		return lsFileName;
	}
}
