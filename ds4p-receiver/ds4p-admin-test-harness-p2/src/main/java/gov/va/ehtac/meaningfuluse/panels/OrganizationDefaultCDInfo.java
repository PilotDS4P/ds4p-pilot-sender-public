/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.panels;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.cas.providers.OrganizationPolicyProvider;
import gov.va.ds4p.policy.reference.Addr;
import gov.va.ds4p.policy.reference.AssignedAuthor;
import gov.va.ds4p.policy.reference.AssignedAuthoringDevice;
import gov.va.ds4p.policy.reference.AssignedPerson;
import gov.va.ds4p.policy.reference.DefaultCustodianInfo;
import gov.va.ds4p.policy.reference.HumanReadibleText;
import gov.va.ds4p.policy.reference.ManufacturingModelName;
import gov.va.ds4p.policy.reference.Name;
import gov.va.ds4p.policy.reference.OrganizationConsentPolicyInfo;
import gov.va.ds4p.policy.reference.OrganizationPolicy;
import gov.va.ds4p.policy.reference.SoftwareName;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class OrganizationDefaultCDInfo extends Panel {
    private TextField nameFLD = new TextField("Org Name");
    private TextField orgIDFLD = new TextField("ID");
    
    private TextField streetAddrFLD = new TextField("Street Address");
    private TextField cityFLD = new TextField("City");
    private TextField stateFLD = new TextField("State");
    private TextField zipFLD =  new TextField("ZipCode");
    private TextField countyFLD = new TextField("County");
    private TextField countryFLD = new TextField("Country");
    private TextField telcomFLD = new TextField("Phone");
    
    private TextField modelNameFLD = new TextField("Model Name");
    private TextField modelCodeFLD = new TextField("Code");
    private TextField softwareNameFLD = new TextField("Application Name");
    private TextField softwareCodeFLD = new TextField("Code");
    
    private TextArea defaultCDText = new TextArea();
    private TextField imageURLFLD = new TextField("CD HR Image Location");
    
    private CheckBox patientAuthor = new CheckBox("Patient Is Author");
    
    private Button saveBTN = new Button("Save");
    
    private gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy orgPolicy = null;
    private gov.va.ds4p.policy.reference.OrganizationConsentPolicyInfo defaultOrgValues = new gov.va.ds4p.policy.reference.OrganizationConsentPolicyInfo();
    
    private OrganizationPolicyProvider provider = new OrganizationPolicyProvider();
    
    public OrganizationDefaultCDInfo() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        this.setStyleName(Runo.PANEL_LIGHT);
        v.setSpacing(true);
        v.setHeight("500px");
        v.setWidth("100%");
        
        //name stuff
        HorizontalLayout h1 = new HorizontalLayout();
        h1.setWidth("100%");
        nameFLD.setWidth("50%");
        orgIDFLD.setWidth("25%");
        patientAuthor.setWidth("25%");
        nameFLD.setEnabled(false);
        orgIDFLD.setEnabled(false);
        h1.addComponent(nameFLD);
        h1.addComponent(orgIDFLD);
        h1.addComponent(patientAuthor);
        
        
        v.addComponent(h1);
        streetAddrFLD.setWidth("100%");
        v.addComponent(streetAddrFLD);
        
        HorizontalLayout h2 = new HorizontalLayout();
        h2.setWidth("100%");
        cityFLD.setWidth("40%");
        stateFLD.setWidth("40%");
        zipFLD.setWidth("20%");
        h2.addComponent(cityFLD);
        h2.addComponent(stateFLD);
        h2.addComponent(zipFLD);
        
        v.addComponent(h2);
        
        HorizontalLayout h5 = new HorizontalLayout();
        h5.setWidth("100%");
        countyFLD.setWidth("40%");
        countryFLD.setWidth("40%");
        telcomFLD.setWidth("20%");
        countryFLD.setValue("U.S.A");
        countryFLD.setEnabled(false);
        h5.addComponent(countyFLD);
        h5.addComponent(countryFLD);
        h5.addComponent(telcomFLD);
        
        v.addComponent(h5);
      
        HorizontalLayout h3 = new HorizontalLayout();
        h3.setWidth("100%");
        modelNameFLD.setWidth("70%");
        modelCodeFLD.setWidth("30%");
        h3.addComponent(modelNameFLD);
        h3.addComponent(modelCodeFLD);
        
        v.addComponent(h3);
        
        HorizontalLayout h4 = new HorizontalLayout();
        h4.setWidth("100%");
        softwareNameFLD.setWidth("70%");
        softwareCodeFLD.setWidth("30%");
        h4.addComponent(softwareNameFLD);
        h4.addComponent(softwareCodeFLD);
        
        v.addComponent(h4);
        
        defaultCDText.setWidth("100%");
        defaultCDText.setHeight("60px");
        defaultCDText.setCaption("Default Organization Constent Directive Text for PDF Form");
        imageURLFLD.setWidth("70%");
        v.addComponent(defaultCDText);
        v.addComponent(imageURLFLD);
        
        v.addComponent(saveBTN);
        
        
        saveBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                saveDefaultCDInfo();
            }
        });
        
               
        
        
    }
    
    public void refreshOrganizationValues() {
        resetFields();
        orgPolicy = AdminContext.getSessionAttributes().getPolicyDB();
        // set name
        nameFLD.setValue(orgPolicy.getOrganizationName());
        orgIDFLD.setValue(orgPolicy.getHomeCommunityId());
        
        
        
        gov.va.ds4p.policy.reference.OrganizationPolicy oP = AdminContext.getSessionAttributes().getOrganizationPolicy();
        defaultOrgValues = oP.getOrganizationConsentPolicyInfo();
        AdminContext.getSessionAttributes().setOrgPolicyConsentPolicyInfo(defaultOrgValues);
        try {
            if (defaultOrgValues != null) {
                //set address info
                Addr addr = defaultOrgValues.getAddr();
                streetAddrFLD.setValue(addr.getStreetAddressLine());
                cityFLD.setValue(addr.getCity());
                stateFLD.setValue(addr.getState());
                countyFLD.setValue(addr.getCounty());
                zipFLD.setValue(addr.getPostalCode());
                //set software information
                ManufacturingModelName mmn = defaultOrgValues.getAssignedAuthoringDevice().getManufacturingModelName();
                modelNameFLD.setValue(mmn.getDisplayName());
                modelCodeFLD.setValue(mmn.getCode());
                SoftwareName sft = defaultOrgValues.getAssignedAuthoringDevice().getSoftwareName();
                softwareNameFLD.setValue(sft.getDisplayName());
                softwareCodeFLD.setValue(sft.getCode());
                patientAuthor.setValue(defaultOrgValues.getAssignedAuthoringDevice().isPatientAuthor());
                //pdf info
                HumanReadibleText hrt = defaultOrgValues.getHumanReadibleText();
                defaultCDText.setValue(hrt.getDisplayText());
                imageURLFLD.setValue(hrt.getImageURL());
                
                telcomFLD.setValue(defaultOrgValues.getDefaultCustodianInfo().getTelcom());
                        
                
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveDefaultCDInfo() {
        OrganizationConsentPolicyInfo orgPInfo = new OrganizationConsentPolicyInfo();
        if (defaultOrgValues != null) {
            orgPInfo = defaultOrgValues;
        }
        
        Addr orgAddr = new Addr();
        AssignedAuthoringDevice device = new AssignedAuthoringDevice();
        HumanReadibleText pdfText = new HumanReadibleText();
        DefaultCustodianInfo custodian = new DefaultCustodianInfo();
        AssignedAuthor author = new AssignedAuthor();
        AssignedPerson person = new AssignedPerson();
        
        //set address info
        orgAddr.setStreetAddressLine((String)streetAddrFLD.getValue());
        orgAddr.setCity((String)cityFLD.getValue());
        orgAddr.setState((String)stateFLD.getValue());
        orgAddr.setCounty((String)countyFLD.getValue());
        orgAddr.setCountry((String)countryFLD.getValue());
        orgAddr.setPostalCode((String)zipFLD.getValue());
        
        ManufacturingModelName manu = new ManufacturingModelName();
        manu.setCode((String)modelCodeFLD.getValue());
        manu.setDisplayName((String)modelNameFLD.getValue());
        
        SoftwareName software = new SoftwareName();
        software.setCode((String)softwareCodeFLD.getValue());
        software.setDisplayName((String)softwareNameFLD.getValue());
        
        device.setManufacturingModelName(manu);
        device.setSoftwareName(software);
        device.setPatientAuthor(patientAuthor.booleanValue());
        
        pdfText.setDisplayText((String)defaultCDText.getValue());
        pdfText.setImageURL((String)imageURLFLD.getValue());
        
        custodian.setAddr(orgAddr);
        custodian.setOrganizationName((String)nameFLD.getValue());
        custodian.setTelcom((String)telcomFLD.getValue());
                
        //set new values
        orgPInfo.setAddr(orgAddr);
        orgPInfo.setAssignedAuthoringDevice(device);
        orgPInfo.setHumanReadibleText(pdfText);
        orgPInfo.setDefaultCustodianInfo(custodian);
        
        Name name = new Name();
        name.setFamily("Authority");
        name.setGiven("Default");
        person.setName(name);
        person.setTypeId("FEISystems");
        person.setClassCode("1^^^&amp;1.1&amp;ISO");
        
        author.setAddr(orgAddr);
        author.setId("1^^^&amp;1.1&amp;ISO");
        author.setRoot("1.3.5.35.1.4436.7");
        author.setAddr(orgAddr);
        author.setAssignedPerson(person);
        
        orgPInfo.setAssignedAuthor(author);

        orgPInfo.setDefaultPatientDemographics(AdminContext.getSessionAttributes().getPatientDemographics());
        
        AdminContext.getSessionAttributes().setOrgPolicyConsentPolicyInfo(orgPInfo);
        defaultOrgValues = orgPInfo;
        
        OrganizationPolicy p = AdminContext.getSessionAttributes().getOrganizationPolicy();
        p.setOrganizationConsentPolicyInfo(orgPInfo);
        AdminContext.getSessionAttributes().setOrganizationPolicy(p);
        
        String newXML = provider.createOrganizationPolicyXMLFromObject(p);
        
        gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy rval = AdminContext.getSessionAttributes().getPolicyDB();
        rval.setOrganizationalRules(newXML);
        AdminContext.getSessionAttributes().setPolicyDB(rval);
        
        try {
            DS4PClinicallyAdaptiveRulesInterface_Service service = new DS4PClinicallyAdaptiveRulesInterface_Service();
            DS4PClinicallyAdaptiveRulesInterface port = service.getDS4PClinicallyAdaptiveRulesInterfacePort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_CAS_ENDPOINT());
            port.setOrganizationalPolicy(rval);     
        }
        catch (Exception ex) {

            ex.printStackTrace();
        }  
    }   
    
    private void resetFields() {
        streetAddrFLD.setValue("");
        cityFLD.setValue("");
        stateFLD.setValue("");
        countyFLD.setValue("");
        zipFLD.setValue("");
        patientAuthor.setValue(false);
        telcomFLD.setValue("");
        modelNameFLD.setValue("");
        modelCodeFLD.setValue("");
        softwareNameFLD.setValue("");
        softwareCodeFLD.setValue("");
        defaultCDText.setValue("");
        imageURLFLD.setValue("");
    }
}
