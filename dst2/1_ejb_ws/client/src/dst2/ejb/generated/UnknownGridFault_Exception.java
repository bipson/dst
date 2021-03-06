
package dst2.ejb.generated;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "UnknownGridFault", targetNamespace = "http://webservice.dst2/")
public class UnknownGridFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private UnknownGridFault faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public UnknownGridFault_Exception(String message, UnknownGridFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public UnknownGridFault_Exception(String message, UnknownGridFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dst2.ejb.generated.UnknownGridFault
     */
    public UnknownGridFault getFaultInfo() {
        return faultInfo;
    }

}
