package it.db.retriever.core;

import java.util.Properties;
/**
 * Classe per la lettura delle configurazioni
 * 
 * @author D.Bertini
 *
 */
public class ConfigurationReader {
	public static void loadConfiguration() {
		try {
			Properties appProps = new Properties();
			appProps.load(ClassLoader.getSystemResourceAsStream("retriever.ini"));
			ApplicationContext.INSTANCE.setConfiguration(appProps);

		} catch (Exception a) {
			a.printStackTrace();
		}
	}
}
