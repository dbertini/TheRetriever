package it.db.retriever.core.configuration.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Row implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6457264053167962510L;
	private List<Object> coloumn;
	
	public Row() {
		this.setColoumn(new ArrayList<Object>());
	}

	/**
	 * @return the coloumn
	 */
	public List<Object> getColoumn() {
		return coloumn;
	}

	/**
	 * @param coloumn the coloumn to set
	 */
	public void setColoumn(List<Object> coloumn) {
		this.coloumn = coloumn;
	}

}
