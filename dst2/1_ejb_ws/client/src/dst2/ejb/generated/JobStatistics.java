
package dst2.ejb.generated;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "JobStatistics", targetNamespace = "http://webservice.dst2/", wsdlLocation = "http://localhost:8080/JobStatistics/service?wsdl")
public class JobStatistics
    extends javax.xml.ws.Service
{

    private final static URL JOBSTATISTICS_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(dst2.ejb.generated.JobStatistics.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = dst2.ejb.generated.JobStatistics.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/JobStatistics/service?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/JobStatistics/service?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        JOBSTATISTICS_WSDL_LOCATION = url;
    }

    public JobStatistics(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public JobStatistics() {
        super(JOBSTATISTICS_WSDL_LOCATION, new QName("http://webservice.dst2/", "JobStatistics"));
    }

    /**
     * 
     * @return
     *     returns Service
     */
    @WebEndpoint(name = "JobPort")
    public dst2.ejb.generated.Service getJobPort() {
        return super.getPort(new QName("http://webservice.dst2/", "JobPort"), Service.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Service
     */
    @WebEndpoint(name = "JobPort")
    public dst2.ejb.generated.Service getJobPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.dst2/", "JobPort"), Service.class, features);
    }

}
