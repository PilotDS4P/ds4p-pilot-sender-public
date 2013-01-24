/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.ruleprocessing;

import gov.va.ds4p.cas.providers.ProblemListProvider;
import gov.va.ds4p.cas.providers.OrganizationPolicyProvider;
import gov.va.ds4p.cas.constants.DS4PConstants;
import gov.va.ds4p.cas.providers.MedicationsProvider;
import gov.va.ds4p.policy.reference.*;
import gov.va.ehtac.ds4p.jpa.Clinicaltagrule;
import gov.va.ehtac.ds4p.jpa.OrganizationalPolicy;
import gov.va.ehtac.ds4p.ws.DS4PClinicallyAdaptiveRulesInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class RuleGenerator {
    private DS4PClinicallyAdaptiveRulesInterface rIntf = new DS4PClinicallyAdaptiveRulesInterface();
    private OrganizationPolicyProvider oProvider = new OrganizationPolicyProvider();
    private OrganizationPolicy policy;
    private StringBuffer gSB = new StringBuffer();
    private String purposeofuse = "";
    private String section = "";
    private List<String> obligations = new ArrayList();
    private String homeCommunityId;
    
    //from organization policy 
    private List<OrganizationTaggingRules> orgTags = new ArrayList();
    private ClinicalTaggingRule currentClinicalRule;
    private String currentSensitivity;
    private String currentItemAction;
    private String currentImpliedConfidentiality;
    private String currentUSPrivacyLaw;
    private String curentEntryObligation;
    private String currentDocumentObligation;
    private String currentRefrainPolicy;
    private boolean patientMasking = false;
    private boolean patientRedaction = false;
    
            
    
    public String GenerateRule(String purposeofuse, List<String> obligations, String servicingOrgHCId) {
        this.purposeofuse = purposeofuse;
        this.homeCommunityId = servicingOrgHCId;
        this.obligations = obligations;
        
        //log input parameters
        System.out.println("Rule Input PURPOSEOFUSE: "+purposeofuse);
        System.out.println("Rule HC ID: "+servicingOrgHCId);
        Iterator iter = obligations.iterator();
        while (iter.hasNext()) {
            String s = (String)iter.next();
            System.out.println("Rule Patient OBLIGATION: "+s);
        }
        
        //get organization policy
        getOrganizationalPolicies();
        
        gSB.append(getHeader());
        String rule = "";
        getClinicalRules(DS4PConstants.PROBLEM_LIST_LOINC_CODE);
//        getClinicalRules(DS4PConstants.ALLERGY_ADVERSE_REACTIONS_LOINC_CODE);
        getClinicalRules(DS4PConstants.MEDICATION_LIST_LOINC_CODE);
//        getClinicalRules(DS4PConstants.LAB_RESULTS_LOINC_CODE);
        
        rule = gSB.toString();
        return rule;
    }
    
    private void getClinicalRules(String loinc) {
        Clinicaltagrule dbrule;
        dbrule = rIntf.getClinicalDomainRule(loinc);
        String obsrules = dbrule.getObservationRules();
        section = loinc;
        
        if (obsrules != null) {
            if (DS4PConstants.PROBLEM_LIST_LOINC_CODE.equals(loinc)) {     
                ProblemListProvider p = new ProblemListProvider();
                ProblemListSensitivityRules rules = p.createProblemListSensitivityRulesObjectFromXML(obsrules);

                Iterator iter = rules.getClinicalTaggingRule().iterator();
                int ruleid = 1;
                while(iter.hasNext()) {
                    currentClinicalRule = (ClinicalTaggingRule)iter.next();
                    String rS = "";
                    String pou = currentClinicalRule.getActReason().getCode();
                    //System.out.println("DOES POU MATCH: -"+purposeofuse+"-"+pou+"-");
                    if (pou.equals(purposeofuse)) {
                        //pou matches so gen rules based on org policy

                        currentSensitivity = currentClinicalRule.getActInformationSensitivityPolicy().getCode();
                        setPatientConstraints();                    
                        processOrganizationalPolicies();

                    }
                }
            }
            else if (DS4PConstants.MEDICATION_LIST_LOINC_CODE.equals(loinc)) {
                MedicationsProvider p = new MedicationsProvider();
                MedicationListSensitivityRules rules = p.createMedicationRulesObjectFromXML(obsrules);

                Iterator iter = rules.getClinicalTaggingRule().iterator();
                int ruleid = 1;
                while(iter.hasNext()) {
                    currentClinicalRule = (ClinicalTaggingRule)iter.next();
                    String rS = "";
                    String pou = currentClinicalRule.getActReason().getCode();
                    //System.out.println("DOES POU MATCH: -"+purposeofuse+"-"+pou+"-");
                    if (pou.equals(purposeofuse)) {
                        //pou matches so gen rules based on org policy

                        currentSensitivity = currentClinicalRule.getActInformationSensitivityPolicy().getCode();
                        setPatientConstraints();                    
                        processOrganizationalPolicies();

                    }
                }                
            }
            else {
                //do nothing
            }
        }
        
    }
    
    private String getHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("package gov.samhsa.ds4ppilot.hcs.rules;\n");
        sb.append("\n");
        sb.append("import gov.samhsa.ds4ppilot.common.beans.XacmlResult;\n");
        sb.append("import gov.samhsa.ds4ppilot.hcs.beans.ClinicalFact;\n");
        sb.append("import gov.samhsa.ds4ppilot.hcs.beans.Confidentiality;\n");
        sb.append("import gov.va.ds4p.cas.RuleExecutionResponse;\n");
        sb.append("\n");
        sb.append("global gov.samhsa.ds4ppilot.common.beans.RuleExecutionContainer ruleExecutionContainer;\n");
        sb.append("\n");
        return sb.toString();
    }
            
    private void getOrganizationalPolicies() {
        try {
            OrganizationalPolicy orgP = rIntf.getOrganizationalPolicy(homeCommunityId);
            policy = oProvider.createOrganizationPolicyObjectFromXML(orgP.getOrganizationalRules());
            Iterator iter = policy.getOrganizationTaggingRules().iterator();
            orgTags = new ArrayList();
            while (iter.hasNext()) {
                OrganizationTaggingRules r = (OrganizationTaggingRules)iter.next();
                if (r.getActReason().getCode().equals(purposeofuse)) {
                    orgTags.add(r);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    
    
    private String getRuleStringREDACT() {
        StringBuffer sb = new StringBuffer();
        sb.append("rule \"Clinical Rule "+currentClinicalRule.getClinicalFact().getDisplayName()+" REDACT\"\n");
        sb.append("dialect \"mvel\"\n");
        sb.append("when\n");
        sb.append("\n");
        sb.append("\t $xacml : XacmlResult(subjectPurposeOfUse ==\""+purposeofuse+"\", eval(\n");
        sb.append("\t\t pdpObligations.contains(\""+DS4PConstants.PATIENT_REDACT_CONSTRUCT+currentSensitivity+"\")\n");
        sb.append("\t))\n");
        sb.append("\t $cd : ClinicalFact(codeSystem == \""+currentClinicalRule.getClinicalFact().getCodeSystem()+"\", code == \""+currentClinicalRule.getClinicalFact().getCode()+"\", c32SectionLoincCode == \""+section+"\")\n");
        sb.append("\n");
        sb.append("then\n");
        sb.append("\n");
        sb.append("\t ruleExecutionContainer.addExecutionResponse(new RuleExecutionResponse(\""+currentClinicalRule.getActInformationSensitivityPolicy().getCode()+"\", \"REDACT\", (String)Confidentiality."+currentClinicalRule.getImpliedConfidentiality().getConfidentiality().getCode()+", \""+currentUSPrivacyLaw+"\", \""+currentDocumentObligation+"\", \""+currentRefrainPolicy+"\", $cd.c32SectionTitle, $cd.c32SectionLoincCode, $cd.observationId, $cd.code, $cd.displayName, \""+currentClinicalRule.getClinicalFact().getCodeSystemName()+"\"))\n");
        sb.append("\n");
        sb.append("end\n");
        sb.append("\n");
        return sb.toString();
    }
    
    private String getRuleStringMask() {
        StringBuffer sb = new StringBuffer();
        sb.append("rule \"Clinical Rule "+currentClinicalRule.getClinicalFact().getDisplayName()+" MASK\"\n");
        sb.append("dialect \"mvel\"\n");
        sb.append("when\n");
        sb.append("\n");
        sb.append("\t $xacml : XacmlResult(subjectPurposeOfUse ==\""+purposeofuse+"\", eval(\n");
        sb.append("\t\t pdpObligations.contains(\""+DS4PConstants.PATIENT_MASK_CONSTRUCT+currentSensitivity+"\")\n");
        sb.append("\t ))\n");
        sb.append("\t $cd : ClinicalFact(codeSystem == \""+currentClinicalRule.getClinicalFact().getCodeSystem()+"\", code == \""+currentClinicalRule.getClinicalFact().getCode()+"\", c32SectionLoincCode == \""+section+"\")\n");
        sb.append("\n");
        sb.append("then\n");
        sb.append("\n");
        sb.append("\t ruleExecutionContainer.addExecutionResponse(new RuleExecutionResponse(\""+currentClinicalRule.getActInformationSensitivityPolicy().getCode()+"\", \"MASK\", (String)Confidentiality."+currentClinicalRule.getImpliedConfidentiality().getConfidentiality().getCode()+", \""+currentUSPrivacyLaw+"\", \""+currentDocumentObligation+"\", \""+currentRefrainPolicy+"\", $cd.c32SectionTitle, $cd.c32SectionLoincCode, $cd.observationId, $cd.code, $cd.displayName, \""+currentClinicalRule.getClinicalFact().getCodeSystemName()+"\"))\n");
        sb.append("\n");
        sb.append("end\n");
        sb.append("\n");
        return sb.toString();
    }
    
    private String getRuleStringNoPatientConstraints() {
        StringBuffer sb = new StringBuffer();
        sb.append("rule \"Clinical Rule "+currentClinicalRule.getClinicalFact().getDisplayName()+" NO PATIENT CONSTRAINTS\"\n");
        sb.append("dialect \"mvel\"\n");
        sb.append("when\n");
        sb.append("\n");
        sb.append("\t $xacml : XacmlResult(subjectPurposeOfUse ==\""+purposeofuse+"\", eval(\n");
        sb.append("\t\t !pdpObligations.contains(\""+DS4PConstants.PATIENT_REDACT_CONSTRUCT+currentSensitivity+"\") &&\n");
        sb.append("\t\t !pdpObligations.contains(\""+DS4PConstants.PATIENT_MASK_CONSTRUCT+currentSensitivity+"\")\n");        
        sb.append("\t ))\n");
        sb.append("\t $cd : ClinicalFact(codeSystem == \""+currentClinicalRule.getClinicalFact().getCodeSystem()+"\", code == \""+currentClinicalRule.getClinicalFact().getCode()+"\", c32SectionLoincCode == \""+section+"\")\n");
        sb.append("\n");
        sb.append("then\n");
        sb.append("\n");
        sb.append("\t ruleExecutionContainer.addExecutionResponse(new RuleExecutionResponse(\""+currentClinicalRule.getActInformationSensitivityPolicy().getCode()+"\", \""+currentItemAction+"\", (String)Confidentiality."+currentClinicalRule.getImpliedConfidentiality().getConfidentiality().getCode()+", \""+currentUSPrivacyLaw+"\", \""+currentDocumentObligation+"\", \""+currentRefrainPolicy+"\", $cd.c32SectionTitle, $cd.c32SectionLoincCode, $cd.observationId, $cd.code, $cd.displayName, \""+currentClinicalRule.getClinicalFact().getCodeSystemName()+"\"))\n");
        sb.append("\n");
        sb.append("end\n");
        sb.append("\n");
        return sb.toString();
    }
    
    private String getRuleStringIgnoresPatientConstraint() {
        StringBuffer sb = new StringBuffer();
        sb.append("rule \"Clinical Rule "+currentClinicalRule.getClinicalFact().getDisplayName()+" IGNORES PATIENT CONSTRAINTS\"\n");
        sb.append("dialect \"mvel\"\n");
        sb.append("when\n");
        sb.append("\n");
        sb.append("\t $xacml : XacmlResult(subjectPurposeOfUse ==\""+purposeofuse+"\")\n");
        sb.append("\t $cd : ClinicalFact(codeSystem == \""+currentClinicalRule.getClinicalFact().getCodeSystem()+"\", code == \""+currentClinicalRule.getClinicalFact().getCode()+"\", c32SectionLoincCode == \""+section+"\")\n");
        sb.append("\n");
        sb.append("then\n");
        sb.append("\n");
        sb.append("\t ruleExecutionContainer.addExecutionResponse(new RuleExecutionResponse(\""+currentClinicalRule.getActInformationSensitivityPolicy().getCode()+"\", \""+currentItemAction+"\", (String)Confidentiality."+currentClinicalRule.getImpliedConfidentiality().getConfidentiality().getCode()+", \""+currentUSPrivacyLaw+"\", \""+currentDocumentObligation+"\", \""+currentRefrainPolicy+"\", $cd.c32SectionTitle, $cd.c32SectionLoincCode, $cd.observationId, $cd.code, $cd.displayName, \""+currentClinicalRule.getClinicalFact().getCodeSystemName()+"\"))\n");
        sb.append("\n");
        sb.append("end\n");
        sb.append("\n");
        return sb.toString();
    }
    
    private void setPatientConstraints() {
        //determine masking
        String mask = DS4PConstants.PATIENT_MASK_CONSTRUCT+currentSensitivity;
        String redact = DS4PConstants.PATIENT_REDACT_CONSTRUCT+currentSensitivity;
        try {
            patientMasking = false;
            patientRedaction = false;
            patientMasking = obligations.contains(mask);
            patientRedaction = obligations.contains(redact);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processOrganizationalPolicies() {
        Iterator iter = orgTags.iterator();
        while (iter.hasNext()) {
            OrganizationTaggingRules o = (OrganizationTaggingRules)iter.next();
            if (o.getActInformationSensitivityPolicy().getCode().equals(currentSensitivity)) {
                String newRule = "";
                currentItemAction = o.getOrgObligationPolicyEntry().getObligationPolicy().getCode();
                currentUSPrivacyLaw = o.getActUSPrivacyLaw().getCode();
                currentDocumentObligation = o.getOrgObligationPolicyDocument().getObligationPolicy().getCode();
                currentRefrainPolicy = o.getRefrainPolicy().getCode();
                String patientconstraint = o.getPatientSensitivityConstraint().getActInformationSensitivityPolicy().getCode();
                String patientaction = o.getPatientRequestedAction().getObligationPolicy().getCode();
                if (!patientconstraint.equals("None") && !patientconstraint.equals(currentSensitivity)) {
                    System.out.println("RULE ERROR: PATIENT CONSTRAINTING SENSITIVITY MUST MATCH RULE SENSITIVITY");
                }
                else {
                    if (patientMasking && patientaction.equals("MASK")) {
                        if (currentItemAction.equals("MASK")) {
                            newRule = getRuleStringMask();
                            gSB.append(newRule);
                        }
                        else {
                            //rule ignores constraint
                            newRule = getRuleStringIgnoresPatientConstraint();
                            gSB.append(newRule);
                        }
                    }
                    else if (patientRedaction && patientaction.equals("REDACT")) {
                        if (currentItemAction.equals("REDACT")) {
                            newRule = getRuleStringREDACT();
                            gSB.append(newRule);
                        }
                        else {
                            newRule = getRuleStringIgnoresPatientConstraint();
                            gSB.append(newRule);                            
                        }
                    }
                    else if (!patientRedaction && !patientMasking ) {
                        //no constraints
                        if (patientaction.equals("None")) {
                            newRule = getRuleStringNoPatientConstraints();
                            gSB.append(newRule);
                        }
                        else {
                            //System.out.println("RULE INFO: No Suitable Org Policy Found");  
                        }
                    }
                    else {
                        //System.out.println("RULE INFO: No Suitable Org Policy Found");                          
                    }
                }
            }
            else {
                //rule does not apply
            }
        }
        
    }
}
