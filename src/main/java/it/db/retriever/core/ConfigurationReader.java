package it.db.retriever.core;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {
	public static void loadConfiguration() {

		try {
			Properties appProps = new Properties();
			appProps.load(ConfigurationReader.class.getResourceAsStream("retriever.ini"));
			System.out.println(appProps.get("test.class"));

		} catch (Exception a) {
			a.printStackTrace();
		}
	}
}
