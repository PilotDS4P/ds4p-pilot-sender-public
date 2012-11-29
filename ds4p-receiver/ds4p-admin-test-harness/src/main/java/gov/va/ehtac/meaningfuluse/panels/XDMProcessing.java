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

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamVariable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;
import gov.va.ehtac.ds4p.ws.dp.NwHINDirectDocumentProcessing;
import gov.va.ehtac.ds4p.ws.dp.NwHINDirectDocumentProcessing_Service;
import gov.va.ehtac.ds4p.ws.dp.Directprocessing;
import gov.va.ehtac.ds4p.ws.recvr.NwHINDirectAuthorizationServices;
import gov.va.ehtac.ds4p.ws.recvr.NwHINDirectAuthorizationServices_Service;
import gov.va.ehtac.meaningfuluse.filter.AdminContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import org.oasis.names.tc.ebxml.v3.ClassificationType;
import org.oasis.names.tc.ebxml.v3.ExtrinsicObjectType;
import org.oasis.names.tc.ebxml.v3.IdentifiableType;
import org.oasis.names.tc.ebxml.v3.RegistryObjectListType;
import org.oasis.names.tc.ebxml.v3.SlotType1;
import org.oasis.names.tc.ebxml.v3.SubmitObjectsRequest;
import org.oasis.names.tc.ebxml.v3.ValueListType;

/**
 *
 * @author Duane DeCouteau
 */
public class XDMProcessing extends Panel {
    Label deleteBTN = new Label();
    private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB    
    private ProgressIndicator progress = new ProgressIndicator();
    private Label label1 = new Label("Drag Direct XDM package");
    private Label label2 = new Label("onto above panel to");
    private Label label3 = new Label("begin processing.");
    
    private Table table = new Table();
    
    //buttons
    private Button refresh = new Button("Refresh Inbox");
    private Button viewMetaData = new Button("View XDM MetaData");
    private Button viewEncryptedDoc = new Button("View Encrypted Document");
    private Button viewDocument = new Button("View Document");
    private Button viewTransformed = new Button("View Transformed Document");
    private Button testDisclosure = new Button("Test Disclosure");
    private Button providerAttributes  = new Button("View Permissions");
    
    
    Window subwindow;
    
    private String qTransform = "../../../../../../resources/transforms/CDA.xsl";
    
    
    private HorizontalSplitPanel hp = new HorizontalSplitPanel();
    
    private String disclosure = "This information has been disclosed to you from records protected by Federal confidentiality rules (42 CFR part 2). The Federal rules prohibit you from making any further disclosure of this information unless further disclosure is expressly permitted by the written consent of the person to whom it pertains or as otherwise permitted by 42 CFR part 2. A general authorization for the release of medical or other information is NOT sufficient for this purpose. The Federal rules restrict any use of the information to criminally investigate or prosecute any alcohol or drug abuse patient.  [52 FR 21809, June 9, 1987; 52 FR 41997, Nov. 2, 1987]";
    
    //metadata
    private List<String> requiredSensitivityPrivileges = new ArrayList();
    private String intendedRecipient = "";
    private String uniquePatientId = "";
    private String intendedPurposeOfUse = "";
    private String refrainPolicy = "";
    
    public XDMProcessing() {
        this.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)this.getContent();
        v.setSpacing(true);
        v.setHeight("600px");
        v.setWidth("100%");
        hp.setStyleName(Runo.SPLITPANEL_SMALL);
        hp.setSplitPosition(20, Sizeable.UNITS_PERCENTAGE);
        hp.setHeight("500px");
        hp.setLocked(false);
        hp.setFirstComponent(getDropArea());
        hp.setSecondComponent(getResultsTable());

