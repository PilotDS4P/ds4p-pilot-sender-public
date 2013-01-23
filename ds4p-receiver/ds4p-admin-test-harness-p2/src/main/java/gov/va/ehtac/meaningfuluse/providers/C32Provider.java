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


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.hl7.v3.ANY;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ED;
import org.hl7.v3.IVLTS;
import org.hl7.v3.IVXBTS;
import org.hl7.v3.POCDMT000040Act;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.POCDMT000040Observation;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.POCDMT000040StructuredBody;
import org.hl7.v3.ST;
import org.hl7.v3.StrucDocText;
import org.hl7.v3.TEL;

/**
 *
 * @author Duane DeCouteau
 */
public class C32Provider {
    private POCDMT000040ClinicalDocument clinicalDocument;
    
    public C32Provider(String fname) {
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
            
            createC32ObjectsFromXML(res);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: C32PROVIDER: "+ex.getMessage());
        }                
    }
    
    private void createC32ObjectsFromXML(String c32) {
        clinicalDocument = null;
        try {
            JAXBContext context = JAXBContext.newInstance(POCDMT000040ClinicalDocument.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            JAXBElement element = (JAXBElement)o;
            clinicalDocument = (POCDMT000040ClinicalDocument)element.getValue();
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
 
}
