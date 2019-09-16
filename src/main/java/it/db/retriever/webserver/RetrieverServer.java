package it.db.retriever.webserver;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.Retriever;

/**
 * Classe per l'inizializzazione del webserver interno del Retriever
 * 
 * @author D.Bertini
 *
 */
public class RetrieverServer {

	public static void startServerRetriver(int aPort) {
		
		LogManager.getLogger(Retriever.class).info("Starting Retriever WebServer");
		
//		ResourceConfig config = new ResourceConfig();
//		config.packages("it.db.retriever.webserver");
//		ServletHolder servlet = new ServletHolder(new ServletContainer(config));
//
//		Server server = new Server(aPort);
//		ServletContextHandler context = new ServletContextHandler(server, "/*");
//		context.addServlet(servlet, "/*");
//
//		try {
//			server.start();
//			server.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			server.destroy();
//		}
		
		LogManager.getLogger(Retriever.class).info("Retriever WebServer started on port " + aPort);
	}
}
