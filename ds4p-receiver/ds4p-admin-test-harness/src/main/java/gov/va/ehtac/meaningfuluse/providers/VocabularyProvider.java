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

import gov.va.ds4p.cas.constants.DS4PConstants;
import gov.va.ds4p.policy.reference.*;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
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
    String pouFile = "../../../../../../resources/Vocabulary/PurposeOfUse.xml";
    String senseFile = "../../../../../../resources/Vocabulary/SensitivityCodes.xml";
    String confFile = "../../../../../../resources/Vocabulary/Confidentialities.xml";
    String obFile = "../../../../../../resources/Vocabulary/ObligationPolicies.xml";
    String refrainFile = "../../../../../../resources/Vocabulary/RefrainPolicies.xml";
    String lawFile = "../../../../../../resources/Vocabulary/USPrivacyLawReferences.xml";
    String patientOblFile = "../../../../../../resources/Vocabulary/PatientObligations.xml";
    
    public VocabularyProvider() {
        //load vocabs into session
        ProcessVocabulary(pouFile, "pou");
        ProcessVocabulary(senseFile, "sensitivities");
        ProcessVocabulary(obFile, "obligations");
        ProcessVocabulary(confFile, "confidentialities");
        ProcessVocabulary(refrainFile, "refrainpolicies");
        ProcessVocabulary(lawFile, "privacylaw");
        ProcessVocabulary(patientOblFile, "patientobligations");
        processAllowedPatientObligations();
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
            
            if (vType.equals("pou")) {
                createPOUObjectsFromXML(res);
            }
            else if (vType.equals("sensitivities")) {
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
            else if (vType.equals("confidentialities")) {
                createConfidentialityObjectsFromXML(res);
            }
            else if (vType.contains("patientobligations")) {
                createPatientObligationObjectsFromXML(res);
            }
            else {
                
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: VOCABULARYPROVIDER: "+ex.getMessage());
        }                
    }
    
    private void createPOUObjectsFromXML(String c32) {
        PurposeOfUse pou = null;
        try {
            JAXBContext context = JAXBContext.newInstance(PurposeOfUse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            pou = (PurposeOfUse)o;
            
            
            AdminContext.getSessionAttributes().setPouList(pou.getActReason());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void createSensitivityObjectsFromXML(String c32) {
        ApplicableSensitivityCodes sense = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableSensitivityCodes.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            sense = (ApplicableSensitivityCodes)o;
            
            
            AdminContext.getSessionAttributes().setSensitivityList(sense.getActInformationSensitivityPolicy());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }

    private void createObligationObjectsFromXML(String c32) {
        ApplicableObligationPolicies obs = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableObligationPolicies.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            obs = (ApplicableObligationPolicies)o;
            
            
            AdminContext.getSessionAttributes().setObligationList(obs.getObligationPolicy());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void createConfidentialityObjectsFromXML(String c32) {
        ApplicableConfidentialities conf = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableConfidentialities.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            conf = (ApplicableConfidentialities)o;
            
            
            AdminContext.getSessionAttributes().setConfidentialityList(conf.getConfidentiality());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void createRefrainPolicyObjectsFromXML(String c32) {
        ApplicableRefrainPolicies refrain = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableRefrainPolicies.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            refrain = (ApplicableRefrainPolicies)o;
            
            
            AdminContext.getSessionAttributes().setRefrainPolicyList(refrain.getRefrainPolicy());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }

    private void createUSPrivacyLawObjectsFromXML(String c32) {
        ApplicableUSLaws law = null;
        try {
            JAXBContext context = JAXBContext.newInstance(ApplicableUSLaws.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            law = (ApplicableUSLaws)o;
            
            
            AdminContext.getSessionAttributes().setUsLawList(law.getActUSPrivacyLaw());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void createPatientObligationObjectsFromXML(String c32) {
        XspaPatientObligations obs = null;
        try {
            JAXBContext context = JAXBContext.newInstance(XspaPatientObligations.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            obs = (XspaPatientObligations)o;
            
            
            AdminContext.getSessionAttributes().setPatientObligations(obs.getXspaPatientObligation());
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void processAllowedPatientObligations() {
        List<String> patientObligations = new ArrayList();
        String redact = DS4PConstants.PATIENT_REDACT_CONSTRUCT;
        String mask = DS4PConstants.PATIENT_MASK_CONSTRUCT;
        Iterator iter = AdminContext.getSessionAttributes().getSensitivityList().iterator();
        while (iter.hasNext()) {
            ActInformationSensitivityPolicy sens = (ActInformationSensitivityPolicy)iter.next();
            patientObligations.add(redact+sens.getCode());
            patientObligations.add(mask+sens.getCode());
        }
        
        AdminContext.getSessionAttributes().setPatientAllowedObligations(patientObligations);
        
    }

}