        v.addComponent(hp);
        v.addComponent(getButtonPanel());
    }
    
    private Panel getDropArea() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setHeight("100%");
        v.addComponent(getDropPanel());
        v.addComponent(label1);
        v.addComponent(label2);
        v.addComponent(label3);
        
        return p;
    }
    
    private Panel getDropPanel() {
        Panel p = new Panel();
        VerticalLayout v = (VerticalLayout) p.getContent();
        v.setSpacing(true);
        //v.setHeight("100%");
        p.setStyleName(Runo.PANEL_LIGHT);
        
        Panel wrapperPanel = new Panel();
        VerticalLayout vDPanel = (VerticalLayout)wrapperPanel.getContent();
        deleteBTN.setIcon(new ThemeResource("../runo/icons/64/folder-add.png"));
        deleteBTN.setWidth("64px");
        deleteBTN.setHeight("64px");

        vDPanel.addComponent(deleteBTN);
        final DragAndDropWrapper wrapper = new DragAndDropWrapper(wrapperPanel);
        wrapper.setWidth("100px");
        wrapper.setHeight("100px");
        
        wrapperPanel.setStyleName(Runo.PANEL_LIGHT);        
        
        wrapper.setDropHandler(new DropHandler() {

                @Override
                public void drop(DragAndDropEvent event) {
                    // expecting this to be an html5 drag
                    WrapperTransferable tr = (WrapperTransferable) event.getTransferable();
                    Html5File[] files = tr.getFiles();
                    if (files != null) {
                        for (final Html5File html5File : files) {
                            final String fileName = html5File.getFileName();

                            if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
                                getWindow()
                                        .showNotification(
                                                "File rejected. Max 2MB files are accepted by Sampler",
                                                Notification.TYPE_WARNING_MESSAGE);
                            } else {

                                final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                                StreamVariable streamVariable = new StreamVariable() {

                                    public OutputStream getOutputStream() {
                                        return bas;
                                    }

                                    public boolean listenProgress() {
                                        return false;
                                    }

                                    public void onProgress(StreamingProgressEvent event) {
                                    }

                                    public void streamingStarted(
                                            StreamingStartEvent event) {
                                    }

                                    public void streamingFinished(
                                            StreamingEndEvent event) {
                                            progress.setVisible(false);
                                            Boolean res = updateProviderProcessingInbox(bas.toByteArray());
                                            if (res.booleanValue()) {
                                                getWindow().showNotification("Processing Complete: "+fileName +" "+html5File.getType()+" "+html5File.getFileSize(), Notification.TYPE_HUMANIZED_MESSAGE);                                        
                                            }
                                            else {
                                                getWindow().showNotification("Processing Error: You Do Not Have Necessary Permissions to Receive and Process Files.", Notification.TYPE_TRAY_NOTIFICATION);                                                                                        
                                            }
                                    }

                                    public void streamingFailed(
                                            StreamingErrorEvent event) {
                                            progress.setVisible(false);
                                    }

                                    public boolean isInterrupted() {
                                        return false;
                                    }
                                };
                                html5File.setStreamVariable(streamVariable);
                                progress.setVisible(true);
                            }
                        }

                    } else {
                        String text = tr.getText();
                        if (text != null) {                            
                            getWindow().showNotification(text, Notification.TYPE_HUMANIZED_MESSAGE);                        
                        }
                    }                    
                }

                @Override
                public AcceptCriterion getAcceptCriterion() {
                    return AcceptAll.get();
                }
         });
        
        v.addComponent(wrapper);
        
      return p;
    }
    
    private Boolean updateProviderProcessingInbox(byte[] payload) {
        Boolean res = Boolean.TRUE;
        try {
//            DS4PContextHandler_Service service = new DS4PContextHandler_Service();
//            DS4PContextHandler port = service.getDS4PContextHandlerPort();
            NwHINDirectDocumentProcessing_Service service = new NwHINDirectDocumentProcessing_Service();
            NwHINDirectDocumentProcessing port = service.getNwHINDirectDocumentProcessingPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_XDM_ENDPOINT());
            String providerId = AdminContext.getProviderAttributes().getUserId();
            String recipientId = AdminContext.getProviderAttributes().getProviderId();
            String purposeOfUse = AdminContext.getProviderAttributes().getPurposeOfUse();
            String role = AdminContext.getProviderAttributes().getRole();
            String organizationName = AdminContext.getProviderAttributes().getOrganization();
            String organizationId = AdminContext.getProviderAttributes().getProviderHomeCommunityId();
            List<String> servicePermissions = AdminContext.getProviderAttributes().getAllowedNwHINActions();
            List<String> sensitivityPrivileges = AdminContext.getProviderAttributes().getAllowedSensitivityActions();
            List<String> requiredPermission = new ArrayList();
            List<String> requiredSensitivityPrivileges = new ArrayList();
            String intendedRecipient = "";
            String uniquePatientId = "";
            String intendedPurposeOfUse = "";
            
            res = port.unpackEnforceXDMPackage(payload, providerId, recipientId, organizationName, organizationId, role, purposeOfUse, servicePermissions, sensitivityPrivileges, intendedPurposeOfUse, intendedRecipient, requiredPermission, requiredSensitivityPrivileges, "NwHINDirectReceive", uniquePatientId);
            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
    
    private Panel getResultsTable() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        table.setStyleName(Runo.TABLE_SMALL);
        table.setCaption("Processed XDM Packages");
        table.setWidth("100%");
        table.setHeight("400px");
        table.setMultiSelect(false);
        table.setSelectable(true);
        table.setImmediate(true); // react at once when something is selected
        table.setEditable(false);
        table.setWriteThrough(false);
        table.setContainerDataSource(populateTable());

        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(false);
        table.setVisibleColumns(new Object[] {"oDate","oPatientName", "oSender", "oConfidentiality", "oRefrainPolicy", "oPurposeOfUse"});
        table.setColumnHeaders(new String[] {"Date Processed", "Patient Name", "From", "Confidentiality", "Primary Refrain Policy", "Intended Purpose Of Use"});

        v.addComponent(table);
        return p;
    }
    
    private IndexedContainer populateTable() {
        IndexedContainer res = null;
        try {
            NwHINDirectDocumentProcessing_Service service = new NwHINDirectDocumentProcessing_Service();
            NwHINDirectDocumentProcessing port = service.getNwHINDirectDocumentProcessingPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_XDM_ENDPOINT());
            List<Directprocessing> list = port.getXDMObjectListByProviderId(AdminContext.getSessionAttributes().getPROVIDER_ID());
            res = createTableContainer(list);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
        
    }
    
    private IndexedContainer createTableContainer(Collection<Directprocessing> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("oDate", String.class, null);
        container.addContainerProperty("oPatientName", String.class, null);
        container.addContainerProperty("oSender", String.class, null);
        container.addContainerProperty("oConfidentiality", String.class, null);
        container.addContainerProperty("oRefrainPolicy", String.class, null);
        container.addContainerProperty("oPurposeOfUse", String.class, null);
        container.addContainerProperty("oObject", Directprocessing.class, null);
        
        for (Directprocessing p : collection) {
            //System.out.println("ITEM ID IS: "+p.getRuleId());
            Item item = container.addItem(p.getIddirectprocessing());
            item.getItemProperty("oDate").setValue(convertDateToString(p.getDateProcessed()));            
            item.getItemProperty("oPatientName").setValue(p.getPatientName());
            item.getItemProperty("oSender").setValue(p.getSendingProviderId());
            item.getItemProperty("oConfidentiality").setValue(p.getConfidentiality());
            item.getItemProperty("oRefrainPolicy").setValue(p.getRefrainPolicy());
            item.getItemProperty("oPurposeOfUse").setValue(p.getPou());
            item.getItemProperty("oObject").setValue(p);
        }
        
        return container;
        
    }
    
    private String convertDateToString(XMLGregorianCalendar xres) {
        Date dt = null;
        String res = "";
        try {
            Calendar xcal = Calendar.getInstance();
            xcal.set(xres.getYear(), xres.getMonth() - 1, xres.getDay(), xres.getHour(), xres.getMinute(), xres.getSecond());
            dt = xcal.getTime();
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    private Panel getButtonPanel() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.setWidth("100%");
        
        refresh.setStyleName(Runo.BUTTON_SMALL);
        viewMetaData.setStyleName(Runo.BUTTON_SMALL);
        viewDocument.setStyleName(Runo.BUTTON_SMALL);
        testDisclosure.setStyleName(Runo.BUTTON_SMALL);
        viewEncryptedDoc.setStyleName(Runo.BUTTON_SMALL);
        viewTransformed.setStyleName(Runo.BUTTON_SMALL);
        providerAttributes.setStyleName(Runo.BUTTON_SMALL);
        
        refresh.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                table.setContainerDataSource(populateTable());
                table.setVisibleColumns(new Object[] {"oDate","oPatientName", "oSender", "oConfidentiality", "oRefrainPolicy", "oPurposeOfUse"});
                table.setColumnHeaders(new String[] {"Date Processed", "Patient Name", "From", "Confidentiality", "Primary Refrain Policy", "Intended Purpose Of Use"});
                table.requestRepaint();
            }
        });
        
        viewMetaData.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Directprocessing obj = getTableObject();
                if (obj != null) {
                    String drl = obj.getMetadata();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("XDM Metadata");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        viewDocument.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Directprocessing obj = getTableObject();
                if (obj != null) {
                    String drl = obj.getDocument();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("Decrypted C32");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        testDisclosure.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                    //getWindow().showNotification(disclosure, Notification.TYPE_WARNING_MESSAGE);
                    boolean res = policyEnforcementPoint("NwHINDirectReDisclose");
                    if (res){
                        if (refrainPolicy.equals("NORDSLCD")) getWindow().showNotification(disclosure, Notification.TYPE_WARNING_MESSAGE);                                               
                        subwindow = new Window("Send C32");
                        subwindow.setWidth("700px");
                        subwindow.setHeight("100%");
                        subwindow.setModal(true);
                        subwindow.addComponent(getEmailClient());
                        getApplication().getMainWindow().addWindow(subwindow);                        
                    }
                    else {
                       getWindow().showNotification("You currently do not have authorization to redisclose this information.  If this message is in error, please contact your Privacy/Security Administrator.", Notification.TYPE_TRAY_NOTIFICATION);                        
                    }
                    
            }
        });
        
        viewEncryptedDoc.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                //is anything selected
                Directprocessing obj = getTableObject();
                if (obj != null) {
                    String drl = obj.getEncrypteddocument();
                    XACMLViewer viewer = new XACMLViewer(drl);
                    subwindow = new Window("Encrypted C32");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(viewer);
                    getApplication().getMainWindow().addWindow(subwindow);
                }
            }
        });
        
        viewTransformed.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                //test for selection
                Directprocessing obj = getTableObject();
                if (obj != null) {
                    boolean res = policyEnforcementPoint("NwHINDirectView");
                    if (res) {
                        if (refrainPolicy.equals("NORDSLCD")) getWindow().showNotification(disclosure, Notification.TYPE_WARNING_MESSAGE);                       
                        String drl = obj.getDocument();
                        subwindow = new Window("Transformed C32");
                        subwindow.setWidth("700px");
                        subwindow.setHeight("100%");
                        subwindow.setModal(true);
                        subwindow.addComponent(createHTMLVersionOfC32(drl));
                        getApplication().getMainWindow().addWindow(subwindow);
                    }
                    else {
                       getWindow().showNotification("You currently do not have authorization to redisclose this information.  If this message is in error, please contact your Privacy/Security Administrator.", Notification.TYPE_TRAY_NOTIFICATION);                                                
                    }
                }
            }
        });
        
        providerAttributes.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                ProviderAuthorizationAttributes pa = new ProviderAuthorizationAttributes();
                
                subwindow = new Window("Provider Identity and Permissions");
                subwindow.setWidth("700px");
                subwindow.setHeight("100%");
                subwindow.setModal(true);
                subwindow.addComponent(pa);
                getApplication().getMainWindow().addWindow(subwindow);
            }
        });
                
        h.addComponent(providerAttributes);
        h.addComponent(refresh);
        h.addComponent(viewMetaData);
        h.addComponent(viewEncryptedDoc);
        h.addComponent(viewDocument);
        h.addComponent(viewTransformed);
        h.addComponent(testDisclosure);
        
        v.addComponent(h);
        
        return p;
    }
    
    private Directprocessing getTableObject() {
        Directprocessing res = null;
        try {
            Object rowId = table.getValue();
            if (rowId != null) {
                res = (Directprocessing)table.getContainerProperty(rowId, "oObject").getValue();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }       
        return res;
    }
    
    private Panel createHTMLVersionOfC32(String c32) {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        v.setHeight("100%");
        v.setWidth("100%");
        Label l = new Label();
        l.setContentMode(Label.CONTENT_XHTML);
        l.setValue(getHTMLVersionOfCDA(c32));
        v.addComponent(l);
        return p;
    }
    
    private String getHTMLVersionOfCDA(String q) {
        String res = "";
        //q is the string of c32
        //s is the stylesheet
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(qTransform)));
            Transformer transformer = tFactory.newTransformer(new StreamSource(reader));
            StringWriter out = new StringWriter();
            Result result = new StreamResult(out);
            transformer.transform(new StreamSource(new StringReader(q)), result);
            res = out.toString();
            System.out.println(res);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
    
    private boolean policyEnforcementPoint(String requestedResource) {
        boolean res = false;
        try {
            NwHINDirectAuthorizationServices_Service service = new NwHINDirectAuthorizationServices_Service();
            NwHINDirectAuthorizationServices port = service.getNwHINDirectAuthorizationServicesPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, AdminContext.getSessionAttributes().getDS4P_AUTH_ENDPOINT());
            String providerId = AdminContext.getProviderAttributes().getUserId();
            String recipientId = AdminContext.getProviderAttributes().getProviderId();
            String purposeOfUse = AdminContext.getProviderAttributes().getPurposeOfUse();
            String role = AdminContext.getProviderAttributes().getRole();
            String organizationName = AdminContext.getProviderAttributes().getOrganization();
            String organizationId = AdminContext.getProviderAttributes().getProviderHomeCommunityId();
            List<String> servicePermissions = AdminContext.getProviderAttributes().getAllowedNwHINActions();
            List<String> sensitivityPrivileges = AdminContext.getProviderAttributes().getAllowedSensitivityActions();
            List<String> requiredPermission = new ArrayList<String>(Arrays.asList(requestedResource));
            if (requestedResource.equals("NwDirectReceive") || requestedResource.equals("NwDirectCollect")) {
                requiredSensitivityPrivileges = new ArrayList();
                intendedRecipient = "";
                uniquePatientId = "";
                intendedPurposeOfUse = "";  
                refrainPolicy = "";
            }
            else {
                processMetaDataFile();
            }

            String decision = port.enforceDirectPolicy(providerId, recipientId, organizationName, organizationId, role, purposeOfUse, servicePermissions, sensitivityPrivileges, intendedPurposeOfUse, intendedRecipient, requiredPermission, requiredSensitivityPrivileges, requestedResource, uniquePatientId);
            if (decision.equals("Permit")) res = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            //getWindow().showNotification(disclosure, Notification.TYPE_WARNING_MESSAGE);
        }
        return res;
    }
    
    private Panel getEmailClient() {
        Panel p = new Panel();
        p.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)p.getContent();
        v.setSpacing(true);
        v.setHeight("100%");
        v.setWidth("100%");
        TextField recipientFLD = new TextField("To:");
        TextField fromFLD = new TextField("From:");
        TextField subjectFLD = new TextField("Subject: ");
        TextArea textArea = new TextArea("Body");
        recipientFLD.setWidth("400px");
        fromFLD.setWidth("400px");
        subjectFLD.setWidth("400px");
        textArea.setWidth("400px");
        textArea.setHeight("300px");
        
        fromFLD.setValue(AdminContext.getProviderAttributes().getProviderId());
        
        
        
        v.addComponent(recipientFLD);
        v.addComponent(fromFLD);
        v.addComponent(subjectFLD);
        v.addComponent(textArea);
        return p;
    }

    private void processMetaDataFile() {
        Directprocessing dp = getTableObject();
        String c32 = new String(dp.getMetadata());
        SubmitObjectsRequest obj = null;
        try {
            JAXBContext context = JAXBContext.newInstance(SubmitObjectsRequest.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(c32);

            Object o = unmarshaller.unmarshal(sr);
            obj = (SubmitObjectsRequest)o;
            
            RegistryObjectListType reg = obj.getRegistryObjectList();
            List<JAXBElement<? extends IdentifiableType>> extrinsicObjects = reg.getIdentifiable();
            if (extrinsicObjects != null && extrinsicObjects.size() > 0) {
                for (JAXBElement<? extends IdentifiableType> jaxb : extrinsicObjects) {
                    if (jaxb.getValue() instanceof ExtrinsicObjectType) {
                        ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType) jaxb.getValue();
                        List<SlotType1> sList = extrinsicObject.getSlot() ;
                        Iterator iter = sList.iterator();
                        while (iter.hasNext()) {
                            SlotType1 slot = (SlotType1)iter.next();
                            String slotName = slot.getName();
                            if (slotName.equals("sourcePatientId")) {
                                processPatientId(slot);
                            }
                            else if (slotName.equals("intendedRecipient")) {
                                processIntendedRecipient(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:purposeofuse")) {
                                processIntendedPOU(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:sensitivitypolicy")) {
                                processSensitivityPolicy(slot);
                            }
                            else if (slotName.equals("urn:siframework.org:ds4p:refrainpolicy")) {
                                processRefrainPolicy(slot);
                            }                            
                            else {
                                //don't care about
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }        
    }
    
    private void processPatientId(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String patientStr = vals.get(0);
        System.out.println(patientStr);
        StringTokenizer st = new StringTokenizer(patientStr, "^");
        uniquePatientId = st.nextToken();
    }
           
    private void processIntendedRecipient(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String recipientStr = vals.get(0);
        System.out.println(recipientStr);
        StringTokenizer st = new StringTokenizer(recipientStr, "^");
        st.nextToken();
        String recvr = st.nextToken();
        intendedRecipient = recvr;
        
    }
    
    private void processIntendedPOU(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String pouStr = vals.get(0);
        System.out.println(pouStr); 
        intendedPurposeOfUse = pouStr;
    }
        
    private void processSensitivityPolicy(SlotType1 slot) {
        requiredSensitivityPrivileges.clear();
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String senseStr = vals.get(0);
        System.out.println(senseStr); 
        requiredSensitivityPrivileges.add(senseStr);
    }
    private void processRefrainPolicy(SlotType1 slot) {
        ValueListType val = slot.getValueList();
        List<String> vals = val.getValue();
        String refrainStr = vals.get(0);
        System.out.println(refrainStr);
        refrainPolicy = refrainStr;
    }
        
}
