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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CloudType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="CloudType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="CloudProvider" type="{}CloudProviderType" maxOccurs="unbounded"/>
 *         &lt;element name="InitialVMs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MinimumVMs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MaximumVMs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CloudType", propOrder = {
    "cloudProviderOrInitialVMsOrMinimumVMs"
})
public class CloudType {

    @XmlElementRefs({
        @XmlElementRef(name = "MaximumVMs", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "MinimumVMs", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "InitialVMs", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CloudProvider", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> cloudProviderOrInitialVMsOrMinimumVMs;

    /**
     * Gets the value of the cloudProviderOrInitialVMsOrMinimumVMs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cloudProviderOrInitialVMsOrMinimumVMs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCloudProviderOrInitialVMsOrMinimumVMs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link CloudProviderType }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getCloudProviderOrInitialVMsOrMinimumVMs() {
        if (cloudProviderOrInitialVMsOrMinimumVMs == null) {
            cloudProviderOrInitialVMsOrMinimumVMs = new ArrayList<JAXBElement<?>>();
        }
        return this.cloudProviderOrInitialVMsOrMinimumVMs;
    }

}
