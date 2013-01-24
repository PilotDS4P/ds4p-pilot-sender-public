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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class ProviderAttributes {

    /**
     * @return the availableNwHINActions
     */
    public static List<String> getAvailableNwHINActions() {
        return availableNwHINActions;
    }
    private String userId = "DrDuane";
    private String providerId = "Duane_Decouteau@direct.healthvault-stage.com";
    private String organization = "Dept. of Veterans Affairs";
    private String providerHomeCommunityId = "2.16.840.1.113883.4.349";
    private String purposeOfUse = "TREAT";
    private List<String> allowedNwHINActions = new ArrayList<String>(Arrays.asList("NwHINDirectReceive", "NwHINDirectCollect", "NwHINDirectView", "NwHINDirectReDisclose"));
    private List<String> allowedSensitivityActions = new ArrayList<String>(Arrays.asList("ETH","PSY","HIV"));
    private String role = "MD/Allopath";
    
    private static final List<String> availableNwHINActions = new ArrayList<String>(Arrays.asList("NwHINDirectReceive", "NwHINDirectCollect", "NwHINDirectView", "NwHINDirectReDisclose"));
    

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the providerId
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @return the providerHomeCommunityId
     */
    public String getProviderHomeCommunityId() {
        return providerHomeCommunityId;
    }

    /**
     * @param providerHomeCommunityId the providerHomeCommunityId to set
     */
    public void setProviderHomeCommunityId(String providerHomeCommunityId) {
        this.providerHomeCommunityId = providerHomeCommunityId;
    }

    /**
     * @return the allowedNwHINActions
     */
    public List<String> getAllowedNwHINActions() {
        return allowedNwHINActions;
    }

    /**
     * @param allowedNwHINActions the allowedNwHINActions to set
     */
    public void setAllowedNwHINActions(List<String> allowedNwHINActions) {
        this.allowedNwHINActions = allowedNwHINActions;
    }

    /**
     * @return the allowedSensitivityActions
     */
    public List<String> getAllowedSensitivityActions() {
        return allowedSensitivityActions;
    }

    /**
     * @param allowedSensitivityActions the allowedSensitivityActions to set
     */
    public void setAllowedSensitivityActions(List<String> allowedSensitivityActions) {
        this.allowedSensitivityActions = allowedSensitivityActions;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
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
    
    
    
    
}
