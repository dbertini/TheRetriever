//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.08.11 alle 10:03:16 AM CEST 
//


package it.db.retriever.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}name"/&gt;
 *         &lt;element ref="{}title"/&gt;
 *         &lt;element ref="{}titleTextColor" minOccurs="0"/&gt;
 *         &lt;element ref="{}titleBkgColor" minOccurs="0"/&gt;
 *         &lt;element ref="{}headers" minOccurs="0"/&gt;
 *         &lt;element ref="{}coloumns" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "title",
    "titleTextColor",
    "titleBkgColor",
    "headers",
    "coloumns"
})
@XmlRootElement(name = "template")
public class Template {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlElement(required = true)
    protected String title;
    protected String titleTextColor;
    protected String titleBkgColor;
    protected Headers headers;
    protected Coloumns coloumns;

    /**
     * Recupera il valore della proprietà name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il valore della proprietà name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Recupera il valore della proprietà title.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Imposta il valore della proprietà title.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Recupera il valore della proprietà titleTextColor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitleTextColor() {
        return titleTextColor;
    }

    /**
     * Imposta il valore della proprietà titleTextColor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitleTextColor(String value) {
        this.titleTextColor = value;
    }

    /**
     * Recupera il valore della proprietà titleBkgColor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitleBkgColor() {
        return titleBkgColor;
    }

    /**
     * Imposta il valore della proprietà titleBkgColor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitleBkgColor(String value) {
        this.titleBkgColor = value;
    }

    /**
     * Recupera il valore della proprietà headers.
     * 
     * @return
     *     possible object is
     *     {@link Headers }
     *     
     */
    public Headers getHeaders() {
        return headers;
    }

    /**
     * Imposta il valore della proprietà headers.
     * 
     * @param value
     *     allowed object is
     *     {@link Headers }
     *     
     */
    public void setHeaders(Headers value) {
        this.headers = value;
    }

    /**
     * Recupera il valore della proprietà coloumns.
     * 
     * @return
     *     possible object is
     *     {@link Coloumns }
     *     
     */
    public Coloumns getColoumns() {
        return coloumns;
    }

    /**
     * Imposta il valore della proprietà coloumns.
     * 
     * @param value
     *     allowed object is
     *     {@link Coloumns }
     *     
     */
    public void setColoumns(Coloumns value) {
        this.coloumns = value;
    }

}
