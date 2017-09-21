package it.db.retriever.exporter;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.configuration.entity.QueryResponse;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.templates.Coloumn;
import it.db.retriever.templates.Template;
import it.db.retriever.utils.StandardParameter;
import it.db.retriever.utils.StringUtils;
/**
 * Exporter per la formattazione Excel secondo un template
 * @author D.Bertini
 *
 */
public class ExcelExporter implements ExportInterface {

	private String fileName = "";

	@Override
	public String export(QueryResponse aQueryResponse, Report aReport) throws Exception {
		// creazione del nome del file nella directory
		this.fileName = StandardParameter.TEMP_EXPORT_PATH + generateFileName() + ".xlsx";

		Template template = null;
		if (aReport.getTemplate() != null && !aReport.getTemplate().trim().equalsIgnoreCase("")) {
			if (ApplicationContext.INSTANCE.isTemplatePresent(aReport.getTemplate().trim())) {
				template = ApplicationContext.INSTANCE.getTemplate(aReport.getTemplate().trim());
			}
		}

		if (template == null) {
			//non ho trovato un template associato
			//quindi si formatta l'excel con tutti i valori
			//di default
			buildDefaultTemplate(aQueryResponse);
		} else {
			//ho trovato un template, quindi applico i
			//valori del template scelto
			buildTemplatedReport(aQueryResponse, template);
		}
		return this.fileName;
	}

