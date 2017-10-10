package it.db.retriever.exporter;

/**
 * Enumeration con i tipi di export che si possono utilizzare
 * L'enum contiene anche la classe da utilizzare per il relativo tipo
 * di Export da effetture 
 * 
 * @author D.Bertini
 *
 */
public enum ExportType {
	CSV("CSV","it.db.retriever.exporter.CsvExporter"), 
	XML("XML","it.db.retriever.exporter.XmlExport"),
	CSVEXCEL("CSVEXCEL","it.db.retriever.exporter.CsvExcelExporter"),
	EXCEL("EXCEL","it.db.retriever.exporter.ExcelExporter");

	private final String text;
	private final String classname;

	ExportType(final String text, String classname) {
		this.text = text;
		this.classname = classname;
	}

	@Override
	public String toString() {
		return text;
	}
	
	public String getClassName() {
		return classname;
	}
}
