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
 * <p>Clase Java para MasterNodeType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="MasterNodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Processor" type="{}ProcessorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Memory" type="{}MemoryType" minOccurs="0"/>
 *         &lt;element name="Storage" type="{}StorageType" minOccurs="0"/>
 *         &lt;element name="OperatingSystem" type="{}OSType" minOccurs="0"/>
 *         &lt;element name="Software" type="{}SoftwareListType" minOccurs="0"/>
 *         &lt;element name="SharedDisks" type="{}AttachedDisksListType" minOccurs="0"/>
 *         &lt;element name="Price" type="{}PriceType" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MasterNodeType", propOrder = {
    "processorOrMemoryOrStorage"
})
public class MasterNodeType {

    @XmlElements({
        @XmlElement(name = "Processor", type = ProcessorType.class),
        @XmlElement(name = "Memory", type = MemoryType.class),
        @XmlElement(name = "Storage", type = StorageType.class),
        @XmlElement(name = "OperatingSystem", type = OSType.class),
        @XmlElement(name = "Software", type = SoftwareListType.class),
        @XmlElement(name = "SharedDisks", type = AttachedDisksListType.class),
        @XmlElement(name = "Price", type = PriceType.class)
    })
    protected List<Object> processorOrMemoryOrStorage;

    /**
     * Gets the value of the processorOrMemoryOrStorage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processorOrMemoryOrStorage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessorOrMemoryOrStorage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessorType }
     * {@link MemoryType }
     * {@link StorageType }
     * {@link OSType }
     * {@link SoftwareListType }
     * {@link AttachedDisksListType }
     * {@link PriceType }
     * 
     * 
     */
    public List<Object> getProcessorOrMemoryOrStorage() {
        if (processorOrMemoryOrStorage == null) {
            processorOrMemoryOrStorage = new ArrayList<Object>();
        }
        return this.processorOrMemoryOrStorage;
    }

}
