/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.filter;

import java.io.Serializable;
import gov.va.ehtac.meaningfuluse.session.DS4PSession;
import gov.va.ehtac.meaningfuluse.session.ProviderAttributes;

/**
 *
 * @author mdufel
 */
public class AdminContext implements Serializable{
 
    private static ThreadLocal<AdminContext> ctx = new ThreadLocal<AdminContext>(); 
    
    static AdminContext getContext(){
        return ctx.get();
    }
    static void setContext(AdminContext ctx){
        AdminContext.ctx.set(ctx);
    }
    
    AdminContext(){
        sessionAttributes = new DS4PSession();
        providerAttributes = new ProviderAttributes();
    }
    
    private DS4PSession sessionAttributes;
    private ProviderAttributes providerAttributes;


    /**
     * @return the patientSessionAttributes
     */
    public static DS4PSession getSessionAttributes() {
        return ctx.get().sessionAttributes;
    }

    /**
     * @param patientSessionAttributes the patientSessionAttributes to set
     */
    public static void setSessionAttributes(DS4PSession sessionAttributes) {
        ctx.get().sessionAttributes = sessionAttributes;
    }
    
    public static ProviderAttributes getProviderAttributes() {
        return ctx.get().providerAttributes;
    }
    
    public static void setProviderAttributes(ProviderAttributes providerAttributes) {
        ctx.get().providerAttributes = providerAttributes;
    }
    
}
