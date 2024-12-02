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
 * <p>Clase Java para GOSAdaptorProperties complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="GOSAdaptorProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MaxExecTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="Reservation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FileCFG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GOSAdaptorProperties", propOrder = {
    "port",
    "maxExecTime",
    "reservation",
    "qos",
    "fileCFG"
})
public class GOSAdaptorProperties {

    @XmlElement(name = "Port")
    protected Integer port;
    @XmlElement(name = "MaxExecTime")
    protected Long maxExecTime;
    @XmlElement(name = "Reservation")
    protected String reservation;
    @XmlElement(name = "QOS")
    protected String qos;
    @XmlElement(name = "FileCFG")
    protected String fileCFG;

    /**
     * Obtiene el valor de la propiedad port.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Define el valor de la propiedad port.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

    /**
     * Obtiene el valor de la propiedad maxExecTime.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMaxExecTime() {
        return maxExecTime;
    }

    /**
     * Define el valor de la propiedad maxExecTime.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMaxExecTime(Long value) {
        this.maxExecTime = value;
    }

    /**
     * Obtiene el valor de la propiedad reservation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservation() {
        return reservation;
    }

    /**
     * Define el valor de la propiedad reservation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservation(String value) {
        this.reservation = value;
    }

    /**
     * Obtiene el valor de la propiedad qos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQOS() {
        return qos;
    }

    /**
     * Define el valor de la propiedad qos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQOS(String value) {
        this.qos = value;
    }

    /**
     * Obtiene el valor de la propiedad fileCFG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileCFG() {
        return fileCFG;
    }

    /**
     * Define el valor de la propiedad fileCFG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileCFG(String value) {
        this.fileCFG = value;
    }

}
