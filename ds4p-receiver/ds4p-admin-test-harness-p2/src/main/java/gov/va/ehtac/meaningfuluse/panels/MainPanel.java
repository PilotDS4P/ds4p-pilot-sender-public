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

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Runo;
import gov.va.ds4p.hcs.ActHealthInformationPurposeOfUseReason;
import gov.va.ds4p.hcs.ActInformationSensitivityPrivacyPolicyType;
import gov.va.ds4p.hcs.ActObligationPolicy;
import gov.va.ds4p.hcs.ActPolicyType;
import gov.va.ds4p.hcs.ActPrivacyPolicyType;
import gov.va.ds4p.hcs.ActRefrainPolicy;
import gov.va.ds4p.hcs.ActSecurityPolicyType;
import gov.va.ds4p.hcs.ClinicalDocumentPolicyControl;
import gov.va.ds4p.hcs.InnerPolicyControl;
import gov.va.ds4p.hcs.OuterPolicyControl;
import gov.va.ds4p.hcs.SecuredMedicalDocument;
import gov.va.ehtac.meaningfuluse.encryption.Crypto;
import gov.va.ehtac.meaningfuluse.providers.ConfigurationProvider;
import gov.va.ehtac.meaningfuluse.providers.VocabularyProvider;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Duane DeCouteau
 */
public class MainPanel extends Panel{
    private TabSheet tabs = new TabSheet();
    private Panel sourcePanel = new Panel();
    private Panel xdmProcessing = new Panel();
    private Panel securedPanel = new Panel();
    private Panel policyPanel = new Panel();
    private Panel resultPanel = new Panel();
    private Panel orgTaggingPanel = new Panel();
    private Panel contextHandlerTest = new Panel();
    private Panel kaironMgr = new Panel();
    private Panel usLawPanel = new Panel();
    
    private Panel clinicalRulePanel = new Panel();
    
    //source documents
    private String baseFname = "../../../../../../resources/documents/";
    private String c32source = "C32_Entries_MinimalErrors.xml";
    private String progressnote = "progressnote.txt";
    
    private Button processDocBTN = new Button("Process Document");
    private ComboBox docTypeCBX = new ComboBox("Document Type");
    private ComboBox obligationCBX = new ComboBox("Obligation Required");
    private ComboBox refrainCBX = new ComboBox("Refrain Policy");
    private ComboBox confidentialityCBX = new ComboBox("Confidentiality");
    private ComboBox sensitivityCBX = new ComboBox("Sensitivity");
    private ComboBox pouCBX = new ComboBox("Purpose of Use");
    
    //encryption panel
    private TextField patientKeyFLD = new TextField("Patient Policy Key");
    private Button encryptBTN = new Button("Encrypt Payload");
    
    
    private String[] docList = new String[]{"Summary of Episode - C32", "Non-Descript Document"};
    private String[] obligationList = new String[]{"AOD","DEID","CPLYCC","ENCRYPT","REDACT"};
    private String[] refrainList = new String[]{"NOAUTH","NOCOLLECT","NODSCLCD","NOINTEGRATE","NOLIST","NOMOU"};
    private String[] confList = new String[]{"U (unrestricted)","L (low)","M (moderate)","N (normal)","R (restricted)","VR (very restricted)"};
    private String[] sensList = new String[]{"ETH","GDIS","HIV","PSY","SDV","SEX","STD","TBOO"};
    private String[] pouList = new String[]{"TREATMENT","EMERGENCY","OPERATIONS","MARKETING","PULICHEALTH","RESEARCH","COVERAGE","PATIENTREQUEST"};
    
    private TextArea sourceTXT = new TextArea();

    private TextArea processTXT = new TextArea();
    private TextArea policyTXT = new TextArea();
    private TextArea resultTXT = new TextArea();
    
    private String loadedClinicalFile = "";
    
    //tagged classified document
    private SecuredMedicalDocument sDoc = new SecuredMedicalDocument();
    private String sDocString = "";
    
    private String logo = "../../../../../resources/images/logo.png";
    private Embedded eLogo;
    
    private String headerLBLText = "<b>Data Segmentation for Privacy Pilot Demonstrations & Reference Model (Phase II)</b>";
    private String footerLBLText = "HIPAAT, Jericho Systems, Mitre, SAMHSA (FEiSystems), Dept. of Veterans Affairs (Edmond Scientific)";
    private Label headerLBL;
    private Label footerLBL;
    
