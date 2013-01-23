/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.panels;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.cas.providers.OrganizationPolicyProvider;
import gov.va.ds4p.policy.reference.Addr;
import gov.va.ds4p.policy.reference.Name;
import gov.va.ds4p.policy.reference.OrganizationConsentPolicyInfo;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.ri.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class OrganizationDefaultPatientCDInfo extends Panel {
    private TextField firstNameFLD = new TextField("First Name");
    private TextField lastNameFLD = new TextField("Last Name");
    private TextField birthDateFLD = new TextField("Birth Date");
    private TextField genderFLD = new TextField("Gender");
    
    private TextField streetAddrFLD = new TextField("Street Address");
    private TextField cityFLD = new TextField("City");
    private TextField stateFLD = new TextField("State");
    private TextField zipFLD =  new TextField("ZipCode");
    private TextField countyFLD = new TextField("County");
    private TextField countryFLD = new TextField("Country");
    private TextField telcomFLD = new TextField("Phone");
    
    private TextField modelNameFLD = new TextField("Model Name");
    
    private TextArea defaultCDText = new TextArea();
    private TextField imageURLFLD = new TextField("CD HR Image Location");
    
    private Button saveBTN = new Button("Save");
    
    private gov.va.ehtac.ds4p.ws.ri.OrganizationalPolicy orgPolicy = null;
    private gov.va.ds4p.policy.reference.OrganizationConsentPolicyInfo defaultOrgValues = null;
    private gov.va.ds4p.policy.reference.DefaultPatientDemographics defaultPatientInfo;
    
    private OrganizationPolicyProvider provider = new OrganizationPolicyProvider();
    
    public OrganizationDefaultPatientCDInfo() {
        VerticalLayout v = (VerticalLayout)this.getContent();
        this.setStyleName(Runo.PANEL_LIGHT);
        v.setSpacing(true);
        v.setHeight("500px");
        v.setWidth("100%");
        
        //name stuff
        HorizontalLayout h1 = new HorizontalLayout();
        h1.setWidth("100%");
        lastNameFLD.setWidth("70%");
        firstNameFLD.setWidth("30%");
        h1.addComponent(lastNameFLD);
        h1.addComponent(firstNameFLD);
        
        v.addComponent(h1);
        
        HorizontalLayout hPatient = new HorizontalLayout();
        hPatient.setWidth("100%");
        birthDateFLD.setWidth("40%");
        genderFLD.setWidth("40%");
        hPatient.addComponent(birthDateFLD);
        hPatient.addComponent(genderFLD);
        
        v.addComponent(hPatient);
        
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
      
        
        saveBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                saveDefaultCDInfo();
            }
        });
        
        v.addComponent(saveBTN);
               
        
        
    }
    
    public void refreshOrganizationValues() {
        resetFields();
        gov.va.ds4p.policy.reference.OrganizationPolicy oP = AdminContext.getSessionAttributes().getOrganizationPolicy();
        defaultOrgValues = oP.getOrganizationConsentPolicyInfo();
        try {
            defaultPatientInfo = defaultOrgValues.getDefaultPatientDemographics();
            if (defaultPatientInfo != null) {
                String lastName = defaultPatientInfo.getName().getFamily();
                String firstName = defaultPatientInfo.getName().getGiven();
                XMLGregorianCalendar bd = defaultPatientInfo.getDefaultPatientBirthDate();
                String gender = defaultPatientInfo.getPatientGender();
                String street = defaultPatientInfo.getAddr().getStreetAddressLine();
                String city = defaultPatientInfo.getAddr().getCity();
                String state = defaultPatientInfo.getAddr().getState();
                String county = defaultPatientInfo.getAddr().getCounty();
                String zip = defaultPatientInfo.getAddr().getPostalCode();
                String telcom = defaultPatientInfo.getPatientTelcom();

                if (lastName != null) { 
                    lastNameFLD.setValue(lastName); 
                }
                if (firstName != null) { 
                    firstNameFLD.setValue(firstName);
                }
                if (bd != null) { 
                    birthDateFLD.setValue(getDateStringFromXMLGregorianCalendar(bd));
                }
                if (gender != null) { 
                    genderFLD.setValue(gender);
                }
                if (street != null) { 
                    streetAddrFLD.setValue(street);
                }
                if (city != null) { 
                    cityFLD.setValue(city);
                }
                if (state != null) { 
                    stateFLD.setValue(state);
                }
                if (zip != null) { 
                    zipFLD.setValue(zip);
                }
                if (county != null) {
                    countyFLD.setValue(county);
                }
                if (telcom != null) {
                    telcomFLD.setValue(telcom);
                }
            }
            else {
                defaultPatientInfo = new gov.va.ds4p.policy.reference.DefaultPatientDemographics();
            }
            AdminContext.getSessionAttributes().setPatientDemographics(defaultPatientInfo);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveDefaultCDInfo() {
        //set address info
        Addr orgAddr = new Addr();
        orgAddr.setStreetAddressLine((String)streetAddrFLD.getValue());
        orgAddr.setCity((String)cityFLD.getValue());
        orgAddr.setState((String)stateFLD.getValue());
        orgAddr.setCounty((String)countyFLD.getValue());
        orgAddr.setCountry((String)countryFLD.getValue());
        orgAddr.setPostalCode((String)zipFLD.getValue());

        defaultPatientInfo.setAddr(orgAddr);
        
        defaultPatientInfo.setDefaultPatientBirthDate(getXMLGregorianCalendarFromDateString((String)birthDateFLD.getValue()));
        
        Name name = new Name();
        name.setFamily((String)lastNameFLD.getValue());
        name.setGiven((String)firstNameFLD.getValue());
        
        defaultPatientInfo.setName(name);
        defaultPatientInfo.setPatientGender((String)genderFLD.getValue());
        defaultPatientInfo.setPatientTelcom((String)telcomFLD.getValue());
        
        gov.va.ds4p.policy.reference.OrganizationPolicy p = AdminContext.getSessionAttributes().getOrganizationPolicy();
        OrganizationConsentPolicyInfo cd = AdminContext.getSessionAttributes().getOrgPolicyConsentPolicyInfo();
        cd.setDefaultPatientDemographics(defaultPatientInfo);
        p.setOrganizationConsentPolicyInfo(cd);
        AdminContext.getSessionAttributes().setOrganizationPolicy(p);
        AdminContext.getSessionAttributes().setPatientDemographics(defaultPatientInfo);
        
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
    
    private String getDateStringFromXMLGregorianCalendar(XMLGregorianCalendar xres) {
        Date dt = null;
        String res = "";
        try {
            Calendar xcal = Calendar.getInstance();
            xcal.set(xres.getYear(), xres.getMonth() - 1, xres.getDay(), xres.getHour(), xres.getMinute(), xres.getSecond());
            dt = xcal.getTime();
            SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMdd");
            try {
                res = sdt.format(dt);
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        catch (Exception ex) {
            System.err.println("LocalPatientSearch "+ex.getMessage());
        }
        return res;
    }
    
    private XMLGregorianCalendar getXMLGregorianCalendarFromDateString(String dt) {
        XMLGregorianCalendar xgc = null;
        try {
            Date date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(dt);
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(date);
            xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return xgc;
    }
    
    private void resetFields() {
        streetAddrFLD.setValue("");
        cityFLD.setValue("");
        stateFLD.setValue("");
        countyFLD.setValue("");
        zipFLD.setValue("");
        telcomFLD.setValue("");
        birthDateFLD.setValue("");
        lastNameFLD.setValue("");
        firstNameFLD.setValue("");
        genderFLD.setValue("");
    }
    
}
