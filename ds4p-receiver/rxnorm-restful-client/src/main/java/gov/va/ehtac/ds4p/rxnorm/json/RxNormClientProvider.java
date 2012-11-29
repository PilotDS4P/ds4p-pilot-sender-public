/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.rxnorm.json;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import gov.va.ehtac.ds4p.rxnorm.Rxnormdata;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Duane DeCouteau
 */
public class RxNormClientProvider {
    private static String targetURL ="http://rxnav.nlm.nih.gov/REST";
    private static String webOp = "drugs?name=";
    
            
    private Rxnormdata createRXObjectsFromXML(String c) {
        Rxnormdata rx = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Rxnormdata.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c);

            Object o = unmarshaller.unmarshal(sr);
            rx = (Rxnormdata)o;
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        } 
        return rx;
    }
    
    public Rxnormdata getDrugs(String drugcomponentname) {
        Rxnormdata rx = null;
        Client client = Client.create();
        String s = webOp+drugcomponentname;
        try {
            String urlParameters = URLEncoder.encode(s, "UTF-8");            
            WebResource webResource = client.resource(targetURL+"/"+urlParameters);
            
            String res = webResource.accept(MediaType.APPLICATION_XML).get(String.class);
            //System.out.println("NLM RESPONSE: "+res);
            rx = createRXObjectsFromXML(res);  
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.destroy();
        }           
        return rx;
    }
}
