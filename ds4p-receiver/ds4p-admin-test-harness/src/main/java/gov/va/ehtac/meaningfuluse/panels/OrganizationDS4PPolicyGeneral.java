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
import gov.va.ds4p.cas.providers.OrganizationPolicyProvider;
import gov.va.ds4p.cas.providers.ProblemListProvider;
import gov.va.ds4p.policy.reference.*;
import gov.va.ehtac.ds4p.ws.ri.Clinicaltagrule;
import gov.va.ehtac.meaningfuluse.cas.interfaces.OrganizationPolicyData;
import gov.va.ehtac.meaningfuluse.displayobjects.ClinicalRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.displayobjects.OrganizationGeneralRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Duane DeCouteau
 */
public class OrganizationDS4PPolicyGeneral extends Panel{
    private Button saveBTN = new Button("Save General Policy Changes");
    private Button addNewBTN = new Button("Add General Policy Rule");
    private Button delBTN = new Button("Delete Selected Rule");
    private Button previewBTN = new Button("Preview Rule(s)");
    private Table ruleTable = new Table();
    
    private OrganizationPolicyData oDataProvider = new OrganizationPolicyData();
    
    public OrganizationDS4PPolicyGeneral() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        this.setStyleName(Runo.PANEL_LIGHT);
        v.setHeight("500px");
        v.setWidth("100%");
        v.setSpacing(true);
        ruleTable.setStyleName(Runo.TABLE_SMALL);
        //get data default for demo
        oDataProvider.getOrganizationPolicyRules("2.16.840.1.113883.3.467");  //default organization is samhsa
                
        ruleTable.setWidth("100%");
        ruleTable.setHeight("350px");
        ruleTable.setMultiSelect(false);
        ruleTable.setSelectable(true);
        ruleTable.setImmediate(true); // react at once when something is selected
        ruleTable.setEditable(true);
        ruleTable.setWriteThrough(true);
        ruleTable.setContainerDataSource(populateRuleTable());

        ruleTable.setColumnReorderingAllowed(true);
        ruleTable.setColumnCollapsingAllowed(false);
        ruleTable.setVisibleColumns(new Object[] {"oPurposeOfUse","oSensitivity","oPatientSensitivity","oPatientRequestedAction", "oUSPrivacyLaw", "oObligationPolicyEntry", "oObligationPolicySection", "oRefrainPolicy"});
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use","Entry Sensitivity", "Patient Constraint", "Patient Requested Action", "US Privacy Law", "Obligation Policy Entry","Obligation Policy Document", "Refrain Policy"});
        
