//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.11.26 a las 11:34:56 AM CET 
//


package es.bsc.compss.types.project.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.bsc.compss.types.project.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Project_QNAME = new QName("", "Project");
    private final static QName _ComputingClusterTypeClusterNode_QNAME = new QName("", "ClusterNode");
    private final static QName _ComputingClusterTypeWorkingDir_QNAME = new QName("", "WorkingDir");
    private final static QName _ComputingClusterTypeUser_QNAME = new QName("", "User");
    private final static QName _ComputingClusterTypeSoftware_QNAME = new QName("", "Software");
    private final static QName _ComputingClusterTypeAdaptors_QNAME = new QName("", "Adaptors");
    private final static QName _ComputingClusterTypeLimitOfTasks_QNAME = new QName("", "LimitOfTasks");
    private final static QName _ComputingClusterTypeApplication_QNAME = new QName("", "Application");
    private final static QName _ComputingClusterTypeInstallDir_QNAME = new QName("", "InstallDir");
    private final static QName _OSTypeType_QNAME = new QName("", "Type");
    private final static QName _OSTypeVersion_QNAME = new QName("", "Version");
    private final static QName _OSTypeDistribution_QNAME = new QName("", "Distribution");
    private final static QName _CloudTypeInitialVMs_QNAME = new QName("", "InitialVMs");
    private final static QName _CloudTypeMinimumVMs_QNAME = new QName("", "MinimumVMs");
    private final static QName _CloudTypeCloudProvider_QNAME = new QName("", "CloudProvider");
    private final static QName _CloudTypeMaximumVMs_QNAME = new QName("", "MaximumVMs");
    private final static QName _ProcessorTypeInternalMemorySize_QNAME = new QName("", "InternalMemorySize");
    private final static QName _ProcessorTypeProcessorProperty_QNAME = new QName("", "ProcessorProperty");
    private final static QName _ProcessorTypeSpeed_QNAME = new QName("", "Speed");
    private final static QName _ProcessorTypeArchitecture_QNAME = new QName("", "Architecture");
    private final static QName _ProcessorTypeComputingUnits_QNAME = new QName("", "ComputingUnits");
    private final static QName _ImageTypePackage_QNAME = new QName("", "Package");
    private final static QName _AdaptorTypeIdentityPath_QNAME = new QName("", "IdentityPath");
    private final static QName _AdaptorTypeSubmissionSystem_QNAME = new QName("", "SubmissionSystem");
    private final static QName _AdaptorTypeBrokerAdaptor_QNAME = new QName("", "BrokerAdaptor");
    private final static QName _AdaptorTypePorts_QNAME = new QName("", "Ports");
    private final static QName _AdaptorTypeKnownHostsPath_QNAME = new QName("", "KnownHostsPath");
    private final static QName _AdaptorTypeProperties_QNAME = new QName("", "Properties");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.bsc.compss.types.project.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProjectType }
     * 
     */
    public ProjectType createProjectType() {
        return new ProjectType();
    }

    /**
     * Create an instance of {@link AdaptorsListType }
     * 
     */
    public AdaptorsListType createAdaptorsListType() {
        return new AdaptorsListType();
    }

    /**
     * Create an instance of {@link ProcessorType }
     * 
     */
    public ProcessorType createProcessorType() {
        return new ProcessorType();
    }

    /**
     * Create an instance of {@link NIOAdaptorProperties }
     * 
     */
    public NIOAdaptorProperties createNIOAdaptorProperties() {
        return new NIOAdaptorProperties();
    }

    /**
     * Create an instance of {@link GOSAdaptorProperties }
     * 
     */
    public GOSAdaptorProperties createGOSAdaptorProperties() {
        return new GOSAdaptorProperties();
    }

    /**
     * Create an instance of {@link ExternalAdaptorProperties }
     * 
     */
    public ExternalAdaptorProperties createExternalAdaptorProperties() {
        return new ExternalAdaptorProperties();
    }

    /**
     * Create an instance of {@link ProcessorPropertyType }
     * 
     */
    public ProcessorPropertyType createProcessorPropertyType() {
        return new ProcessorPropertyType();
    }

    /**
     * Create an instance of {@link ImageType }
     * 
     */
    public ImageType createImageType() {
        return new ImageType();
    }

    /**
     * Create an instance of {@link InstanceTypeType }
     * 
     */
    public InstanceTypeType createInstanceTypeType() {
        return new InstanceTypeType();
    }

    /**
     * Create an instance of {@link CloudPropertyType }
     * 
     */
    public CloudPropertyType createCloudPropertyType() {
        return new CloudPropertyType();
    }

    /**
     * Create an instance of {@link ServiceType }
     * 
     */
    public ServiceType createServiceType() {
        return new ServiceType();
    }

    /**
     * Create an instance of {@link PackageType }
     * 
     */
    public PackageType createPackageType() {
        return new PackageType();
    }

    /**
     * Create an instance of {@link AttachedDisksListType }
     * 
     */
    public AttachedDisksListType createAttachedDisksListType() {
        return new AttachedDisksListType();
    }

    /**
     * Create an instance of {@link ClusterNodeType }
     * 
     */
    public ClusterNodeType createClusterNodeType() {
        return new ClusterNodeType();
    }

    /**
     * Create an instance of {@link InstanceTypesType }
     * 
     */
    public InstanceTypesType createInstanceTypesType() {
        return new InstanceTypesType();
    }

    /**
     * Create an instance of {@link PropertyAdaptorType }
     * 
     */
    public PropertyAdaptorType createPropertyAdaptorType() {
        return new PropertyAdaptorType();
    }

    /**
     * Create an instance of {@link DataNodeType }
     * 
     */
    public DataNodeType createDataNodeType() {
        return new DataNodeType();
    }

    /**
     * Create an instance of {@link MemoryType }
     * 
     */
    public MemoryType createMemoryType() {
        return new MemoryType();
    }

    /**
     * Create an instance of {@link AdaptorType }
     * 
     */
    public AdaptorType createAdaptorType() {
        return new AdaptorType();
    }

    /**
     * Create an instance of {@link ComputeNodeType }
     * 
     */
    public ComputeNodeType createComputeNodeType() {
        return new ComputeNodeType();
    }

    /**
     * Create an instance of {@link StorageType }
     * 
     */
    public StorageType createStorageType() {
        return new StorageType();
    }

    /**
     * Create an instance of {@link SubmissionSystemType }
     * 
     */
    public SubmissionSystemType createSubmissionSystemType() {
        return new SubmissionSystemType();
    }

    /**
     * Create an instance of {@link InteractiveType }
     * 
     */
    public InteractiveType createInteractiveType() {
        return new InteractiveType();
    }

    /**
     * Create an instance of {@link CloudType }
     * 
     */
    public CloudType createCloudType() {
        return new CloudType();
    }

    /**
     * Create an instance of {@link CloudPropertiesType }
     * 
     */
    public CloudPropertiesType createCloudPropertiesType() {
        return new CloudPropertiesType();
    }

    /**
     * Create an instance of {@link HttpType }
     * 
     */
    public HttpType createHttpType() {
        return new HttpType();
    }

    /**
     * Create an instance of {@link AttachedDiskType }
     * 
     */
    public AttachedDiskType createAttachedDiskType() {
        return new AttachedDiskType();
    }

    /**
     * Create an instance of {@link ImagesType }
     * 
     */
    public ImagesType createImagesType() {
        return new ImagesType();
    }

    /**
     * Create an instance of {@link BatchType }
     * 
     */
    public BatchType createBatchType() {
        return new BatchType();
    }

    /**
     * Create an instance of {@link KubernetesDeploymentType }
     * 
     */
    public KubernetesDeploymentType createKubernetesDeploymentType() {
        return new KubernetesDeploymentType();
    }

    /**
     * Create an instance of {@link ApplicationType }
     * 
     */
    public ApplicationType createApplicationType() {
        return new ApplicationType();
    }

    /**
     * Create an instance of {@link SoftwareListType }
     * 
     */
    public SoftwareListType createSoftwareListType() {
        return new SoftwareListType();
    }

    /**
     * Create an instance of {@link OSType }
     * 
     */
    public OSType createOSType() {
        return new OSType();
    }

    /**
     * Create an instance of {@link MasterNodeType }
     * 
     */
    public MasterNodeType createMasterNodeType() {
        return new MasterNodeType();
    }

    /**
     * Create an instance of {@link ComputingClusterType }
     * 
     */
    public ComputingClusterType createComputingClusterType() {
        return new ComputingClusterType();
    }

    /**
     * Create an instance of {@link PriceType }
     * 
     */
    public PriceType createPriceType() {
        return new PriceType();
    }

    /**
     * Create an instance of {@link CloudProviderType }
     * 
     */
    public CloudProviderType createCloudProviderType() {
        return new CloudProviderType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Project")
    public JAXBElement<ProjectType> createProject(ProjectType value) {
        return new JAXBElement<ProjectType>(_Project_QNAME, ProjectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClusterNodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ClusterNode", scope = ComputingClusterType.class)
    public JAXBElement<ClusterNodeType> createComputingClusterTypeClusterNode(ClusterNodeType value) {
        return new JAXBElement<ClusterNodeType>(_ComputingClusterTypeClusterNode_QNAME, ClusterNodeType.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "WorkingDir", scope = ComputingClusterType.class)
    public JAXBElement<String> createComputingClusterTypeWorkingDir(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeWorkingDir_QNAME, String.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "User", scope = ComputingClusterType.class)
    public JAXBElement<String> createComputingClusterTypeUser(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeUser_QNAME, String.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SoftwareListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Software", scope = ComputingClusterType.class)
    public JAXBElement<SoftwareListType> createComputingClusterTypeSoftware(SoftwareListType value) {
        return new JAXBElement<SoftwareListType>(_ComputingClusterTypeSoftware_QNAME, SoftwareListType.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdaptorsListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Adaptors", scope = ComputingClusterType.class)
    public JAXBElement<AdaptorsListType> createComputingClusterTypeAdaptors(AdaptorsListType value) {
        return new JAXBElement<AdaptorsListType>(_ComputingClusterTypeAdaptors_QNAME, AdaptorsListType.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "LimitOfTasks", scope = ComputingClusterType.class)
    public JAXBElement<Integer> createComputingClusterTypeLimitOfTasks(Integer value) {
        return new JAXBElement<Integer>(_ComputingClusterTypeLimitOfTasks_QNAME, Integer.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Application", scope = ComputingClusterType.class)
    public JAXBElement<ApplicationType> createComputingClusterTypeApplication(ApplicationType value) {
        return new JAXBElement<ApplicationType>(_ComputingClusterTypeApplication_QNAME, ApplicationType.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "InstallDir", scope = ComputingClusterType.class)
    public JAXBElement<String> createComputingClusterTypeInstallDir(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeInstallDir_QNAME, String.class, ComputingClusterType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OSTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Type", scope = OSType.class)
    public JAXBElement<OSTypeType> createOSTypeType(OSTypeType value) {
        return new JAXBElement<OSTypeType>(_OSTypeType_QNAME, OSTypeType.class, OSType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Version", scope = OSType.class)
    public JAXBElement<String> createOSTypeVersion(String value) {
        return new JAXBElement<String>(_OSTypeVersion_QNAME, String.class, OSType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Distribution", scope = OSType.class)
    public JAXBElement<String> createOSTypeDistribution(String value) {
        return new JAXBElement<String>(_OSTypeDistribution_QNAME, String.class, OSType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "InitialVMs", scope = CloudType.class)
    public JAXBElement<Integer> createCloudTypeInitialVMs(Integer value) {
        return new JAXBElement<Integer>(_CloudTypeInitialVMs_QNAME, Integer.class, CloudType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MinimumVMs", scope = CloudType.class)
    public JAXBElement<Integer> createCloudTypeMinimumVMs(Integer value) {
        return new JAXBElement<Integer>(_CloudTypeMinimumVMs_QNAME, Integer.class, CloudType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CloudProviderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CloudProvider", scope = CloudType.class)
    public JAXBElement<CloudProviderType> createCloudTypeCloudProvider(CloudProviderType value) {
        return new JAXBElement<CloudProviderType>(_CloudTypeCloudProvider_QNAME, CloudProviderType.class, CloudType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MaximumVMs", scope = CloudType.class)
    public JAXBElement<Integer> createCloudTypeMaximumVMs(Integer value) {
        return new JAXBElement<Integer>(_CloudTypeMaximumVMs_QNAME, Integer.class, CloudType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "InternalMemorySize", scope = ProcessorType.class)
    public JAXBElement<Float> createProcessorTypeInternalMemorySize(Float value) {
        return new JAXBElement<Float>(_ProcessorTypeInternalMemorySize_QNAME, Float.class, ProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessorPropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ProcessorProperty", scope = ProcessorType.class)
    public JAXBElement<ProcessorPropertyType> createProcessorTypeProcessorProperty(ProcessorPropertyType value) {
        return new JAXBElement<ProcessorPropertyType>(_ProcessorTypeProcessorProperty_QNAME, ProcessorPropertyType.class, ProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Speed", scope = ProcessorType.class)
    public JAXBElement<Float> createProcessorTypeSpeed(Float value) {
        return new JAXBElement<Float>(_ProcessorTypeSpeed_QNAME, Float.class, ProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Type", scope = ProcessorType.class)
    public JAXBElement<String> createProcessorTypeType(String value) {
        return new JAXBElement<String>(_OSTypeType_QNAME, String.class, ProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Architecture", scope = ProcessorType.class)
    public JAXBElement<String> createProcessorTypeArchitecture(String value) {
        return new JAXBElement<String>(_ProcessorTypeArchitecture_QNAME, String.class, ProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ComputingUnits", scope = ProcessorType.class)
    public JAXBElement<Integer> createProcessorTypeComputingUnits(Integer value) {
        return new JAXBElement<Integer>(_ProcessorTypeComputingUnits_QNAME, Integer.class, ProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "WorkingDir", scope = ImageType.class)
    public JAXBElement<String> createImageTypeWorkingDir(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeWorkingDir_QNAME, String.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "User", scope = ImageType.class)
    public JAXBElement<String> createImageTypeUser(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeUser_QNAME, String.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PackageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Package", scope = ImageType.class)
    public JAXBElement<PackageType> createImageTypePackage(PackageType value) {
        return new JAXBElement<PackageType>(_ImageTypePackage_QNAME, PackageType.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdaptorsListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Adaptors", scope = ImageType.class)
    public JAXBElement<AdaptorsListType> createImageTypeAdaptors(AdaptorsListType value) {
        return new JAXBElement<AdaptorsListType>(_ComputingClusterTypeAdaptors_QNAME, AdaptorsListType.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Application", scope = ImageType.class)
    public JAXBElement<ApplicationType> createImageTypeApplication(ApplicationType value) {
        return new JAXBElement<ApplicationType>(_ComputingClusterTypeApplication_QNAME, ApplicationType.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "LimitOfTasks", scope = ImageType.class)
    public JAXBElement<Integer> createImageTypeLimitOfTasks(Integer value) {
        return new JAXBElement<Integer>(_ComputingClusterTypeLimitOfTasks_QNAME, Integer.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "InstallDir", scope = ImageType.class)
    public JAXBElement<String> createImageTypeInstallDir(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeInstallDir_QNAME, String.class, ImageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "User", scope = AdaptorType.class)
    public JAXBElement<String> createAdaptorTypeUser(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeUser_QNAME, String.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IdentityPath", scope = AdaptorType.class)
    public JAXBElement<String> createAdaptorTypeIdentityPath(String value) {
        return new JAXBElement<String>(_AdaptorTypeIdentityPath_QNAME, String.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmissionSystemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SubmissionSystem", scope = AdaptorType.class)
    public JAXBElement<SubmissionSystemType> createAdaptorTypeSubmissionSystem(SubmissionSystemType value) {
        return new JAXBElement<SubmissionSystemType>(_AdaptorTypeSubmissionSystem_QNAME, SubmissionSystemType.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BrokerAdaptor", scope = AdaptorType.class)
    public JAXBElement<String> createAdaptorTypeBrokerAdaptor(String value) {
        return new JAXBElement<String>(_AdaptorTypeBrokerAdaptor_QNAME, String.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NIOAdaptorProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Ports", scope = AdaptorType.class)
    public JAXBElement<NIOAdaptorProperties> createAdaptorTypePorts(NIOAdaptorProperties value) {
        return new JAXBElement<NIOAdaptorProperties>(_AdaptorTypePorts_QNAME, NIOAdaptorProperties.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "KnownHostsPath", scope = AdaptorType.class)
    public JAXBElement<String> createAdaptorTypeKnownHostsPath(String value) {
        return new JAXBElement<String>(_AdaptorTypeKnownHostsPath_QNAME, String.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExternalAdaptorProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Properties", scope = AdaptorType.class)
    public JAXBElement<ExternalAdaptorProperties> createAdaptorTypeProperties(ExternalAdaptorProperties value) {
        return new JAXBElement<ExternalAdaptorProperties>(_AdaptorTypeProperties_QNAME, ExternalAdaptorProperties.class, AdaptorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "WorkingDir", scope = ComputeNodeType.class)
    public JAXBElement<String> createComputeNodeTypeWorkingDir(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeWorkingDir_QNAME, String.class, ComputeNodeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "User", scope = ComputeNodeType.class)
    public JAXBElement<String> createComputeNodeTypeUser(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeUser_QNAME, String.class, ComputeNodeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdaptorsListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Adaptors", scope = ComputeNodeType.class)
    public JAXBElement<AdaptorsListType> createComputeNodeTypeAdaptors(AdaptorsListType value) {
        return new JAXBElement<AdaptorsListType>(_ComputingClusterTypeAdaptors_QNAME, AdaptorsListType.class, ComputeNodeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Application", scope = ComputeNodeType.class)
    public JAXBElement<ApplicationType> createComputeNodeTypeApplication(ApplicationType value) {
        return new JAXBElement<ApplicationType>(_ComputingClusterTypeApplication_QNAME, ApplicationType.class, ComputeNodeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "LimitOfTasks", scope = ComputeNodeType.class)
    public JAXBElement<Integer> createComputeNodeTypeLimitOfTasks(Integer value) {
        return new JAXBElement<Integer>(_ComputingClusterTypeLimitOfTasks_QNAME, Integer.class, ComputeNodeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "InstallDir", scope = ComputeNodeType.class)
    public JAXBElement<String> createComputeNodeTypeInstallDir(String value) {
        return new JAXBElement<String>(_ComputingClusterTypeInstallDir_QNAME, String.class, ComputeNodeType.class, value);
    }

}
