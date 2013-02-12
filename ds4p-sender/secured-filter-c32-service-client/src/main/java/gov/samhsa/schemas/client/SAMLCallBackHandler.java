/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.security.saml;

import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import com.sun.xml.wss.impl.callback.SAMLCallback;
import com.sun.xml.wss.impl.callback.SignatureKeyCallback;
import com.sun.xml.wss.saml.Assertion;
import com.sun.xml.wss.saml.Conditions;
import com.sun.xml.wss.saml.NameID;
import com.sun.xml.wss.saml.SAMLAssertionFactory;
import com.sun.xml.wss.saml.Subject;
import com.sun.xml.wss.saml.SubjectConfirmation;
import com.sun.xml.wss.saml.SubjectConfirmationData;
import gov.va.ds4p.cas.constants.DS4PConstants;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 *
 * @author Duane DeCouteau
 */
public class SAMLCallBackHandler implements CallbackHandler {
    
    private String keyStoreURL;
    private String keyStorePassword;
    private String keyStoreType;
    
    private String trustStoreURL;
    private String trustStorePassword;
    private String trustStoreType;
    
    private KeyStore keyStore;
    private KeyStore trustStore;
    
    private  static Element svAssertion = null;
        
    //public static final String senderVouchesConfirmation_saml20 = "urn:oasis:names:tc:SAML:2.0:cm:sender-vouches";
    public static final String holderOfKeyConfirmation_saml20 = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    
    public static final String nameIdFormat = "urn:oasis:names:tc:SAML:2.0:nameid-format:X509SubjectName";
    
    private static final String fileSeparator = System.getProperty("file.separator");
    
