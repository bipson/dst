
package dst2.ejb.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dst2.ejb.generated package. 
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

    private final static QName _UnknownGridFault_QNAME = new QName("http://webservice.dst2/", "UnknownGridFault");
    private final static QName _GetStatisticsResponse_QNAME = new QName("http://webservice.dst2/", "getStatisticsResponse");
    private final static QName _Gridname_QNAME = new QName("http://webservice.dst2/", "gridname");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dst2.ejb.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UnknownGridFault }
     * 
     */
    public UnknownGridFault createUnknownGridFault() {
        return new UnknownGridFault();
    }

    /**
     * Create an instance of {@link JobStatisticsDTO }
     * 
     */
    public JobStatisticsDTO createJobStatisticsDTO() {
        return new JobStatisticsDTO();
    }

    /**
     * Create an instance of {@link ExecutionDTO }
     * 
     */
    public ExecutionDTO createExecutionDTO() {
        return new ExecutionDTO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownGridFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dst2/", name = "UnknownGridFault")
    public JAXBElement<UnknownGridFault> createUnknownGridFault(UnknownGridFault value) {
        return new JAXBElement<UnknownGridFault>(_UnknownGridFault_QNAME, UnknownGridFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JobStatisticsDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dst2/", name = "getStatisticsResponse")
    public JAXBElement<JobStatisticsDTO> createGetStatisticsResponse(JobStatisticsDTO value) {
        return new JAXBElement<JobStatisticsDTO>(_GetStatisticsResponse_QNAME, JobStatisticsDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.dst2/", name = "gridname")
    public JAXBElement<String> createGridname(String value) {
        return new JAXBElement<String>(_Gridname_QNAME, String.class, null, value);
    }

}
