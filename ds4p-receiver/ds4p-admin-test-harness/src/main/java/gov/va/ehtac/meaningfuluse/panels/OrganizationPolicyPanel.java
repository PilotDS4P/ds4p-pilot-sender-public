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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;

/**
 *
 * @author Duane DeCouteau
 */
public class OrganizationPolicyPanel extends Panel{
    private TabSheet tabs = new TabSheet();
    private Panel generalOrgInfo = new Panel();
    private OrganizationDS4PPolicyGeneral generalPolicy;
    private Panel organizationHCSPolicy = new Panel();
    
    public OrganizationPolicyPanel() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);
        
        tabs.setWidth("100%");
        tabs.setHeight("650px");
        VerticalLayout v1 = new VerticalLayout();
        VerticalLayout v2 = new VerticalLayout();
        VerticalLayout v3 = new VerticalLayout();
        
        generalOrgInfo = new OrganizationGeneralInfo(this);
        generalPolicy = new OrganizationDS4PPolicyGeneral();
        organizationHCSPolicy = new RuleGenerationTesting();
        
        v1.addComponent(generalOrgInfo);
        v2.addComponent(generalPolicy);
        v3.addComponent(organizationHCSPolicy);
        
        tabs.addTab(v1, "General Info", null);
        tabs.addTab(v2, "Base Organizational Policy", null);
        tabs.addTab(v3, "Rule Generation Testing", null);
        
        v.addComponent(tabs);
        
    }

    public void refreshOrganizationRules() {
        generalPolicy.refreshRules();
    }
}
