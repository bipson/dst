
package dst2.ejb.generated;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "JobStatisticsInterface", targetNamespace = "http://webservice.dst2/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface JobStatisticsInterface {


    /**
     * 
     * @param arg0
     * @return
     *     returns dst2.ejb.generated.JobStatisticsDTO
     * @throws UnknownGridFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getStatistics", targetNamespace = "http://webservice.dst2/", className = "dst2.ejb.generated.GetStatistics")
    @ResponseWrapper(localName = "getStatisticsResponse", targetNamespace = "http://webservice.dst2/", className = "dst2.ejb.generated.GetStatisticsResponse")
    public JobStatisticsDTO getStatistics(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws UnknownGridFault_Exception
    ;

}