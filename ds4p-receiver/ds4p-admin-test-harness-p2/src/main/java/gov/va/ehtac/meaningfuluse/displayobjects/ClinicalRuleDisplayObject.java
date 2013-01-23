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
public class ClinicalRuleDisplayObject {
    
    private Integer ruleId;
    private String purposeOfUse;
    private String displayName;
    private String code;
    private String sensitivityCode;
    private String impliedConfidentiality;

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
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the sensitivityCode
     */
    public String getSensitivityCode() {
        return sensitivityCode;
    }

    /**
     * @param sensitivityCode the sensitivityCode to set
     */
    public void setSensitivityCode(String sensitivityCode) {
        this.sensitivityCode = sensitivityCode;
    }

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
     * @return the impliedConfidentiality
     */
    public String getImpliedConfidentiality() {
        return impliedConfidentiality;
    }

    /**
     * @param impliedConfidentiality the impliedConfidentiality to set
     */
    public void setImpliedConfidentiality(String impliedConfidentiality) {
        this.impliedConfidentiality = impliedConfidentiality;
    }
    
}
