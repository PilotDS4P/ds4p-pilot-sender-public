/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.displayobjects;

/**
 *
 * @author Duane DeCouteau
 */
public class OrganizationGeneralRuleDisplayObject {
    private Integer ruleId;
    private String entrySensitivity;
    private String patientConstraint;
    private String patientRequestAction;
    private String purposeOfUse;
    private String usPrivacyLaw;
    private String obligationPolicyEntry;
    private String obligationPolicySection;
    private String refrainPolicy;

    /**
     * @return the ruleId
     */
    public Integer getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId the ruleId to set
     */
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return the purposeOfUse
     */
    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    /**
     * @param purposeOfUse the purposeOfUse to set
     */
    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    /**
     * @return the usPrivacyLaw
     */
    public String getUsPrivacyLaw() {
        return usPrivacyLaw;
    }

    /**
     * @param usPrivacyLaw the usPrivacyLaw to set
     */
    public void setUsPrivacyLaw(String usPrivacyLaw) {
        this.usPrivacyLaw = usPrivacyLaw;
    }


    /**
     * @return the refrainPolicy
     */
    public String getRefrainPolicy() {
        return refrainPolicy;
    }

    /**
     * @param refrainPolicy the refrainPolicy to set
     */
    public void setRefrainPolicy(String refrainPolicy) {
        this.refrainPolicy = refrainPolicy;
    }

    /**
     * @return the obligationPolicyEntry
     */
    public String getObligationPolicyEntry() {
        return obligationPolicyEntry;
    }

    /**
     * @param obligationPolicyEntry the obligationPolicyEntry to set
     */
    public void setObligationPolicyEntry(String obligationPolicyEntry) {
        this.obligationPolicyEntry = obligationPolicyEntry;
    }

    /**
     * @return the obligationPolicySection
     */
    public String getObligationPolicySection() {
        return obligationPolicySection;
    }

    /**
     * @param obligationPolicySection the obligationPolicySection to set
     */
    public void setObligationPolicySection(String obligationPolicySection) {
        this.obligationPolicySection = obligationPolicySection;
    }

    /**
     * @return the patientConstraint
     */
    public String getPatientConstraint() {
        return patientConstraint;
    }

    /**
     * @param patientConstraint the patientConstraint to set
     */
    public void setPatientConstraint(String patientConstraint) {
        this.patientConstraint = patientConstraint;
    }

    /**
     * @return the patientRequestAction
     */
    public String getPatientRequestAction() {
        return patientRequestAction;
    }

    /**
     * @param patientRequestAction the patientRequestAction to set
     */
    public void setPatientRequestAction(String patientRequestAction) {
        this.patientRequestAction = patientRequestAction;
    }

    /**
     * @return the entrySensitivity
     */
    public String getEntrySensitivity() {
        return entrySensitivity;
    }

    /**
     * @param entrySensitivity the entrySensitivity to set
     */
    public void setEntrySensitivity(String entrySensitivity) {
        this.entrySensitivity = entrySensitivity;
    }
    
}
