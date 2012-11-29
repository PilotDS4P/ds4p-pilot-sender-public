/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */

package UtsMetathesaurusMetaData;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "UtsFault", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/")
public class UtsFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private UtsFault faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public UtsFault_Exception(String message, UtsFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public UtsFault_Exception(String message, UtsFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: UtsMetathesaurusMetaData.UtsFault
     */
    public UtsFault getFaultInfo() {
        return faultInfo;
    }

}
