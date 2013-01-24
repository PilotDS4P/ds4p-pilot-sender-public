/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.cas.interfaces;

import gov.va.ds4p.cas.providers.OrganizationPolicyProvider;
import gov.va.ds4p.policy.reference.OrganizationTaggingRules;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.meaningfuluse.displayobjects.OrganizationGeneralRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class OrganizationPolicyData {
    //String endpoint = "http://vmcentral:8080/DS4PACSServices/DS4PClinicallyAdaptiveRulesInterface?wsdl";

    public OrganizationPolicyData() {
        
    }
    
    public void getOrganizationPolicyRules(String oid) {
        gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy policy = null;
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
            policy = port.getOrganizationalPolicy(oid);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //create object from string
        AdminContext.getSessionAttributes().setPolicyDB(policy);
        AdminContext.getSessionAttributes().setOrganizationName(policy.getOrganizationName());
        AdminContext.getSessionAttributes().setOrganizationOid(policy.getHomeCommunityId());
        AdminContext.getSessionAttributes().setOrganizationUSLaw(policy.getApplicableUsLaw());
        
        OrganizationPolicyProvider provider = new OrganizationPolicyProvider();
        
        String sPolicy = policy.getOrganizationalRules();
        if (sPolicy == null || sPolicy.length() < 1) {
            AdminContext.getSessionAttributes().setoGRules(new ArrayList());
        }
        else {
                gov.va.ds4p.policy.reference.OrganizationPolicy xPolicy =  provider.createOrganizationPolicyObjectFromXML(policy.getOrganizationalRules());
                AdminContext.getSessionAttributes().setOrganizationPolicy(xPolicy);
                processRulesForDisplay(xPolicy);
        }
    }
    
    private void processRulesForDisplay(gov.va.ds4p.policy.reference.OrganizationPolicy xPolicy) {
        Iterator iter = xPolicy.getOrganizationTaggingRules().iterator();
        List<OrganizationGeneralRuleDisplayObject> cRules = new ArrayList();
        int ruleid = 1;
        while(iter.hasNext()) {
            OrganizationTaggingRules r = (OrganizationTaggingRules)iter.next();
            OrganizationGeneralRuleDisplayObject obj = new OrganizationGeneralRuleDisplayObject();
            obj.setRuleId(new Integer(ruleid));
            obj.setEntrySensitivity(r.getActInformationSensitivityPolicy().getCode());
            obj.setObligationPolicyEntry(r.getOrgObligationPolicyEntry().getObligationPolicy().getCode());        
            obj.setObligationPolicySection(r.getOrgObligationPolicyDocument().getObligationPolicy().getCode());
            obj.setPatientConstraint(r.getPatientSensitivityConstraint().getActInformationSensitivityPolicy().getCode());
            obj.setPatientRequestAction(r.getPatientRequestedAction().getObligationPolicy().getCode());
            obj.setPurposeOfUse(r.getActReason().getCode());
            obj.setRefrainPolicy(r.getRefrainPolicy().getCode());
            obj.setUsPrivacyLaw(r.getActUSPrivacyLaw().getCode());
            cRules.add(obj);
            ruleid++;
        }
        AdminContext.getSessionAttributes().setoGRules(cRules);
    }
    
    public Boolean saveOrganizationPolicyRules() {
        Boolean res = Boolean.TRUE;
        gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy rval = AdminContext.getSessionAttributes().getPolicyDB();
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
            res = port.setOrganizationalPolicy(rval);     
        }
        catch (Exception ex) {
            res = Boolean.FALSE;
            ex.printStackTrace();
        }
        return res;
    }
    
}
