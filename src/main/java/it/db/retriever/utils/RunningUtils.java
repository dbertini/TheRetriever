package it.db.retriever.utils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.db.retriever.core.ApplicationContext;

/**
 * Classe di utilità dove sono presenti metodi di utility 
 * per il runtime: debug, log, ecc ecc
 * @author D.Bertini
 *
 */
public class RunningUtils {
	
	
	
	/**
	 * 
	 * Metodo che genera il report con tutti i 
	 * datasource ed i report attivi in un dato
	 * momento.
	 * 
	 */
	public static void writeRunningReport() {
		try {
			//Apertura del workbook di Excell
			XSSFWorkbook wb = new XSSFWorkbook();
			//creazione del foglio di lavoro per i report
			XSSFSheet activeReportSheet = wb.createSheet("Active Report");
			
			//creazione dello stile per le celle di tipo testata
			XSSFCellStyle testataStyle = (XSSFCellStyle) wb.createCellStyle();
			testataStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(192,192,192)));
			testataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			testataStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			testataStyle.setBorderBottom(BorderStyle.MEDIUM);
			testataStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			testataStyle.setBorderTop(BorderStyle.MEDIUM);
			testataStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			testataStyle.setBorderLeft(BorderStyle.MEDIUM);
			testataStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			testataStyle.setBorderRight(BorderStyle.MEDIUM);
			
			
			XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
			cellStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			cellStyle.setBorderBottom(BorderStyle.MEDIUM);
			cellStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			cellStyle.setBorderTop(BorderStyle.MEDIUM);
			cellStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			cellStyle.setBorderLeft(BorderStyle.MEDIUM);
			cellStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(0,0,0)));
			cellStyle.setBorderRight(BorderStyle.MEDIUM);
			
			//aggiungo la prima riga del report
			XSSFRow row = activeReportSheet.createRow(0);
			
			XSSFCell nomeDelReportCell = row.createCell(0);
		    XSSFCell descrizioneCell = row.createCell(1);
		    XSSFCell cronCell = row.createCell(2);
		    XSSFCell lastStartExecutionCell = row.createCell(3);
		    XSSFCell lastStopExecutionCell = row.createCell(4);
		    
		    //cella per il nome del report
		    nomeDelReportCell.setCellStyle(testataStyle);
		    nomeDelReportCell.setCellValue("Nome del report");
		    //cella per la descrizione del report
		    descrizioneCell.setCellStyle(testataStyle);
		    descrizioneCell.setCellValue("Descrizione");
		    //cella per il crontab del report
		    cronCell.setCellStyle(testataStyle);
		    cronCell.setCellValue("Schedulazione");
		    //Ultima esecuzione del report
		    lastStartExecutionCell.setCellStyle(testataStyle);
		    lastStartExecutionCell.setCellValue("Data inizio ultima Esecuzione");
		    //fine dell'utlima esecuzione del report
		    lastStopExecutionCell.setCellStyle(testataStyle);
		    lastStopExecutionCell.setCellValue("Data fine ultima Esecuzione");

		    int[] idx = { 1 };
		    //si scorrono tutti i report attivi e si aggiorna il foglio dei report
		    ApplicationContext.INSTANCE.getReports().stream().forEach( report -> {
		    	XSSFRow lrow = activeReportSheet.createRow(idx[0]);
		    	
		    	XSSFCell lNomeDelReportCell = lrow.createCell(0);
			    XSSFCell lDescrizioneCell = lrow.createCell(1);
			    XSSFCell lCronCell = lrow.createCell(2);
			    XSSFCell lLastStartExecutionCell = lrow.createCell(3);
			    XSSFCell lLastStopExecutionCell = lrow.createCell(4);
			    
			    
			    lNomeDelReportCell.setCellValue(report.getName());
			    lDescrizioneCell.setCellValue(report.getDescription());
			    lCronCell.setCellValue(report.getCron());
			    
			    if(report.getLastStartTime() != null) {
			    	SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			    	lLastStartExecutionCell.setCellValue(fmt.format(report.getLastStartTime()));
			    } else {
			    	lLastStartExecutionCell.setCellValue("NO-RUNS");
			    }
			    
			    if(report.getLastStopTime() != null) {
			    	SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			    	lLastStopExecutionCell.setCellValue(fmt.format(report.getLastStopTime()));
			    } else {
			    	lLastStopExecutionCell.setCellValue("NO-RUNS");
			    }

			    lNomeDelReportCell.setCellStyle(cellStyle);
			    lDescrizioneCell.setCellStyle(cellStyle);
			    lCronCell.setCellStyle(cellStyle);
			    lLastStartExecutionCell.setCellStyle(cellStyle);
			    lLastStopExecutionCell.setCellStyle(cellStyle);
			    
		    	idx[0]++;
		    });
		    
		    //faccio l'auto size delle colonne del foglio
		    activeReportSheet.autoSizeColumn(0);
		    activeReportSheet.autoSizeColumn(1);
		    activeReportSheet.autoSizeColumn(2);
		    activeReportSheet.autoSizeColumn(3);
		    activeReportSheet.autoSizeColumn(4);
		    
		    /* ********************************************************************* */
		    //si passa adesso alla creazione del foglio con tutti i datasource attivi
		    //creazione del foglio di lavoro per i report
		    XSSFSheet activeDataSourcesSheet = wb.createSheet("Active DataSources");
		    
		    //aggiungo la prima riga del report
			XSSFRow rowds = activeDataSourcesSheet.createRow(0);
			
			XSSFCell nomeDSCell = rowds.createCell(0);
		    XSSFCell descrizioneDSCell = rowds.createCell(1);
		    
		    nomeDSCell.setCellStyle(testataStyle);
		    nomeDSCell.setCellValue("Nome DataSource");
		    
		    descrizioneDSCell.setCellStyle(testataStyle);
		    descrizioneDSCell.setCellValue("Descrizione DataSource");
		    
			
		    int[] idxds = { 1 };
		    //scorro tutti i datasource attivi
		    ApplicationContext.INSTANCE.getDataSources().stream().forEach(ds -> {
		    	XSSFRow lrow = activeDataSourcesSheet.createRow(idxds[0]);
		    	
		    	XSSFCell lNomeDSCell = lrow.createCell(0);
			    XSSFCell lDescrizioneDSCell = lrow.createCell(1);

			    lNomeDSCell.setCellValue(ds.getName());
			    lDescrizioneDSCell.setCellValue(ds.getDescription());

			    lNomeDSCell.setCellStyle(cellStyle);
			    lDescrizioneDSCell.setCellStyle(cellStyle);

			    idxds[0]++;
		    });
		    
		    //faccio l'auto size delle colonne del foglio
		    activeDataSourcesSheet.autoSizeColumn(0);
		    activeDataSourcesSheet.autoSizeColumn(1);
		    
		    
		    
		    /* ********************************************************************* */
		    //si passa adesso alla creazione del foglio con tutti i template attivi
		    //creazione del foglio di lavoro per i template
		    XSSFSheet activeTemplatesSheet = wb.createSheet("Active Templates");
		    
		    //aggiungo la prima riga del report
			XSSFRow rowtp = activeTemplatesSheet.createRow(0);

			XSSFCell nomeTpCell = rowtp.createCell(0);

			nomeTpCell.setCellStyle(testataStyle);
			nomeTpCell.setCellValue("Nome Template");
		    int[] idxtp = { 1 };
		    //scorro tutti i datasource attivi
		    ApplicationContext.INSTANCE.getTemplates().stream().forEach(tp -> {
		    	XSSFRow lrow = activeTemplatesSheet.createRow(idxtp[0]);
		    	XSSFCell lNomeTPCell = lrow.createCell(0);
		    	lNomeTPCell.setCellValue(tp.getName());
			    lNomeTPCell.setCellStyle(cellStyle);
			    idxtp[0]++;
		    });
		    
		    //faccio l'auto size delle colonne del foglio
		    activeTemplatesSheet.autoSizeColumn(0);

		    
		    
		    //scrivo il file, sovrascrivendo il vecchio oppure
		    //creandone uno nuovo
		    FileOutputStream out = new FileOutputStream(StandardParameter.REPORT_FILE);
		    wb.write(out);
		    out.close();
		    wb.close();
			
		} catch(Exception a) {
			LogManager.getLogger(RunningUtils.class).warn("Errore durante la creazione del file report " + StandardParameter.REPORT_FILE);
			LogManager.getLogger(RunningUtils.class).warn(a);
		}
	}

}
