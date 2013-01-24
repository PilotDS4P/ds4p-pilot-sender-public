/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.panels;

import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 *
 * @author Duane DeCouteau
 */
public class XACMLViewer extends Panel {
    private TextArea payload = new TextArea();    
    public XACMLViewer(String xacmlvalue) {
        //this.setCaption(xacmltype);
        VerticalLayout v = (VerticalLayout)this.getContent();
        this.setStyleName(Runo.PANEL_LIGHT);
        v.setSpacing(true);
        v.setHeight("100%");
        v.setWidth("100%");
        payload.setWidth("100%");
        payload.setHeight("680px");
        //payload.setEnabled(false);
        
        payload.setValue(xacmlvalue);
        v.addComponent(payload);
    }
}
