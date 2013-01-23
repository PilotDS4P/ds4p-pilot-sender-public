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

import gov.va.ds4p.cas.providers.MedicationsProvider;
import gov.va.ds4p.policy.reference.ClinicalTaggingRule;
import gov.va.ds4p.policy.reference.MedicationListSensitivityRules;
import gov.va.ehtac.ds4p.ws.ri.Clinicaltagrule;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.meaningfuluse.displayobjects.ClinicalRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class MedList {
    //String endpoint = "http://vmcentral:8080/DS4PACSServices/DS4PClinicallyAdaptiveRulesInterface?wsdl";
    
    
    public MedList() {
        
    }
    
    public void getMedListRules(String loincCode) {
        Clinicaltagrule rules = null;
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
            rules = port.getClinicalDomainRule(loincCode);         
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //create object from string
        if (rules.getObservationRules() == null) {
            MedicationListSensitivityRules sRules = new MedicationListSensitivityRules();
            AdminContext.getSessionAttributes().setMedRule(sRules);
            AdminContext.getSessionAttributes().setRuleObject(rules);
            processRulesForDisplay(sRules);
        }
        else {
            MedicationsProvider provider = new MedicationsProvider();
            MedicationListSensitivityRules sRules = provider.createMedicationRulesObjectFromXML(rules.getObservationRules());
            AdminContext.getSessionAttributes().setMedRule(sRules);
            AdminContext.getSessionAttributes().setRuleObject(rules);
            processRulesForDisplay(sRules);
        }
    }
    
    private void processRulesForDisplay(MedicationListSensitivityRules rule) {
        Iterator iter = rule.getClinicalTaggingRule().iterator();
        List<ClinicalRuleDisplayObject> cRules = new ArrayList();
        int ruleid = 1;
        while(iter.hasNext()) {
            ClinicalTaggingRule r = (ClinicalTaggingRule)iter.next();
            ClinicalRuleDisplayObject obj = new ClinicalRuleDisplayObject();
            obj.setRuleId(new Integer(ruleid));
            obj.setCode(r.getClinicalFact().getCode());
            obj.setPurposeOfUse(r.getActReason().getCode());
            obj.setImpliedConfidentiality(r.getImpliedConfidentiality().getConfidentiality().getCode());
            obj.setDisplayName(r.getClinicalFact().getDisplayName());
            obj.setSensitivityCode(r.getActInformationSensitivityPolicy().getCode());
            cRules.add(obj);
            ruleid++;
        }
        AdminContext.getSessionAttributes().setcRules(cRules);
    }
    
    public Boolean saveMedListRules() {
        Boolean res = Boolean.TRUE;
        Clinicaltagrule rval = AdminContext.getSessionAttributes().getRuleObject();
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
            res = port.setClinicalDomainTaggingRule(rval);       
        }
        catch (Exception ex) {
            res = Boolean.FALSE;
            ex.printStackTrace();
        }
        return res;
    }
}
