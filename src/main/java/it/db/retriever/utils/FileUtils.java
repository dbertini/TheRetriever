package it.db.retriever.utils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.utils.StandardParameter.ReportStatus;

/**
 * Classe di utility per i file
 * 
 * @author D.Bertini
 *
 */
public class FileUtils {
	/**
	 * Metodo per controllare se il file di input supera il limite fissato di 4,5Mb
	 * 
	 * @param aFile
	 *            File da controllare
	 * 
	 * @return true se il file rispetta la dimensione massima, altrimenti false
	 * 
	 * @throws IOException
	 *             Eccezione sui file
	 */
	public static boolean checkFileLength(String aFile) throws IOException {

		Path imageFilePath = Paths.get(aFile);
		FileChannel imageFileChannel = FileChannel.open(imageFilePath);
		long imageFileSize = imageFileChannel.size();
		imageFileChannel.close();

		if (imageFileSize > StandardParameter.LIMIT_FILE_SIZE)
			return false;
		else
			return true;
	}

	/**
	 * Metodo utilizzato per il salvataggio dei dati all'interno del 
	 * file datastore, per tenere traccia dei report eseguiti
	 * 
	 * @param aReport {@link Report} di cui tenere traccia
	 * @param aReportStatus enum {@link ReportStatus} con gli stati del report
	 */
	public static void storeDataLog(Report aReport, ReportStatus aReportStatus) {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			Files.write(
					Paths.get(StandardParameter.STORIC_DATASTORE_FILE),
					Collections.singletonList(
							"EXECUTION ID: " + aReport.getExecutionId() +
							" " + aReportStatus.toString() + " - REPORT: " + aReport.getName() + 
							" START DATE: " + (aReport.getLastStartTime()==null?"NO-RUN":fmt.format(aReport.getLastStartTime())) + 
							" END DATE: " +   (aReport.getLastStopTime()==null?"NO-RUN":fmt.format(aReport.getLastStopTime()))
							), 
					StandardOpenOption.APPEND, 
					StandardOpenOption.CREATE);
		} catch (Exception e) {
			LogManager.getLogger(FileUtils.class).warn(e);
		}
	}

}
