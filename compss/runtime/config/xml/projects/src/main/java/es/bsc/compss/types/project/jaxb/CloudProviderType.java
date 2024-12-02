//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.11.26 a las 11:34:56 AM CET 
//


package es.bsc.compss.types.project.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CloudProviderType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="CloudProviderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="2">
 *         &lt;element name="Images" type="{}ImagesType"/>
 *         &lt;element name="InstanceTypes" type="{}InstanceTypesType"/>
 *         &lt;element name="LimitOfVMs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Properties" type="{}CloudPropertiesType" minOccurs="0"/>
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
@XmlType(name = "CloudProviderType", propOrder = {
    "imagesOrInstanceTypesOrLimitOfVMs"
})
public class CloudProviderType {

    @XmlElements({
        @XmlElement(name = "Images", type = ImagesType.class),
        @XmlElement(name = "InstanceTypes", type = InstanceTypesType.class),
        @XmlElement(name = "LimitOfVMs", type = Integer.class),
        @XmlElement(name = "Properties", type = CloudPropertiesType.class)
    })
    protected List<Object> imagesOrInstanceTypesOrLimitOfVMs;
    @XmlAttribute(name = "Name", required = true)
    protected String name;

    /**
     * Gets the value of the imagesOrInstanceTypesOrLimitOfVMs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imagesOrInstanceTypesOrLimitOfVMs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImagesOrInstanceTypesOrLimitOfVMs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImagesType }
     * {@link InstanceTypesType }
     * {@link Integer }
     * {@link CloudPropertiesType }
     * 
     * 
     */
    public List<Object> getImagesOrInstanceTypesOrLimitOfVMs() {
        if (imagesOrInstanceTypesOrLimitOfVMs == null) {
            imagesOrInstanceTypesOrLimitOfVMs = new ArrayList<Object>();
        }
        return this.imagesOrInstanceTypesOrLimitOfVMs;
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
