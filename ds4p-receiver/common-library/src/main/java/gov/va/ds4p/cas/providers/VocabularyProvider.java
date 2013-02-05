/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.cas.providers;

import gov.va.ds4p.cas.constants.DS4PConstants;
import gov.va.ds4p.policy.reference.ApplicableObligationPolicies;
import gov.va.ds4p.policy.reference.ApplicableRefrainPolicies;
import gov.va.ds4p.policy.reference.ApplicableSensitivityCodes;
import gov.va.ds4p.policy.reference.ApplicableUSLaws;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Duane DeCouteau
 */
public class VocabularyProvider {
    private String senseFile = "/vocabulary/SensitivityCodes.xml";
    private String obFile = "/vocabulary/ObligationPolicies.xml";
    private String refrainFile = "/vocabulary/RefrainPolicies.xml";
    private String lawFile = "/vocabulary/USPrivacyLawReferences.xml";
    
    private ApplicableObligationPolicies documentHandlingObligations;
    private ApplicableRefrainPolicies refrainObligations;
    private ApplicableSensitivityCodes dataSegmentationObligations;
    private ApplicableUSLaws privacyLawObligations;
    
    public VocabularyProvider() {
        //load vocabs into session
        ProcessVocabulary(senseFile, "sensitivities");
        ProcessVocabulary(obFile, "obligations");
        ProcessVocabulary(refrainFile, "refrainpolicies");
        ProcessVocabulary(lawFile, "privacylaw");
    }
    
    
    private void ProcessVocabulary(String fname, String vType) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fname)));
            ByteArrayOutputStream aout = new ByteArrayOutputStream();
            BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(aout));
            int result = r.read();
            while(result != -1) {
                byte b = (byte)result;
                aout.write(b);
                result = r.read();
            }
            String res = aout.toString();
            aout.close();
            r.close();
            
            if (vType.equals("sensitivities")) {
                createSensitivityObjectsFromXML(res);
            }
            else if (vType.equals("obligations")) {
                createObligationObjectsFromXML(res);
                
            }
            else if (vType.equals("refrainpolicies")) {
                createRefrainPolicyObjectsFromXML(res);
            }
            else if (vType.equals("privacylaw")) {
                createUSPrivacyLawObjectsFromXML(res);
            }
            else {
                
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: VOCABULARYPROVIDER: "+ex.getMessage());
        }                
    }
        
    private void createSensitivityObjectsFromXML(String c32) {

        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableSensitivityCodes.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            setDataSegmentationObligations((ApplicableSensitivityCodes)o);
                        
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }

    private void createObligationObjectsFromXML(String c32) {
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableObligationPolicies.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            setDocumentHandlingObligations((ApplicableObligationPolicies)o);
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
        
    private void createRefrainPolicyObjectsFromXML(String c32) {
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableRefrainPolicies.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            setRefrainObligations((ApplicableRefrainPolicies)o);
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }

    private void createUSPrivacyLawObjectsFromXML(String c32) {
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableUSLaws.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            setPrivacyLawObligations((ApplicableUSLaws)o);
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
    }

    /**
     * @return the documentHandlingObligations
     */
    public ApplicableObligationPolicies getDocumentHandlingObligations() {
        return documentHandlingObligations;
    }

    /**
     * @param documentHandlingObligations the documentHandlingObligations to set
     */
    public void setDocumentHandlingObligations(ApplicableObligationPolicies documentHandlingObligations) {
        this.documentHandlingObligations = documentHandlingObligations;
    }

    /**
     * @return the refrainObligations
     */
    public ApplicableRefrainPolicies getRefrainObligations() {
        return refrainObligations;
    }

    /**
     * @param refrainObligations the refrainObligations to set
     */
    public void setRefrainObligations(ApplicableRefrainPolicies refrainObligations) {
        this.refrainObligations = refrainObligations;
    }

    /**
     * @return the dataSegmentationObligations
     */
    public ApplicableSensitivityCodes getDataSegmentationObligations() {
        return dataSegmentationObligations;
    }

    /**
     * @param dataSegmentationObligations the dataSegmentationObligations to set
     */
    public void setDataSegmentationObligations(ApplicableSensitivityCodes dataSegmentationObligations) {
        this.dataSegmentationObligations = dataSegmentationObligations;
    }

    /**
     * @return the privacyLawObligations
     */
    public ApplicableUSLaws getPrivacyLawObligations() {
        return privacyLawObligations;
    }

    /**
     * @param privacyLawObligations the privacyLawObligations to set
     */
    public void setPrivacyLawObligations(ApplicableUSLaws privacyLawObligations) {
        this.privacyLawObligations = privacyLawObligations;
    }
}
