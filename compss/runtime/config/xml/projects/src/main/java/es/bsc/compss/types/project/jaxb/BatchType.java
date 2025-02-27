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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para BatchType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="BatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Queue" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="BatchProperties" type="{}GOSAdaptorProperties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchType", propOrder = {
    "queue",
    "batchProperties"
})
public class BatchType {

    @XmlElement(name = "Queue", required = true)
    protected List<String> queue;
    @XmlElement(name = "BatchProperties")
    protected GOSAdaptorProperties batchProperties;

    /**
     * Gets the value of the queue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the queue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQueue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getQueue() {
        if (queue == null) {
            queue = new ArrayList<String>();
        }
        return this.queue;
    }

    /**
     * Obtiene el valor de la propiedad batchProperties.
     * 
     * @return
     *     possible object is
     *     {@link GOSAdaptorProperties }
     *     
     */
    public GOSAdaptorProperties getBatchProperties() {
        return batchProperties;
    }

    /**
     * Define el valor de la propiedad batchProperties.
     * 
     * @param value
     *     allowed object is
     *     {@link GOSAdaptorProperties }
     *     
     */
    public void setBatchProperties(GOSAdaptorProperties value) {
        this.batchProperties = value;
    }

}
