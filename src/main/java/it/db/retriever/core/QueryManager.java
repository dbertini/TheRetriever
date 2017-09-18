package it.db.retriever.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.configuration.entity.QueryResponse;
import it.db.retriever.core.configuration.entity.Row;
/**
 * Classe che si preoccupa di connettersi al database ed
 * eseguire la select desiderata
 * @author D.Bertini
 *
 */
public class QueryManager {

	/**
	 * Il metodo tramite il nome del datasource si connette alla corretta 
	 * sorgente dati ed esegue l'sql ritornando la lista dei dati e varie altre informazioni
	 * sulla tipologia delle colonne
	 * 
	 * @param aDataSourceName Nome del datasource
	 * @param aSQL sql da eseguire
	 * @return {@link QueryResponse}
	 * 
	 */
	public static QueryResponse executeSQL(String aDataSourceName, String aSQL) {
		QueryResponse qr = new QueryResponse();
		Connection connection = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			LogManager.getLogger(QueryManager.class).debug("SQL da eseguire: " + aSQL);
			
			LogManager.getLogger(QueryManager.class).debug("Creazione della connessione");
			connection = ConnectionProvider.getConnection(ApplicationContext.INSTANCE.getDataSource(aDataSourceName));
			LogManager.getLogger(QueryManager.class).debug("Creazione del PreparedStatement");
			pst = connection.prepareStatement(aSQL);
			LogManager.getLogger(QueryManager.class).debug("Esecuzione della select");
			rst = pst.executeQuery();
			LogManager.getLogger(QueryManager.class).debug("Recupero delle label delle colonne");
			
			int columnCount = rst.getMetaData().getColumnCount();
			
			for (int i = 0; i < columnCount; i++) {
				//recupero le label delle colonne
				LogManager.getLogger(QueryManager.class).debug("Trovato campo con label: " + rst.getMetaData().getColumnLabel(i+1));
				try {
					qr.getLabels().add(rst.getMetaData().getColumnLabel(i+1));
				} catch(Exception e) {
					//nel caso di errore 
					//aggiungo una label di default
					qr.getLabels().add("COL_" + (i+1));
				}
				
				try {
					//recupero il tipo delle colonne
					LogManager.getLogger(QueryManager.class).debug("ColumnType: " + rst.getMetaData().getColumnType(i+1));
					qr.getColoumnType().add(rst.getMetaData().getColumnType(i+1));
				} catch (Exception e) {
					//in caso di errore aggiungo un tipo varchar di defualt
					qr.getColoumnType().add(java.sql.Types.VARCHAR);
				}
				
				try {
					//recupero li tipo delle colonne in maniera descrittiva
					LogManager.getLogger(QueryManager.class).debug("ColumnTypeName: " + rst.getMetaData().getColumnTypeName(i+1));
					qr.getColoumnTypeName().add(rst.getMetaData().getColumnTypeName(i+1));
				} catch (Exception e) {
					qr.getColoumnTypeName().add("VARCHAR");
				}

				LogManager.getLogger(QueryManager.class).debug("-----------------------------------------------------------------");
			}
			LogManager.getLogger(QueryManager.class).info("Retriving data....");
			
			//per ogni riga trovata tramite la query
			//creo un oggetto riga che contiene i dati
			while (rst.next()) {
				Row row = new Row();
				for (int i = 0; i < columnCount; i++) {
					row.getColoumn().add(rst.getObject(i+1));
				}
				qr.getRows().add(row);
			}
			LogManager.getLogger(QueryManager.class).info("Data retrived....");
		} catch (ClassNotFoundException | SQLException e) {
			LogManager.getLogger(QueryManager.class).fatal(e);
		} finally {
			try {
				rst.close();
			} catch (SQLException e2) {
				LogManager.getLogger(QueryManager.class).fatal(e2);
			}
			
			try {
				pst.close();
			} catch (SQLException e1) {
				LogManager.getLogger(QueryManager.class).fatal(e1);
			}
			
			try {
				connection.close();
			} catch (SQLException e) {
				LogManager.getLogger(QueryManager.class).fatal(e);
			}
		}
		
		return qr;
	}
}
