//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.11.26 a las 11:34:56 AM CET 
//


package es.bsc.compss.types.project.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para PriceType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="PriceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="TimeUnit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PricePerUnit" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PriceType", propOrder = {

})
public class PriceType {

    @XmlElement(name = "TimeUnit")
    protected int timeUnit;
    @XmlElement(name = "PricePerUnit")
    protected float pricePerUnit;

    /**
     * Obtiene el valor de la propiedad timeUnit.
     * 
     */
    public int getTimeUnit() {
        return timeUnit;
    }

    /**
     * Define el valor de la propiedad timeUnit.
     * 
     */
    public void setTimeUnit(int value) {
        this.timeUnit = value;
    }

    /**
     * Obtiene el valor de la propiedad pricePerUnit.
     * 
     */
    public float getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Define el valor de la propiedad pricePerUnit.
     * 
     */
    public void setPricePerUnit(float value) {
        this.pricePerUnit = value;
    }

}
