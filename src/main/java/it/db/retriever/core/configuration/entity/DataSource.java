package it.db.retriever.core.configuration.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Classe per il mapping di tutti i dati utili 
 * alla connessione al database. Questa classe mappa
 * l'xml relativo ai file che si troveranno nella cartalla "datasources"
 * 
 * @author D.Bertini
 *
 */
@XmlRootElement
public class DataSource implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3302843107747210195L;
	
	private String name;
	private String url;
	private String username;
	private String password;
	private String driver;
	private String description;
	
	
	public DataSource() {
	}
	
	/**
	 * Metodo costruttore senza inizializzazione
	 * dei parametri
	 * 
	 * @param aName nome del datasource
	 */
	public DataSource(String aName) {
		this.name = aName;
	}

	/**
	 * Metodo costruttore con l'inizializzazione dei parametri
	 * 
	 * @param aName nome del datasource
	 * @param aUrl url jdbc per la connessione al database
	 * @param aUsername username di connessione
	 * @param aPassword password di connessione
	 * @param aDriver driver da utilizzare per la connessione. 
	 *                E' importante aggiungere al classpath la libreria
	 *                che contiene questa classe.
	 */
	public DataSource(String aName, String aUrl, String aUsername, String aPassword, String aDriver) {
		this.name = aName;
		this.url = aUrl;
		this.username = aUsername;
		this.password = aPassword;
		this.driver = aDriver;
		this.description = "no-description";
	}

	/**
	 * Metodo costruttore con l'inizializzazione dei parametri
	 * 
	 * @param aName nome del datasource
	 * @param aUrl url jdbc per la connessione al database
	 * @param aUsername username di connessione
	 * @param aPassword password di connessione
	 * @param aDriver driver da utilizzare per la connessione. 
	 *                E' importante aggiungere al classpath la libreria
	 *                che contiene questa classe.
	 * @param aDescription descrizione del datasource
	 */
	public DataSource(String aName, String aUrl, String aUsername, String aPassword, String aDriver, String aDescription) {
		this.name = aName;
		this.url = aUrl;
		this.username = aUsername;
		this.password = aPassword;
		this.driver = aDriver;
		this.description = aDescription;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	@XmlElement
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	/**
	 * @param driver the driver to set
	 */
	@XmlElement
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
