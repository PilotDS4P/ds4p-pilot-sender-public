/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package gov.samhsa.schemas.client;

import gov.va.ehtac.ds4p.ws.DS4PAudit;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.0
 * 2012-09-24T16:49:28.851-04:00
 * Generated source version: 2.6.0
 * 
 */

@javax.jws.WebService(
                      serviceName = "DS4PAuditService",
                      portName = "DS4PAuditPort",
                      targetNamespace = "http://ws.ds4p.ehtac.va.gov/",
                      wsdlLocation = "classpath:DS4PAuditService.wsdl",
                      endpointInterface = "gov.va.ehtac.ds4p.ws.DS4PAudit")
                      
public class DS4PAuditImpl implements DS4PAudit {

    private static final Logger LOG = Logger.getLogger(DS4PAuditImpl.class.getName());
    
    protected static boolean returnedValueOfUpdateAuthorizationEventWithAnnotatedDoc;
    protected static boolean returnedValueOfupdateAuthorizationEventWithExecRules;

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#getAllAuthorizationEvent(*
     */
    public java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> getAllAuthorizationEvent() { 
        LOG.info("Executing operation getAllAuthorizationEvent");
        try {
            java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#getAuthorizationEventByHIEMsgId(java.lang.String  hieMsgId )*
     */
    public gov.va.ehtac.ds4p.ws.AuthLog getAuthorizationEventByHIEMsgId(java.lang.String hieMsgId) { 
        LOG.info("Executing operation getAuthorizationEventByHIEMsgId");
        System.out.println(hieMsgId);
        try {
            gov.va.ehtac.ds4p.ws.AuthLog _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#getAuthorizationByStartEndDates(java.lang.String  startdate ,)java.lang.String  enddate )*
     */
    public java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> getAuthorizationByStartEndDates(java.lang.String startdate,java.lang.String enddate) { 
        LOG.info("Executing operation getAuthorizationByStartEndDates");
        System.out.println(startdate);
        System.out.println(enddate);
        try {
            java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#getAuthorizationByHealthCareObject(java.lang.String  healthcareobject )*
     */
    public java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> getAuthorizationByHealthCareObject(java.lang.String healthcareobject) { 
        LOG.info("Executing operation getAuthorizationByHealthCareObject");
        System.out.println(healthcareobject);
        try {
            java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#updateAuthorizationEventWithAnnotatedDoc(java.lang.String  hieMsgId ,)java.lang.String  doc )*
     */
    public java.lang.Boolean updateAuthorizationEventWithAnnotatedDoc(java.lang.String hieMsgId,java.lang.String doc) { 
        LOG.info("Executing operation updateAuthorizationEventWithAnnotatedDoc");
        System.out.println(hieMsgId);
        System.out.println(doc);
        try {
            java.lang.Boolean _return = returnedValueOfUpdateAuthorizationEventWithAnnotatedDoc;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#getAuthorizationByUId(java.lang.String  uniqueId )*
     */
    public java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> getAuthorizationByUId(java.lang.String uniqueId) { 
        LOG.info("Executing operation getAuthorizationByUId");
        System.out.println(uniqueId);
        try {
            java.util.List<gov.va.ehtac.ds4p.ws.AuthLog> _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#updateAuthorizationEventWithExecRules(java.lang.String  hieMsgId ,)java.lang.String  execRules )*
     */
    public java.lang.Boolean updateAuthorizationEventWithExecRules(java.lang.String hieMsgId,java.lang.String execRules) { 
        LOG.info("Executing operation updateAuthorizationEventWithExecRules");
        System.out.println(hieMsgId);
        System.out.println(execRules);
        try {
            java.lang.Boolean _return =returnedValueOfupdateAuthorizationEventWithExecRules;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#saveAuthorizationEvent(gov.va.ehtac.ds4p.ws.AuthLog  authobj )*
     */
    public java.lang.Boolean saveAuthorizationEvent(gov.va.ehtac.ds4p.ws.AuthLog authobj) { 
        LOG.info("Executing operation saveAuthorizationEvent");
        System.out.println(authobj);
        try {
            java.lang.Boolean _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.va.ehtac.ds4p.ws.DS4PAudit#updateAuthorizationEventWithDRL(java.lang.String  hieMsgId ,)java.lang.String  genDrl )*
     */
    public java.lang.Boolean updateAuthorizationEventWithDRL(java.lang.String hieMsgId,java.lang.String genDrl) { 
        LOG.info("Executing operation updateAuthorizationEventWithDRL");
        System.out.println(hieMsgId);
        System.out.println(genDrl);
        try {
            java.lang.Boolean _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}

