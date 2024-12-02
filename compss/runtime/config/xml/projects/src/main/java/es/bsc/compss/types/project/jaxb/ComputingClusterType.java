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
 * <p>Clase Java para ComputingClusterType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ComputingClusterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="LimitOfTasks" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="InstallDir" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkingDir" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ClusterNode" type="{}ClusterNodeType" maxOccurs="unbounded"/>
 *         &lt;element name="Software" type="{}SoftwareListType" minOccurs="0"/>
 *         &lt;element name="User" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Application" type="{}ApplicationType" minOccurs="0"/>
 *         &lt;element name="Adaptors" type="{}AdaptorsListType" minOccurs="0"/>
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
@XmlType(name = "ComputingClusterType", propOrder = {
    "limitOfTasksOrInstallDirOrWorkingDir"
})
public class ComputingClusterType {

    @XmlElementRefs({
        @XmlElementRef(name = "InstallDir", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ClusterNode", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WorkingDir", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Application", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LimitOfTasks", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Software", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Adaptors", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "User", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> limitOfTasksOrInstallDirOrWorkingDir;
    @XmlAttribute(name = "Name", required = true)
    protected String name;

    /**
     * Gets the value of the limitOfTasksOrInstallDirOrWorkingDir property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the limitOfTasksOrInstallDirOrWorkingDir property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLimitOfTasksOrInstallDirOrWorkingDir().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link ClusterNodeType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link ApplicationType }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link SoftwareListType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdaptorsListType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getLimitOfTasksOrInstallDirOrWorkingDir() {
        if (limitOfTasksOrInstallDirOrWorkingDir == null) {
            limitOfTasksOrInstallDirOrWorkingDir = new ArrayList<JAXBElement<?>>();
        }
        return this.limitOfTasksOrInstallDirOrWorkingDir;
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
