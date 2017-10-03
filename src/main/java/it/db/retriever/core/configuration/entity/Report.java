package it.db.retriever.core.configuration.entity;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5983843553253510673L;
	
	private String name;
	private String sql;
	private String datasource;
	private String cron;
	private String description;
	private String recipient;
	private String cclist;
	private String ccnlist;
	private String export;
	private String template;
	
	private String filename;

	//variabili utilizzate per il logging delle esecuzioni
	private Date lastStartTime;
	private Date lastStopTime;
	private String executionId = "0";
	
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
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}
	/**
	 * @param sql the sql to set
	 */
	@XmlElement
	public void setSql(String sql) {
		this.sql = sql;
	}
	/**
	 * @return the datasource
	 */
	public String getDatasource() {
		return datasource;
	}
	/**
	 * @param datasource the datasource to set
	 */
	@XmlElement
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	/**
	 * @return the cron
	 */
	public String getCron() {
		return cron;
	}
	/**
	 * @param cron the cron to set
	 */
	@XmlElement
	public void setCron(String cron) {
		this.cron = cron;
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
	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	@XmlElement
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the cclist
	 */
	public String getCclist() {
		return cclist;
	}
	/**
	 * @param cclist the cclist to set
	 */
	@XmlElement
	public void setCclist(String cclist) {
		this.cclist = cclist;
	}
	/**
	 * @return the ccnlist
	 */
	public String getCcnlist() {
		return ccnlist;
	}
	/**
	 * @param ccnlist the ccnlist to set
	 */
	@XmlElement
	public void setCcnlist(String ccnlist) {
		this.ccnlist = ccnlist;
	}
	/**
	 * @return the export
	 */
	public String getExport() {
		return export;
	}
	/**
	 * @param export the export to set
	 */
	public void setExport(String export) {
		this.export = export;
	}
	/**
	 * @return the lastStartTime
	 */
	public Date getLastStartTime() {
		return lastStartTime;
	}
	/**
	 * @param lastStartTime the lastStartTime to set
	 */
	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	/**
	 * @return the lastStopTime
	 */
	public Date getLastStopTime() {
		return lastStopTime;
	}
	/**
	 * @param lastStopTime the lastStopTime to set
	 */
	public void setLastStopTime(Date lastStopTime) {
		this.lastStopTime = lastStopTime;
	}
	/**
	 * @return the executionId
	 */
	public String getExecutionId() {
		return executionId;
	}
	/**
	 * @param executionId the executionId to set
	 */
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	@XmlElement
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getFilename() {
		return filename;
	}

	@XmlElement
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
