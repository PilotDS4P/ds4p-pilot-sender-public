/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.ws;

import gov.va.ds4p.cas.providers.ClinicalDocumentProvider;
import gov.va.ds4p.cas.providers.OrganizationPolicyProvider;
import gov.va.ds4p.cas.providers.XACMLPolicyProviderForCDA;
import gov.va.ds4p.policy.reference.OrganizationPolicy;
import gov.va.ehtac.ds4p.jpa.OrganizationalPolicy;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Duane DeCouteau
 */
@WebService(serviceName = "CDAR2ConsentDirective")
public class CDAR2ConsentDirective {
    DS4PClinicallyAdaptiveRulesInterface rule = new DS4PClinicallyAdaptiveRulesInterface();
    OrganizationPolicyProvider provider = new OrganizationPolicyProvider();
    ClinicalDocumentProvider cProvider = new ClinicalDocumentProvider();
    XACMLPolicyProviderForCDA xProvider = new XACMLPolicyProviderForCDA();
    

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCDAR2ConsentDirective")
    public byte[] getCDAR2ConsentDirective(@WebParam(name = "patientName") String patientName, @WebParam(name = "patientId") String patientId, 
                                           @WebParam(name = "patientIdType") String patientIdType, @WebParam(name = "patientGender") String patientGender, 
                                           @WebParam(name = "patientDateOfBirth") String patientDateOfBirth, @WebParam(name = "authorization") String authorization, 
                                           @WebParam(name = "primaryPOU") String primaryPOU, @WebParam(name = "allowedPOU") List<String> allowedPOU, 
                                           @WebParam(name = "intendedRecipient") String intendedRecipient, 
                                           @WebParam(name = "allowedRecipients") List<String> allowedRecipients, @WebParam(name = "maskingActions") List<String> maskingActions, 
                                           @WebParam(name = "redactActions") List<String> redactActions, @WebParam(name = "homeCommunityId") String homeCommunityId) {
        //TODO write your implementation code here:
        
        System.out.println(patientName +" "+patientId+" "+patientIdType+" "+patientGender+" "+patientDateOfBirth+" "+authorization+" "+primaryPOU+" "+intendedRecipient+" "+homeCommunityId);
        Iterator iter = allowedPOU.iterator();
        String res = "";
        while (iter.hasNext()) {
            res = res + (String)iter.next()+" "; 
        }
        System.out.println("ALLOWED POUS "+res);
        
        iter = allowedRecipients.iterator();
        res = "";
        while (iter.hasNext()) {
            res = res + (String)iter.next()+" "; 
        }
        System.out.println("ALLOWED Recipients "+res);
        
        iter = maskingActions.iterator();
        res = "";
        while (iter.hasNext()) {
            res = res + (String)iter.next()+" "; 
        }
        System.out.println("MASK ACTIONS "+res);
        
        iter = redactActions.iterator();
        res = "";
        while (iter.hasNext()) {
            res = res + (String)iter.next()+" "; 
        }
        System.out.println("REDACT ACTIONS "+res);
        
        DS4PClinicallyAdaptiveRulesInterface rule = new DS4PClinicallyAdaptiveRulesInterface();
        
        OrganizationalPolicy p = rule.getOrganizationalPolicy(homeCommunityId);
        OrganizationPolicy p2 = provider.createOrganizationPolicyObjectFromXML(p.getOrganizationalRules());
        
        String pdfText = "test pdf";
        String xacmlText = xProvider.createPatientConsentXACMLPolicy(p2, patientId, authorization, allowedPOU, allowedRecipients, maskingActions);
        System.out.println("XACML:\n"+xacmlText);
        
        
        try {
            pdfText = p2.getOrganizationConsentPolicyInfo().getHumanReadibleText().getDisplayText();
        }
        catch (Exception px) {
            px.printStackTrace();
        }
        
        res = cProvider.createConsentDirective(p2, patientName, patientId, patientIdType, patientDateOfBirth, 
                                                patientGender, authorization, primaryPOU, allowedPOU, 
                                                intendedRecipient, allowedRecipients, maskingActions, redactActions, pdfText, xacmlText);
        
        
        //System.out.println("CDA RESULTS: "+res);
        
        return res.getBytes();
    }
    
    
}
