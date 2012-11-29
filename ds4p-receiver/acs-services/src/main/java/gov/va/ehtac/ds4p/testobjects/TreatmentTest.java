/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.testobjects;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.oasis.names.tc.xspa.v2.PolicyEnforcementObject;
import org.oasis.names.tc.xspa.v2.XspaResource;

/**
 *
 * @author Duane DeCouteau
 */
public class TreatmentTest {
    PolicyEnforcementObject obj = new PolicyEnforcementObject();
    
    public PolicyEnforcementObject getTestDecision(XspaResource resource) {
        obj = new PolicyEnforcementObject();
        if (resource.getResourceId().equals("111111")) {  //permit no obligations patient
            obj.setPdpDecision("Permit");
            obj.setPdpStatus("ok");
            obj.setRequestTime(getCurrentDateTime());
            obj.setResourceId(resource.getResourceId());
            obj.setResourceName(resource.getResourceName());
            obj.setResponseTime(getCurrentDateTime());
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:encrypt:ETH");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:encrypt:PSY");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law:42CFRPart2");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:refrain-policy:NORDSCLCD");            
        }
        else if (resource.getResourceId().equals("222222")) { //patient with patient obligations
            obj.setPdpDecision("Permit");
            obj.setPdpStatus("ok");
            obj.setRequestTime(getCurrentDateTime());
            obj.setResourceId(resource.getResourceId());
            obj.setResourceName(resource.getResourceName());
            obj.setResponseTime(getCurrentDateTime());
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:patient:redact:ETH");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:patient:redact:PSY");            
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:encrypt:ETH");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:encrypt:PSY");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law:42CFRPart2");
            obj.getPdpObligation().add("urn:oasis:names:tc:xspa:2.0:resource:org:refrain-policy:NORDSCLCD");                        
        }
        else if (resource.getResourceId().equals("333333")) { // patient deny
            obj.setPdpDecision("Deny");
            obj.setPdpStatus("ok");
            obj.setRequestTime(getCurrentDateTime());
            obj.setResourceId(resource.getResourceId());
            obj.setResourceName(resource.getResourceName());
            obj.setResponseTime(getCurrentDateTime());            
        }        
        else {
            //do nothing
        }
        
        return obj;
    }
    
   private XMLGregorianCalendar getCurrentDateTime() {
       XMLGregorianCalendar xgc = null;
       try {
        GregorianCalendar gc = new GregorianCalendar();
        DatatypeFactory dtf = DatatypeFactory.newInstance();
        xgc = dtf.newXMLGregorianCalendar(gc);
       }
       catch (Exception ex) {
           ex.printStackTrace();
       }
        return xgc;
   }
}