    //private Application app;
    
    
    public MainPanel(Application app) {
        //this.app = app;
        //load configs
        ConfigurationProvider confProvider = new ConfigurationProvider();
        //load vocabulary
        VocabularyProvider voc = new VocabularyProvider();
        VerticalLayout v = (VerticalLayout)this.getContent();
        this.setStyleName(Runo.PANEL_LIGHT);
        v.setSpacing(true);
        
        //set header
        eLogo = new Embedded("",new ClassResource(logo, app));
        headerLBL = new Label(headerLBLText);
        headerLBL.setContentMode(Label.CONTENT_XHTML);
        
        v.addComponent(eLogo);
        v.addComponent(headerLBL);
        
        setThingsToImmediate();
        createDropDowns();
//        createSourcePanel();
//        createSecuredPanel();
//        createPolicyRequestPanel();
        createResultsPanel();
        clinicalRulePanel = new ClinicalRuleManagement();
        contextHandlerTest = new ContextHandlerTesting();
        orgTaggingPanel = new OrganizationPolicyPanel();
        resultPanel = new AuditLogs();
        kaironMgr = getKaironMgr();
        xdmProcessing = new XDMProcessing();
        //tabs
        tabs.setWidth("100%");
        tabs.setHeight("700px");
        VerticalLayout v1 = new VerticalLayout();
        VerticalLayout v2 = new VerticalLayout();
        VerticalLayout v3 = new VerticalLayout();
        VerticalLayout v4 = new VerticalLayout();
        VerticalLayout v5 = new VerticalLayout();
        VerticalLayout v6 = new VerticalLayout();
        VerticalLayout v7 = new VerticalLayout();
        VerticalLayout v8 = new VerticalLayout();
        VerticalLayout v9 = new VerticalLayout();
        
        v1.addComponent(clinicalRulePanel);
        v2.addComponent(orgTaggingPanel);
        v3.addComponent(contextHandlerTest);
        v4.addComponent(xdmProcessing);
        //v5.addComponent(securedPanel);
        //v6.addComponent(policyPanel);
        v7.addComponent(resultPanel);
        v8.addComponent(kaironMgr);
        v9.addComponent(usLawPanel);
        
        tabs.addTab(v8, "Kairon Consent Manager", null);
        tabs.addTab(v1, "Clinical Annotation Rules", null);
        //tabs.addTab(v9, "Privacy Law", null);
        tabs.addTab(v2, "Organization Annotation Rules/Consent Directive Defaults", null);
        tabs.addTab(v3, "ACS ContextHandler Testing", null);
        tabs.addTab(v7, "Access Control Decisions", null);        
        tabs.addTab(v4, "NwHIN Direct Document Processing", null);
//        tabs.addTab(v5, "Secured Medical Document", null);
//        tabs.addTab(v6, "Policy Request", null);
        
        //tabs.getTab(3).setEnabled(false);
//        tabs.getTab(6).setEnabled(false);
//        tabs.getTab(7).setEnabled(false);
//        tabs.getTab(8).setEnabled(false);
        
        v.addComponent(tabs);   
        //footer 
        footerLBL = new Label(footerLBLText);
        footerLBL.setContentMode(Label.CONTENT_XHTML);
        HorizontalLayout hftr = new HorizontalLayout();
        hftr.setWidth("100%");
        hftr.setSpacing(true);
        hftr.addComponent(new Label("Health Information Protection And Associated Technologies (HIPAAT)"));
        hftr.addComponent(new Label("Jericho Systems"));
        hftr.addComponent(new Label("Mitre"));
        hftr.addComponent(new Label("SAMHSA (FEiSystems)"));
        hftr.addComponent(new Label("Dept. of Veteran Affairs (Edmond Scientific)"));
        //hftr.setComponentAlignment(footerLBL, Alignment.MIDDLE_CENTER);
        v.addComponent(hftr);
    }
    
    private void createSourcePanel() {
        sourcePanel = new Panel();
        sourcePanel.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)sourcePanel.getContent();
        v.setWidth("100%");

        Panel outer = new Panel();
        outer.setCaption("Outer Policy Control");
        VerticalLayout oV = (VerticalLayout)outer.getContent();
        HorizontalLayout oH = new HorizontalLayout();
        oH.setSpacing(true);
        oH.addComponent(obligationCBX);        
        oH.addComponent(refrainCBX);
        oV.addComponent(oH);
        
