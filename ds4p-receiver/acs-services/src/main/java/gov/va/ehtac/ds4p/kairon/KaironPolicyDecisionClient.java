/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.kairon;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.json.JSONObject;



/**
 *
 * @author Duane DeCouteau
 * This returns an authorization decision which interrupted for further processing in the ACS
 */
public class KaironPolicyDecisionClient {
    private String requestorId;
    private String patientId;
    private String purposeOfUse;
    private String recipientId;
    private static String targetURL ="http://direct.rhex.us:8081/policyReasoner/policy/patientPolicy";
    
    private static String jsonString = "{\"PolicyQuery\":{\"Sender\":\"1\",\"Query\":\"\",\"PatientAcctID\":\"7\",\"Purpose\":\"treatment\",\"Recipient\":\"doc@jhu.edu\"}}";
    
    public KaironPolicyDecisionClient() {
        
    }
    
    
    public KaironPolicyObject getAuthorizationJerseyClient(String requestorId, String patientId, String purposeOfUse, String recipientId) {
        KaironPolicyObject kO = null;
        this.requestorId = requestorId;
        this.patientId = patientId;
        this.purposeOfUse = purposeOfUse;
        this.recipientId = recipientId;
        jsonString = "{\"PolicyQuery\":{\"Sender\":\""+requestorId+"\",\"Query\":\"\",\"PatientAcctID\":\""+patientId+"\",\"Purpose\":\""+purposeOfUse+"\",\"Recipient\":\""+recipientId+"\"}}";
        Client client = Client.create();
        try {
            String urlParameters = URLEncoder.encode(jsonString, "UTF-8");            
            WebResource webResource = client.resource(targetURL+"/"+urlParameters);
            
            String res = webResource.accept(MediaType.APPLICATION_JSON).get(String.class);
            System.out.println("Response : "+ res);
            kO = new KaironPolicyObject(res);            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.destroy();
        }
        
        return kO;
    }
}
