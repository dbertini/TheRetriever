package it.db.retriever.reportutils;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.QueryManager;
import it.db.retriever.core.configuration.entity.QueryResponse;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.exporter.ExportInterface;
import it.db.retriever.exporter.ExportType;
import it.db.retriever.jobs.ReportJob;
import it.db.retriever.sender.ReportSenderInterface;
import it.db.retriever.utils.FileUtils;
import it.db.retriever.utils.RunningUtils;
import it.db.retriever.utils.StandardParameter;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 */
public class ReportUtil {

    /**
     * Metodo per eseguire un report
     *
     * @param aReport {@link Report} da eseguire
     */
    public static void runReport(Report aReport) throws Exception {

        //variabile per tenere in memoria il nome del file
        String fileName = "";
        LogManager.getLogger(ReportUtil.class).info("----------------------------------------------------------");

        //settaggio della data di inizio elaborazione
        Date today = new Date();
        aReport.setLastStartTime(today);
        //settaggio dell'ID univoco del report
        Random rdm = new Random();
        aReport.setExecutionId("" + today.getTime() + rdm.nextInt());
        //salvataggio dello stato INIT del report
        FileUtils.storeDataLog(aReport, StandardParameter.ReportStatus.INIT);
        LogManager.getLogger(ReportUtil.class).info("Running job " + aReport.getName());

        LogManager.getLogger(ReportUtil.class).info("Esecuzione della query per il report " + aReport.getName());
        // esecuzione dell'sql trovato nel report
        QueryResponse qr = QueryManager.executeSQL(aReport.getDatasource(), aReport.getSql());


        LogManager.getLogger(ReportUtil.class).info("Sono stati trovati " + qr.getRows().size() + " record per il report " + aReport.getName());

        // preparazione di tutti gli oggetti per l'export
        try {
            // recupero il tipo di export da effettuare
            ExportType type = ExportType.valueOf(aReport.getExport());
            // Creo runtime la classe dell'export
            LogManager.getLogger(ReportUtil.class).info("Estrazione di tipo " + type + " per il report " + aReport.getName());
            ExportInterface export = (ExportInterface) Class.forName(type.getClassName()).newInstance();
            // esecuzione dell'export
            fileName = export.export(qr, aReport);
            LogManager.getLogger(ReportUtil.class).info("File " + fileName + " generato per il report " + aReport.getName());

        } catch (Exception e) {
            LogManager.getLogger(ReportUtil.class).error(e);
            //salvataggio dell'errore durante l'esecuzione del report
            FileUtils.storeDataLog(aReport, StandardParameter.ReportStatus.ERROR);
            throw new Exception(e);
        }

        //si controlla la grandezza del file

        try {
            //controllo la dimensione del file
            if (!FileUtils.checkFileLength(fileName)) {

                LogManager.getLogger(ReportUtil.class).info("Il file " + fileName + " generato per il report "
                        + aReport.getName() + " supera i 4,5 Mb di dimensione, verrà quindi compresso.");

                //il file supera la dimensione di 4,5 MB
                //quindi si procede ad effettuare lo zip

                String zipFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
                byte[] buffer = new byte[1024];
                //creazione dello stream per il file zip
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(fos);

                LogManager.getLogger(ReportJob.class).info("Compressione del file " + fileName + " in corso.");
                ZipEntry ze = new ZipEntry(fileName);
                zos.putNextEntry(ze);
                //stream di lettura del file csv
                FileInputStream in = new FileInputStream(new File(fileName));
                int len;
                while ((len = in.read(buffer)) > 0) {
                    //scrittura del file all'interno dello zip
                    zos.write(buffer, 0, len);
                }
                //chiudura dello stream di lettura del file csv
                in.close();

                //chiusura delle entry dello zip
                zos.closeEntry();
                //chiudura dello stream del file zip
                zos.close();

                LogManager.getLogger(ReportJob.class).info("File " + fileName + " compresso correttamente.");
                LogManager.getLogger(ReportJob.class).info("Nome del file zip " + zipFileName);

                LogManager.getLogger(ReportJob.class).debug("Elimino il vecchio file " + fileName);
                //eliminazione del file csv
                File file = new File(fileName);
                file.delete();
                LogManager.getLogger(ReportJob.class).debug("File " + fileName + " eliminato correttamente.");

                //cambio il nome e ci metto quello del file zip in modo da inviare quello
                fileName = zipFileName;
            }

        } catch (IOException e) {
            //salvataggio dell'errore durante l'esecuzione del report
            FileUtils.storeDataLog(aReport, StandardParameter.ReportStatus.ERROR);
            LogManager.getLogger(ReportUtil.class).error(e);
            throw new Exception(e);
        }


        try {
            LogManager.getLogger(ReportUtil.class).info("Invio mail generato per il report " + aReport.getName());
            // Preparo l'oggetto per l'invio della mail tramite exchange
            ReportSenderInterface act = (ReportSenderInterface) Class.forName(ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.class")).newInstance();
            // invio della mail con allegato
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            String HTMLBody = aReport.getDescription() + " <br/> Report generato in data <b>" + fmt.format(new java.util.Date()) + "</b>";

            act.sendMailWithAttachments(aReport.getDescription(), HTMLBody,
                    aReport.getRecipient(), aReport.getCclist(), aReport.getCcnlist(), fileName);

            LogManager.getLogger(ReportUtil.class).debug("Eliminazione del file " + fileName);
            //eliminazione del file
            File file = new File(fileName);
            file.delete();
            LogManager.getLogger(ReportUtil.class).debug("File " + fileName + " eliminato correttamente.");

            LogManager.getLogger(ReportUtil.class).info("Job " + aReport.getName() + " eseguito correttamente.");
            LogManager.getLogger(ReportUtil.class).info("----------------------------------------------------------");
            //settaggio della data di fine elaborazione
            aReport.setLastStopTime(new java.util.Date());
            //si ri-genera il report con le informazioni di esecuzione
            RunningUtils.writeRunningReport();
            //salvataggio dello stato di completamento del report
            FileUtils.storeDataLog(aReport, StandardParameter.ReportStatus.FINISHED);

        } catch (Exception e) {
            //salvataggio dell'errore durante l'esecuzione del report
            FileUtils.storeDataLog(aReport, StandardParameter.ReportStatus.ERROR);
            LogManager.getLogger(ReportUtil.class).error(e);
            throw new Exception(e);
        }
    }
}