    private String home = "/home/appadmin/glassfish3/glassfish";
    String client_priv_key_alias="xws-security-client";
    private static PublicKey pubKey;
    private static PrivateKey privKey;
    
    
    public SAMLCallBackHandler() {
        try {

            this.keyStoreURL = home + fileSeparator + "domains" + fileSeparator + "domain1" +
                    fileSeparator + "config" + fileSeparator + "keystore.jks";
            this.keyStoreType = "JKS";
            this.keyStorePassword = "changeit";
                                                                                                                                                             
            this.trustStoreURL = home + fileSeparator + "domains" + fileSeparator + "domain1" +
				fileSeparator + "config" + fileSeparator + "cacerts.jks";
            this.trustStoreType = "JKS";
            this.trustStorePassword = "changeit";
 
            initKeyStore();
            initTrustStore();			
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }        
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i=0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof SAMLCallback) {
                try {
                    SAMLCallback samlCallback = (SAMLCallback)callbacks[i];
                    if (samlCallback.getConfirmationMethod().equals(SAMLCallback.HOK_ASSERTION_TYPE)) {
                            samlCallback.setAssertionElement(createSAMLAssertion());
                            svAssertion =samlCallback.getAssertionElement(); 
                            String res = dumpNodeToString(svAssertion);
                            SAMLTokenConstants.setRECENT_REQUEST(res);  //for display purpose during testing
                    }
                    else if (samlCallback.getConfirmationMethod().equals(SAMLCallback.SV_ASSERTION_TYPE)) {
                            throw new Exception("SAML Assertion Type is not matched.");                      }
                    else {
                            throw new Exception("SAML Assertion Type is not matched.");                       
                    }    
                }
                catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        
        }
    }
    
    private Element createSAMLAssertion() {
        try {
            Assertion assertion = null;
            // create the assertion id
            String aID = String.valueOf(System.currentTimeMillis());                        
            
            GregorianCalendar c = new GregorianCalendar();
            long beforeTime = c.getTimeInMillis();
            // roll the time by one hour
            long offsetHours = 60*60*1000;

            c.setTimeInMillis(beforeTime - offsetHours);
            GregorianCalendar before= (GregorianCalendar)c.clone();
            
            c = new GregorianCalendar();
            long afterTime = c.getTimeInMillis();
            c.setTimeInMillis(afterTime + offsetHours);
            GregorianCalendar after = (GregorianCalendar)c.clone();
            
            GregorianCalendar issueInstant = new GregorianCalendar();
            // statements
            List statements = new LinkedList();

            SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);
            
            String compliantName = "CN="+ SAMLTokenConstants.getUSER_NAME()+","+
                                   "OU="+ SAMLTokenConstants.getUSER_ORGANIZATION_UNIT() +","+
                                   "O="+ SAMLTokenConstants.getUSER_ORGANIZATION() +","+
                                   "L="+ SAMLTokenConstants.getUSER_CITY() +","+
                                   "ST="+ SAMLTokenConstants.getUSER_STATE() +","+
                                   "C="+ SAMLTokenConstants.getUSER_COUNTRY(); 
            

            NameID nmId = factory.createNameID(compliantName, null, nameIdFormat);
            
            //default priv key cert req
            SignatureKeyCallback.DefaultPrivKeyCertRequest request =
	            new SignatureKeyCallback.DefaultPrivKeyCertRequest();

            getDefaultPrivKeyCert(request);
            
            if ( request.getX509Certificate() == null ) {
                throw new RuntimeException("Not able to resolve the Default Certificate");
            }                                                                                                                 
            pubKey = request.getX509Certificate().getPublicKey();
            privKey = request.getPrivateKey();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            Document doc = docFactory.newDocumentBuilder().newDocument();

            KeyInfo keyInfo = new KeyInfo(doc);
            keyInfo.addKeyValue(pubKey);

            List subConfirmation = new ArrayList();
            subConfirmation.add(holderOfKeyConfirmation_saml20);
			SubjectConfirmationData scd = factory.createSubjectConfirmationData(null, null, null, null, null, keyInfo.getElement());


            SubjectConfirmation scf = factory.createSubjectConfirmation(nmId, scd, holderOfKeyConfirmation_saml20);
           
            Subject subj = factory.createSubject(nmId, scf);
            
            List attributes = getAttributeList(factory);
            
            statements.add(
            factory.createAttributeStatement(attributes));
            
            Conditions conditions = factory.createConditions(before, after, null, null, null, null);
            
            assertion = factory.createAssertion(aID, nmId, issueInstant, conditions, null, subj, statements);  
            
 
            return assertion.sign(pubKey, privKey);
            
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
            
        }
        
    }
    
    private static List getAttributeList(SAMLAssertionFactory factory) {
            List attributes = new LinkedList();

            List attributeValues = new LinkedList();

            try {
                //add subject id
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getSUBJECT_ID());
                attributes.add(factory.createAttribute(DS4PConstants.SUBJECT_ID_NS, attributeValues));

                //add locality
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getSUBJECT_LOCALITY());
                attributes.add(factory.createAttribute(DS4PConstants.SUBJECT_LOCALITY_NS, attributeValues));

                //add structured role
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getSUBJECT_STRUCTURED_ROLE());
                attributes.add(factory.createAttribute(DS4PConstants.SUBJECT_STRUCTURED_ROLE_NS, attributeValues));

                //add purpose of use
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getSUBJECT_PURPOSE_OF_USE());
                attributes.add(factory.createAttribute(DS4PConstants.SUBJECT_PURPOSE_OF_USE_NS, attributeValues));

                    //add permissions granted
                    //attributeValues = new LinkedList();
                    try {
                        List<String> perms = SAMLTokenConstants.getSUBJECT_PERMISSIONS();
                        Iterator iter = perms.iterator();
                        attributeValues = new LinkedList();            
                        while (iter.hasNext()) {
                            attributeValues.add((String)iter.next());            

                        }
                        attributes.add(factory.createAttribute(DS4PConstants.SUBJECT_SENSITIVITY_PRIVILEGES, attributeValues));            
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }

                //resource name
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getRESOURCE_NAME());
                attributes.add(factory.createAttribute(DS4PConstants.RESOURCE_NWHIN_SERVICE_NS, attributeValues));


                //add resource-id
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getRESOURCE_ID());
                attributes.add(factory.createAttribute(DS4PConstants.RESOURCE_ID_NS, attributeValues));

                //add resource type
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getRESOURCE_TYPE());
                attributes.add(factory.createAttribute(DS4PConstants.RESOURCE_TYPE_NS, attributeValues));

                //add resource action
                attributeValues = new LinkedList();
                attributeValues.add(SAMLTokenConstants.getRESOURCE_ACTION_ID());
                attributes.add(factory.createAttribute(DS4PConstants.RESOURCE_ACTION_ID_NS, attributeValues));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        
            return attributes;
    }
    
    private void initKeyStore() throws IOException {
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(new FileInputStream(keyStoreURL), keyStorePassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initTrustStore() throws IOException {
        try {
            trustStore = KeyStore.getInstance(trustStoreType);
            trustStore.load(new FileInputStream(trustStoreURL), trustStorePassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void getDefaultPrivKeyCert(
    SignatureKeyCallback.DefaultPrivKeyCertRequest request)
    throws IOException {
        
        String uniqueAlias = null;
        try {
            Enumeration aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String currentAlias = (String) aliases.nextElement();
                if (currentAlias.equals(client_priv_key_alias)){
                    if (keyStore.isKeyEntry(currentAlias)) {
                        Certificate thisCertificate = keyStore.getCertificate(currentAlias);
                        if (thisCertificate != null) {
                            if (thisCertificate instanceof X509Certificate) {
                                if (uniqueAlias == null) {
                                    uniqueAlias = currentAlias;                                
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (uniqueAlias != null) {
                request.setX509Certificate(
                (X509Certificate) keyStore.getCertificate(uniqueAlias));
                request.setPrivateKey(
                (PrivateKey) keyStore.getKey(uniqueAlias, keyStorePassword.toCharArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setKeyInfrastructure() {
            //default priv key cert req
        try {
            SignatureKeyCallback.DefaultPrivKeyCertRequest request =
	            new SignatureKeyCallback.DefaultPrivKeyCertRequest();

            getDefaultPrivKeyCert(request);
            
            if ( request.getX509Certificate() == null ) {
                throw new RuntimeException("Not able to resolve the Default Certificate");
            }                                                                                                                 
            pubKey = request.getX509Certificate().getPublicKey();
            privKey = request.getPrivateKey();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private String dumpNodeToString(Node node) {
        String res = "";
        try {
            DOMSource domSource = new DOMSource(node);
            StreamResult result = new StreamResult(new StringWriter());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer xform = null;
            xform = tf.newTransformer();
            xform.transform(domSource, result);
            res = result.getWriter().toString();
        }
        catch (Exception ex) {
            System.err.println("SAMLCallBackHandler:dumpNodeToString "+ex.getMessage());
        }
        return res;
    }
    
}
