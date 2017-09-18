//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.08.11 alle 10:03:16 AM CEST 
//


package it.db.retriever.templates;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
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
 *         &lt;element ref="{}heading"/&gt;
 *         &lt;element ref="{}textColor" minOccurs="0"/&gt;
 *         &lt;element ref="{}bkgColor" minOccurs="0"/&gt;
 *         &lt;element ref="{}valign" minOccurs="0"/&gt;
 *         &lt;element ref="{}halign" minOccurs="0"/&gt;
 *         &lt;element ref="{}boxed" minOccurs="0"/&gt;
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
    "heading",
    "textColor",
    "bkgColor",
    "valign",
    "halign",
    "boxed"
})
public class HeaderDefinition {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlElement(required = true)
    protected String heading;
    protected String textColor;
    protected String bkgColor;
    @XmlList
    protected List<String> valign;
    @XmlList
    protected List<String> halign;
    protected BoxedType boxed;

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
     * Recupera il valore della proprietà heading.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Imposta il valore della proprietà heading.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeading(String value) {
        this.heading = value;
    }

    /**
     * Recupera il valore della proprietà textColor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Imposta il valore della proprietà textColor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextColor(String value) {
        this.textColor = value;
    }

    /**
     * Recupera il valore della proprietà bkgColor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBkgColor() {
        return bkgColor;
    }

    /**
     * Imposta il valore della proprietà bkgColor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBkgColor(String value) {
        this.bkgColor = value;
    }

    /**
     * Gets the value of the valign property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valign property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValign().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return lista delle configurazioni per Vertical Align
     */
    public List<String> getValign() {
        if (valign == null) {
            valign = new ArrayList<String>();
        }
        return this.valign;
    }

    /**
     * Gets the value of the halign property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the halign property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalign().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return lista delle configurazioni di Horizzontal Align
     */
    public List<String> getHalign() {
        if (halign == null) {
            halign = new ArrayList<String>();
        }
        return this.halign;
    }

    /**
     * Recupera il valore della proprietà boxed.
     * 
     * @return
     *     possible object is
     *     {@link BoxedType }
     *     
     */
    public BoxedType getBoxed() {
        return boxed;
    }

    /**
     * Imposta il valore della proprietà boxed.
     * 
     * @param value
     *     allowed object is
     *     {@link BoxedType }
     *     
     */
    public void setBoxed(BoxedType value) {
        this.boxed = value;
    }

}