	/**
	 * Metodo che costruisce un file excel utilizzando un template dei default
	 * 
	 * @param aQueryResponse oggetto {@link QueryResponse} con il risultato dell'esecuzione della query
	 * @throws Exception Eccezione generica
	 */
	private void buildDefaultTemplate(QueryResponse aQueryResponse) throws Exception {

		// Apertura del workbook di Excell
		XSSFWorkbook wb = new XSSFWorkbook();
		// creazione del foglio di lavoro per i report
		XSSFSheet sheet = wb.createSheet("Report");

		// creazione dello stile per le celle di tipo testata
		XSSFCellStyle testataStyle = (XSSFCellStyle) wb.createCellStyle();
		testataStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(192, 192, 192)));
		testataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		testataStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		testataStyle.setBorderBottom(BorderStyle.MEDIUM);
		testataStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		testataStyle.setBorderTop(BorderStyle.MEDIUM);
		testataStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		testataStyle.setBorderLeft(BorderStyle.MEDIUM);
		testataStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		testataStyle.setBorderRight(BorderStyle.MEDIUM);

		XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
		cellStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		cellStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		cellStyle.setBorderLeft(BorderStyle.MEDIUM);
		cellStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
		cellStyle.setBorderRight(BorderStyle.MEDIUM);

		// aggiungo la prima riga del report
		XSSFRow hrow = sheet.createRow(0);

		int count[] = { 0 };

		aQueryResponse.getLabels().stream().forEachOrdered(label -> {
			XSSFCell hcell = hrow.createCell(count[0]);
			hcell.setCellStyle(testataStyle);
			hcell.setCellValue(label);
			count[0]++;
		});

		int rowCount[] = { 1 };
		aQueryResponse.getRows().stream().forEachOrdered(row -> {

			XSSFRow rrow = sheet.createRow(rowCount[0]);

			int rowSize = row.getColoumn().size();
			for (int i = 0; i < rowSize; i++) {

				XSSFCell rcell = rrow.createCell(i);
				rcell.setCellStyle(cellStyle);
				try {
					// controllo i tipi delle colonne per effettuare il corretto cast
					if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.NUMERIC) {
						if (row.getColoumn().get(i) != null) {
							BigDecimal bd = (BigDecimal) row.getColoumn().get(i);
							rcell.setCellValue(bd.doubleValue());
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.INTEGER) {
						if (row.getColoumn().get(i) != null) {
							BigInteger in = (BigInteger) row.getColoumn().get(i);
							rcell.setCellValue(in.intValue());
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.VARCHAR) {

						if (row.getColoumn().get(i) != null) {
							rcell.setCellValue((String) row.getColoumn().get(i));
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.TIMESTAMP) {

						if (row.getColoumn().get(i) != null) {
							rcell.setCellValue(new Date(((Timestamp) row.getColoumn().get(i)).getTime()));
							XSSFCellStyle t = (XSSFCellStyle) cellStyle.clone();
							XSSFCreationHelper createHelper = wb.getCreationHelper();
							t.setDataFormat(createHelper.createDataFormat().getFormat(StandardParameter.TIMESTAMP_FORMAT));
							rcell.setCellStyle(t);
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.DATE) {
						if (row.getColoumn().get(i) != null) {
							rcell.setCellValue(new Date(((java.sql.Date) row.getColoumn().get(i)).getTime()));
							XSSFCellStyle t = (XSSFCellStyle) cellStyle.clone();
							XSSFCreationHelper createHelper = wb.getCreationHelper();
							t.setDataFormat(createHelper.createDataFormat().getFormat(StandardParameter.DATA_FORMAT));
							rcell.setCellStyle(t);
						} else {
							rcell.setCellValue("");
						}
					} else {
						if (row.getColoumn().get(i) != null) {
							rcell.setCellValue((String) row.getColoumn().get(i));
						} else {
							rcell.setCellValue("");
						}
					}
				} catch (Exception ex) {
					rcell.setCellValue("");
					LogManager.getLogger(CsvExporter.class).warn(ex);
				}
			}
			rowCount[0]++;
		});
		// settaggio dell'autosize delle colonne
		for (int i = 0; i < count[0]; i++) {
			sheet.autoSizeColumn(i);
		}
		// scrivo il file, sovrascrivendo il vecchio oppure
		// creandone uno nuovo
		FileOutputStream out = new FileOutputStream(this.fileName);
		wb.write(out);
		out.close();
		wb.close();
	}

	/**
	 * 
	 * Metodo per creare un export excel templatizzato
	 * 
	 * @param aQueryResponse oggetto {@link QueryResponse} con il risultato dell'esecuzione della query
	 * @param aTemplate oggetto {@link Template} con le configurazioni per l'export
	 * 
	 * @throws Exception Eccezione generica
	 */
	private void buildTemplatedReport(QueryResponse aQueryResponse, Template aTemplate) throws Exception {
		// Apertura del workbook di Excell
		XSSFWorkbook wb = new XSSFWorkbook();
		// creazione del foglio di lavoro per i report
		XSSFSheet sheet = wb.createSheet("Report");
		int rowcount[] = { 0 };

		// si controlla se è stato inserito un titolo per il report
		if (aTemplate.getTitle() != null && !aTemplate.getTitle().trim().equalsIgnoreCase("")) {
			// creo una riga
			XSSFRow row = sheet.createRow(rowcount[0]);
			// creo una cella
			XSSFCell cell = row.createCell(0);
			// aggiungo il titolo alla cella
			cell.setCellValue(aTemplate.getTitle().trim());

			//si controlla se è stato scelto un colore di background per il titolo
			if ((aTemplate.getTitleBkgColor() != null && !aTemplate.getTitleBkgColor().trim().equalsIgnoreCase(""))
					|| (aTemplate.getTitleTextColor() != null
							&& !aTemplate.getTitleTextColor().trim().equalsIgnoreCase(""))) {

				XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();

				if (aTemplate.getTitleBkgColor() != null && !aTemplate.getTitleBkgColor().trim().equalsIgnoreCase("")) {
					//setting del colore di backgrouond del titolo
					style.setFillForegroundColor(
							new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getTitleBkgColor().trim())));
					style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				}

				if (aTemplate.getTitleTextColor() != null
						&& !aTemplate.getTitleTextColor().trim().equalsIgnoreCase("")) {
					//setting del colore del testo del titolo
					XSSFFont font = wb.createFont();
					font.setColor(
							new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getTitleTextColor().trim())));
					style.setFont(font);
				}
				// aggiungo lo style appena creato
				cell.setCellStyle(style);
			}

			// aggiorno il numero della riga
			rowcount[0]++;
		}

		// Si passa all'inserimento degli header delle colonne
		XSSFCellStyle headerStyle = (XSSFCellStyle) wb.createCellStyle();
		// con gli eventuali settaggi di default creo lo style della testata
		// si crea il background color
		if (aTemplate.getHeaders().getBkgColor() != null && !aTemplate.getHeaders().getBkgColor().trim().equalsIgnoreCase("")) {
			headerStyle.setFillForegroundColor(new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getHeaders().getBkgColor().trim())));
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		// si crea lo style per il colore del testo
		if (aTemplate.getHeaders().getTextColor() != null && !aTemplate.getHeaders().getTextColor().trim().equalsIgnoreCase("")) {
			XSSFFont font = wb.createFont();
			font.setColor(new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getHeaders().getTextColor().trim())));
			headerStyle.setFont(font);
		}
		
		//si controlla il fatto che sia stato scelto un allineamento verticale generico
		if( aTemplate.getHeaders().getValign() != null && !aTemplate.getHeaders().getValign().isEmpty() ) {
			//applico l'allineamento verticale
			headerStyle.setVerticalAlignment(VerticalAlignment.valueOf(aTemplate.getHeaders().getValign().get(0).trim().toUpperCase()));
		}

		//si controlla il fatto che sia stato scelto un allineamento orizzontale generico
		if( aTemplate.getHeaders().getHalign() != null && !aTemplate.getHeaders().getHalign().isEmpty() ) {
			//applico l'allineamento orizzontale
			headerStyle.setAlignment(HorizontalAlignment.valueOf(aTemplate.getHeaders().getHalign().get(0).trim().toUpperCase()));
		}
		
		//si controlla se è stato scelto di default si riquadrare le celle di header
		if(aTemplate.getHeaders().getBoxed() != null && aTemplate.getHeaders().getBoxed().getValue() != null && !aTemplate.getHeaders().getBoxed().getValue().trim().equalsIgnoreCase("")) {
			if( aTemplate.getHeaders().getBoxed().getValue().trim().equalsIgnoreCase("Y") || aTemplate.getHeaders().getBoxed().getValue().trim().equalsIgnoreCase("S")) {
				//si è scelto di riquadrare di default le celle di testata
				
				//aggiunta dello stile sul tipo do bordo
				if( aTemplate.getHeaders().getBoxed().getBorder() !=null && !aTemplate.getHeaders().getBoxed().getBorder().isEmpty()) {
					//si è scelto uno specifico tipo di bordo
					headerStyle.setBorderBottom(BorderStyle.valueOf(aTemplate.getHeaders().getBoxed().getBorder().get(0).trim().toUpperCase()));
					headerStyle.setBorderTop(BorderStyle.valueOf(aTemplate.getHeaders().getBoxed().getBorder().get(0).trim().toUpperCase()));
					headerStyle.setBorderLeft(BorderStyle.valueOf(aTemplate.getHeaders().getBoxed().getBorder().get(0).trim().toUpperCase()));
					headerStyle.setBorderRight(BorderStyle.valueOf(aTemplate.getHeaders().getBoxed().getBorder().get(0).trim().toUpperCase()));
				} else {
					//non è stata scelta lo specifico tipo di bordo e si
					//applica quello di default
					headerStyle.setBorderBottom(BorderStyle.THIN);
					headerStyle.setBorderTop(BorderStyle.THIN);
					headerStyle.setBorderLeft(BorderStyle.THIN);
					headerStyle.setBorderRight(BorderStyle.THIN);
				}
				
				//controllo sul colore del bordo
				if( aTemplate.getHeaders().getBoxed().getColor() != null && !aTemplate.getHeaders().getBoxed().getColor().trim().equalsIgnoreCase("")) {
					//se scelto si associa un colore del bordo
					headerStyle.setBottomBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getHeaders().getBoxed().getColor().trim())));
					headerStyle.setTopBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getHeaders().getBoxed().getColor().trim())));
					headerStyle.setLeftBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getHeaders().getBoxed().getColor().trim())));
					headerStyle.setRightBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(aTemplate.getHeaders().getBoxed().getColor().trim())));	
				} else {
					//non si è scelto, quindi si aggiunge quello di default
					headerStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
					headerStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
					headerStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
					headerStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));	
				}
			}
		}
		

		if (aTemplate.getHeaders().getHeadersDefinition() == null ||
			aTemplate.getHeaders().getHeadersDefinition().getHeaderDefinition() == null ||
			aTemplate.getHeaders().getHeadersDefinition().getHeaderDefinition().isEmpty()) {
			// non sono state inserite definizioni particolari
			// quindi si utilizzano quelle generiche eventualmente create sopra

			// creo una riga
			XSSFRow row = sheet.createRow(rowcount[0]);

			int colcount[] = { 0 };
			aQueryResponse.getLabels().stream().forEachOrdered(label -> {
				XSSFCell hcell = row.createCell(colcount[0]);
				hcell.setCellStyle(headerStyle);
				hcell.setCellValue(label);
				colcount[0]++;
			});
			rowcount[0]++;
		} else {
			// creo una riga
			XSSFRow row = sheet.createRow(rowcount[0]);

			int colcount[] = { 0 };
			aQueryResponse.getLabels().stream().forEachOrdered(label -> {

				// faccio il settaggio dei valori di default
				XSSFCell hcell = row.createCell(colcount[0]);
				hcell.setCellStyle(headerStyle);
				hcell.setCellValue(label);

				aTemplate.getHeaders().getHeadersDefinition().getHeaderDefinition().parallelStream().forEach(hdef -> {
					// se ho trovato la configurazione per una specifica colonna
					// creo lo style nuovo
					if (hdef.getName().equalsIgnoreCase(label)) {
						XSSFCellStyle lStyle = (XSSFCellStyle) headerStyle.clone();
						if (hdef.getBkgColor() != null && !hdef.getBkgColor().trim().equalsIgnoreCase("")) {
							lStyle.setFillForegroundColor(
									new XSSFColor(StringUtils.getColorFromRGBString(hdef.getBkgColor().trim())));
							lStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						}
						// si crea lo style per il colore del testo
						if (hdef.getTextColor() != null && !hdef.getTextColor().trim().equalsIgnoreCase("")) {
							XSSFFont font = wb.createFont();
							font.setColor(new XSSFColor(StringUtils.getColorFromRGBString(hdef.getTextColor().trim())));
							lStyle.setFont(font);
						}

						//si controlla il fatto che sia stato scelto un allineamento verticale 
						if( hdef.getValign() != null && !hdef.getValign().isEmpty() ) {
							//applico l'allineamento verticale
							lStyle.setVerticalAlignment(VerticalAlignment.valueOf(hdef.getValign().get(0).trim().toUpperCase()));
						}

						//si controlla il fatto che sia stato scelto un allineamento orizzontale 
						if( hdef.getHalign() != null && !hdef.getHalign().isEmpty() ) {
							//applico l'allineamento orizzontale
							lStyle.setAlignment(HorizontalAlignment.valueOf(hdef.getHalign().get(0).trim().toUpperCase()));
						}
						
						//si controlla se è stato scelto di default si riquadrare le celle di header
						if(hdef.getBoxed() != null && hdef.getBoxed().getValue() != null && !hdef.getBoxed().getValue().trim().equalsIgnoreCase("")) {
							if( hdef.getBoxed().getValue().trim().equalsIgnoreCase("Y") || hdef.getBoxed().getValue().trim().equalsIgnoreCase("S")) {
								//si è scelto di riquadrare le celle di testata
								
								//aggiunta dello stile sul tipo do bordo
								if(hdef.getBoxed().getBorder() !=null && !hdef.getBoxed().getBorder().isEmpty()) {
									//si è scelto uno specifico tipo di bordo
									lStyle.setBorderBottom(BorderStyle.valueOf(hdef.getBoxed().getBorder().get(0).trim().toUpperCase()));
									lStyle.setBorderTop(BorderStyle.valueOf(hdef.getBoxed().getBorder().get(0).trim().toUpperCase()));
									lStyle.setBorderLeft(BorderStyle.valueOf(hdef.getBoxed().getBorder().get(0).trim().toUpperCase()));
									lStyle.setBorderRight(BorderStyle.valueOf(hdef.getBoxed().getBorder().get(0).trim().toUpperCase()));
								} else {
									//non è stata scelta lo specifico tipo di bordo e si
									//applica quello di default
									lStyle.setBorderBottom(BorderStyle.THIN);
									lStyle.setBorderTop(BorderStyle.THIN);
									lStyle.setBorderLeft(BorderStyle.THIN);
									lStyle.setBorderRight(BorderStyle.THIN);
								}
								
								//controllo sul colore del bordo
								if( hdef.getBoxed().getColor() != null && !hdef.getBoxed().getColor().trim().equalsIgnoreCase("")) {
									//se scelto si associa un colore del bordo
									lStyle.setBottomBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(hdef.getBoxed().getColor().trim())));
									lStyle.setTopBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(hdef.getBoxed().getColor().trim())));
									lStyle.setLeftBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(hdef.getBoxed().getColor().trim())));
									lStyle.setRightBorderColor(new XSSFColor(StringUtils.getColorFromRGBString(hdef.getBoxed().getColor().trim())));	
								} else {
									//non si è scelto, quindi si aggiunge quello di default
									lStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));	
								}
							} else {
								//si è scelto di rimuovere la riquadratura
								lStyle.setBorderBottom(BorderStyle.NONE);
								lStyle.setBorderTop(BorderStyle.NONE);
								lStyle.setBorderLeft(BorderStyle.NONE);
								lStyle.setBorderRight(BorderStyle.NONE);
							}
						}

						// settaggio dell'eventuale nuovo titolo definito
						if (hdef.getHeading() != null && !hdef.getHeading().trim().equalsIgnoreCase(""))
							hcell.setCellValue(hdef.getHeading());

						// settaggio del nuovo stile
						hcell.setCellStyle(lStyle);
					}
				});
				colcount[0]++;
			});
			rowcount[0]++;
		}

		// scrittura delle righe dei dati trovati
		XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();

		aQueryResponse.getRows().stream().forEachOrdered(row -> {

			XSSFRow rrow = sheet.createRow(rowcount[0]);

			int rowSize = row.getColoumn().size();
			for (int i = 0; i < rowSize; i++) {

				XSSFCell rcell = rrow.createCell(i);
				rcell.setCellStyle(cellStyle);
				try {
					//si ricerca una configurazione particolare per la colonna che stiamo analizzando
					Coloumn colDef = null;
					if (aTemplate.getColoumns() != null &&
						aTemplate.getColoumns().getColoumn() != null &&
						!aTemplate.getColoumns().getColoumn().isEmpty()) {
						int labelpos[] = { i };
						colDef = aTemplate.getColoumns().getColoumn().stream().filter(
								x -> x.getName().trim().equalsIgnoreCase(aQueryResponse.getLabels().get(labelpos[0])))
								.findAny().orElse(null);
					}
					//se ho trovato una definizione vado a sistemare lo style
					if(colDef!=null) {
						XSSFCellStyle lstyle = (XSSFCellStyle)cellStyle.clone();
						//si setta l'eventuale colore di sfondo
						if (colDef.getBkgColor() != null && !colDef.getBkgColor().trim().equalsIgnoreCase("")) {
							lstyle.setFillForegroundColor(new XSSFColor(StringUtils.getColorFromRGBString(colDef.getBkgColor().trim())));
							lstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						}
						// si crea lo style per il colore del testo
						if (colDef.getTextColor() != null && !colDef.getTextColor().trim().equalsIgnoreCase("")) {
							XSSFFont font = wb.createFont();
							font.setColor(new XSSFColor(StringUtils.getColorFromRGBString(colDef.getTextColor().trim())));
							lstyle.setFont(font);
						}
						
						//se settato inserisco l'allineamento verticale della cella
						if(colDef.getHalign() != null && !colDef.getHalign().isEmpty()) {
							try {
								lstyle.setAlignment(HorizontalAlignment.valueOf(colDef.getHalign().get(0).trim().toUpperCase()));
							} catch (Exception e) {
								LogManager.getLogger(ExcelExporter.class).warn("Errore durante la formattazione del report per il horizzontal align");
							}
						}
						//se settato inserisco l'allineamento orizzontale della cella
						if(colDef.getValign() != null && !colDef.getValign().isEmpty()) {
							try {
								lstyle.setVerticalAlignment(VerticalAlignment.valueOf(colDef.getValign().get(0).trim().toUpperCase()));
							} catch (Exception e) {
								LogManager.getLogger(ExcelExporter.class).warn("Errore durante la formattazione del report per il vertical align");
							}
						}
						
						//controllo il fatto che sia stato scelto di riquadrare la cella
						if(colDef.getBoxed() != null && colDef.getBoxed().getValue() != null &&  !colDef.getBoxed().getValue().trim().equalsIgnoreCase("")) {
							
							
							if(colDef.getBoxed().getValue().trim().equalsIgnoreCase("Y") || colDef.getBoxed().getValue().trim().equalsIgnoreCase("S")) {
								//dato che è stato scelto di riquadrare la cella controllo il fatto che sia stato scelto o meno
								//un tipo preciso di riquadratura
								if(colDef.getBoxed().getBorder() != null && !colDef.getBoxed().getBorder().isEmpty()) {
									//colore di defuale
									java.awt.Color color = new java.awt.Color(0,0,0);
									
									if(colDef.getBoxed().getColor() != null  && !colDef.getBoxed().getColor().trim().equalsIgnoreCase("")) {
										//è stato scelto di utilizzare un colore specifico per i bordi
										color = StringUtils.getColorFromRGBString(colDef.getBoxed().getColor().trim());
									}
									//è stato scelto uno specifico tipo di riquadratura
									lstyle.setBottomBorderColor(new XSSFColor(color));
									lstyle.setBorderBottom(BorderStyle.valueOf(colDef.getBoxed().getBorder().get(0).trim().toUpperCase()));
									lstyle.setTopBorderColor(new XSSFColor(color));
									lstyle.setBorderTop(BorderStyle.valueOf(colDef.getBoxed().getBorder().get(0).trim().toUpperCase()));
									lstyle.setLeftBorderColor(new XSSFColor(color));
									lstyle.setBorderLeft(BorderStyle.valueOf(colDef.getBoxed().getBorder().get(0).trim().toUpperCase()));
									lstyle.setRightBorderColor(new XSSFColor(color));
									lstyle.setBorderRight(BorderStyle.valueOf(colDef.getBoxed().getBorder().get(0).trim().toUpperCase()));
								} else {
									//utilizziamo la riquadratura di default
									lstyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lstyle.setBorderBottom(BorderStyle.THIN);
									lstyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lstyle.setBorderTop(BorderStyle.THIN);
									lstyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lstyle.setBorderLeft(BorderStyle.THIN);
									lstyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
									lstyle.setBorderRight(BorderStyle.THIN);
								}
							} else {
								lstyle.setBorderBottom(BorderStyle.NONE);
								lstyle.setBorderTop(BorderStyle.NONE);
								lstyle.setBorderLeft(BorderStyle.NONE);
								lstyle.setBorderRight(BorderStyle.NONE);
							}
						}

						//setto il nuovo stile sulla cella
						rcell.setCellStyle(lstyle);
					}

					// controllo i tipi delle colonne per effettuare il corretto cast
					if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.NUMERIC) {
						if (row.getColoumn().get(i) != null) {
							
							//si controlla se è stato scelta una formattazione 
							//per il tipo di dato numerico
							if(colDef!=null && colDef.getFormat() !=null && !colDef.getFormat().trim().equalsIgnoreCase("")) {
								try {
									//si è scelta una formattazione per il numero
									//quindi si prova ad applicarla
									DecimalFormat myFormatter = new DecimalFormat(colDef.getFormat().trim());
									rcell.setCellValue(myFormatter.format(row.getColoumn().get(i)));
								} catch (Exception e) {
									LogManager.getLogger(ExcelExporter.class).warn("Errore durante la formattazione del valore numerico per il campo: " + aQueryResponse.getLabels().get(i) + " valore utilizzato: " + colDef.getFormat().trim());
									LogManager.getLogger(ExcelExporter.class).warn(e);
									//in caso di formattazione non corretta
									//inserisco il valore come normale numero
									BigDecimal bd = (BigDecimal) row.getColoumn().get(i);
									rcell.setCellValue(bd.doubleValue());
								}
							} else {
								//la formattazione non è stata scelta, quindi 
								//si inserisce come normale numero
								BigDecimal bd = (BigDecimal) row.getColoumn().get(i);
								rcell.setCellValue(bd.doubleValue());
							}
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.INTEGER) {
						if (row.getColoumn().get(i) != null) {
							
							//si controlla se è stato scelta una formattazione 
							//per il tipo di dato numerico
							if(colDef!=null && colDef.getFormat() !=null && !colDef.getFormat().trim().equalsIgnoreCase("")) {
								try {
									//si è scelta una formattazione per il numero
									//quindi si prova ad applicarla
									DecimalFormat myFormatter = new DecimalFormat(colDef.getFormat().trim());
									rcell.setCellValue(myFormatter.format(row.getColoumn().get(i)));
								} catch (Exception e) {
									LogManager.getLogger(ExcelExporter.class).warn("Errore durante la formattazione del valore numerico per il campo: " + aQueryResponse.getLabels().get(i) + " valore utilizzato: " + colDef.getFormat().trim());
									LogManager.getLogger(ExcelExporter.class).warn(e);
									//in caso di formattazione non corretta
									//inserisco il valore come normale numero
									BigInteger in = (BigInteger) row.getColoumn().get(i);
									rcell.setCellValue(in.intValue());
								}
							} else {
								//la formattazione non è stata scelta, quindi 
								//si inserisce come normale numero
								BigInteger in = (BigInteger) row.getColoumn().get(i);
								rcell.setCellValue(in.intValue());
							}
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.VARCHAR) {

						if (row.getColoumn().get(i) != null) {
							rcell.setCellValue((String) row.getColoumn().get(i));
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.TIMESTAMP) {

						if (row.getColoumn().get(i) != null) {
							//inserisco il valore della cella
							rcell.setCellValue(new Date(((Timestamp) row.getColoumn().get(i)).getTime()));
							//clonazione dello stile per applicare le modifiche solo
							//allo stile della specifica cella
							XSSFCellStyle t = (XSSFCellStyle) rcell.getCellStyle().clone();
							//inizializzazione dell'oggetto per formattare le celle
							XSSFCreationHelper createHelper = wb.getCreationHelper();
							
							if(colDef!=null && colDef.getFormat() !=null && !colDef.getFormat().trim().equalsIgnoreCase(""))
								t.setDataFormat(createHelper.createDataFormat().getFormat(colDef.getFormat().trim()));//applico la formattazione scelta dall'utente
							else
								t.setDataFormat(createHelper.createDataFormat().getFormat(StandardParameter.TIMESTAMP_FORMAT));//applico la formattazione di default
							//applico alla sola cella il nuovo stile
							rcell.setCellStyle(t);
						} else {
							rcell.setCellValue("");
						}
					} else if (aQueryResponse.getColoumnType().get(i) == java.sql.Types.DATE) {
						if (row.getColoumn().get(i) != null) {
							//inserisco il valore della cella
							rcell.setCellValue(new Date(((java.sql.Date) row.getColoumn().get(i)).getTime()));
							//clonazione dello stile per applicare le modifiche solo
							//allo stile della specifica cella
							XSSFCellStyle t = (XSSFCellStyle) rcell.getCellStyle().clone();
							//inizializzazione dell'oggetto per formattare le celle
							XSSFCreationHelper createHelper = wb.getCreationHelper();

							if(colDef!=null && colDef.getFormat() !=null && !colDef.getFormat().trim().equalsIgnoreCase(""))
								t.setDataFormat(createHelper.createDataFormat().getFormat(colDef.getFormat().trim()));//applico la formattazione scelta dall'utente
							else
								t.setDataFormat(createHelper.createDataFormat().getFormat(StandardParameter.DATA_FORMAT));//applico la formattazione di default

							//applico alla sola cella il nuovo stile
							rcell.setCellStyle(t);
						} else {
							rcell.setCellValue("");
						}
					} else {
						if (row.getColoumn().get(i) != null) {
							rcell.setCellValue((String) row.getColoumn().get(i));
						} else {
							rcell.setCellValue("");
						}
					}
				} catch (Exception ex) {
					rcell.setCellValue("");//in caso di qualsiasi errore metto la cella vuota
					LogManager.getLogger(ExcelExporter.class).warn(ex);
				}
			}
			rowcount[0]++;
		});

		// settaggio dell'autosize delle colonne
		for (int i = 0; i < aQueryResponse.getLabels().size(); i++) {
			sheet.autoSizeColumn(i);
		}

		// scrivo il file, sovrascrivendo il vecchio oppure
		// creandone uno nuovo
		FileOutputStream out = new FileOutputStream(this.fileName);
		wb.write(out);
		out.close();
		wb.close();
	}
}