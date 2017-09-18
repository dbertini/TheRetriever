package it.db.retriever.exporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.QueryResponse;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.utils.StandardParameter;

/**
 * Classe per effettuare l'export in formato XML
 * La struttura dell'XML generato sarà simile alla seguete: <br>
 * {@code <REPORT>
 * 	 <COL_1>value</COL_1>
 *   ...
 *   ...
 *   <COL_N>value</COL_N>
 * </REPORT>}
 * 
 * 
 * @author D.Bertini
 *
 */
public class XmlExport implements ExportInterface {

	/* (non-Javadoc)
	 * @see it.bcc.sinergia.retriever.exporter.ExportInterface#export(it.bcc.sinergia.retriever.core.configuration.entity.QueryResponse)
	 */
	@Override
	public String export(QueryResponse aQueryResponse, Report aReport) throws Exception {
		//creazione del nome del file nella directory
		String lsFileName = StandardParameter.TEMP_EXPORT_PATH + generateFileName() + ".xml";
		
		//apertura dello stream per la scrittura del file
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(lsFileName));
		
		//scrittura del primo TAG XML
		writer.write("<REPORT>");
		writer.write("\n");
		writer.flush();
		
		//Adesso si crea il template di ogni riga XML
		StringBuffer xml = new StringBuffer();
		int[] idx = { 0 };
		aQueryResponse.getLabels().stream().forEachOrdered(label -> {
			//la label prende il posto nel nome del tag
			xml.append("<"+label.toUpperCase()+">{"+idx[0]+"}</"+label.toUpperCase()+">");
			idx[0]++;
		});
		
		LogManager.getLogger(XmlExport.class).debug("XML creato: " + xml.toString());
		
		//per ogni riga trovata faccio il parsing tra gli oggetti
		//ed il template creato prima
		aQueryResponse.getRows().stream().forEachOrdered(row -> {
			try {
				writer.write("<ROW>" + MessageFormat.format(xml.toString(), row.getColoumn().toArray()) + "</ROW>");
				writer.write("\n");
			} catch (IOException e) {
				LogManager.getLogger(XmlExport.class).error(e);
			}
		});
		//scrittura del TAG di chiusura del report
		writer.write("</REPORT>");
		//chiusura del file
		writer.flush();
		writer.close();
		return lsFileName;
	}

}
