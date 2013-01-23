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

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.policy.reference.ActReason;
import gov.va.ehtac.ds4p.ws.ch.DS4PContextHandler;
import gov.va.ehtac.ds4p.ws.ch.DS4PContextHandler_Service;
import gov.va.ehtac.ds4p.ws.ch.EnforcePolicy.Xsparesource;
import gov.va.ehtac.ds4p.ws.ch.EnforcePolicy.Xspasubject;
import gov.va.ehtac.ds4p.ws.ch.EnforcePolicyResponse;
import gov.va.ehtac.ds4p.ws.ch.PolicyEnforcementObject;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class ContextHandlerTesting extends Panel {
    Xspasubject subj = new Xspasubject();
    Xsparesource resource = new Xsparesource();
    PolicyEnforcementObject res = new PolicyEnforcementObject();

    TextField hcName = new TextField("Sender Home Community Name");
    TextField hcId = new TextField("Sender Home Community Id");
    TextField facility = new TextField("Sender Facility");
    TextField subjectId = new TextField("Recipient");
    ComboBox pouCBX = new ComboBox("Purpose of Use");
    
    TextField resId = new TextField("Unique Patient Id");
    TextField resName = new TextField("Resource or Service Name");
    TextField resType = new TextField("Resource Type");
    
    TextField pdpDecision = new TextField("PDP Decision");
    TextArea obligations = new TextArea("PDP Obligations (Patient)");
    TextField pdpStatus = new TextField("PDP Status Message");
    
    Button enforcePolicyBTN = new Button("Request Policy Descision");
    
    TextField msgId = new TextField("Message ID");
    //Button genUUIDBTN = new Button("Gen UUID");
    
    //String endpoint = "http://174.78.146.228:8080/DS4PACSServices/DS4PContextHandler?wsdl";
    
    public ContextHandlerTesting() {
        this.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);
        v.setHeight("100%");
        v.setWidth("100%");
        subjectId.setWidth("300px");
        hcName.setWidth("300px");
        hcId.setWidth("200px");
        facility.setWidth("400px");
        resId.setWidth("200px");
        resName.setWidth("200px");
        resType.setWidth("200px");
        obligations.setWidth("400px");
        obligations.setHeight("100px");
        pdpStatus.setWidth("400px");

        populateCBX();
        
        Label sLabel = new Label("<b><u>Subject Information</u></b>");
        sLabel.setContentMode(Label.CONTENT_XHTML);
        // add subject components
        v.addComponent(sLabel);
        msgId.setEnabled(false);
        msgId.setImmediate(true);
        msgId.setWidth("400px");
        v.addComponent(msgId);
        HorizontalLayout hv1 = new HorizontalLayout();
        hv1.setSpacing(true);
        hv1.addComponent(subjectId);
        hv1.addComponent(hcName);
        v.addComponent(hv1);
        HorizontalLayout hv2 = new HorizontalLayout();
        hv2.setSpacing(true);
        hv2.addComponent(hcId);
        hv2.addComponent(facility);
        v.addComponent(hv2);
        v.addComponent(pouCBX);
        
        //addResource Information
        HorizontalLayout hv3 = new HorizontalLayout();
        hv3.setSpacing(true);
        hv3.addComponent(resId);
        hv3.addComponent(resName);
        hv3.addComponent(resType);
        Label plabel = new Label("<b><u>Resource Information</u></b>");
        plabel.setContentMode(Label.CONTENT_XHTML);
        v.addComponent(plabel);
        v.addComponent(hv3);
        
        
        pdpDecision.setImmediate(true);
        obligations.setImmediate(true);
        HorizontalLayout hv4 = new HorizontalLayout();
        hv4.setSpacing(true);
        Label alabel = new Label("<b><u>Access Control Decision</u><b>");
        alabel.setContentMode(Label.CONTENT_XHTML);
        hv4.addComponent(pdpDecision);
        hv4.addComponent(obligations);
        v.addComponent(alabel);
        v.addComponent(hv4);
        v.addComponent(pdpStatus);
        
        enforcePolicyBTN.setImmediate(true);
        enforcePolicyBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                pdpDecision.setValue("");
                obligations.setValue("");
                pdpStatus.setValue("");
                UUID msg = UUID.randomUUID();
                msgId.setValue(msg.toString());
                setSubject();
                setResource();
                try {
                    DS4PContextHandler_Service service = new DS4PContextHandler_Service();
                    DS4PContextHandler port = service.getDS4PContextHandlerPort();
                    ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CH_ENDPOINT());
                    EnforcePolicyResponse.Return ret = port.enforcePolicy(subj, resource);
                    pdpDecision.setValue(ret.getPdpDecision());
                    pdpStatus.setValue(ret.getPdpStatus());
                    List<String> obs = ret.getPdpObligation();
                    Iterator iter = obs.iterator();
                    StringBuffer sb = new StringBuffer();
                    while (iter.hasNext()) {
                        String r = (String)iter.next();
                        sb.append(r + "\n");
                    }
                    obligations.setValue(sb.toString());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        v.addComponent(enforcePolicyBTN);
        
    }
    
    private void setSubject() {
        subj.setMessageId((String)msgId.getValue());
        subj.setOrganization((String)hcName.getValue());
        subj.setOrganizationId((String)facility.getValue());
        subj.setSubjectId((String)subjectId.getValue());
        subj.setSubjectLocality((String)hcId.getValue());
        subj.setSubjectPurposeOfUse((String)pouCBX.getValue());
       
    }
    
    private void setResource() {
        resource.setResourceAction("Execute");
        resource.setResourceId((String)resId.getValue());
        resource.setResourceName((String)resName.getValue());
        resource.setResourceType((String)resType.getValue());
    }
    
    private void populateCBX() {
        List<ActReason> plist = AdminContext.getSessionAttributes().getPouList();
        Iterator iter = plist.iterator();
        while (iter.hasNext()) {
            ActReason reason = (ActReason)iter.next();
            pouCBX.addItem(reason.getCode());
            pouCBX.setItemCaption(reason.getCode(), reason.getDisplayName());
        }
    }

    
    
}
