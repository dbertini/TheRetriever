package it.db.retriever.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.DataSource;

/**
 * Classe utilizzata per la connessione al database 
 * tramite i parametri passati.
 * 
 * @author D.Bertini
 *
 */
public class ConnectionProvider {
	
	/**
	 * Metodo che dato un datasource ritorna la connessione al database
	 * 
	 * @param aDataSource {@link DataSource} per il quale si vuole recuperare la connessione
	 * @return {@link Connection} al database
	 * @throws SQLException Eccezione generica SQL
	 * @throws ClassNotFoundException Eccezione di classe non trovata per il driver usato per la connessione al database
	 */
	public static Connection getConnection (DataSource aDataSource) throws SQLException, ClassNotFoundException {

		LogManager.getLogger(ConnectionProvider.class).info("Initializing connection for " + aDataSource.getName());
		LogManager.getLogger(ConnectionProvider.class).info("Using driver " + aDataSource.getDriver());
		Class.forName(aDataSource.getDriver());
		LogManager.getLogger(ConnectionProvider.class).info("Using URL: " + aDataSource.getUrl());
		LogManager.getLogger(ConnectionProvider.class).info("Using username: " + aDataSource.getUsername());

		if( LogManager.getLogger(ConnectionProvider.class).getLevel().compareTo(Level.DEBUG) <= 0 )
			LogManager.getLogger(ConnectionProvider.class).debug("Password utilizzata: " + aDataSource.getPassword());
		else
			LogManager.getLogger(ConnectionProvider.class).info("Password utilizzata: *********");

		Connection con = DriverManager.getConnection(aDataSource.getUrl(), aDataSource.getUsername(), aDataSource.getPassword());
		//Set the autocommit to false to avoid mistakes
		con.setAutoCommit(false);

		LogManager.getLogger(ConnectionProvider.class).info("Connection succesfull for datasource " + aDataSource.getName());

		return con;
	}
	
	/**
	 * Metodo per testare la connessione al data
	 * @param aDataSource {@link DataSource} da testare
	 * @return true se la connessione riesce, alrimenti false
	 */
	public static boolean testConnection(DataSource aDataSource) {
		boolean connected = false;
		Connection con = null;
		try {
			con = getConnection(aDataSource);
			
			connected = con.isValid(1000);
			
		} catch (Exception e) {
			LogManager.getLogger(ConnectionProvider.class).fatal(e);
		} finally {
			try {
				con.close();
			} catch (Exception e) { }
		}
		return connected;
	}
}
