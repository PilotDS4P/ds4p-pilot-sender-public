/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.cdaconsentserviceclient;

import gov.va.ehtac.ds4p.ws.cda.CDAR2ConsentDirective;
import gov.va.ehtac.ds4p.ws.cda.CDAR2ConsentDirective_Service;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class CDAR2ConsentDirectiveGenerator {
    private String endpoint = "http://174.78.146.228:8080/DS4PACSServices/CDAR2ConsentDirective?wsdl";
    private String patientName;
    private String patientId;
    private String patientIDType;
    private String patientDateOfBirth;
    private String patientGender;
    private String intendedPOU;
    private List<String> allowedPOU = new ArrayList();
    private String primaryRecipient;
    private List<String> allowedRecipients = new ArrayList();;
    private String authorization;
    private List<String> maskingActions = new ArrayList();
    private List<String> redactActions = new ArrayList();
    private String homeCommunityId;
    
    
    
    public CDAR2ConsentDirectiveGenerator() {
        
    }
    
    public String generatePatientConsentDirective(String patientName, String patientId, String patientIdType, String patientGender, 
                                                  String patientDateOfBirth, String authorization, String intendedPOU, List<String> allowedPOU,
                                                  String primaryRecipient, List<String> allowedRecipients, List<String> maskingActions, 
                                                  List<String> redactActions, String homeCommunityId) { 
        
        String res = "";
        this.patientName = patientName;
        this.patientId = patientId;
        this.patientIDType = patientIdType;
        this.patientGender = patientGender;
        this.patientDateOfBirth = patientDateOfBirth;
        this.authorization = authorization;
        this.intendedPOU = intendedPOU;
        if (!allowedPOU.isEmpty()) this.allowedPOU = allowedPOU;
        this.primaryRecipient = primaryRecipient;
        if (!allowedRecipients.isEmpty()) this.allowedRecipients = allowedRecipients;
        if (!maskingActions.isEmpty()) this.maskingActions = maskingActions;
        if (!redactActions.isEmpty()) this.redactActions = redactActions;
        this.homeCommunityId = homeCommunityId;
        
        res = generateCD();
        
        return res;
    }
    
    private String generateCD() {
        String res = "";
        try {
            CDAR2ConsentDirective_Service service = new CDAR2ConsentDirective_Service();
            CDAR2ConsentDirective port = service.getCDAR2ConsentDirectivePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
            byte[] bRes = port.getCDAR2ConsentDirective(patientName, patientId, patientIDType, patientGender, patientDateOfBirth, authorization, intendedPOU, allowedPOU, primaryRecipient, allowedRecipients, maskingActions, redactActions, homeCommunityId);
            res = new String(bRes);
            //for testing purposes write output to temp workspace area
            writeOutputFile(res);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }       
        return res;
    }    
    
    private void writeOutputFile(String s) {
        try {
            FileWriter fw = new FileWriter("C:/Workspace/cdar2consentdirective.xml");
            StringWriter sw = new StringWriter();
            sw.write(s);
            fw.write(sw.toString());
            fw.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
