/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.testdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class MitreConsentData {
    private String patientName = "Asample Patientone";
    private String patientId = "PUI100010060001";
    private String patientIDType = "ehr";
    private String patientDateOfBirth = "19960101";
    private String patientGender = "F";
    private String intendedPOU = "TREAT";
    private List<String> allowedPOU = new ArrayList();
    private String primaryRecipient = "Duane_Decouteau@direct.healthvault-stage.com";
    private List<String> allowedRecipients = new ArrayList();;
    private String authorization = "Permit";
    private List<String> maskingActions = new ArrayList();
    private List<String> redactActions = new ArrayList();
    private String homeCommunityId = "2.16.840.1.113883.3.467";
    
    public MitreConsentData() {
        allowedPOU = Arrays.asList(new String[]{"TREAT", "ETREAT"});
        allowedRecipients = Arrays.asList(new String[]{"Duane_Decouteau@direct.healthvault-stage.com", "dr.taylor@aero.org"});
        maskingActions = Arrays.asList(new String[]{"HIV"});
        redactActions = Arrays.asList(new String[] {"ETH","PSY"});
        
    }

    /**
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the patientIDType
     */
    public String getPatientIDType() {
        return patientIDType;
    }

    /**
     * @param patientIDType the patientIDType to set
     */
    public void setPatientIDType(String patientIDType) {
        this.patientIDType = patientIDType;
    }

    /**
     * @return the patientDateOfBirth
     */
    public String getPatientDateOfBirth() {
        return patientDateOfBirth;
    }

    /**
     * @param patientDateOfBirth the patientDateOfBirth to set
     */
    public void setPatientDateOfBirth(String patientDateOfBirth) {
        this.patientDateOfBirth = patientDateOfBirth;
    }

    /**
     * @return the patientGender
     */
    public String getPatientGender() {
        return patientGender;
    }

    /**
     * @param patientGender the patientGender to set
     */
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    /**
     * @return the intendedPOU
     */
    public String getIntendedPOU() {
        return intendedPOU;
    }

    /**
     * @param intendedPOU the intendedPOU to set
     */
    public void setIntendedPOU(String intendedPOU) {
        this.intendedPOU = intendedPOU;
    }

    /**
     * @return the allowedPOU
     */
    public List<String> getAllowedPOU() {
        return allowedPOU;
    }

    /**
     * @param allowedPOU the allowedPOU to set
     */
    public void setAllowedPOU(List<String> allowedPOU) {
        this.allowedPOU = allowedPOU;
    }

    /**
     * @return the primaryRecipient
     */
    public String getPrimaryRecipient() {
        return primaryRecipient;
    }

    /**
     * @param primaryRecipient the primaryRecipient to set
     */
    public void setPrimaryRecipient(String primaryRecipient) {
        this.primaryRecipient = primaryRecipient;
    }

    /**
     * @return the allowedRecipients
     */
    public List<String> getAllowedRecipients() {
        return allowedRecipients;
    }

    /**
     * @param allowedRecipients the allowedRecipients to set
     */
    public void setAllowedRecipients(List<String> allowedRecipients) {
        this.allowedRecipients = allowedRecipients;
    }

    /**
     * @return the authorization
     */
    public String getAuthorization() {
        return authorization;
    }

    /**
     * @param authorization the authorization to set
     */
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    /**
     * @return the maskingActions
     */
    public List<String> getMaskingActions() {
        return maskingActions;
    }

    /**
     * @param maskingActions the maskingActions to set
     */
    public void setMaskingActions(List<String> maskingActions) {
        this.maskingActions = maskingActions;
    }

    /**
     * @return the redactActions
     */
    public List<String> getRedactActions() {
        return redactActions;
    }

    /**
     * @param redactActions the redactActions to set
     */
    public void setRedactActions(List<String> redactActions) {
        this.redactActions = redactActions;
    }

    /**
     * @return the homeCommunityId
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * @param homeCommunityId the homeCommunityId to set
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }
    
}
