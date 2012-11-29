/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.ws;

import com.jerichosystems.authz.saml2xacml2.simple.SAMLBoundXACMLService;
import com.jerichosystems.authz.saml2xacml2.simple.SAMLBoundXACMLServicePortType;
import com.jerichosystems.authz.saml2xacml2.simple.assertion.AssertionType;
import com.jerichosystems.authz.saml2xacml2.simple.assertion.NameIDType;
import com.jerichosystems.authz.saml2xacml2.simple.context.*;
import com.jerichosystems.authz.saml2xacml2.simple.policy.ObligationsType;
import com.jerichosystems.authz.saml2xacml2.simple.protocol.XACMLAuthzDecisionQueryType;
import gov.va.ds4p.cas.constants.DS4PConstants;
import gov.va.ehtac.ds4p.jpa.AuthLog;
import java.io.StringWriter;
import java.util.*;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.oasis.names.tc.xspa.v2.PolicyEnforcementObject;
import org.oasis.names.tc.xspa.v2.XacmlResultType;
import org.oasis.names.tc.xspa.v2.XacmlStatusDetailType;
import org.oasis.names.tc.xspa.v2.XacmlStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Duane DeCouteau
 */
@WebService(serviceName = "NwHINDirectAuthorizationServices")
public class NwHINDirectAuthorizationServices {
    ///private static final Logger log = LoggerFactory.getLogger(NwHINDirectAuthorizationServices.class);    
    
    private XACMLAuthzDecisionQueryType query = new XACMLAuthzDecisionQueryType();
    
    private Date requeststart;
    private Date requestcomplete;

