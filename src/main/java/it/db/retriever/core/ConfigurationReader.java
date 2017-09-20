package it.db.retriever.core;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
/**
 * Classe per la lettura delle configurazioni
 * 
 * @author D.Bertini
 *
 */
public class ConfigurationReader {
	public static void loadConfiguration() throws Exception {
		try {
			Properties appProps = new Properties();
			appProps.load(ClassLoader.getSystemResourceAsStream("retriever.properties"));
			ApplicationContext.INSTANCE.setConfiguration(appProps);

		} catch (Exception a) {
			LogManager.getLogger(ConfigurationReader.class).fatal("Errore grave durante la lettura delle configurazioni.", a);
			throw a;
		}
	}
}