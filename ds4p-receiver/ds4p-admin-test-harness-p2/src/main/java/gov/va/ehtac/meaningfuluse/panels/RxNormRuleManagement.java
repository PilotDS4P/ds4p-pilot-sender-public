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
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.cas.providers.MedicationsProvider;
import gov.va.ds4p.cas.providers.ProblemListProvider;
import gov.va.ds4p.policy.reference.*;
import gov.va.ehtac.ds4p.rxnorm.ConceptGroup;
import gov.va.ehtac.ds4p.rxnorm.ConceptProperties;
import gov.va.ehtac.ds4p.rxnorm.DrugGroup;
import gov.va.ehtac.ds4p.rxnorm.Rxnormdata;
import gov.va.ehtac.ds4p.rxnorm.json.RxNormClientProvider;
import gov.va.ehtac.ds4p.ws.ri.Clinicaltagrule;
import gov.va.ehtac.meaningfuluse.cas.interfaces.MedList;
import gov.va.ehtac.meaningfuluse.displayobjects.ClinicalRuleDisplayObject;
import gov.va.ehtac.meaningfuluse.displayobjects.UMLSDisplayObject;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.util.*;


/**
 *
 * @author Duane DeCouteau
 */
public class RxNormRuleManagement extends Panel {
    private RxNormClientProvider rxnormprovider = new RxNormClientProvider();
    private Button saveBTN = new Button("Save Policy Changes");
    private Button addNewBTN = new Button("Add New Sensitivity Rule");
    private Button editBTN = new Button("Edit Rule");
    private Button delBTN = new Button("Delete Selected Rule");
    private Button previewBTN = new Button("Preview Rule");
    private Tree sectionTree = new Tree();
    private Table ruleTable = new Table();
    private Table medTable = new Table();  
    
    private Window subwindow; //search window
    
    private Panel rulePanel = new Panel();
    
    private TextField searchstr = new TextField("Enter Search String");
    private Button searchBTN = new Button("Search");
    private Button selectBTN = new Button("Select");
    private ComboBox pouCBX = new ComboBox("Purpose Of Use");
    private ComboBox sensCBX = new ComboBox("Sensitivity");
    private ComboBox confCBX = new ComboBox("Implied Confidentiality");
    
    
    private String searchvalue = "";
    
    private boolean isOkToAdd = false;
    
    private MedList medProvider = new MedList();
    
    public RxNormRuleManagement() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setHeight("600px");
        v.setWidth("100%");
        this.setStyleName(Runo.PANEL_LIGHT);
        
        ruleTable.setStyleName(Runo.TABLE_SMALL);
        medTable.setStyleName(Runo.TABLE_SMALL);
        
        populateComboBoxes();
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
        ruleTable.setEditable(false);
        ruleTable.setWriteThrough(false);
        ruleTable.setContainerDataSource(populateRuleTable());

        ruleTable.setColumnReorderingAllowed(true);
        ruleTable.setColumnCollapsingAllowed(false);
        ruleTable.setVisibleColumns(new Object[] {"oPurposeOfUse","oDisplayName", "oCode", "oSensitivity", "oConfidentiality"});
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use", "Drug Name", "RxNORM CODE", "Sensitivity", "Implied Confidentiality"});
                
        addNewBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                    isOkToAdd = true;
                    Panel p2 = buildProblemListPanel();
                    subwindow = new Window("NLM RxNORM Search");
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
                saveRulesML();
            }
        });
        
        editBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object rowId = ruleTable.getValue();
                if (rowId != null) {
                    Panel p2 = editPanel();
                    subwindow = new Window("Edit Selected Medication");
                    subwindow.setWidth("1000px");
                    subwindow.setHeight("200px");
                    subwindow.setModal(true);
                    subwindow.addComponent(p2);
                    getApplication().getMainWindow().addWindow(subwindow);                    
                }
                else {
                    getWindow().showNotification("Warning", "Nothing is selected.", Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });

        HorizontalLayout hm = new HorizontalLayout();
        hm.setSpacing(true);
        hm.addComponent(saveBTN);
        hm.addComponent(addNewBTN);
        hm.addComponent(editBTN);
        hm.addComponent(delBTN);
        //hm.addComponent(previewBTN);
        
        v.addComponent(ruleTable);
        v.addComponent(hm);
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
        ruleTable.setColumnHeaders(new String[] {"Purpose Of Use", "Drug Name", "RxNORM CODE", "Sensitivity", "Implied Confidentiality"});
        ruleTable.requestRepaint();
    }
    
    private Panel buildProblemListPanel() {
        final Panel p = new Panel();

        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setWidth("100%");
        v.setHeight("100%");
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        medTable = new Table();
        medTable.setStyleName(Runo.TABLE_SMALL);
        searchBTN.setImmediate(true);
        selectBTN.setImmediate(true);
        pouCBX.setImmediate(true);
        sensCBX.setImmediate(true);
        confCBX.setImmediate(true);
        h.addComponent(searchstr);
        h.addComponent(searchBTN);
        h.setComponentAlignment(searchBTN, Alignment.BOTTOM_CENTER);
        h.addComponent(selectBTN);
        h.setComponentAlignment(selectBTN, Alignment.BOTTOM_CENTER);
        h.addComponent(pouCBX);
        h.setComponentAlignment(pouCBX, Alignment.BOTTOM_CENTER);
        h.addComponent(sensCBX);
        h.setComponentAlignment(sensCBX, Alignment.BOTTOM_CENTER);
        h.addComponent(confCBX);
        h.setComponentAlignment(confCBX, Alignment.BOTTOM_CENTER);
        
        searchBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchvalue = (String)searchstr.getValue();
                if (searchvalue != null) {
                    medTable.setContainerDataSource(populateSearchTable());
                    medTable.setVisibleColumns(new Object[] {"oCode","oDisplayName"});
                    medTable.setColumnHeaders(new String[] {"Code", "Display Name"});
                    medTable.requestRepaint();
                }
                else {
                    
                }
            }
        });
        
        selectBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Set rowIds = (Set)medTable.getValue();
                if (!rowIds.isEmpty()) {
                    IndexedContainer idx = (IndexedContainer)ruleTable.getContainerDataSource();                    
                    Iterator iter = rowIds.iterator();
                    while (iter.hasNext()) {
                        Object rowId = (Object)iter.next();
                        UMLSDisplayObject obj = (UMLSDisplayObject)medTable.getContainerProperty(rowId, "oPLObject").getValue();

                        Integer id = new Integer(ruleTable.getContainerDataSource().size() + 1);
                        //System.out.println("ORG DROP NAME "+name+" ORG ID "+id.intValue());

                        Item aItem = idx.addItem(id);
                        aItem.getItemProperty("oPurposeOfUse").setValue((String)pouCBX.getValue());
                        aItem.getItemProperty("oDisplayName").setValue(obj.getDisplayName());
                        aItem.getItemProperty("oCode").setValue(obj.getCode());
                        aItem.getItemProperty("oSensitivity").setValue((String)sensCBX.getValue());
                        aItem.getItemProperty("oConfidentiality").setValue((String)confCBX.getValue());        
                    }
                    refreshRules(idx);
                    getApplication().getMainWindow().removeWindow(subwindow);
                }
                
            }
        });
        
        medTable.setWidth("100%");
        medTable.setHeight("500px");
        medTable.setMultiSelect(true);
        //medTable.setMultiSelectMode(AbstractSelect.MultiSelectMode.SIMPLE);
        medTable.setSelectable(true);
        medTable.setImmediate(true); // react at once when something is selected
        medTable.setEditable(false);
        medTable.setWriteThrough(false);
        medTable.setContainerDataSource(populateSearchTable());

        medTable.setColumnReorderingAllowed(true);
        medTable.setColumnCollapsingAllowed(false);
        medTable.setVisibleColumns(new Object[] {"oCode","oDisplayName"});
        medTable.setColumnHeaders(new String[] {"Code", "Display Name"});
        
        v.addComponent(h);
        v.addComponent(medTable);
        return p;
    }    
    
    
    private IndexedContainer populateSearchTable() {
        IndexedContainer res = null;
        List<UMLSDisplayObject> medlist = new ArrayList();
        if (searchvalue != null && searchvalue.length() > 0) {
            try {
                searchvalue = searchvalue.trim();
                Rxnormdata rData = rxnormprovider.getDrugs(searchvalue);
                DrugGroup dg = rData.getDrugGroup();
                Iterator iter = dg.getConceptGroup().iterator();
                while (iter.hasNext()) {
                    ConceptGroup cg = (ConceptGroup)iter.next();
                    Iterator iter2 = cg.getConceptProperties().iterator();
                    while (iter2.hasNext()) {
                        ConceptProperties cp = (ConceptProperties)iter2.next();
                        UMLSDisplayObject obj = new UMLSDisplayObject();
                        obj.setCode(cp.getRxcui().getContent());
                        obj.setDisplayName(cp.getName().getContent());
                        medlist.add(obj);
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        res = createMedListContainer(medlist);
        return res;
    }
    
    private IndexedContainer createMedListContainer(Collection<UMLSDisplayObject> collection) {
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
 
    
    private void populateComboBoxes() {
        Iterator iter = AdminContext.getSessionAttributes().getPouList().iterator();
        pouCBX.addItem("Any");
        pouCBX.setItemCaption("Any", "Any");
        while (iter.hasNext()) {
            ActReason pou = (ActReason)iter.next();
            pouCBX.addItem(pou.getCode());
            pouCBX.setItemCaption(pou.getCode(), pou.getDisplayName());
        }
        Iterator iter2 = AdminContext.getSessionAttributes().getSensitivityList().iterator();
        while (iter2.hasNext()) {
            ActInformationSensitivityPolicy act = (ActInformationSensitivityPolicy)iter2.next();
            sensCBX.addItem(act.getCode());
            sensCBX.setItemCaption(act.getCode(), act.getDisplayName());
        }

        Iterator iter3 = AdminContext.getSessionAttributes().getConfidentialityList().iterator();
        while (iter3.hasNext()) {
            Confidentiality conf = (Confidentiality)iter3.next();
            confCBX.addItem(conf.getCode());
            confCBX.setItemCaption(conf.getCode(), conf.getDisplayName());
        }
    }
    
    private void saveRulesML() {
       MedicationListSensitivityRules ruleset = new MedicationListSensitivityRules();
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
            fact.setCodeSystemName("RxNorm");
            fact.setCodeSystem("2.16.840.1.113883.6.88");
            
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
       MedicationsProvider plxmlprovider = new MedicationsProvider();
       String mxml = plxmlprovider.createMedicationRulesXMLFromObject(ruleset);
       tRule.setObservationRules(mxml);
       AdminContext.getSessionAttributes().setRuleObject(tRule);
       Boolean res = medProvider.saveMedListRules();
    }
    
    private Panel editPanel() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        TextField nField = new TextField("Drug Name");
        TextField nId = new TextField("Code");
        nField.setWidth("300px");
        nId.setWidth("80px");
        //nField.setEnabled(false);
        //nId.setEnabled(false);
        
        Button editSaveBTN = new Button("Save");
        Button editCancelBTN = new Button("Cancel");
        
        final Object rowId = ruleTable.getValue();
//        IndexedContainer idx = (IndexedContainer)ruleTable.getContainerDataSource();
//        Item item = idx.getItem(rowId);
        String pou = (String)ruleTable.getContainerProperty(rowId, "oPurposeOfUse").getValue();
        String disp = (String)ruleTable.getContainerProperty(rowId, "oDisplayName").getValue();
        String code = (String)ruleTable.getContainerProperty(rowId, "oCode").getValue();
        String sens = (String)ruleTable.getContainerProperty(rowId, "oSensitivity").getValue();
        String conf = (String)ruleTable.getContainerProperty(rowId, "oConfidentiality").getValue();
        
        pouCBX.setValue(pou);
        nField.setValue(disp);
        nId.setValue(code);
        sensCBX.setValue(sens);
        confCBX.setValue(conf);

        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.setWidth("100%");
        h.addComponent(nField);
        h.addComponent(nId);
        h.addComponent(pouCBX);
        h.addComponent(sensCBX);
        h.addComponent(confCBX);
        
        HorizontalLayout h2 = new HorizontalLayout();
        h2.setSpacing(true);
        h2.addComponent(editSaveBTN);
        h2.addComponent(editCancelBTN);

        editSaveBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                ruleTable.getContainerProperty(rowId, "oPurposeOfUse").setValue((String)pouCBX.getValue());
                ruleTable.getContainerProperty(rowId, "oSensitivity").setValue((String)sensCBX.getValue());
                ruleTable.getContainerProperty(rowId, "oConfidentiality").setValue((String)confCBX.getValue());
                saveRulesML();
                getApplication().getMainWindow().removeWindow(subwindow);
            }
        });
        
        editCancelBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getApplication().getMainWindow().removeWindow(subwindow);
            }
        });
        
        
        v.addComponent(h);
        v.addComponent(h2);
        
        return p;
    }
}
