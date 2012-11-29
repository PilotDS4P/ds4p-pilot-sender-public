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

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.policy.reference.ActUSPrivacyLaw;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy;
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
public class OrganizationGeneralInfo extends Panel {
    private Table table = new Table();
    private TextField orgNameFLD = new TextField("Organization Name");
    private TextField orgIdFLD = new TextField("Organization ID");
    private ComboBox lawCBX = new ComboBox("Primary US Law");
    private Button saveBTN = new Button("Save Organization Changes");
    //String endpoint = "http://vmcentral:8080/DS4PACSServices/DS4PClinicallyAdaptiveRulesInterface?wsdl"; 
    
    private OrganizationalPolicy selectedPolicy;
    private OrganizationPolicyPanel mparent;
    
    public OrganizationGeneralInfo(final OrganizationPolicyPanel mparent) {
        this.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);
        
        this.mparent = mparent;
        
        table.setStyleName(Runo.TABLE_SMALL);
        table.setWidth("100%");
        table.setHeight("350px");
        table.setMultiSelect(false);
        table.setSelectable(true);
        table.setImmediate(true); // react at once when something is selected
        table.setEditable(false);
        table.setWriteThrough(true);
        table.setContainerDataSource(populateTable());

        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(false);
        table.setVisibleColumns(new Object[] {"orgName","orgId","oUSLaw"});
        table.setColumnHeaders(new String[] {"Organizations Name","Home Community ID", "Primary US Law"});
        
        table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object rowId = table.getValue();
                if (rowId != null) {
                    OrganizationalPolicy obj = (OrganizationalPolicy)table.getContainerProperty(rowId, "oObject").getValue();
                    selectedPolicy = obj;
                    orgNameFLD.setValue(obj.getOrganizationName());
                    orgIdFLD.setValue(obj.getHomeCommunityId());
                    lawCBX.setValue(obj.getApplicableUsLaw());
                    orgNameFLD.requestRepaint();
                    orgIdFLD.requestRepaint();
                    lawCBX.requestRepaint();
                    AdminContext.getSessionAttributes().setSelectedOrganization(obj.getHomeCommunityId());
                    mparent.refreshOrganizationRules();
                }                

            }
        });
        
        //populate law cbx
        Iterator iter = AdminContext.getSessionAttributes().getUsLawList().iterator();
        while (iter.hasNext()) {
            ActUSPrivacyLaw act = (ActUSPrivacyLaw)iter.next();
            lawCBX.addItem(act.getCode());
            lawCBX.setItemCaption(act.getCode(), act.getDisplayName());
        }
        
        saveBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object rowId = table.getValue();
                if (rowId != null) {
                    selectedPolicy.setApplicableUsLaw((String)lawCBX.getValue());
                    selectedPolicy.setHomeCommunityId((String)orgIdFLD.getValue());
                    selectedPolicy.setOrganizationName((String)orgNameFLD.getValue());
                    try {
                        DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
                        DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
                        ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
                        Boolean res = port.setOrganizationalPolicy(selectedPolicy);
                        refreshTable();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        orgNameFLD.setWidth("250px");
        orgIdFLD.setWidth("250px");
        orgNameFLD.setImmediate(true);
        orgIdFLD.setImmediate(true);
        lawCBX.setImmediate(true);
        h.addComponent(orgNameFLD);
        h.addComponent(orgIdFLD);
        h.addComponent(lawCBX);
        v.addComponent(table);
        v.addComponent(h);
        v.addComponent(saveBTN);
    }
    
    private IndexedContainer populateTable() {
        IndexedContainer res = null;
        List<OrganizationalPolicy> lres = new ArrayList();
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
             lres = port.getAllOrganizationalPolicy();
             res = createTableContainer(lres);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
       
    }
    private IndexedContainer createTableContainer(Collection<OrganizationalPolicy> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("orgName", String.class, null);
        container.addContainerProperty("orgId", String.class, null);        
        container.addContainerProperty("oUSLaw", String.class, null);
        container.addContainerProperty("oObject", OrganizationalPolicy.class, null);        
        
        for (OrganizationalPolicy p : collection) {
            //System.out.println("ITEM ID IS: "+p.getRuleId());
            Item item = container.addItem(p.getIdorganizationalPolicy());
            item.getItemProperty("orgName").setValue(p.getOrganizationName());
            item.getItemProperty("orgId").setValue(p.getHomeCommunityId());            
            item.getItemProperty("oUSLaw").setValue(p.getApplicableUsLaw()); 
            item.getItemProperty("oObject").setValue(p);             
        }        
        return container;
    }
    
    private void refreshTable() {
        IndexedContainer idx = populateTable();
        table.setContainerDataSource(idx);
        table.setVisibleColumns(new Object[] {"orgName","orgId","oUSLaw"});
        table.setColumnHeaders(new String[] {"Organizations Name","Home Community ID", "Primary US Law"});
        table.requestRepaint();      
    }
     
}
