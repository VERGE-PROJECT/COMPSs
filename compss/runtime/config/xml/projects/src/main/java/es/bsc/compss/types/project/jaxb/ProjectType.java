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
 * <p>Clase Java para ProjectType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ProjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="MasterNode" type="{}MasterNodeType"/>
 *         &lt;element name="ComputeNode" type="{}ComputeNodeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DataNode" type="{}DataNodeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Service" type="{}ServiceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Http" type="{}HttpType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Cloud" type="{}CloudType" minOccurs="0"/>
 *         &lt;element name="ComputingCluster" type="{}ComputingClusterType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="KubernetesDeployment" type="{}KubernetesDeploymentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectType", propOrder = {
    "masterNodeOrComputeNodeOrDataNode"
})
public class ProjectType {

    @XmlElements({
        @XmlElement(name = "MasterNode", type = MasterNodeType.class),
        @XmlElement(name = "ComputeNode", type = ComputeNodeType.class),
        @XmlElement(name = "DataNode", type = DataNodeType.class),
        @XmlElement(name = "Service", type = ServiceType.class),
        @XmlElement(name = "Http", type = HttpType.class),
        @XmlElement(name = "Cloud", type = CloudType.class),
        @XmlElement(name = "ComputingCluster", type = ComputingClusterType.class),
        @XmlElement(name = "KubernetesDeployment", type = KubernetesDeploymentType.class)
    })
    protected List<Object> masterNodeOrComputeNodeOrDataNode;

    /**
     * Gets the value of the masterNodeOrComputeNodeOrDataNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the masterNodeOrComputeNodeOrDataNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMasterNodeOrComputeNodeOrDataNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MasterNodeType }
     * {@link ComputeNodeType }
     * {@link DataNodeType }
     * {@link ServiceType }
     * {@link HttpType }
     * {@link CloudType }
     * {@link ComputingClusterType }
     * {@link KubernetesDeploymentType }
     * 
     * 
     */
    public List<Object> getMasterNodeOrComputeNodeOrDataNode() {
        if (masterNodeOrComputeNodeOrDataNode == null) {
            masterNodeOrComputeNodeOrDataNode = new ArrayList<Object>();
        }
        return this.masterNodeOrComputeNodeOrDataNode;
    }

}
