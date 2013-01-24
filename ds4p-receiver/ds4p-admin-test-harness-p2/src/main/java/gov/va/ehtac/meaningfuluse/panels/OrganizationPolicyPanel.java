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
import com.vaadin.ui.themes.Runo;
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
    private OrganizationDefaultCDInfo defaultOrgInfo;
    private OrganizationDefaultPatientCDInfo defaultPatientInfo;
    private DS4PeXchange eXchange;
    
    public OrganizationPolicyPanel() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);
        this.setStyleName(Runo.PANEL_LIGHT);
        
        tabs.setWidth("100%");
        tabs.setHeight("650px");
        VerticalLayout v1 = new VerticalLayout();
        VerticalLayout v2 = new VerticalLayout();
        VerticalLayout v3 = new VerticalLayout();
        VerticalLayout v4 = new VerticalLayout();
        VerticalLayout v5 = new VerticalLayout();
        VerticalLayout v6 = new VerticalLayout();
        //VerticalLayout v7 = new VerticalLayout();

        
        generalOrgInfo = new OrganizationGeneralInfo(this);
        generalPolicy = new OrganizationDS4PPolicyGeneral();
        organizationHCSPolicy = new RuleGenerationTesting();
        defaultOrgInfo = new OrganizationDefaultCDInfo();
        defaultPatientInfo = new OrganizationDefaultPatientCDInfo();
        eXchange = new DS4PeXchange();
        
        v1.addComponent(generalOrgInfo);
        v2.addComponent(generalPolicy);
        v3.addComponent(organizationHCSPolicy);
        v4.addComponent(defaultOrgInfo);
        v5.addComponent(defaultPatientInfo);
        v6.addComponent(eXchange);
        
        tabs.addTab(v1, "Organizations", null);
        tabs.addTab(v2, "Base Organizational Policy", null);
        tabs.addTab(v3, "Rule Generation Testing", null);
        tabs.addTab(v4, "Organization Default Info", null);
        tabs.addTab(v5, "Default Test Patient Info", null);
        tabs.addTab(v6, "Exchange (Pull) Testing", null);
        
        v.addComponent(tabs);
        
    }

    public void refreshOrganizationRules() {
        generalPolicy.refreshRules();
        defaultOrgInfo.refreshOrganizationValues();
        defaultPatientInfo.refreshOrganizationValues();
    }
}
