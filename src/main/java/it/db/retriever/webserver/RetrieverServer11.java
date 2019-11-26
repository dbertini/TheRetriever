package it.db.retriever.webserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import it.db.retriever.webserver.services11.InfoServiceConfiguration;
import it.db.retriever.webserver.services11.InfoVersionService;

public class RetrieverServer11 {
	public static void startServerRetriver(int aPort) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(aPort), 0);
			server.createContext("/rest/api/v11/info/version", new InfoVersionService());
			server.createContext("/rest/api/v11/info/configuration", new InfoServiceConfiguration() );
			server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
