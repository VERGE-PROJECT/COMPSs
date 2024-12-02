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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para SubmissionSystemType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SubmissionSystemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Batch" type="{}BatchType" minOccurs="0"/>
 *         &lt;element name="Interactive" type="{}InteractiveType" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmissionSystemType", propOrder = {
    "batchOrInteractive"
})
public class SubmissionSystemType {

    @XmlElements({
        @XmlElement(name = "Batch", type = BatchType.class),
        @XmlElement(name = "Interactive", type = InteractiveType.class)
    })
    protected List<Object> batchOrInteractive;

    /**
     * Gets the value of the batchOrInteractive property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the batchOrInteractive property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchOrInteractive().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BatchType }
     * {@link InteractiveType }
     * 
     * 
     */
    public List<Object> getBatchOrInteractive() {
        if (batchOrInteractive == null) {
            batchOrInteractive = new ArrayList<Object>();
        }
        return this.batchOrInteractive;
    }

}
