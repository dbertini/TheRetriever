//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.08.11 alle 10:03:16 AM CEST 
//


package it.db.retriever.templates;

import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TitleTextColor_QNAME = new QName("", "titleTextColor");
    private final static QName _HeadersDefinition_QNAME = new QName("", "headersDefinition");
    private final static QName _Heading_QNAME = new QName("", "heading");
    private final static QName _Format_QNAME = new QName("", "format");
    private final static QName _Valign_QNAME = new QName("", "valign");
    private final static QName _Title_QNAME = new QName("", "title");
    private final static QName _TextColor_QNAME = new QName("", "textColor");
    private final static QName _BkgColor_QNAME = new QName("", "bkgColor");
    private final static QName _Boxed_QNAME = new QName("", "boxed");
    private final static QName _HeaderDefinition_QNAME = new QName("", "headerDefinition");
    private final static QName _TitleBkgColor_QNAME = new QName("", "titleBkgColor");
    private final static QName _Name_QNAME = new QName("", "name");
    private final static QName _Halign_QNAME = new QName("", "halign");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Template }
     * @return {@link Template }
     */
    public Template createTemplate() {
        return new Template();
    }

    /**
     * Create an instance of {@link Headers }
     * @return {@link Headers }
     */
    public Headers createHeaders() {
        return new Headers();
    }

    /**
     * Create an instance of {@link BoxedType }
     * @return {@link BoxedType }
     */
    public BoxedType createBoxedType() {
        return new BoxedType();
    }

    /**
     * Create an instance of {@link HeadersDefinition }
     * @return {@link HeadersDefinition }
     */
    public HeadersDefinition createHeadersDefinition() {
        return new HeadersDefinition();
    }

    /**
     * Create an instance of {@link Coloumns }
     * @return {@link Coloumns }
     */
    public Coloumns createColoumns() {
        return new Coloumns();
    }

    /**
     * Create an instance of {@link Coloumn }
     * @return {@link Coloumn }
     */
    public Coloumn createColoumn() {
        return new Coloumn();
    }

    /**
     * Create an instance of {@link HeaderDefinition }
     * @return {@link HeaderDefinition }
     */
    public HeaderDefinition createHeaderDefinition() {
        return new HeaderDefinition();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return  {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "titleTextColor")
    public JAXBElement<String> createTitleTextColor(String value) {
        return new JAXBElement<String>(_TitleTextColor_QNAME, String.class, null, value);
    }

   /**
    * Create an instance of {@link JAXBElement }{@code <}{@link HeadersDefinition }{@code >}}
    * @param value {@link HeadersDefinition }
    * @return {@link JAXBElement }{@code <}{@link HeadersDefinition }{@code >}}
    */
    @XmlElementDecl(namespace = "", name = "headersDefinition")
    public JAXBElement<HeadersDefinition> createHeadersDefinition(HeadersDefinition value) {
        return new JAXBElement<HeadersDefinition>(_HeadersDefinition_QNAME, HeadersDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "heading")
    public JAXBElement<String> createHeading(String value) {
        return new JAXBElement<String>(_Heading_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */

    @XmlElementDecl(namespace = "", name = "format")
    public JAXBElement<String> createFormat(String value) {
        return new JAXBElement<String>(_Format_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}}
     * 
     * @param value {@link List }{@code <}{@link String }{@code >}
     * @return {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}
     */
    @XmlElementDecl(namespace = "", name = "valign")
    public JAXBElement<List<String>> createValign(List<String> value) {
        return new JAXBElement<List<String>>(_Valign_QNAME, ((Class) List.class), null, ((List<String> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */ 
    @XmlElementDecl(namespace = "", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "textColor")
    public JAXBElement<String> createTextColor(String value) {
        return new JAXBElement<String>(_TextColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "bkgColor")
    public JAXBElement<String> createBkgColor(String value) {
        return new JAXBElement<String>(_BkgColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BoxedType }{@code >}}
     * 
     * @param value {@link BoxedType }
     * @return {@link JAXBElement }{@code <}{@link BoxedType }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "boxed")
    public JAXBElement<BoxedType> createBoxed(BoxedType value) {
        return new JAXBElement<BoxedType>(_Boxed_QNAME, BoxedType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HeaderDefinition }{@code >}}
     * 
     * @param value {@link HeaderDefinition }
     * @return {@link JAXBElement }{@code <}{@link HeaderDefinition }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "headerDefinition")
    public JAXBElement<HeaderDefinition> createHeaderDefinition(HeaderDefinition value) {
        return new JAXBElement<HeaderDefinition>(_HeaderDefinition_QNAME, HeaderDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "titleBkgColor")
    public JAXBElement<String> createTitleBkgColor(String value) {
        return new JAXBElement<String>(_TitleBkgColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     * @param value String
     * @return {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}}
     * 
     * @param value {@link List }{@code <}{@link String }{@code >}
     * @return  {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}
     */
    @XmlElementDecl(namespace = "", name = "halign")
    public JAXBElement<List<String>> createHalign(List<String> value) {
        return new JAXBElement<List<String>>(_Halign_QNAME, ((Class) List.class), null, ((List<String> ) value));
    }

}
