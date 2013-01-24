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

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.policy.reference.ActReason;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy;
import gov.va.ehtac.meaningfuluse.displayobjects.PatientAllowedObligations;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.BindingProvider;


/**
 *
 * @author Duane DeCouteau
 */
public class RuleGenerationTesting extends Panel {
    private HorizontalSplitPanel hzSplit = new HorizontalSplitPanel();
    private TextField msgId = new TextField("Message ID");
    private ComboBox orgCBX = new ComboBox("Organization");
    private ComboBox pouCBX = new ComboBox("Purpose Of Use");
    private Table oblTable = new Table();
    private Button addRowBTN = new Button("Add Obl.");
    private Button delRowBTN = new Button("Del Obl.");
    private Button genBTN = new Button("Generate Rules");
    
    private TextArea rule = new TextArea();

    //String endpoint = "http://vmcentral:8080/DS4PACSServices/DS4PClinicallyAdaptiveRulesInterface?wsdl"; 
    
    
    private List<OrganizationalPolicy> oPolicy = new ArrayList();
    
    public RuleGenerationTesting() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);
        v.setWidth("100%");
        v.setHeight("500px");
        this.setStyleName(Runo.PANEL_LIGHT);        
        getOrganizations();
                
        hzSplit.setStyleName(Runo.SPLITPANEL_SMALL);
        hzSplit.setSplitPosition(30, Sizeable.UNITS_PERCENTAGE);
        hzSplit.setHeight("100%");
        hzSplit.setLocked(false);
        hzSplit.setFirstComponent(createAttributePanel());
        hzSplit.setSecondComponent(getRulePanel());
        
        
        v.addComponent(hzSplit);
        
        
    }
    
    private Panel createAttributePanel() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        p.setCaption("Rule Test Parameters");
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        v.setWidth("100%");
        v.setHeight("100%");
        
        //populate orgcbx
        Iterator iter = oPolicy.iterator();
        while (iter.hasNext()) {
            OrganizationalPolicy o = (OrganizationalPolicy)iter.next();
            orgCBX.addItem(o.getHomeCommunityId());
            orgCBX.setItemCaption(o.getHomeCommunityId(), o.getOrganizationName());
        }
        
        //populate pous
        iter = AdminContext.getSessionAttributes().getPouList().iterator();
        while (iter.hasNext()) {
            ActReason act = (ActReason)iter.next();
            pouCBX.addItem(act.getCode());
            pouCBX.setItemCaption(act.getCode(), act.getDisplayName());
        }
        
        msgId.setWidth("100%");
        
        oblTable.setStyleName(Runo.TABLE_SMALL);
        oblTable.setWidth("100%");
        oblTable.setHeight("150px");
        oblTable.setMultiSelect(false);
        oblTable.setSelectable(true);
        oblTable.setImmediate(true); // react at once when something is selected
        oblTable.setEditable(true);
        oblTable.setWriteThrough(true);
        oblTable.setContainerDataSource(populateTable());

        oblTable.setColumnReorderingAllowed(true);
        oblTable.setColumnCollapsingAllowed(false);
        oblTable.setVisibleColumns(new Object[] {"obligations"});
        oblTable.setColumnHeaders(new String[] {"Patient Obligations"});
        
        oblTable.setTableFieldFactory(new TableFieldFactory() {

            @Override
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                ComboBox box = new ComboBox();
                
                if (propertyId.equals("obligations")) {
                    Iterator iter = AdminContext.getSessionAttributes().getPatientAllowedObligations().iterator();
                    while (iter.hasNext()) {
                        String s = (String)iter.next();
                        box.addItem(s);
                    }
                    box.setImmediate(true);
                    box.setWidth("100%");
                }
                else {
                    return new TextField();
                }
                return box;
            }
        });
        
        addRowBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                    Integer id = new Integer(oblTable.getContainerDataSource().size() + 1);
                    IndexedContainer idx = (IndexedContainer)oblTable.getContainerDataSource();
                    Item aItem = idx.addItem(id);
                    aItem.getItemProperty("obligations").setValue("");
                    refreshTable(idx);
            }
        });
        
        delRowBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object rowId = oblTable.getValue(); 
                if (rowId != null) {
                    oblTable.removeItem(rowId);
                    oblTable.requestRepaint();
                }
            }
        });
        
        genBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                rule.setValue("");
                String hcOid = (String)orgCBX.getValue();
                String pou = (String)pouCBX.getValue();
                String msg = (String)msgId.getValue();
                List<String> pObligations = new ArrayList();
                Container c = oblTable.getContainerDataSource();
                Iterator iter = c.getItemIds().iterator();
                while (iter.hasNext()) {
                    int iid = (Integer)iter.next();
                    Item item = c.getItem(iid);
                    pObligations.add((String)item.getItemProperty("obligations").getValue());     
                }
                //get rule from web service
                try {
                    DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
                    DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
                    ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
                    String res = port.getCASRuleSetStringByPOUObligationsAndHomeCommunityId(pou, pObligations, hcOid, msg);
                    rule.setValue(res);
                    rule.requestRepaint();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }        
            }
        });
        
        orgCBX.setImmediate(true);
        pouCBX.setImmediate(true);
        addRowBTN.setStyleName(Runo.BUTTON_SMALL);
        delRowBTN.setStyleName(Runo.BUTTON_SMALL);
        addRowBTN.setImmediate(true);
        delRowBTN.setImmediate(true);
        rule.setImmediate(true);
        
        v.addComponent(msgId);
        v.addComponent(orgCBX);
        v.addComponent(pouCBX);
        v.addComponent(oblTable);
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.addComponent(addRowBTN);
        h.addComponent(delRowBTN);
        v.addComponent(h);
        v.addComponent(genBTN);
        
        return p;
    }
    
   private void getOrganizations() {
        List<OrganizationalPolicy> lres = new ArrayList();
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
             lres = port.getAllOrganizationalPolicy();
             oPolicy = lres;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }    
   } 
   
    private IndexedContainer populateTable() {
        IndexedContainer res = null;
        List<PatientAllowedObligations> lres = new ArrayList();
        try {
             res = createTableContainer(lres);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
       
    }
    private IndexedContainer createTableContainer(Collection<PatientAllowedObligations> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("obligations", String.class, null);
        
        for (PatientAllowedObligations p : collection) {
            //System.out.println("ITEM ID IS: "+p.getRuleId());
            Item item = container.addItem(p.getObligationId());
            item.getItemProperty("obligations").setValue(p.getObligation());
        }        
        return container;
    }
    private void refreshTable(IndexedContainer idx) {
        oblTable.setContainerDataSource(idx);
        oblTable.setVisibleColumns(new Object[] {"obligations"});
        oblTable.setColumnHeaders(new String[] {"Patient Obligations"});
        oblTable.requestRepaint();
    }
    
    private Panel getRulePanel() {
        Panel r = new Panel();
        VerticalLayout v = (VerticalLayout)r.getContent();
        r.setStyleName(Runo.PANEL_LIGHT);
        r.setCaption("Generated Rule");
        v.setSpacing(true);
        rule.setWidth("100%");
        rule.setHeight("320px");
        rule.setImmediate(true);
        v.setWidth("100%");
        v.setHeight("100%");
        v.addComponent(rule);
        
        return r;
    }
   
}
