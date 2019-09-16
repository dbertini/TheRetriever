package it.db.retriever.webserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class RetrieverServer11 {
	public static void startServerRetriver(int aPort) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(aPort), 0);
			//server.createContext("/requests", new ServletHandler());
			//server.createContext("/", new HtmlHandler());
			server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
