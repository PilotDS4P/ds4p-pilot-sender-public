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
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.policy.reference.ActInformationSensitivityPolicy;
import gov.va.ds4p.policy.reference.ActReason;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class ProviderAuthorizationAttributes extends Panel {
    private TextField userId = new TextField("User ID");
    private TextField subjectId = new TextField("Subject ID");
    private TextField organization = new TextField("Organization ID");
    private TextField organizationId = new TextField("Organization Name");
    private TextField role = new TextField("Role");
    private ComboBox pouCBX = new ComboBox("Purpose Of Use");
    private Table allowedResources = new Table();
    private Table sensitivityPermissions = new Table();
    private Button addResource = new Button("Add Resource");
    private Button remResource = new Button("Remove Resource");
    private Button addSens = new Button("Add Privilege");
    private Button remSens = new Button("Remove Privilege");
    private Button saveBTN = new Button("Save Changes");
    
    
   
    public ProviderAuthorizationAttributes() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        this.setStyleName(Runo.PANEL_LIGHT);
        v.setSpacing(true);
        v.setHeight("100%");
        v.setWidth("100%");
        
        userId.setWidth("400px");
        subjectId.setWidth("400px");
        organization.setWidth("400px");
        organizationId.setWidth("400px");
        role.setWidth("400px");
        pouCBX.setWidth("400px");
        
        setResourceTable();
        setPrivilegesTable();
        populatePOU();
        
        //set field values
        userId.setValue(AdminContext.getProviderAttributes().getUserId());
        subjectId.setValue(AdminContext.getProviderAttributes().getProviderId());
        organization.setValue(AdminContext.getProviderAttributes().getOrganization());
        organizationId.setValue(AdminContext.getProviderAttributes().getProviderHomeCommunityId());
        role.setValue(AdminContext.getProviderAttributes().getRole());
        pouCBX.setValue(AdminContext.getProviderAttributes().getPurposeOfUse());
        
        
        addResource.setStyleName(Runo.BUTTON_SMALL);
        remResource.setStyleName(Runo.BUTTON_SMALL);
        addSens.setStyleName(Runo.BUTTON_SMALL);
        remSens.setStyleName(Runo.BUTTON_SMALL);
        saveBTN.setStyleName(Runo.BUTTON_SMALL);
        
        addResource.setImmediate(true);
        remResource.setImmediate(true);
        addSens.setImmediate(true);
        remSens.setImmediate(true);
        saveBTN.setImmediate(true);
        
        addResource.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                    Integer id = new Integer(allowedResources.getContainerDataSource().size() + 1);
                    IndexedContainer idx = (IndexedContainer)allowedResources.getContainerDataSource();
                    Item aItem = idx.addItem(id);
                    aItem.getItemProperty("oResource").setValue("");
                    refreshResourceTable(idx);
            }
        });
        
        remResource.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object rowId = allowedResources.getValue(); 
                if (rowId != null) {
                    allowedResources.removeItem(rowId);
                    allowedResources.requestRepaint();
                }
            }
        });
        
        addSens.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                    Integer id = new Integer(sensitivityPermissions.getContainerDataSource().size() + 1);
                    IndexedContainer idx = (IndexedContainer)sensitivityPermissions.getContainerDataSource();
                    Item aItem = idx.addItem(id);
                    aItem.getItemProperty("oPrivilege").setValue("");
                    refreshPrivilegeTable(idx);
            }
        });
        
        remSens.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object rowId = sensitivityPermissions.getValue(); 
                if (rowId != null) {
                    sensitivityPermissions.removeItem(rowId);
                    sensitivityPermissions.requestRepaint();
                }
            }
        });
        
        saveBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AdminContext.getProviderAttributes().setPurposeOfUse((String)pouCBX.getValue());
                AdminContext.getProviderAttributes().getAllowedNwHINActions().clear();
                AdminContext.getProviderAttributes().getAllowedSensitivityActions().clear();
                List<String> pResources = new ArrayList();
                Container c = allowedResources.getContainerDataSource();
                Iterator iter = c.getItemIds().iterator();
                while (iter.hasNext()) {
                    int iid = (Integer)iter.next();
                    Item item = c.getItem(iid);
                    pResources.add((String)item.getItemProperty("oResource").getValue());     
                }
                AdminContext.getProviderAttributes().setAllowedNwHINActions(pResources);

                List<String> pSensitivity = new ArrayList();
                Container d = sensitivityPermissions.getContainerDataSource();
                Iterator iter2 = d.getItemIds().iterator();
                while (iter2.hasNext()) {
                    int iid = (Integer)iter2.next();
                    Item item = d.getItem(iid);
                    pSensitivity.add((String)item.getItemProperty("oPrivilege").getValue());     
                }
                AdminContext.getProviderAttributes().setAllowedSensitivityActions(pSensitivity); 
            }
        });
        
        v.addComponent(userId);
        v.addComponent(subjectId);
        v.addComponent(organization);
        v.addComponent(organizationId);
        v.addComponent(role);
        v.addComponent(pouCBX);
        v.addComponent(allowedResources);
        
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.addComponent(addResource);
        h.addComponent(remResource);
        
        v.addComponent(h);
        
        v.addComponent(sensitivityPermissions);
        
        HorizontalLayout h2 = new HorizontalLayout();
        h2.setSpacing(true);
        h2.addComponent(addSens);
        h2.addComponent(remSens);
        
        v.addComponent(h2);
        v.addComponent(saveBTN);
        
    }
    
    private void setResourceTable() {
        allowedResources.setStyleName(Runo.TABLE_SMALL);
        allowedResources.setWidth("500px");
        allowedResources.setHeight("150px");
        allowedResources.setMultiSelect(false);
        allowedResources.setSelectable(true);
        allowedResources.setImmediate(true); // react at once when something is selected
        allowedResources.setEditable(true);
        allowedResources.setWriteThrough(true);
        allowedResources.setContainerDataSource(populateAllowedResources());

        allowedResources.setColumnReorderingAllowed(true);
        allowedResources.setColumnCollapsingAllowed(false);
        allowedResources.setVisibleColumns(new Object[] {"oResource"});
        allowedResources.setColumnHeaders(new String[] {"Resource Permissions"});
        
        allowedResources.setTableFieldFactory(new TableFieldFactory() {

            @Override
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                ComboBox box = new ComboBox();
                
                if (propertyId.equals("oResource")) {
                    Iterator iter = AdminContext.getProviderAttributes().getAvailableNwHINActions().iterator();
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
        
    }
    
    private IndexedContainer populateAllowedResources() {
        IndexedContainer res = null;
        List<String> lres = AdminContext.getProviderAttributes().getAllowedNwHINActions();
        res = createAllowedResourceContainer(lres);
        return res;
    }
    private IndexedContainer createAllowedResourceContainer(Collection<String> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("oResource", String.class, null);
        
        int i = 1;
        for (String p : collection) {
            Item item = container.addItem(new Integer(i));
            item.getItemProperty("oResource").setValue(p);
            System.out.println("Resource "+p);
            i++;
        }        
        return container;
    }
    
    private void setPrivilegesTable() {
        sensitivityPermissions.setStyleName(Runo.TABLE_SMALL);
        sensitivityPermissions.setWidth("500px");
        sensitivityPermissions.setHeight("150px");
        sensitivityPermissions.setMultiSelect(false);
        sensitivityPermissions.setSelectable(true);
        sensitivityPermissions.setImmediate(true); // react at once when something is selected
        sensitivityPermissions.setEditable(true);
        sensitivityPermissions.setWriteThrough(true);
        sensitivityPermissions.setContainerDataSource(populateSensitivityPermissions());

        sensitivityPermissions.setColumnReorderingAllowed(true);
        sensitivityPermissions.setColumnCollapsingAllowed(false);
        sensitivityPermissions.setVisibleColumns(new Object[] {"oPrivilege"});
        sensitivityPermissions.setColumnHeaders(new String[] {"Sensitivity Privilege"});
        
        sensitivityPermissions.setTableFieldFactory(new TableFieldFactory() {

            @Override
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                ComboBox box = new ComboBox();
                
                if (propertyId.equals("oPrivilege")) {
                    Iterator iter = AdminContext.getSessionAttributes().getSensitivityList().iterator();
                    while (iter.hasNext()) {
                        ActInformationSensitivityPolicy s = (ActInformationSensitivityPolicy)iter.next();
                        box.addItem(s.getCode());
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
    }
    private IndexedContainer populateSensitivityPermissions() {
        IndexedContainer res = null;
        List<String> lres = AdminContext.getProviderAttributes().getAllowedSensitivityActions();
        res = createSensitivityPermissionsContainer(lres);
        return res;
    }
    
    private IndexedContainer createSensitivityPermissionsContainer(Collection<String> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("oPrivilege", String.class, null);
        
        int i = 1;
        for (String p : collection) {
            Item item = container.addItem(new Integer(i));
            item.getItemProperty("oPrivilege").setValue(p);
            System.out.println("Privilege "+p);
            i++;
        }        
        return container;
    }
    
    private void refreshResourceTable(IndexedContainer idx) {
        allowedResources.setContainerDataSource(idx);
        allowedResources.setVisibleColumns(new Object[] {"oResource"});
        allowedResources.setColumnHeaders(new String[] {"Resource Permissions"});
        allowedResources.requestRepaint();        
    }
    
    private void refreshPrivilegeTable(IndexedContainer idx) {
        sensitivityPermissions.setContainerDataSource(idx);
        sensitivityPermissions.setVisibleColumns(new Object[] {"oPrivilege"});
        sensitivityPermissions.setColumnHeaders(new String[] {"Sensitivity Privilege"});
        sensitivityPermissions.requestRepaint();        
    }
    
   private void populatePOU() {
      Iterator pouList = AdminContext.getSessionAttributes().getPouList().iterator();
      while (pouList.hasNext()) {
          ActReason reas = (ActReason)pouList.next();
          pouCBX.addItem(reas.getCode());
      }
   }
    
}
