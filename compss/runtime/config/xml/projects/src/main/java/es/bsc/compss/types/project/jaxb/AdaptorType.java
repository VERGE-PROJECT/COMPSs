//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.11.26 a las 11:34:56 AM CET 
//


package es.bsc.compss.types.project.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AdaptorType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AdaptorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="SubmissionSystem" type="{}SubmissionSystemType"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="Ports" type="{}NIOAdaptorProperties" minOccurs="0"/>
 *           &lt;element name="BrokerAdaptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="User" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KnownHostsPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdentityPath" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Properties" type="{}ExternalAdaptorProperties" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="Name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdaptorType", propOrder = {
    "submissionSystemOrPortsOrBrokerAdaptor"
})
public class AdaptorType {

    @XmlElementRefs({
        @XmlElementRef(name = "SubmissionSystem", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "User", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "KnownHostsPath", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Properties", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Ports", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "IdentityPath", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "BrokerAdaptor", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> submissionSystemOrPortsOrBrokerAdaptor;
    @XmlAttribute(name = "Name", required = true)
    protected String name;

    /**
     * Gets the value of the submissionSystemOrPortsOrBrokerAdaptor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the submissionSystemOrPortsOrBrokerAdaptor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubmissionSystemOrPortsOrBrokerAdaptor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link SubmissionSystemType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link ExternalAdaptorProperties }{@code >}
     * {@link JAXBElement }{@code <}{@link NIOAdaptorProperties }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getSubmissionSystemOrPortsOrBrokerAdaptor() {
        if (submissionSystemOrPortsOrBrokerAdaptor == null) {
            submissionSystemOrPortsOrBrokerAdaptor = new ArrayList<JAXBElement<?>>();
        }
        return this.submissionSystemOrPortsOrBrokerAdaptor;
    }

    /**
     * Obtiene el valor de la propiedad name.
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
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}