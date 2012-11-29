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
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.cas.providers.ProblemListProvider;
import gov.va.ds4p.policy.reference.*;
import gov.va.ehtac.ds4p.ws.ri.Clinicaltagrule;
import gov.va.ehtac.meaningfuluse.cas.interfaces.ProblemList;
import gov.va.ehtac.meaningfuluse.displayobjects.ClinicalRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.displayobjects.UMLSDisplayObject;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import gov.va.ehtac.meaningfuluse.uts.UtsClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class ProblemListRuleManagement extends Panel {
    private Button saveBTN = new Button("Save Policy Changes");
    private Button addNewBTN = new Button("Add New Sensitivity Rule");
    private Button delBTN = new Button("Delete Selected Rule");
    private Button previewBTN = new Button("Preview Rule");
    private Tree sectionTree = new Tree();
    private Table ruleTable = new Table();
    private Table plTable = new Table();  // problem list ref data
    
    private Window subwindow; //search window
    
    private Panel rulePanel = new Panel();
    
    private TextField searchstr = new TextField("Disorders Containing Word(s)");
    private Button searchBTN = new Button("Search");
    private Button selectBTN = new Button("Select");
    
    //private String endpoint = "http://vmcentral:8080/DS4PACSServices/DS4PReferenceData?wsdl";
    
    private String searchvalue = "";
    
    private boolean isOkToAdd = false;
    
    private ProblemList plProvider = new ProblemList();
    
    private UtsClient umlsClient = new UtsClient();
    
    public ProblemListRuleManagement() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setHeight("600px");
        v.setWidth("100%");
        this.setStyleName(Runo.PANEL_LIGHT);
        
        ruleTable.setStyleName(Runo.TABLE_SMALL);
        plTable.setStyleName(Runo.TABLE_SMALL);
        
        rulePanel = buildRuleTable();
        
        
        v.addComponent(rulePanel);
        
    }
    
    private Panel buildRuleTable() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        ruleTable.setWidth("100%");
        ruleTable.setHeight("400px");
        ruleTable.setMultiSelect(false);
        ruleTable.setSelectable(true);
        ruleTable.setImmediate(true); // react at once when something is selected
        ruleTable.setEditable(true);
        ruleTable.setWriteThrough(true);
        ruleTable.setContainerDataSource(populateRuleTable());

        ruleTable.setColumnReorderingAllowed(true);
        ruleTable.setColumnCollapsingAllowed(false);
        ruleTable.setVisibleColumns(new Object[] {"oPurposeOfUse","oDisplayName", "oCode", "oSensitivity", "oConfidentiality"});
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use", "Problem", "SNOMED-CT CODE", "Sensitivity", "Implied Confidentiality"});
        
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
                else if (propertyId.equals("oConfidentiality")) {
                    Iterator iter = AdminContext.getSessionAttributes().getConfidentialityList().iterator();
                    while (iter.hasNext()) {
                        Confidentiality conf = (Confidentiality)iter.next();
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
        
        addNewBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                    isOkToAdd = true;
                    Panel p2 = buildProblemListPanel();
                    subwindow = new Window("UMLS SNOMED CT Search");
                    subwindow.setWidth("900px");
                    subwindow.setHeight("650px");
                    subwindow.setModal(true);
                    subwindow.addComponent(p2);
                    getApplication().getMainWindow().addWindow(subwindow);
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
        
        saveBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveRulesPL();
            }
        });

        HorizontalLayout hm = new HorizontalLayout();
        hm.setSpacing(true);
        hm.addComponent(saveBTN);
        hm.addComponent(addNewBTN);
        hm.addComponent(delBTN);
        //hm.addComponent(previewBTN);
        
        v.addComponent(ruleTable);
        v.addComponent(hm);
        return p;
    }
    
    private Panel buildProblemListPanel() {
        final Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setWidth("100%");
        v.setHeight("100%");
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        plTable = new Table();
        plTable.setStyleName(Runo.TABLE_SMALL);
        searchBTN.setImmediate(true);
        selectBTN.setImmediate(true);
        h.addComponent(searchstr);
        h.addComponent(searchBTN);
        h.setComponentAlignment(searchBTN, Alignment.BOTTOM_CENTER);
        h.addComponent(selectBTN);
        h.setComponentAlignment(selectBTN, Alignment.BOTTOM_CENTER);
        searchBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchvalue = (String)searchstr.getValue();
                if (searchvalue != null) {
                    plTable.setContainerDataSource(populateSearchTable());
                    plTable.setVisibleColumns(new Object[] {"oCode","oDisplayName"});
                    plTable.setColumnHeaders(new String[] {"Code", "Display Name"});
                    plTable.requestRepaint();
                }
                else {
                    
                }
            }
        });
        
        selectBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object rowId = plTable.getValue();
                if (rowId != null && isOkToAdd ) {
                    UMLSDisplayObject obj = (UMLSDisplayObject)plTable.getContainerProperty(rowId, "oPLObject").getValue();
                    
                    Integer id = new Integer(ruleTable.getContainerDataSource().size() + 1);
                    //System.out.println("ORG DROP NAME "+name+" ORG ID "+id.intValue());

                    IndexedContainer idx = (IndexedContainer)ruleTable.getContainerDataSource();
                    Item aItem = idx.addItem(id);
                    aItem.getItemProperty("oPurposeOfUse").setValue("");
                    aItem.getItemProperty("oDisplayName").setValue(obj.getDisplayName());
                    aItem.getItemProperty("oCode").setValue(obj.getCode());
                    aItem.getItemProperty("oSensitivity").setValue("");
                    aItem.getItemProperty("oConfidentiality").setValue("");
                    refreshRules(idx);         
                    isOkToAdd = false;
                    getApplication().getMainWindow().removeWindow(subwindow);
                }                
            }
        });
        
        plTable.setWidth("100%");
        plTable.setHeight("500px");
        plTable.setMultiSelect(false);
        plTable.setSelectable(true);
        plTable.setImmediate(true); // react at once when something is selected
        plTable.setEditable(false);
        plTable.setWriteThrough(false);
        plTable.setContainerDataSource(populateSearchTable());

        plTable.setColumnReorderingAllowed(true);
        plTable.setColumnCollapsingAllowed(false);
        plTable.setVisibleColumns(new Object[] {"oCode","oDisplayName"});
        plTable.setColumnHeaders(new String[] {"Code", "Display Name"});
        
        v.addComponent(h);
        v.addComponent(plTable);
        return p;
    }    
    
    
    
    private IndexedContainer populateRuleTable() {
        IndexedContainer res = null;
        res = createRuleContainer(AdminContext.getSessionAttributes().getcRules());
        return res;
    }
    
    private IndexedContainer createRuleContainer(Collection<ClinicalRuleDisplayObject> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("oPurposeOfUse", String.class, null);
        container.addContainerProperty("oDisplayName", String.class, null);
        container.addContainerProperty("oCode", String.class, null);
        container.addContainerProperty("oSensitivity", String.class, null);
        container.addContainerProperty("oConfidentiality", String.class, null);
        
        for (ClinicalRuleDisplayObject p : collection) {
            //System.out.println("ITEM ID IS: "+p.getRuleId());
            Item item = container.addItem(p.getRuleId());
            item.getItemProperty("oPurposeOfUse").setValue(p.getPurposeOfUse());            
            item.getItemProperty("oDisplayName").setValue(p.getDisplayName());
            item.getItemProperty("oCode").setValue(p.getCode());
            item.getItemProperty("oSensitivity").setValue(p.getSensitivityCode());
            item.getItemProperty("oConfidentiality").setValue(p.getImpliedConfidentiality());
        }
        
        return container;
    }
    
    public void refreshRules(IndexedContainer idx) {
        ruleTable.setContainerDataSource(idx);
        ruleTable.setVisibleColumns(new Object[] {"oPurposeOfUse","oDisplayName", "oCode", "oSensitivity", "oConfidentiality"});
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use", "Problem", "SNOMED-CT CODE", "Sensitivity", "Implied Confidentiality"});
        ruleTable.requestRepaint();
    }
    
    
    private IndexedContainer populateSearchTable() {
        IndexedContainer res = null;
        List<UMLSDisplayObject> problemList = new ArrayList();
        if (searchvalue != null && searchvalue.length() > 0) {
            try {
                searchvalue = searchvalue.trim();
    //            DS4PReferenceData_Service service = new DS4PReferenceData_Service();
    //            DS4PReferenceData port = service.getDS4PReferenceDataPort();
    //            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_REFDATA_ENDPOINT());
    //            problemList = port.getProblemListThatContainsString(searchvalue); 
                problemList = umlsClient.getProblemListResults(searchvalue);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        res = createProblemListContainer(problemList);
        return res;
    }
    
    private IndexedContainer createProblemListContainer(Collection<UMLSDisplayObject> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("oCode", String.class, null);
        container.addContainerProperty("oDisplayName", String.class, null);
        container.addContainerProperty("oPLObject", UMLSDisplayObject.class, null);
        
        int i = 1;
        for (UMLSDisplayObject p : collection) {
            //System.out.println("ITEM ID IS: "+p.getIdsnomedproblemlist());
            Item item = container.addItem(new Integer(i));
            item.getItemProperty("oCode").setValue(p.getCode());
            item.getItemProperty("oDisplayName").setValue(p.getDisplayName());
            item.getItemProperty("oPLObject").setValue(p); 
            i++;
        }
        return container;
    }

    private void saveRulesPL() {
       ProblemListSensitivityRules ruleset = new ProblemListSensitivityRules();
       Container c = ruleTable.getContainerDataSource();
       Iterator iter = c.getItemIds().iterator();
       while (iter.hasNext()) {
            ClinicalTaggingRule rule = new ClinicalTaggingRule();
            int iid = (Integer)iter.next();
            Item item = c.getItem(iid);
            //loinc info
            rule.setCode(AdminContext.getSessionAttributes().getRuleSectionCode());
            rule.setCodeSystem(AdminContext.getSessionAttributes().getRuleSectionCodeSystems());
            rule.setCodeSystemName(AdminContext.getSessionAttributes().getRuleSectionCodeSystemName());
            rule.setDisplayName(AdminContext.getSessionAttributes().getRuleSectionDisplayName());
            
            //Clinical fact infor
            ClinicalFact fact = new ClinicalFact();
            fact.setCode((String)item.getItemProperty("oCode").getValue());
            fact.setDisplayName((String)item.getItemProperty("oDisplayName").getValue());
            fact.setCodeSystemName("SNOMED CT");
            fact.setCodeSystem("2.16.840.1.113883.6.96");
            
            rule.setClinicalFact(fact);
            
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
            
            //implied entry level confidentiality to be rolled up to section
            ImpliedConfidentiality iConf = new ImpliedConfidentiality();
            Confidentiality conf = new Confidentiality();
            conf.setCode((String)item.getItemProperty("oConfidentiality").getValue());
            conf.setCodeSystemName("urn:hl7-org:v3");
            conf.setCodeSystem("2.16.840.1.113883.5.25");
            
            iConf.setConfidentiality(conf);
            rule.setImpliedConfidentiality(iConf);
                       
            ruleset.getClinicalTaggingRule().add(rule);
       }
       Clinicaltagrule tRule = AdminContext.getSessionAttributes().getRuleObject();
       ProblemListProvider plxmlprovider = new ProblemListProvider();
       String mxml = plxmlprovider.createProblemListSensitivityRulesXMLFromObject(ruleset);
       tRule.setObservationRules(mxml);
       AdminContext.getSessionAttributes().setRuleObject(tRule);
       Boolean res = plProvider.saveProblemListRules();
    }    
}
