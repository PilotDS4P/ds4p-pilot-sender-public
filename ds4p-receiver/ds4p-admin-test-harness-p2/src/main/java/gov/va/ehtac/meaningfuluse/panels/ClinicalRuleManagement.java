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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import gov.va.ehtac.meaningfuluse.cas.interfaces.MedList;
import gov.va.ehtac.meaningfuluse.cas.interfaces.ProblemList;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;

/**
 *
 * @author Duane DeCouteau
 */
public class ClinicalRuleManagement extends Panel {
    private HorizontalSplitPanel hp = new HorizontalSplitPanel();
    private Tree sectionTree = new Tree();
    
    
    private ProblemList plProvider = new ProblemList();
    private MedList medProvider = new MedList();
    
    private ProblemListRuleManagement plRules = null;
    private RxNormRuleManagement medRules = null;
    
    
    public ClinicalRuleManagement() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setHeight("600px");
        v.setWidth("100%");
        this.setStyleName(Runo.PANEL_LIGHT);
        
        
        buildNavTree();
        hp.setStyleName(Runo.SPLITPANEL_SMALL);
        hp.setSplitPosition(20, Sizeable.UNITS_PERCENTAGE);
        hp.setHeight("100%");
        hp.setLocked(false);
        hp.setFirstComponent(sectionTree);
        hp.setSecondComponent(getBlankPanel());
        
        
        v.addComponent(hp);
      
    }
    
    
    private void buildNavTree() {
        sectionTree.setCaption("Clinical Domain - Section");
        sectionTree.addItem("Problem List");
        sectionTree.addItem("Allergies and Adverse Reactions");
        sectionTree.addItem("Medications");
        sectionTree.addItem("Lab Results");
        
        sectionTree.addListener(new Tree.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String p = (String)sectionTree.getValue();
                if (p.equals("Problem List")) {
                    AdminContext.getSessionAttributes().setRuleSectionCode("11450-4");
                    AdminContext.getSessionAttributes().setRuleSectionDisplayName(p);
                    plProvider.getProblemListRules("11450-4");
                    if (plRules == null) plRules = new ProblemListRuleManagement();
                    hp.setSecondComponent(plRules);
                }
                else if (p.equals("Allergies and Adverse Reactions")) {
                    AdminContext.getSessionAttributes().setRuleSectionCode("48765-2");
                    AdminContext.getSessionAttributes().setRuleSectionDisplayName(p);
                    hp.setSecondComponent(new Panel());
                }
                else if (p.equals("Medications")) {
                    AdminContext.getSessionAttributes().setRuleSectionCode("10160-0");
                    AdminContext.getSessionAttributes().setRuleSectionDisplayName(p);
                    medProvider.getMedListRules("10160-0");
                    if (medRules == null) medRules = new RxNormRuleManagement();
                    hp.setSecondComponent(medRules);                   
                }
                else if (p.equals("Lab Results")) {
                    AdminContext.getSessionAttributes().setRuleSectionCode("30954-2");
                    AdminContext.getSessionAttributes().setRuleSectionDisplayName(p);
                    hp.setSecondComponent(new Panel());
                }
                else {
                    hp.setSecondComponent(getBlankPanel());
                }
            }
        });
        
        sectionTree.setImmediate(true);
        
    }
    
    private Panel getBlankPanel() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        return p;
    }
    

}