        Panel inner = new Panel();
        inner.setCaption("Inner Policy Control");
        VerticalLayout iV = (VerticalLayout)inner.getContent();
        HorizontalLayout iH = new HorizontalLayout();
        iH.setSpacing(true);
        iH.addComponent(confidentialityCBX);
        iH.addComponent(pouCBX);
        iH.addComponent(sensitivityCBX);
        iV.addComponent(iH);

        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.addComponent(docTypeCBX);
        h.addComponent(outer);
        h.addComponent(inner);
        
        patientKeyFLD.setWidth("300px");
        HorizontalLayout h2 = new HorizontalLayout();
        h2.setSpacing(true);
        h2.addComponent(patientKeyFLD);
        h2.addComponent(processDocBTN);        
        h2.setComponentAlignment(processDocBTN, Alignment.BOTTOM_CENTER);
        
        sourceTXT.setWidth("100%");
        sourceTXT.setHeight("420px");
        
        docTypeCBX.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Property p = event.getProperty();
                String doctype = (String)p.getValue();
                if ("Summary of Episode - C32".equals(doctype)) {
                    sourceTXT.setValue(getFileAsString(c32source));
                    sourceTXT.requestRepaint();
                }
                else if ("Non-Descript Document".equals(doctype)) {
                    sourceTXT.setValue(getFileAsString(progressnote));
                    sourceTXT.requestRepaint();
                }
                else {
                    // do nothing
                }
            }
        });
        
        processDocBTN.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                createSecuredMedicalDocument();
            }
        });
        
        
        
        v.addComponent(h);
        v.addComponent(h2);
        v.addComponent(sourceTXT);        
        
    }

    
    private void createSecuredPanel() {
        securedPanel = new Panel();
        securedPanel.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)securedPanel.getContent();
        v.setWidth("100%");
        processTXT.setWidth("100%");
        processTXT.setHeight("580px");
        v.addComponent(processTXT);
    }
    private void createPolicyRequestPanel() {
        policyPanel = new Panel();
        policyPanel.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)policyPanel.getContent();
        v.setWidth("100%");
    }
    private void createResultsPanel() {
        resultPanel = new Panel();
        resultPanel.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)resultPanel.getContent();
        v.setWidth("100%");        
    }
    
    private void createDropDowns() {
        //doc type list
        for (int i = 0; i < docList.length; i++) {
            docTypeCBX.addItem(docList[i]);
        }
        //obligation type
        for (int i = 0; i < obligationList.length; i++) {
            obligationCBX.addItem(obligationList[i]);
        }
        //obligationCBX.setMultiSelect(true);
        // refrain type
        for (int i = 0; i < refrainList.length; i++) {
            refrainCBX.addItem(refrainList[i]);
        }
        //refrainCBX.setMultiSelect(true);
        // conf codes
        for (int i = 0; i < confList.length; i++) {
            confidentialityCBX.addItem(confList[i]);
        }
        //confidentialityCBX.setMultiSelect(true);
        //sensitivity codes
        for (int i = 0; i < sensList.length; i++) {
            sensitivityCBX.addItem(sensList[i]);
        }
        //sensitivityCBX.setMultiSelect(true);
        //pou list
        for (int i = 0; i < pouList.length; i++) {
            pouCBX.addItem(pouList[i]);
        }
        //pouCBX.setMultiSelect(true);
    }

    private String getFileAsString(String selectedDoc) {
        String res = null;
        try {
            String mFile = baseFname + selectedDoc;
            BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(mFile)));
            ByteArrayOutputStream aout = new ByteArrayOutputStream();
            BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(aout));
            int result = r.read();
            while(result != -1) {
                byte b = (byte)result;
                aout.write(b);
                result = r.read();
            }
            res = aout.toString();
            loadedClinicalFile = res;
            aout.close();
            r.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
 
    private void setThingsToImmediate() {
        docTypeCBX.setImmediate(true);
        sourceTXT.setImmediate(true);
        
    }
    
    private void createSecuredMedicalDocument() {
        sDoc = new SecuredMedicalDocument();
        sDocString = "";
        //update outer policy wrapper
        sDoc.setDateDocumentGenerated(convertDateToXMLGregorianCalendar(new Date()));
        sDoc.setDocumentId(generateUniqueDocumentID());
        //create Security Policy
        OuterPolicyControl outer = new OuterPolicyControl();
        ActSecurityPolicyType securitypolicytype = new ActSecurityPolicyType();
        ActObligationPolicy obligationpolicytype = new ActObligationPolicy();
        ActRefrainPolicy refrainpolicytype = new ActRefrainPolicy();
        //set obligations
        
        obligationpolicytype.getHL7ConceptCode().add((String)obligationCBX.getValue());
        refrainpolicytype.getHL7ConceptCode().add((String)refrainCBX.getValue());
        securitypolicytype.setActObligationPolicy(obligationpolicytype);
        securitypolicytype.setActRefrainPolicy(refrainpolicytype);
        outer.setActSecurityPolicyType(securitypolicytype);
        sDoc.setOuterPolicyControl(outer);
        
       //update inner policy wrapper
        InnerPolicyControl inner = new InnerPolicyControl();
        inner.setConfidentiality((String)confidentialityCBX.getValue());
        //handledocumentpolicytype
        ClinicalDocumentPolicyControl documentpolicytype = new ClinicalDocumentPolicyControl();
        ActHealthInformationPurposeOfUseReason purposeofuse = new ActHealthInformationPurposeOfUseReason();
        ActInformationSensitivityPrivacyPolicyType sensitivity = new ActInformationSensitivityPrivacyPolicyType();
        purposeofuse.getHL7ConceptCode().add((String)pouCBX.getValue());
        sensitivity.setHL7ConceptCode((String)sensitivityCBX.getValue());
        documentpolicytype.setActHealthInformationPurposeOfUseReason(purposeofuse);
        documentpolicytype.setActInformationSensitivityPrivacyPolicyType(sensitivity);
        
        inner.setClinicalDocumentPolicyControl(documentpolicytype);
        
        //deliver the patient consent abac policy
        ActPolicyType actpolicy = new ActPolicyType();
        ActPrivacyPolicyType pT = new ActPrivacyPolicyType();
        pT.setPolicyType("XACML");
        pT.setPolicyAttributeValueSet("urn:uuid:8f543ea9-3919-4101-9d88-66b1534bbe38");
        actpolicy.getActPrivacyPolicyType().add(pT);
        
        inner.getClinicalDocumentPolicyControl().setActPolicyType(actpolicy);
        
        inner.getClinicalDocumentPolicyControl().setAESEncryptedDocumentPayload(encryptMedicalDocument());
        //insert unsecured document
        sDoc.getOuterPolicyControl().setInnerPolicyControl(inner);
        
        //create XML String
        try {
            JAXBContext context = JAXBContext.newInstance(SecuredMedicalDocument.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(sDoc, sw);

            sDocString = sw.toString();
            processTXT.setValue(sDocString);
            processTXT.requestRepaint();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private String encryptMedicalDocument() {
        String key = (String)patientKeyFLD.getValue();
        Crypto encrypter = new Crypto(key);
        String eLoadedClinicalDocument = encrypter.encrypt(loadedClinicalFile);
        return eLoadedClinicalDocument;
//        SecuredMedicalDocument encryptedSDoc = sDoc;
//        encryptedSDoc.getOuterPolicyControl().getInnerPolicyControl().getClinicalDocumentPolicyControl().setAESEncryptedDocumentPayload(eLoadedClinicalDocument);
//        
//        //create Encrypted Doc String
//        try {
//            JAXBContext context = JAXBContext.newInstance(SecuredMedicalDocument.class);
//            Marshaller marshaller = context.createMarshaller();
//            StringWriter sw = new StringWriter();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            marshaller.marshal(encryptedSDoc, sw);
//
//            String nString = sw.toString();
//            processTXT.setValue(nString);
//            processTXT.requestRepaint();
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
    }
    
    private XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date dt) {
        XMLGregorianCalendar xcal = null;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dt);
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        }
        catch (Exception ex) {
            
            ex.printStackTrace();
        }
        return xcal;
    }
    
    private String generateUniqueDocumentID() {
        String res = "";
        UUID id = UUID.randomUUID();
        res = id.toString();
        res = "urn:uuid:" + res;
        return res;
    }
    
    private Panel getKaironMgr() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        v.setHeight("100%");
        v.setWidth("100%");
        Embedded e = new Embedded("", new ExternalResource(
                "http://direct.rhex.us:8081/requestManager/"));
        //e.setAlternateText("Vaadin web site");
        e.setType(Embedded.TYPE_BROWSER);
        e.setWidth("100%");
        e.setHeight("550px");
        v.addComponent(e);        
        
        return p;
    }
}
