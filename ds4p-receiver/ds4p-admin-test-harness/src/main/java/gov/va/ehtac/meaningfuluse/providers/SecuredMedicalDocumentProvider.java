/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.providers;

import gov.va.ds4p.hcs.SecuredMedicalDocument;

/**
 *
 * @author Duane DeCouteau
 */
public class SecuredMedicalDocumentProvider {
    private SecuredMedicalDocument sDocument;
    private String securedMedicalDocumentString = "";
    
    public SecuredMedicalDocumentProvider(String filename) {
        
    }
    
    private void createObjectFromXmlString(String s) {

    }
    
    private void createXmlStringFromObject(SecuredMedicalDocument sDoc) {
        
    }

    /**
     * @return the sDocument
     */
    public SecuredMedicalDocument getsDocument() {
        return sDocument;
    }

    /**
     * @param sDocument the sDocument to set
     */
    public void setsDocument(SecuredMedicalDocument sDocument) {
        this.sDocument = sDocument;
    }

    /**
     * @return the securedMedicalDocumentString
     */
    public String getSecuredMedicalDocumentString() {
        return securedMedicalDocumentString;
    }

    /**
     * @param securedMedicalDocumentString the securedMedicalDocumentString to set
     */
    public void setSecuredMedicalDocumentString(String securedMedicalDocumentString) {
        this.securedMedicalDocumentString = securedMedicalDocumentString;
    }
}
