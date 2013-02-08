/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.xdm;

import gov.va.ehtac.ds4p.jpa.Directprocessing;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 *
 * @author Duane DeCouteau
 */
public class XDMProcessing {
    private Directprocessing dp = new Directprocessing();
    private List<String> reqSensitivity = new ArrayList();
    private List<String> reqPrivacyLaw = new ArrayList();
    private List<String> reqObligations = new ArrayList();
    
    public XDMProcessing() {
        
    }
    
    public Directprocessing processFile(byte[] payload) {
        dp = new Directprocessing();
        dp.setXdmfile(payload);
        dp.setDateProcessed(new Date());
//        dp.setProviderId("Duane_Decouteau@direct.healthvault-stage.com");
//        dp.setSendingProviderId("leo.smith@direct.obhita-stage.org");
//        dp.setOriginatingFacility("2.16.840.1.113883.3.467");
//        dp.setFacilityType("Substance abuse treatment center");
//        dp.setFaciltyTypeCode("");
//        dp.setPatientHomeCommunity("2.16.840.1.113883.3.467");
//        dp.setPatientName("Patientone, Asample");
//        dp.setPou("TREAT");
//        dp.setRefrainPolicy("NORDSLCD");
//        dp.setUniquePatientId("100015060001");
//        dp.setConfidentiality("R");
        
        //need to add sender information
        
        
        byte[] buffer = new byte[1024];
        byte[] eKey;
        byte[] eMaskKey;
        SecretKey docKey = null;
        SecretKey maskKey = null;
        Document d = null;
        
        try {
            ByteArrayInputStream bas = new ByteArrayInputStream(payload);
            ZipInputStream zis = new ZipInputStream(bas);
            
            ZipEntry ze = zis.getNextEntry();
            
            while (ze != null) {
                String fname = ze.getName();
                
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
                if (fname.equals("kekEncryptionKey")) {
                    eKey = bos.toByteArray();
                    docKey = getSecretKey(eKey);
                    dp.setDockey(bos.toByteArray());
                }
                else if (fname.equals("kekMaskingKey")) {
                    eMaskKey = bos.toByteArray();
                    maskKey = getSecretKey(eMaskKey);
                    dp.setDockeyMasking(bos.toByteArray());
                }
                else if (fname.equals("SUBSET01/CDA.xsl")) {
                    dp.setCdaxsl(new String(bos.toByteArray()));
                }
                else if (fname.equals("SUBSET01/DOCUMENT.xml")) {
                    d = getDocument(bos.toByteArray());
                    dp.setEncrypteddocument(new String(bos.toByteArray()));
                }
                else if (fname.equals("SUBSET01/METADADATA.xml")) {
                    dp.setMetadata(new String(bos.toByteArray()));
                    processMetaDataFile(bos.toByteArray());
                }
                else {
                    System.err.println("UNKNOWN FILE NAME: "+fname);
                }
                bos.close();                 
                ze = zis .getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            String unencrypteddocument = getDecryptedDocument(docKey, maskKey, d);
            dp.setDocument(unencrypteddocument);           
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return dp;
    }
    
    private Document getDocument(byte[] docArray) {
        Document d = null;
        String docString = new String(docArray);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource bi = new InputSource(new StringReader(docString));
            d = builder.parse(bi);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return d;
    }
    
    private SecretKey getSecretKey(byte[] bKey) {
        SecretKey res = null;
        DESedeKeySpec desedeEncryptKeySpec;
        try {
            desedeEncryptKeySpec = new DESedeKeySpec(bKey);
            SecretKeyFactory skfEncrypt = SecretKeyFactory.getInstance("DESede");
	    res = skfEncrypt.generateSecret(desedeEncryptKeySpec);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
//        try {
//            SecretKey skey = new SecretKeySpec(bKey, 0, bKey.length, "DES");
//            res = (Key)skey;
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
        System.out.println("SECRET KEY IS : "+res.toString());
        return res;
    }
    
    private String getDecryptedDocument(Key docKey, Key maskKey, Document doc) {
        String res = "";
        try {
            Document d = DecryptTool.getUnencryptedDocument(docKey, maskKey, doc);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(d);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(bos);
            transformer.transform(source, result);  
            res = new String(bos.toByteArray());
            bos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
       
        
        return res;
    }
    
//    private void processMetaData(byte[] payload) {
//         Document d = null;
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            ByteArrayInputStream bi = new ByteArrayInputStream(payload);
//            d = builder.parse(bi);
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//       
//    }
    
    private void processMetaDataFile(byte[] payload) {
        String c32 = new String(payload);
        SubmitObjectsRequest obj = null;
        try {
            JAXBContext context = JAXBContext.newInstance(SubmitObjectsRequest.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            obj = (SubmitObjectsRequest)o;
            
            RegistryObjectListType reg = obj.getRegistryObjectList();
            List<JAXBElement<? extends IdentifiableType>> extrinsicObjects = reg.getIdentifiable();
            if (extrinsicObjects != null && extrinsicObjects.size() > 0) {
                for (JAXBElement<? extends IdentifiableType> jaxb : extrinsicObjects) {
                    if (jaxb.getValue() instanceof ExtrinsicObjectType) {
                        ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType) jaxb.getValue();
                        List<SlotType1> sList = extrinsicObject.getSlot() ;
                        Iterator iter = sList.iterator();
                        while (iter.hasNext()) {
                            SlotType1 slot = (SlotType1)iter.next();
                            String slotName = slot.getName();
                            if (slotName.equals("sourcePatientId")) {
                                processPatientId(slot);
                            }
                            else if (slotName.equals("authorTelecommunication")) {
                                processAuthor(slot);
                            }
                            else if (slotName.equals("sourcePatientInfo")) {
                                processPatientName(slot);
                            }
                            else if (slotName.equals("intendedRecipient")) {
                                processIntendedRecipient(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:purposeofuse")) {
                                processIntendedPOU(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:obligationpolicy")) {
                                processObligation(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:refrainpolicy")) {
                                processRefrainPolicy(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:sensitivitypolicy")) {
                                processSensitivityPolicy(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:usprivacylaw")) {
                                processUSPrivacyLaw(slot);
                            }
                            else {
                                //don't care about
                            }
                        }
                        //process classifications
                        List<ClassificationType> classList = extrinsicObject.getClassification();
                        Iterator cIter = classList.iterator();
                        while (cIter.hasNext()) {
                            ClassificationType cType = (ClassificationType)cIter.next();
                            System.out.println("CLASSIFICATION: "+cType.getId());
                            if (cType.getId().equals("cl03")) {
                                dp.setConfidentiality(cType.getNodeRepresentation());
                            }
                            else if (cType.getId().equals("c108")) {
                                dp.setFacilityType(cType.getName().getLocalizedString().get(0).getValue());
                                dp.setFaciltyTypeCode(cType.getNodeRepresentation());
                            }
                            else {
                                //dont care
                            }  
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void processPatientId(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String patientStr = vals.get(0);
        System.out.println(patientStr);
        StringTokenizer st = new StringTokenizer(patientStr, "^");
        dp.setUniquePatientId(st.nextToken());
        String hc = st.nextToken();
        hc = hc.replaceAll("&", "");
        hc = hc.replaceAll("ISO", "");
        dp.setPatientHomeCommunity(hc);  
        //clear this up
        dp.setOriginatingFacility(hc);
    }
    
    private void processPatientName(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> l = val.getValue();
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
            String v = (String)iter.next();
            if (v.indexOf("PID-5") > -1) {
                StringTokenizer st = new StringTokenizer(v, "|");
                st.nextToken();
                StringTokenizer st2 = new StringTokenizer(st.nextToken(), "^");
                String lastname = st2.nextToken();
                String firstname = st2.nextToken();
                dp.setPatientName(lastname+","+firstname);
            }
        }
    }
    
    private void processAuthor(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String authorStr = vals.get(0);
        System.out.println(authorStr);
        StringTokenizer st = new StringTokenizer(authorStr, "^");
        st.nextToken();
        String author = st.nextToken();
        dp.setSendingProviderId(author);
    }
    
    private void processIntendedRecipient(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String recipientStr = vals.get(0);
        System.out.println(recipientStr);
        StringTokenizer st = new StringTokenizer(recipientStr, "^");
        st.nextToken();
        String recvr = st.nextToken();
        dp.setProviderId(recvr);
        
    }
    
    private void processIntendedPOU(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String pouStr = vals.get(0);
        System.out.println(pouStr); 
        dp.setPou(pouStr);
    }
    
    private void processObligation(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String oblStr = vals.get(0);
        System.out.println(oblStr); 
        getReqObligations().add(oblStr);
    }
    
    private void processRefrainPolicy(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String refrainStr = vals.get(0);
        System.out.println(refrainStr);
        dp.setRefrainPolicy(refrainStr);
    }
    
    private void processSensitivityPolicy(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String senseStr = vals.get(0);
        System.out.println(senseStr); 
        getReqSensitivity().add(senseStr);
    }
    
    private void processUSPrivacyLaw(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String lawStr = vals.get(0);
        System.out.println(lawStr);
        getReqPrivacyLaw().add(lawStr);
    }

    /**
     * @return the reqSensitivity
     */
    public List<String> getReqSensitivity() {
        return reqSensitivity;
    }

    /**
     * @param reqSensitivity the reqSensitivity to set
     */
    public void setReqSensitivity(List<String> reqSensitivity) {
        this.reqSensitivity = reqSensitivity;
    }

    /**
     * @return the reqPrivacyLaw
     */
    public List<String> getReqPrivacyLaw() {
        return reqPrivacyLaw;
    }

    /**
     * @param reqPrivacyLaw the reqPrivacyLaw to set
     */
    public void setReqPrivacyLaw(List<String> reqPrivacyLaw) {
        this.reqPrivacyLaw = reqPrivacyLaw;
    }

    /**
     * @return the reqObligations
     */
    public List<String> getReqObligations() {
        return reqObligations;
    }

    /**
     * @param reqObligations the reqObligations to set
     */
    public void setReqObligations(List<String> reqObligations) {
        this.reqObligations = reqObligations;
    }
    
    
}
