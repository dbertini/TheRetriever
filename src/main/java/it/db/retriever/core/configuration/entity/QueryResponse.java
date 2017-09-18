package it.db.retriever.core.configuration.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Oggetto di risposta a seguito dell'esecuzione
 * di una query. Oltre all'intero set dei dati trovati saranno presenti anche
 * una serie di altre informazioni, come la label delle colonne ed il tipo
 * @author D.Bertini
 *
 */
public class QueryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4637239199326472142L;
	/**
	 * Label delle colonne
	 */
	private List<String> labels;
	/**
	 * Tipi delle colonne trovate in maniera testuale
	 */
	private List<String> coloumnTypeName;
	/**
	 * Tipi delle colonne
	 */
	private List<Integer> coloumnType;
	
	private List<Row> rows;
	
	/**
	 * Metodo costruttore
	 */
	public QueryResponse() {
		this.labels = new ArrayList<String>();
		this.coloumnTypeName = new ArrayList<String>();
		this.coloumnType = new ArrayList<Integer>();
		this.rows = new ArrayList<Row>();
	}

	/**
	 * @return the labels
	 */
	public List<String> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	/**
	 * @return the coloumnTypeName
	 */
	public List<String> getColoumnTypeName() {
		return coloumnTypeName;
	}

	/**
	 * @param coloumnTypeName the coloumnTypeName to set
	 */
	public void setColoumnTypeName(List<String> coloumnTypeName) {
		this.coloumnTypeName = coloumnTypeName;
	}

	/**
	 * @return the coloumnType
	 */
	public List<Integer> getColoumnType() {
		return coloumnType;
	}

	/**
	 * @param coloumnType the coloumnType to set
	 */
	public void setColoumnType(List<Integer> coloumnType) {
		this.coloumnType = coloumnType;
	}

	/**
	 * @return the rows
	 */
	public List<Row> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	
}
