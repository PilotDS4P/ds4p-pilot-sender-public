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


import gov.va.ehtac.ds4p.ws.au.AuthLog;
import gov.va.ehtac.ds4p.ws.au.DS4PAudit;
import gov.va.ehtac.ds4p.ws.au.DS4PAuditService;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class AuditLogs extends Panel {
        Table table = new Table("Authorization Requests");
        private Button refresh = new Button("Refresh");
        private Button viewRequest = new Button("View Authorization Request");
        private Button viewResponse = new Button("View Policy Engine Response");
        private Button viewObligations = new Button("View Obligations");
        private Button viewDRL = new Button("View Generated DRL");
        private Button viewExecRules = new Button("View Rules That Executed");
        private Button viewC32 = new Button("View Annotated C32");
        
        //private Button setFilterBTN = new Button("Set Filter");
        //private ComboBox cb = new ComboBox("Filter By Request Type");
        //private String[] objList = new String[] {"DocumentQueryIn","DocumentQueryOut","DocumentRetrieveIn","DocumentRetrieveOut","PatientDiscoveryIn", "PatientDiscoveryOut", "DocumentSubmissionIn", "DocumentSubmissionOut", "Unknown Resource"};
        //private TextField patientFilter = new TextField("Filter By Patient");
        
        Window subwindow;
        
        private String auditEndpoint;
        
        private boolean noResults = false;
        
        //
    
    public AuditLogs(){
        this.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);

        auditEndpoint = AdminContext.getSessionAttributes().getDS4P_AUDIT_ENDPOINT();
        
        table.setStyleName(Runo.TABLE_SMALL);
        table.setWidth("100%");
        table.setHeight("400px");
        table.setSelectable(true);
        table.setMultiSelect(false);
        table.setImmediate(true); // react at once when something is selected
        table.setContainerDataSource(populateAuthorizationRequests());
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(false);
        table.setVisibleColumns(new Object[] {"msgDate","healthcareObject","purposeOfUse","requestor","uniqueIdentifier","decision","responsetime", "messageId"});
        table.setColumnHeaders(new String[] {"Date","Resource","POU","Recipient","Patient ID","PDP Decision","Resp. Time(ms)", "Message ID"});
        
        refresh.setIcon(new ThemeResource("../runo/icons/16/reload.png"));
        viewRequest.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        viewResponse.setIcon(new ThemeResource("../runo/icons/16/document.png"));
        viewObligations.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
        viewDRL.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
        viewExecRules.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
        viewC32.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
        
        refresh.setStyleName(Runo.BUTTON_SMALL);
        viewRequest.setStyleName(Runo.BUTTON_SMALL);
        viewResponse.setStyleName(Runo.BUTTON_SMALL);
        viewObligations.setStyleName(Runo.BUTTON_SMALL);
        viewDRL.setStyleName(Runo.BUTTON_SMALL);
        viewExecRules.setStyleName(Runo.BUTTON_SMALL);
        viewC32.setStyleName(Runo.BUTTON_SMALL);
        
        //viewObligations.setEnabled(false);
        
        refresh.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                ((IndexedContainer)table.getContainerDataSource()).removeAllContainerFilters();
                table.setContainerDataSource(populateAuthorizationRequests());
                table.setVisibleColumns(new Object[] {"msgDate","healthcareObject","purposeOfUse","requestor","uniqueIdentifier","decision", "responsetime", "messageId"});
                table.setColumnHeaders(new String[] {"Date","Resource","POU","Recipient","Patient ID","PDP Decision", "Resp. Time(ms)", "Message ID"});
                table.requestRepaint();
            }
        });
        
        viewRequest.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AuthLog log = getAuthLogObject();
                if (log != null) {
                    String drl = log.getXacmlRequest();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("XACML Request");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        viewResponse.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AuthLog log = getAuthLogObject();
                if (log != null) {
                    String drl = log.getXacmlResponse();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("XACML Response - Authorization Decision");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        viewObligations.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AuthLog log = getAuthLogObject();
                if (log != null) {
                    String drl = log.getObligations();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("XACML Response - Obligations");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }                
            }
        });
        
        viewDRL.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AuthLog log = getAuthLogObject();
                if (log != null) {
                    String drl = log.getGenDrl();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("Generated DRL (Annotation Rules)");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        viewExecRules.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AuthLog log = getAuthLogObject();
                if (log != null) {
                    String drl = log.getExecRules();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("Executed Annotation Rules");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        viewC32.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AuthLog log = getAuthLogObject();
                if (log != null) {
                    String drl = log.getAnnotatedDoc();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("Resultant Annotated C32");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
                
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.addComponent(refresh);
        h.addComponent(viewRequest);
        h.addComponent(viewResponse);
        h.addComponent(viewObligations);
        h.addComponent(viewDRL);
        h.addComponent(viewExecRules);
        h.addComponent(viewC32);
        
        v.addComponent(table);
        v.addComponent(h);
        
        
        //if (noResults) getApplication().getMainWindow().showNotification("ERROR: No results are available for this node.  The node may not be available or is configured incorrectly.", Notification.TYPE_ERROR_MESSAGE);
        
    }
    
    
    
    private IndexedContainer populateAuthorizationRequests() {
        IndexedContainer container = new IndexedContainer();
        DS4PAuditService service = new DS4PAuditService();
        DS4PAudit port = service.getDS4PAuditPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, auditEndpoint);
        List<AuthLog> aList = port.getAllAuthorizationEvent();
        
        if (aList == null || aList.isEmpty()) {
            noResults = true;
        }
        else {
            noResults = false;
        }
        
        container = createIndexedContainer(aList);
        return container;
    }
    
    private IndexedContainer createIndexedContainer(Collection<AuthLog> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("msgDate", String.class, null);
        container.addContainerProperty("healthcareObject", String.class, null);
        container.addContainerProperty("purposeOfUse", String.class, null);
        container.addContainerProperty("requestor", String.class, null);
        container.addContainerProperty("uniqueIdentifier", String.class, null);
        container.addContainerProperty("decision", Button.class, null);
        container.addContainerProperty("responsetime", Long.class, null);
        container.addContainerProperty("messageId", String.class, null);
        //container.addContainerProperty("authLogObject", AuthLog.class, null);

        int i = 0;
        for (AuthLog p : collection) {
            i++;
            Integer id = new Integer(i);
            Item item = container.addItem(id);
            item.getItemProperty("msgDate").setValue(convertDateToString(p.getMsgDate()));
            item.getItemProperty("healthcareObject").setValue(p.getHealthcareObject());
            item.getItemProperty("purposeOfUse").setValue(p.getPurposeOfUse());
            item.getItemProperty("requestor").setValue(p.getRequestor());
            item.getItemProperty("uniqueIdentifier").setValue(p.getUniqueIdentifier());
            item.getItemProperty("decision").setValue(getButtonWithIcon(p.getDecision()));
            item.getItemProperty("responsetime").setValue(new Long(p.getResponseTime()));
            item.getItemProperty("messageId").setValue(p.getHieMsgId());
            //item.getItemProperty("authLogObject").setValue(p);
        }
        return container;
    }
    private String convertDateToString(XMLGregorianCalendar xres) {
        Date dt = null;
        String res = "";
        try {
            Calendar xcal = Calendar.getInstance();
            xcal.set(xres.getYear(), xres.getMonth() - 1, xres.getDay(), xres.getHour(), xres.getMinute(), xres.getSecond());
            dt = xcal.getTime();
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                res = sdt.format(dt);
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        catch (Exception ex) {
            System.err.println("LocalPatientSearch "+ex.getMessage());
        }
        return res;
    }
    
    private Button getButtonWithIcon(String val) {
        Button res = new Button(val);
        res.setStyleName(Runo.BUTTON_LINK);
        if ("Permit".equals(val)) {
            res.setIcon(new ThemeResource("../runo/icons/16/ok.png"));
        }
        else {
            res.setIcon(new ThemeResource("../runo/icons/16/attention.png"));
        }
        return res;
    }
    
    private AuthLog getAuthLogObject() {
        AuthLog res = null;
        try {
            Object rowId = table.getValue();
            if (rowId != null) {
                String obj = (String)table.getContainerProperty(rowId, "messageId").getValue();
            
                DS4PAuditService service = new DS4PAuditService();
                DS4PAudit port = service.getDS4PAuditPort();
                ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, auditEndpoint);
                res = port.getAuthorizationEventByHIEMsgId(obj);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}