    private static DatatypeFactory dateFac;
    static {
        try{
            dateFac = DatatypeFactory.newInstance();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private String recipientId;;
    private String userId;
    private String organization;
    private String organizationId;
    private String role;
    private String purposeOfUse;
    private String requestedResource;
    private List<String> servicePermissions = new ArrayList();
    private List<String> sensitivityPrivileges = new ArrayList();
    private String intendedRecipient;
    private String intendedPurposeOfUse;
    private List<String> requiredPermissions;
    private List<String> requiredPrivileges;
    private String uniquePatientId;
    
    private String pdpEndpoint = "http://75.145.119.97/SAMLBoundXACMLService/services/SAMLBoundXACMLService";
    //private String pdpEndpoint = "http://vmcentral:11013/SAMLBoundXACMLService/services/SAMLBoundXACMLService";
    
    private ResultType resxacml;
    
    private DS4PAudit auditservice = new DS4PAudit();
    
    private PolicyEnforcementObject obj = new PolicyEnforcementObject();

    /**
     * Web service operation
     */
    @WebMethod(operationName = "enforceDirectPolicy")
    public String enforceDirectPolicy(@WebParam(name = "providerId") String providerId, @WebParam(name = "recipientId") String recipientId, 
                                @WebParam(name = "organizationName") String organizationName, 
                                @WebParam(name = "organizationId") String organizationId, 
                                @WebParam(name = "role") String role, 
                                @WebParam(name = "purposeOfUse") String purposeOfUse, 
                                @WebParam(name = "servicePermissions") List<String> servicePermissions, 
                                @WebParam(name = "sensitivityPrivileges") List<String> sensitivityPrivileges, 
                                @WebParam(name = "intendedPurposeOfUse") String intendedPurposeOfUse, 
                                @WebParam(name = "intendedRecipient") String intendedRecipient, 
                                @WebParam(name = "requiredPermission") List<String> requiredPermission, 
                                @WebParam(name = "requiredSensitivityPrivileges") List<String> requiredSensitivityPrivileges, 
                                @WebParam(name = "requestedResource") String requestedResource, 
                                @WebParam(name = "uniquePatientId") String uniquePatientId) {
        //TODO write your implementation code here:
        this.recipientId = recipientId;
        this.userId = providerId;
        this.organization = organizationName;
        this.organizationId = organizationId;
        this.role = role;
        this.purposeOfUse = purposeOfUse;
        this.requestedResource = requestedResource;
        this.servicePermissions = servicePermissions;
        this.sensitivityPrivileges = sensitivityPrivileges;
        this.intendedPurposeOfUse = intendedPurposeOfUse;
        this.intendedRecipient  = intendedRecipient;
        this.requiredPermissions = requiredPermission;
        this.requiredPrivileges = requiredSensitivityPrivileges;
        this.uniquePatientId = uniquePatientId;
        obj = new PolicyEnforcementObject();
        this.requeststart = new Date();

        try {
            
                query = new XACMLAuthzDecisionQueryType();
                setXACMLRequest();
                resxacml = getXACMLPDPDecision();

                DecisionType d = resxacml.getDecision();
                StatusType s = resxacml.getStatus();
                ObligationsType o = resxacml.getObligations();

                XacmlResultType x = new XacmlResultType();
                x.setXacmlResultTypeDecision(d.value());
                x.setXacmlResultTypeResourceId(resxacml.getResourceId());

                XacmlStatusType xStat = new XacmlStatusType();
                xStat.setXacmlStatusCodeType(s.getStatusCode().getValue());
                xStat.setXacmlStatusMessage(s.getStatusMessage());
                XacmlStatusDetailType detail = new XacmlStatusDetailType();

                if (s.getStatusDetail() != null) {
                    List<Object> sObjs = s.getStatusDetail().getAny();
                    Iterator sObjsIter = sObjs.iterator();
                    while (sObjsIter.hasNext()) {
                        String objs = (String) sObjsIter.next();
                        detail.getXacmlStatusDetail().add(objs);
                    }
                }

                obj.setPdpDecision(d.value());
                obj.setPdpStatus("ok");
                //the following is a work around for XACML 2.0
                obj.getPdpObligation().clear();
                obj.setPdpRequest(dumpRequestToString());
                obj.setPdpResponse(dumpResponseToString(resxacml));
                obj.setRequestTime(convertDateToXMLGregorianCalendar(requeststart));
                obj.setResponseTime(convertDateToXMLGregorianCalendar(new Date()));
                obj.setResourceName(requestedResource);
                obj.setResourceId(uniquePatientId);
                obj.setHomeCommunityId(organizationId);
                //obj.setXacmlResultType(x);                
        }
        catch (Exception ex) {
            obj.setPdpDecision(DS4PConstants.INDETERMINATE);
            obj.setPdpStatus("Error Processing Kairon Request: "+ex.getMessage());
            obj.setRequestTime(getCurrentDateTime());
            obj.setResponseTime(getCurrentDateTime());
            obj.setResourceId(uniquePatientId);
            obj.setResourceName(requestedResource);
            obj.setHomeCommunityId(organizationId);
            ex.printStackTrace();
        }
        this.requestcomplete = new Date();
        logEvent();
        return obj.getPdpDecision();
        
    }
    
   private void setXACMLRequest() {
        query = new XACMLAuthzDecisionQueryType();
        query.setInputContextOnly(Boolean.TRUE);
        query.setReturnContext(Boolean.TRUE);
        query.setConsent("my consent");
        query.setDestination("my destination");
        UUID msg = UUID.randomUUID();
        
        query.setID(msg.toString());
        query.setIssueInstant(dateFac.newXMLGregorianCalendar());
        query.setVersion("2.0");
        query.setIssuer(new NameIDType());

        //Test DoD to VA
        RequestType rt = new RequestType();
        SubjectType st = new SubjectType();
        ActionType act = new ActionType();
        ResourceType resource = new ResourceType();
        EnvironmentType environment = new EnvironmentType();
        //Subject Information Identifier
        AttributeType at = new AttributeType();
        at.setAttributeId(DS4PConstants.SUBJECT_ID_NS);
        at.setDataType(DS4PConstants.STRING);
        AttributeValueType avt = new AttributeValueType();
        avt.getContent().add(recipientId);
        at.getAttributeValue().add(avt);
        st.getAttribute().add(at);
        //Subject Purpose of Use
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.SUBJECT_PURPOSE_OF_USE_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(purposeOfUse);
        at.getAttributeValue().add(avt);
        st.getAttribute().add(at);
        //Subject Home Community Identifier
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.SUBJECT_LOCALITY_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(organizationId);
        at.getAttributeValue().add(avt);
        st.getAttribute().add(at);
        //Subject ROLE
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.SUBJECT_STRUCTURED_ROLE_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(role);
        at.getAttributeValue().add(avt);
        st.getAttribute().add(at);

        //the next subject attributes may be informational only as our focus on nwhin is homeCommunity...
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.SUBJECT_ORGANIZATION_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(organization);
        at.getAttributeValue().add(avt);
        st.getAttribute().add(at);
        //this is location-clinic placeholder
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.SUBJECT_ORGANIZATION_ID_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add("Test Facility");
        at.getAttributeValue().add(avt);
        st.getAttribute().add(at);
        //add subject resource permissions
        // add organization obligations
        if (!servicePermissions.isEmpty()) {
            Iterator iter = servicePermissions.iterator();
            at = new AttributeType();
            at.setAttributeId(DS4PConstants.SUBJECT_SERVICE_PERMISSIONS);
            at.setDataType(DS4PConstants.STRING);
            while (iter.hasNext()) {
                String s = (String)iter.next();
                avt = new AttributeValueType();
                avt.getContent().add(s);
                at.getAttributeValue().add(avt);
            }
            if (!at.getAttributeValue().isEmpty()) st.getAttribute().add(at);
        }
        
        if (!sensitivityPrivileges.isEmpty()) {
            Iterator iter = sensitivityPrivileges.iterator();
            at = new AttributeType();
            at.setAttributeId(DS4PConstants.SUBJECT_SENSITIVITY_PRIVILEGES);
            at.setDataType(DS4PConstants.STRING);
            while (iter.hasNext()) {
                String s = (String)iter.next();
                avt = new AttributeValueType();
                avt.getContent().add(s);
                at.getAttributeValue().add(avt);
            }
            if (!at.getAttributeValue().isEmpty()) st.getAttribute().add(at);
        }
        
        //Set Resource Attributes - Organization and Region
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.RESOURCE_NWHIN_SERVICE_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(requestedResource);
        at.getAttributeValue().add(avt);
        resource.getAttribute().add(at);

        at = new AttributeType();
        at.setAttributeId(DS4PConstants.RESOURCE_LOCALITY_NS);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(organizationId);
        at.getAttributeValue().add(avt);
        resource.getAttribute().add(at);

//        //Kairon Patient Consent
//        at = new AttributeType();
//        at.setAttributeId(DS4PConstants.MITRE_PATIENT_AUTHORIZATION);
//        at.setDataType(DS4PConstants.STRING);
//        avt = new AttributeValueType();
//        avt.getContent().add(kaironauthz);
//        at.getAttributeValue().add(avt);
//        resource.getAttribute().add(at);
        //intended recipient
        
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.RESOURCE_INTENDED_RECIPIENT);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(intendedRecipient);
        at.getAttributeValue().add(avt);
        resource.getAttribute().add(at);
        
        at = new AttributeType();
        at.setAttributeId(DS4PConstants.RESOURCE_INTENDED_PURPOSEOFUSE);
        at.setDataType(DS4PConstants.STRING);
        avt = new AttributeValueType();
        avt.getContent().add(intendedPurposeOfUse);
        at.getAttributeValue().add(avt);
        resource.getAttribute().add(at);
        
        // add organization obligations
        if (!requiredPermissions.isEmpty()) {
            Iterator iter = requiredPermissions.iterator();
            at = new AttributeType();
            at.setAttributeId(DS4PConstants.RESOURCE_SERVICE_PERMISSIONS);
            at.setDataType(DS4PConstants.STRING);
            while (iter.hasNext()) {
                String s = (String)iter.next();
                avt = new AttributeValueType();
                avt.getContent().add(s);
                at.getAttributeValue().add(avt);
            }
            if (!at.getAttributeValue().isEmpty()) resource.getAttribute().add(at);
        }

        // add patient obligations
        if (!requiredPrivileges.isEmpty()) {
            Iterator iter = requiredPrivileges.iterator();
            at = new AttributeType();
            at.setAttributeId(DS4PConstants.RESOURCE_SENSITIVITY_PERMISSIONS);
            at.setDataType(DS4PConstants.STRING);
            while (iter.hasNext()) {
                String s = (String)iter.next();
                avt = new AttributeValueType();
                avt.getContent().add(s);
                at.getAttributeValue().add(avt);
            }
            if (!at.getAttributeValue().isEmpty()) resource.getAttribute().add(at);
        }

        rt.getSubject().add(st);
        rt.getResource().add(resource);
        rt.setAction(act);
        rt.setEnvironment(environment);
        
        query.setRequest(rt);   
   }    
    
    private ResultType getXACMLPDPDecision() {
        ResultType res = null;
        try {
            SAMLBoundXACMLService svc = new SAMLBoundXACMLService();
            SAMLBoundXACMLServicePortType port = svc.getSAMLBoundXACMLServicePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pdpEndpoint);
//            if (securedMode) ((BindingProvider)port).getRequestContext().put(com.sun.xml.ws.developer.JAXWSProperties.SSL_SOCKET_FACTORY, proxy);
            

            com.jerichosystems.authz.saml2xacml2.simple.protocol.ResponseType resp = port.xacmlAuthzDecisionQuery(query);
            AssertionType aT = (AssertionType)resp.getAssertionOrEncryptedAssertion().get(0);
            com.jerichosystems.authz.saml2xacml2.simple.assertion.XACMLAuthzDecisionStatementType stmt = (com.jerichosystems.authz.saml2xacml2.simple.assertion.XACMLAuthzDecisionStatementType) aT.getStatementOrAuthnStatementOrAuthzDecisionStatement().get(0);
            com.jerichosystems.authz.saml2xacml2.simple.context.ResponseType resptype = stmt.getResponse();
            res = resptype.getResult().get(0);
        }
        catch (Exception e) {
            //log.warn("Authorization Decision Not Available",e);
           e.printStackTrace();
        }
        return res;
    }
    private XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date dt) {
        XMLGregorianCalendar xcal = null;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dt);
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        }
        catch (Exception ex) {
            //log.warn("", ex);
            ex.printStackTrace();
        }
        return xcal;
    }
    private String dumpRequestToString() {   
        String res = "";
        JAXBElement<RequestType> element = new JAXBElement<RequestType>(new QName("urn:oasis:names:tc:xacml:2.0:context:schema:os", "Request"), RequestType.class, query.getRequest());
        try {
            JAXBContext context = JAXBContext.newInstance(RequestType.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.marshal(element, sw);
            res = sw.toString();
        }
        catch (Exception ex) {
            //log.warn("Unable to Dump Request ToString", ex);
            ex.printStackTrace();
        }
        return res;
    }
    
    private String dumpResponseToString(ResultType resp) {
        String res = "";
        JAXBElement<ResultType> element = new JAXBElement<ResultType>(new QName("urn:oasis:names:tc:xacml:2.0:context:schema:os", "Result"), ResultType.class, resp);
        try {
            JAXBContext context = JAXBContext.newInstance(ResultType.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.marshal(element, sw);
            res = sw.toString();
        }
        catch (Exception ex) {
            //log.warn("Unable to Dump Response toString",ex);
            ex.printStackTrace();
        }  
        return res;
    }
   private void logEvent() {
            AuthLog log = new AuthLog();
                //log.setDecision(d.value());
                log.setHealthcareObject(requestedResource);
                log.setMsgDate(new Date());
                StringBuffer sb = new StringBuffer();
                if (!obj.getPdpObligation().isEmpty()) {
                    Iterator iter = obj.getPdpObligation().iterator();
                    while (iter.hasNext()) {
                        String obS = (String)iter.next() + "\n";
                        sb.append(obS);
                    }
                    log.setObligations(sb.toString());
                }
                else {
                    log.setObligations("");
                }
                log.setDecision(obj.getPdpDecision());
                log.setPurposeOfUse(purposeOfUse);
                log.setRequestor(recipientId);
                log.setUniqueIdentifier(uniquePatientId);
                log.setXacmlRequest(dumpRequestToString());
                //log.setXacmlRequest("");
                log.setXacmlResponse(dumpResponseToString(resxacml));
                //log.setXacmlResponse("");
                long startTime = requeststart.getTime();
                long endTime = requestcomplete.getTime();
                long respTime = endTime - startTime;
                log.setHieMsgId(query.getID());

                log.setServicingOrg(organizationId);

                log.setResponseTime(respTime);
                
            //log.setHieMsgId(currSubject.getMessageId());
            logEvent(log);
       
   }  
   
   private void logEvent(AuthLog authlog) {
      try {
          auditservice.persist(authlog);
      }
      catch (Exception ex) {
          ex.printStackTrace();
      }
   }
   private XMLGregorianCalendar getCurrentDateTime() {
       XMLGregorianCalendar xgc = null;
       try {
        GregorianCalendar gc = new GregorianCalendar();
        DatatypeFactory dtf = DatatypeFactory.newInstance();
        xgc = dtf.newXMLGregorianCalendar(gc);
       }
       catch (Exception ex) {
           ex.printStackTrace();
       }
        return xgc;
   }
   
}
