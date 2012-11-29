/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.session;

import gov.va.ehtac.meaningfuluse.uts.UtsClient;
import gov.va.ds4p.policy.reference.*;
import gov.va.ehtac.ds4p.ws.ri.Clinicaltagrule;
import gov.va.ehtac.meaningfuluse.displayobjects.ClinicalRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.displayobjects.OrganizationGeneralRuleDisplayObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class DS4PSession {
    
    private String ruleSectionCode;
    private String ruleSectionCodeSystems = "2.16.840.1.113883.5.25";
    private String ruleSectionCodeSystemName = "LOINC";
    private String ruleSectionDisplayName;
    
    private List<ClinicalRuleDisplayObject> cRules = new ArrayList();
    private List<OrganizationGeneralRuleDisplayObject> oGRules = new ArrayList(); 
    
    //ref data
    private List<ActReason> pouList = new ArrayList();
    private List<ActInformationSensitivityPolicy> sensitivityList = new ArrayList();
    private List<ObligationPolicy> obligationList = new ArrayList();
    private List<Confidentiality> confidentialityList = new ArrayList();
    private List<RefrainPolicy> refrainPolicyList = new ArrayList();
    private List<ActUSPrivacyLaw> usLawList = new ArrayList();
    private List<XspaPatientObligation> patientObligations = new ArrayList();
    
    private Clinicaltagrule ruleObject = null;  //represents current tagging rule in editor
    private ProblemListSensitivityRules plRule = new ProblemListSensitivityRules();
    private MedicationListSensitivityRules medRule = new MedicationListSensitivityRules();
    private List<ClinicalTaggingRule> plRuleSet = new ArrayList();
    
    private gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy policyDB = null;
    private String organizationName = "";
    private String organizationOid = "";
    private String organizationUSLaw = "";
    
    private String selectedOrganization = "2.16.840.1.113883.3.467";
    
    private List<String> patientAllowedObligations = new ArrayList();
    
    //endpoints
    private String DS4P_CAS_ENDPOINT;
    private String DS4P_CH_ENDPOINT;
    private String DS4P_REFDATA_ENDPOINT;
    private String DS4P_AUDIT_ENDPOINT;
    private String DS4P_XDM_ENDPOINT;
    private String DS4P_AUTH_ENDPOINT;
    
    private String PROVIDER_ID = "Duane_Decouteau@direct.healthvault-stage.com";
    /**
     * @return the ruleSectionCode
     */
    public String getRuleSectionCode() {
        return ruleSectionCode;
    }

    /**
     * @param ruleSectionCode the ruleSectionCode to set
     */
    public void setRuleSectionCode(String ruleSectionCode) {
        this.ruleSectionCode = ruleSectionCode;
    }

    /**
     * @return the ruleSectionCodeSystems
     */
    public String getRuleSectionCodeSystems() {
        return ruleSectionCodeSystems;
    }

    /**
     * @param ruleSectionCodeSystems the ruleSectionCodeSystems to set
     */
    public void setRuleSectionCodeSystems(String ruleSectionCodeSystems) {
        this.ruleSectionCodeSystems = ruleSectionCodeSystems;
    }

    /**
     * @return the ruleSectionCodeSystemName
     */
    public String getRuleSectionCodeSystemName() {
        return ruleSectionCodeSystemName;
    }

    /**
     * @param ruleSectionCodeSystemName the ruleSectionCodeSystemName to set
     */
    public void setRuleSectionCodeSystemName(String ruleSectionCodeSystemName) {
        this.ruleSectionCodeSystemName = ruleSectionCodeSystemName;
    }

    /**
     * @return the ruleSectionDisplayName
     */
    public String getRuleSectionDisplayName() {
        return ruleSectionDisplayName;
    }

    /**
     * @param ruleSectionDisplayName the ruleSectionDisplayName to set
     */
    public void setRuleSectionDisplayName(String ruleSectionDisplayName) {
        this.ruleSectionDisplayName = ruleSectionDisplayName;
    }

    /**
     * @return the cRules
     */
    public List<ClinicalRuleDisplayObject> getcRules() {
        return cRules;
    }

    /**
     * @param cRules the cRules to set
     */
    public void setcRules(List<ClinicalRuleDisplayObject> cRules) {
        this.cRules = cRules;
    }

    /**
     * @return the pouList
     */
    public List<ActReason> getPouList() {
        return pouList;
    }

    /**
     * @param pouList the pouList to set
     */
    public void setPouList(List<ActReason> pouList) {
        this.pouList = pouList;
    }

    /**
     * @return the sensitivityList
     */
    public List<ActInformationSensitivityPolicy> getSensitivityList() {
        return sensitivityList;
    }

    /**
     * @param sensitivityList the sensitivityList to set
     */
    public void setSensitivityList(List<ActInformationSensitivityPolicy> sensitivityList) {
        this.sensitivityList = sensitivityList;
    }

    /**
     * @return the obligationList
     */
    public List<ObligationPolicy> getObligationList() {
        return obligationList;
    }

    /**
     * @param obligationList the obligationList to set
     */
    public void setObligationList(List<ObligationPolicy> obligationList) {
        this.obligationList = obligationList;
    }

    /**
     * @return the confidentialityList
     */
    public List<Confidentiality> getConfidentialityList() {
        return confidentialityList;
    }

    /**
     * @param confidentialityList the confidentialityList to set
     */
    public void setConfidentialityList(List<Confidentiality> confidentialityList) {
        this.confidentialityList = confidentialityList;
    }

    /**
     * @return the refrainPolicyList
     */
    public List<RefrainPolicy> getRefrainPolicyList() {
        return refrainPolicyList;
    }

    /**
     * @param refrainPolicyList the refrainPolicyList to set
     */
    public void setRefrainPolicyList(List<RefrainPolicy> refrainPolicyList) {
        this.refrainPolicyList = refrainPolicyList;
    }

    /**
     * @return the usLawList
     */
    public List<ActUSPrivacyLaw> getUsLawList() {
        return usLawList;
    }

    /**
     * @param usLawList the usLawList to set
     */
    public void setUsLawList(List<ActUSPrivacyLaw> usLawList) {
        this.usLawList = usLawList;
    }

    /**
     * @return the oGRules
     */
    public List<OrganizationGeneralRuleDisplayObject> getoGRules() {
        return oGRules;
    }

    /**
     * @param oGRules the oGRules to set
     */
    public void setoGRules(List<OrganizationGeneralRuleDisplayObject> oGRules) {
        this.oGRules = oGRules;
    }

    /**
     * @return the patientObligations
     */
    public List<XspaPatientObligation> getPatientObligations() {
        return patientObligations;
    }

    /**
     * @param patientObligations the patientObligations to set
     */
    public void setPatientObligations(List<XspaPatientObligation> patientObligations) {
        this.patientObligations = patientObligations;
    }

    /**
     * @return the plRule
     */
    public ProblemListSensitivityRules getPlRule() {
        return plRule;
    }

    /**
     * @param plRule the plRule to set
     */
    public void setPlRule(ProblemListSensitivityRules plRule) {
        this.plRule = plRule;
    }

    /**
     * @return the ruleObject
     */
    public Clinicaltagrule getRuleObject() {
        return ruleObject;
    }

    /**
     * @param ruleObject the ruleObject to set
     */
    public void setRuleObject(Clinicaltagrule ruleObject) {
        this.ruleObject = ruleObject;
    }

    /**
     * @return the plRuleSet
     */
    public List<ClinicalTaggingRule> getPlRuleSet() {
        return plRuleSet;
    }

    /**
     * @param plRuleSet the plRuleSet to set
     */
    public void setPlRuleSet(List<ClinicalTaggingRule> plRuleSet) {
        this.plRuleSet = plRuleSet;
    }

    /**
     * @return the policyDB
     */
    public gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy getPolicyDB() {
        return policyDB;
    }

    /**
     * @param policyDB the policyDB to set
     */
    public void setPolicyDB(gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy policyDB) {
        this.policyDB = policyDB;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the organizationOid
     */
    public String getOrganizationOid() {
        return organizationOid;
    }

    /**
     * @param organizationOid the organizationOid to set
     */
    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    /**
     * @return the organizationUSLaw
     */
    public String getOrganizationUSLaw() {
        return organizationUSLaw;
    }

    /**
     * @param organizationUSLaw the organizationUSLaw to set
     */
    public void setOrganizationUSLaw(String organizationUSLaw) {
        this.organizationUSLaw = organizationUSLaw;
    }

    /**
     * @return the selectedOrganization
     */
    public String getSelectedOrganization() {
        return selectedOrganization;
    }

    /**
     * @param selectedOrganization the selectedOrganization to set
     */
    public void setSelectedOrganization(String selectedOrganization) {
        this.selectedOrganization = selectedOrganization;
    }

    /**
     * @return the patientAllowedObligations
     */
    public List<String> getPatientAllowedObligations() {
        return patientAllowedObligations;
    }

    /**
     * @param patientAllowedObligations the patientAllowedObligations to set
     */
    public void setPatientAllowedObligations(List<String> patientAllowedObligations) {
        this.patientAllowedObligations = patientAllowedObligations;
    }

    /**
     * @return the DS4P_CAS_ENDPOINT
     */
    public String getDS4P_CAS_ENDPOINT() {
        return DS4P_CAS_ENDPOINT;
    }

    /**
     * @param DS4P_CAS_ENDPOINT the DS4P_CAS_ENDPOINT to set
     */
    public void setDS4P_CAS_ENDPOINT(String DS4P_CAS_ENDPOINT) {
        this.DS4P_CAS_ENDPOINT = DS4P_CAS_ENDPOINT;
    }

    /**
     * @return the DS4P_CH_ENDPOINT
     */
    public String getDS4P_CH_ENDPOINT() {
        return DS4P_CH_ENDPOINT;
    }

    /**
     * @param DS4P_CH_ENDPOINT the DS4P_CH_ENDPOINT to set
     */
    public void setDS4P_CH_ENDPOINT(String DS4P_CH_ENDPOINT) {
        this.DS4P_CH_ENDPOINT = DS4P_CH_ENDPOINT;
    }

    /**
     * @return the DS4P_REFDATA_ENDPOINT
     */
    public String getDS4P_REFDATA_ENDPOINT() {
        return DS4P_REFDATA_ENDPOINT;
    }

    /**
     * @param DS4P_REFDATA_ENDPOINT the DS4P_REFDATA_ENDPOINT to set
     */
    public void setDS4P_REFDATA_ENDPOINT(String DS4P_REFDATA_ENDPOINT) {
        this.DS4P_REFDATA_ENDPOINT = DS4P_REFDATA_ENDPOINT;
    }

    /**
     * @return the DS4P_AUDIT_ENDPOINT
     */
    public String getDS4P_AUDIT_ENDPOINT() {
        return DS4P_AUDIT_ENDPOINT;
    }

    /**
     * @param DS4P_AUDIT_ENDPOINT the DS4P_AUDIT_ENDPOINT to set
     */
    public void setDS4P_AUDIT_ENDPOINT(String DS4P_AUDIT_ENDPOINT) {
        this.DS4P_AUDIT_ENDPOINT = DS4P_AUDIT_ENDPOINT;
    }

    /**
     * @return the medRule
     */
    public MedicationListSensitivityRules getMedRule() {
        return medRule;
    }

    /**
     * @param medRule the medRule to set
     */
    public void setMedRule(MedicationListSensitivityRules medRule) {
        this.medRule = medRule;
    }

    /**
     * @return the DS4P_XDM_ENDPOINT
     */
    public String getDS4P_XDM_ENDPOINT() {
        return DS4P_XDM_ENDPOINT;
    }

    /**
     * @param DS4P_XDM_ENDPOINT the DS4P_XDM_ENDPOINT to set
     */
    public void setDS4P_XDM_ENDPOINT(String DS4P_XDM_ENDPOINT) {
        this.DS4P_XDM_ENDPOINT = DS4P_XDM_ENDPOINT;
    }

    /**
     * @return the PROVIDER_ID
     */
    public String getPROVIDER_ID() {
        return PROVIDER_ID;
    }

    /**
     * @param PROVIDER_ID the PROVIDER_ID to set
     */
    public void setPROVIDER_ID(String PROVIDER_ID) {
        this.PROVIDER_ID = PROVIDER_ID;
    }

    /**
     * @return the DS4P_AUTH_ENDPOINT
     */
    public String getDS4P_AUTH_ENDPOINT() {
        return DS4P_AUTH_ENDPOINT;
    }

    /**
     * @param DS4P_AUTH_ENDPOINT the DS4P_AUTH_ENDPOINT to set
     */
    public void setDS4P_AUTH_ENDPOINT(String DS4P_AUTH_ENDPOINT) {
        this.DS4P_AUTH_ENDPOINT = DS4P_AUTH_ENDPOINT;
    }

}