        ruleTable.setTableFieldFactory(new TableFieldFactory() {

            @Override
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                ComboBox box = new ComboBox();
                
                if (propertyId.equals("oPurposeOfUse")) {
                    Iterator iter = AdminContext.getSessionAttributes().getPouList().iterator();
                    while (iter.hasNext()) {
                        ActReason pou = (ActReason)iter.next();
                        box.addItem(pou.getCode());
                        box.setItemCaption(pou.getCode(), pou.getDisplayName());
                    }
                }
                else if (propertyId.equals("oSensitivity")) {
                    Iterator iter = AdminContext.getSessionAttributes().getSensitivityList().iterator();
                    while (iter.hasNext()) {
                        ActInformationSensitivityPolicy act = (ActInformationSensitivityPolicy)iter.next();
                        box.addItem(act.getCode());
                        box.setItemCaption(act.getCode(), act.getDisplayName());
                    }
                }                
                else if (propertyId.equals("oPatientSensitivity")) {
                    Iterator iter = AdminContext.getSessionAttributes().getSensitivityList().iterator();
                    box.addItem("None");
                    box.setItemCaption("None", "None");
                    while (iter.hasNext()) {
                        ActInformationSensitivityPolicy act = (ActInformationSensitivityPolicy)iter.next();
                        box.addItem(act.getCode());
                        box.setItemCaption(act.getCode(), act.getDisplayName());
                    }
                }
                else if (propertyId.equals("oPatientRequestedAction")) {
                    //Iterator iter = AdminContext.getSessionAttributes().getObligationList().iterator();
                    box.addItem("None");
                    box.addItem("REDACT");
                    box.addItem("MASK");                    
                }
                else if (propertyId.equals("oUSPrivacyLaw")) {
                    Iterator iter = AdminContext.getSessionAttributes().getUsLawList().iterator();
                    while (iter.hasNext()) {
                        ActUSPrivacyLaw act = (ActUSPrivacyLaw)iter.next();
                        box.addItem(act.getCode());
                        box.setItemCaption(act.getCode(), act.getDisplayName());
                    }
                }
                else if (propertyId.equals("oObligationPolicyEntry")) {
                    Iterator iter = AdminContext.getSessionAttributes().getObligationList().iterator();
                    box.addItem("None");
                    box.setItemCaption("None", "None");                    
                    while (iter.hasNext()) {
                        ObligationPolicy conf = (ObligationPolicy)iter.next();
                        box.addItem(conf.getCode());
                        box.setItemCaption(conf.getCode(), conf.getDisplayName());
                    }
                }
                else if (propertyId.equals("oObligationPolicySection")) {
                    Iterator iter = AdminContext.getSessionAttributes().getObligationList().iterator();
                    box.addItem("None");
                    box.setItemCaption("None", "None");                    
                    while (iter.hasNext()) {
                        ObligationPolicy conf = (ObligationPolicy)iter.next();
                        box.addItem(conf.getCode());
                        box.setItemCaption(conf.getCode(), conf.getDisplayName());
                    }
                }                
                else if (propertyId.equals("oRefrainPolicy")) {
                    Iterator iter = AdminContext.getSessionAttributes().getRefrainPolicyList().iterator();
                    box.addItem("None");
                    box.setItemCaption("None", "None");                    
                    while (iter.hasNext()) {
                        RefrainPolicy conf = (RefrainPolicy)iter.next();
                        box.addItem(conf.getCode());
                        box.setItemCaption(conf.getCode(), conf.getDisplayName());
                    }                    
                }
                else {
                    return new TextField();
                }
                return box;
            }
        });
        
        delBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object rowId = ruleTable.getValue(); 
                if (rowId != null) {
                    ruleTable.removeItem(rowId);
                    ruleTable.requestRepaint();
                }

            }
        });

        addNewBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                    Integer id = new Integer(ruleTable.getContainerDataSource().size() + 1);
                    //System.out.println("ORG DROP NAME "+name+" ORG ID "+id.intValue());

                    IndexedContainer idx = (IndexedContainer)ruleTable.getContainerDataSource();
                    Item aItem = idx.addItem(id);
                    aItem.getItemProperty("oPurposeOfUse").setValue("");
                    aItem.getItemProperty("oSensitivity").setValue("");
                    aItem.getItemProperty("oPatientSensitivity").setValue("");
                    aItem.getItemProperty("oPatientRequestedAction").setValue("");
                    aItem.getItemProperty("oUSPrivacyLaw").setValue("");
                    aItem.getItemProperty("oObligationPolicyEntry").setValue("");
                    aItem.getItemProperty("oObligationPolicySection").setValue("");
                    aItem.getItemProperty("oRefrainPolicy").setValue("");
                    refreshRules(idx);         
            }
        });
        
        saveBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                saveOrgRules();
            }
        });
        
        
        saveBTN.setImmediate(true);
        addNewBTN.setImmediate(true);
        delBTN.setImmediate(true);
        previewBTN.setImmediate(true);
        
        HorizontalLayout hm = new HorizontalLayout();
        hm.setSpacing(true);
        hm.addComponent(saveBTN);
        hm.addComponent(addNewBTN);
        hm.addComponent(delBTN);
        //hm.addComponent(previewBTN);
        
        v.addComponent(ruleTable);
        v.addComponent(hm);
                
    }
    
    private IndexedContainer populateRuleTable() {
        IndexedContainer res = null;
        res = createRuleContainer(AdminContext.getSessionAttributes().getoGRules());
        return res;
    }

    private IndexedContainer createRuleContainer(Collection<OrganizationGeneralRuleDisplayObject> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("oPurposeOfUse", String.class, null);
        container.addContainerProperty("oSensitivity", String.class, null);        
        container.addContainerProperty("oPatientSensitivity", String.class, null);
        container.addContainerProperty("oPatientRequestedAction", String.class, null);        
        container.addContainerProperty("oUSPrivacyLaw", String.class, null);
        container.addContainerProperty("oObligationPolicyEntry", String.class, null);
        container.addContainerProperty("oObligationPolicySection", String.class, null);        
        container.addContainerProperty("oRefrainPolicy", String.class, null);
        
        for (OrganizationGeneralRuleDisplayObject p : collection) {
            //System.out.println("ITEM ID IS: "+p.getRuleId());
            Item item = container.addItem(p.getRuleId());
            item.getItemProperty("oPurposeOfUse").setValue(p.getPurposeOfUse());
            item.getItemProperty("oSensitivity").setValue(p.getEntrySensitivity());            
            item.getItemProperty("oPatientSensitivity").setValue(p.getPatientConstraint()); 
            item.getItemProperty("oPatientRequestedAction").setValue(p.getPatientRequestAction());             
            item.getItemProperty("oUSPrivacyLaw").setValue(p.getUsPrivacyLaw());
            item.getItemProperty("oObligationPolicyEntry").setValue(p.getObligationPolicyEntry());
            item.getItemProperty("oObligationPolicySection").setValue(p.getObligationPolicySection());
            item.getItemProperty("oRefrainPolicy").setValue(p.getRefrainPolicy());
        }
        
        return container;
    }

    public void refreshRules(IndexedContainer idx) {
        ruleTable.setContainerDataSource(idx);
        ruleTable.setVisibleColumns(new Object[] {"oPurposeOfUse","oSensitivity","oPatientSensitivity","oPatientRequestedAction", "oUSPrivacyLaw", "oObligationPolicyEntry", "oObligationPolicySection", "oRefrainPolicy"});
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use","Entry Sensitivity", "Patient Constraint", "Patient Requested Action", "US Privacy Law", "Obligation Policy Entry","Obligation Policy Document", "Refrain Policy"});
        ruleTable.requestRepaint();
    }
    
    public void refreshRules() {
        oDataProvider.getOrganizationPolicyRules(AdminContext.getSessionAttributes().getSelectedOrganization());
        IndexedContainer idx = populateRuleTable();
        ruleTable.setContainerDataSource(idx);
        ruleTable.setVisibleColumns(new Object[] {"oPurposeOfUse","oSensitivity","oPatientSensitivity","oPatientRequestedAction", "oUSPrivacyLaw", "oObligationPolicyEntry", "oObligationPolicySection", "oRefrainPolicy"});
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use","Entry Sensitivity", "Patient Constraint", "Patient Requested Action", "US Privacy Law", "Obligation Policy Entry","Obligation Policy Document", "Refrain Policy"});
        ruleTable.requestRepaint();        
    }
    
    private void saveOrgRules() {
       gov.va.ds4p.policy.reference.OrganizationPolicy ruleset = new gov.va.ds4p.policy.reference.OrganizationPolicy();
       Container c = ruleTable.getContainerDataSource();
       Iterator iter = c.getItemIds().iterator();
       ruleset.setHomeCommunityId(AdminContext.getSessionAttributes().getOrganizationOid());
       ruleset.setOrgName(AdminContext.getSessionAttributes().getOrganizationName());
       ruleset.setUsPrivacyLaw(AdminContext.getSessionAttributes().getOrganizationUSLaw());
       while (iter.hasNext()) {
            OrganizationTaggingRules rule = new OrganizationTaggingRules();
            int iid = (Integer)iter.next();
            Item item = c.getItem(iid);
            
            //purpose of use
            ActReason reason = new ActReason();
            reason.setCode((String)item.getItemProperty("oPurposeOfUse").getValue());
            reason.setCodeSystemName("urn:hl7-org:v3");
            reason.setCodeSystem("2.16.840.1.113883.5.8");
            
            rule.setActReason(reason);
            
            //sensitivity
            ActInformationSensitivityPolicy sensitivity = new ActInformationSensitivityPolicy();
            sensitivity.setCode((String)item.getItemProperty("oSensitivity").getValue());
            sensitivity.setCodeSystemName("urn:hl7-org:v3");
            sensitivity.setCodeSystem("2.16.840.1.113883.1.11.20429");
            
            rule.setActInformationSensitivityPolicy(sensitivity);
            
            //set patient sensitivity
            ActInformationSensitivityPolicy psensitivity = new ActInformationSensitivityPolicy();
            psensitivity.setCode((String)item.getItemProperty("oPatientSensitivity").getValue());
            psensitivity.setCodeSystemName("urn:hl7-org:v3");
            psensitivity.setCodeSystem("2.16.840.1.113883.1.11.20429");
            
            PatientSensitivityConstraint pconstraint = new PatientSensitivityConstraint();
            pconstraint.setActInformationSensitivityPolicy(psensitivity);
            rule.setPatientSensitivityConstraint(pconstraint);
            
            ObligationPolicy pObligation = new ObligationPolicy();
            pObligation.setCode((String)item.getItemProperty("oPatientRequestedAction").getValue());
            pObligation.setCodeSystemName("urn:hl7-org:v3");
            pObligation.setCodeSystem("2.16.840.1.113883.1.11.20445");
            
            PatientRequestedAction pAction = new PatientRequestedAction();
            pAction.setObligationPolicy(pObligation);
            rule.setPatientRequestedAction(pAction);
            
            ActUSPrivacyLaw law = new ActUSPrivacyLaw();
            law.setCode((String)item.getItemProperty("oUSPrivacyLaw").getValue());
            law.setCodeSystemName("urn:hl7-org:v3");
            law.setCodeSystem("2.16.840.1.113883.5.1138");
            
            rule.setActUSPrivacyLaw(law);
            
            ObligationPolicy oEntry = new ObligationPolicy();
            oEntry.setCode((String)item.getItemProperty("oObligationPolicyEntry").getValue());
            oEntry.setCodeSystemName("urn:hl7-org:v3");
            oEntry.setCodeSystem("2.16.840.1.113883.1.11.20445");
            
            OrgObligationPolicyEntry oOEntry = new OrgObligationPolicyEntry();
            oOEntry.setObligationPolicy(oEntry);
            rule.setOrgObligationPolicyEntry(oOEntry);
            
            ObligationPolicy oDoc = new ObligationPolicy();
            oDoc.setCode((String)item.getItemProperty("oObligationPolicySection").getValue());
            oDoc.setCodeSystemName("urn:hl7-org:v3");
            oDoc.setCodeSystem("2.16.840.1.113883.1.11.20445");
            
            OrgObligationPolicyDocument oODoc = new OrgObligationPolicyDocument();
            oODoc.setObligationPolicy(oDoc);
            rule.setOrgObligationPolicyDocument(oODoc);
            
            RefrainPolicy refrain = new RefrainPolicy();
            refrain.setCode((String)item.getItemProperty("oRefrainPolicy").getValue());
            refrain.setCodeSystemName("urn:hl7-org:v3");
            refrain.setCodeSystem("2.16.840.1.113883.1.11.20446");
            
            rule.setRefrainPolicy(refrain);
            
            ruleset.getOrganizationTaggingRules().add(rule);
       }
       gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy tRule = AdminContext.getSessionAttributes().getPolicyDB();
       OrganizationPolicyProvider plxmlprovider = new OrganizationPolicyProvider();
       String mxml = plxmlprovider.createOrganizationPolicyXMLFromObject(ruleset);
       tRule.setOrganizationalRules(mxml);
       tRule.setApplicableUsLaw(AdminContext.getSessionAttributes().getOrganizationUSLaw());
       tRule.setHomeCommunityId(AdminContext.getSessionAttributes().getOrganizationOid());
       tRule.setOrganizationName(AdminContext.getSessionAttributes().getOrganizationName());
       AdminContext.getSessionAttributes().setPolicyDB(tRule);
       Boolean res = oDataProvider.saveOrganizationPolicyRules();       
    }
}
