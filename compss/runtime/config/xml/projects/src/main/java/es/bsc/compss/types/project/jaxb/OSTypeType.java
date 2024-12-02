//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.11.26 a las 11:34:56 AM CET 
//


package es.bsc.compss.types.project.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para OSTypeType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="OSTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Linux"/>
 *     &lt;enumeration value="Windows"/>
 *     &lt;enumeration value="MacOS"/>
 *     &lt;enumeration value="FreeBSD"/>
 *     &lt;enumeration value="SunOS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OSTypeType")
@XmlEnum
public enum OSTypeType {

    @XmlEnumValue("Linux")
    LINUX("Linux"),
    @XmlEnumValue("Windows")
    WINDOWS("Windows"),
    @XmlEnumValue("MacOS")
    MAC_OS("MacOS"),
    @XmlEnumValue("FreeBSD")
    FREE_BSD("FreeBSD"),
    @XmlEnumValue("SunOS")
    SUN_OS("SunOS");
    private final String value;

    OSTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OSTypeType fromValue(String v) {
        for (OSTypeType c: OSTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
