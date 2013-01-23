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

import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.io.*;
import java.util.Properties;

/**
 *
 * @author Duane DeCouteau
 */
public class ConfigurationProvider {
    String configFile = "../../../../../../resources/config/Config.properties";

    public ConfigurationProvider() {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(configFile)));
            Properties prop = new Properties();
            prop.load(r);
            r.close(); 
            
            String cas = prop.getProperty("DS4P_CAS_ENDPOINT");
            String ch = prop.getProperty("DS4P_CH_ENDPOINT");
            String ref = prop.getProperty("DS4P_REFDATA_ENDPOINT");
            String au = prop.getProperty("DS4P_AUDIT_ENDPOINT");
            String xm = prop.getProperty("DS4P_XDM_ENDPOINT");
            String dauth = prop.getProperty("DS4P_AUTH_ENDPOINT");
            AdminContext.getSessionAttributes().setDS4P_CAS_ENDPOINT(cas);
            AdminContext.getSessionAttributes().setDS4P_CH_ENDPOINT(ch);
            AdminContext.getSessionAttributes().setDS4P_REFDATA_ENDPOINT(ref);
            AdminContext.getSessionAttributes().setDS4P_AUDIT_ENDPOINT(au);
            AdminContext.getSessionAttributes().setDS4P_XDM_ENDPOINT(xm);
            AdminContext.getSessionAttributes().setDS4P_AUTH_ENDPOINT(dauth);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: CONFIGPROVIDER: "+ex.getMessage());
        }                
        
    }
}
