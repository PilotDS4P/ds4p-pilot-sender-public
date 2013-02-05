/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.panels;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;
import gov.va.ehtac.ds4p.ws.ch.EnforcePolicy;
import gov.va.ehtac.ds4p.ws.ch.EnforcePolicy.Xsparesource;
import gov.va.ehtac.ds4p.ws.ch.EnforcePolicy.Xspasubject;
import gov.va.ehtac.ds4p.ws.ch.FilterC32Service;
import gov.va.ehtac.ds4p.ws.ch.FilterC32ServicePortType;
import gov.va.ehtac.ds4p.ws.ch.RegisteryStoredQueryRequest;
import gov.va.ehtac.ds4p.ws.ch.RegisteryStoredQueryResponse;
import gov.va.ehtac.ds4p.ws.ch.RetrieveDocumentSetRequest;
import gov.va.ehtac.ds4p.ws.ch.RetrieveDocumentSetResponse;
import gov.va.ehtac.meaningfuluse.displayobjects.ExchangeResults;
import gov.va.ehtac.meaningfuluse.encryption.DecryptTool;
import gov.va.ehtac.meaningfuluse.session.ProviderAttributes;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse.DocumentResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import oasis.names.tc.ebxml_regrep.xsd.query._3.*;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Duane DeCouteau
 */
public class DS4PeXchange extends Panel {
    private ComboBox patientList = new ComboBox("Available Patients");
    private Button searchBTN = new Button("Search");
    private Button viewXMLBTN = new Button("Retrieve and View Document XML");
    private Button unEncryptBTN = new Button("View Decrypted XML");
    private Button transformBTN = new Button("View with StyleSheet");
    private Table table = new Table("Doc Query Results");
    private TextArea area = new TextArea();
    private ProviderAttributes provider = new ProviderAttributes();
    private String patientName = "";
    private String patientId = "";
    private String docId = "";
    private String docType = "";
    private String messageId = "";
    private String respId = "";
    private String currentDocument = "";
    private String decryptDocument = "";
    private byte[] keyD;
    private byte[] keyM;
    private String metaData = "";
    
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmssZ";
    private static final String REGULAR_DATE_FORMAT = "MM/dd/yyyy";
    
    private static final String DOC_QUERY = "query";
    private static final String DOC_RETRIEVE = "retrieve";
    
    Window subwindow;
    
    private String qTransform = "../../../../../../resources/transforms/CDA.xsl";
    
    
    public DS4PeXchange() {
        this.setStyleName(Runo.PANEL_LIGHT);
        VerticalLayout v = (VerticalLayout)this.getContent();
        populatePatientList();
        
        v.setSpacing(true);
        v.setWidth("100%");
        patientList.setWidth("70%");
        HorizontalLayout h = new HorizontalLayout();
        h.setWidth("100%");
        h.addComponent(patientList);
        h.addComponent(searchBTN);
        
        v.addComponent(h);
        table.setStyleName(Runo.TABLE_SMALL);
        table.setWidth("100%");
        table.setHeight("80px");
        table.setMultiSelect(false);
        table.setSelectable(true);
        table.setImmediate(true); // react at once when something is selected
        table.setEditable(false);
        table.setWriteThrough(true);
        table.setContainerDataSource(populateTable());

        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(false);
        table.setVisibleColumns(new Object[] {"patientId", "docId", "docType", "respId", "msgId"});
        table.setColumnHeaders(new String[] {"Patient ID", "Document ID", "Document Type", "Repository", "Message"});
        
        table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object rowId = table.getValue();
                if (rowId != null) {
                    patientName = (String)table.getContainerProperty(rowId, "patientName").getValue();
                    patientId = (String)table.getContainerProperty(rowId, "patientId").getValue();
                    docId = (String)table.getContainerProperty(rowId, "docId").getValue();
                    docType = (String)table.getContainerProperty(rowId, "docType").getValue();
                    respId = (String)table.getContainerProperty(rowId, "respId").getValue();
                    messageId = (String)table.getContainerProperty(rowId, "msgId").getValue();                   
                }                

            }
        });
        
        
        v.addComponent(table);
        area.setWidth("100%");
        area.setHeight("250px");
        area.setCaption("No Documents Selected");
        v.addComponent(area);
        
        HorizontalLayout h2 = new HorizontalLayout();
        h2.setWidth("100%");
        h2.addComponent(viewXMLBTN);
        h2.addComponent(unEncryptBTN);
        h2.addComponent(transformBTN);
        v.addComponent(h2);
        
        searchBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                patientId = (String)patientList.getValue();
                patientName = (String)patientList.getCaption();
                currentDocument = "";
                decryptDocument = "";
                area.setValue(currentDocument);
                area.setCaption("No Documents Selected");
                String res = lookUpPatient();
                if (res.indexOf("repositoryUniqueId") > -1) {
                    AdhocQueryResponse adhoc = getQueryResponse(res);
                
                    
                    RegistryObjectListType rList = adhoc.getRegistryObjectList();
                    List<ExchangeResults> xList = processMetaData(rList);
                    refreshTable(xList);
                }
                else {
                    //throw warning 
                    docId = "NA";
                    docType = "NA";
                    messageId = "NA";
                    respId = "1.3.6.1.4.1.21367.2010.1.2.1040";
                    ExchangeResults eR = new ExchangeResults(patientName, patientId, docId, docType, "", "", "", messageId, respId);
                    List<ExchangeResults> xList = new ArrayList();
                    xList.add(eR);
                    refreshTable(xList);
                    displayErrorMessage(res);                    
                }
            }
        });
        
        viewXMLBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object rowId = table.getValue();
                if (rowId != null) {
                    getDocument();
                    area.setCaption("Selected Document (Raw)");
                    area.setValue(currentDocument);
                    area.requestRepaint();
                }
                else {
                    getWindow().showNotification("Warning", "No row selected, Please select document for retrieval.", Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        
        unEncryptBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (currentDocument.length() == 0) {
                    getWindow().showNotification("Warning", "You must select and retrieve document first.", Notification.TYPE_WARNING_MESSAGE);                    
                }
                else {
                    
                    try {
                        ihe.iti.xds_b._2007.RetrieveDocumentSetResponse xdsbRetrieveDocumentSetResponse = unmarshallFromXml(ihe.iti.xds_b._2007.RetrieveDocumentSetResponse.class, currentDocument);
                            DocumentResponse documentResponse = xdsbRetrieveDocumentSetResponse.getDocumentResponse().get(0);
                        if (docType.equals("Consult Notes")) {
                            byte[] processDocBytes = documentResponse.getDocument();                    
                        
                            decryptDocument = decryptDocument(processDocBytes, keyD, keyM);
                        }
                        else {
                            byte[] processDocBytes = documentResponse.getDocument();
                            decryptDocument = new String(processDocBytes);
                        }
                        area.setCaption("Selected Document Decrypted");
                        area.setValue(decryptDocument);
                        area.requestRepaint();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        transformBTN.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (decryptDocument.length() == 0) {
                    getWindow().showNotification("Warning", "You must select and retrieve document first, then view decrypted version, before attempting to transform it.", Notification.TYPE_WARNING_MESSAGE);                                        
                }
                else {
                    subwindow = new Window("Transformed C32");
                    subwindow.setWidth("700px");
                    subwindow.setHeight("100%");
                    subwindow.setModal(true);
                    subwindow.addComponent(createHTMLVersionOfC32(decryptDocument));
                    getApplication().getMainWindow().addWindow(subwindow);                
                }
            }
        });
        
        patientList.setImmediate(true);
        searchBTN.setImmediate(true);
        viewXMLBTN.setImmediate(true);
        area.setImmediate(true);
    }
    
    private void refreshTable(List<ExchangeResults> lres) {
        IndexedContainer res = createTableContainer(lres);
        table.setContainerDataSource(res);
        table.setVisibleColumns(new Object[] {"patientId", "docId", "docType", "respId", "msgId"});
        table.setColumnHeaders(new String[] {"Patient ID", "Document ID", "Document Type", "Repository", "Message"});
        table.requestRepaint();
    }
    
    private void displayErrorMessage(String err) {
         getWindow().showNotification("Warning - Registry Lookup Return No Results.", "", Notification.TYPE_WARNING_MESSAGE);                            
    }
    
    private void populatePatientList() {
        patientList.addItem("PUI100010060001");
        patientList.setItemCaption("PUI100010060001", "Asample Patientone");
        patientList.addItem("PUI100010060007");
        patientList.setItemCaption("PUI100010060007", "Asample Patienttwo");
    }
    
    private IndexedContainer populateTable() {
        IndexedContainer res = new IndexedContainer();
        if (patientId != null || patientId.length() == 0) {
            List<ExchangeResults> lres = new ArrayList();
            res = createTableContainer(lres);
        }
        else {
            String ad = lookUpPatient();
            List<ExchangeResults> lres = new ArrayList();
            res = createTableContainer(lres);
            
        }
        return res;
    }
    
    private IndexedContainer createTableContainer(Collection<ExchangeResults> collection) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("patientName", String.class, null);
        container.addContainerProperty("patientId", String.class, null);
        container.addContainerProperty("docId", String.class, null);
        container.addContainerProperty("docType", String.class, null);        
        container.addContainerProperty("msgId", String.class, null);
        container.addContainerProperty("respId", String.class, null);
        container.addContainerProperty("oExchangeResults", ExchangeResults.class, null);
        
        Integer tt = new Integer(1);
        for (ExchangeResults p : collection) {
            //System.out.println("ITEM ID IS: "+p.getRuleId());
            Item item = container.addItem(tt);
            item.getItemProperty("patientName").setValue(p.getPatientName());
            item.getItemProperty("patientId").setValue(p.getPatientId());            
            item.getItemProperty("docId").setValue(p.getDocId()); 
            item.getItemProperty("docType").setValue(p.getDocType());
            item.getItemProperty("msgId").setValue(p.getMesgId()); 
            item.getItemProperty("respId").setValue(p.getRespId());
            item.getItemProperty("oExchangeResults").setValue(p);
            tt++;
        }        
        return container;
    }
    
    private Xspasubject getXspaSubject(String action) {
        Xspasubject sub = new Xspasubject();
        sub.setSubjectId(provider.getProviderId());
        sub.setUserId(provider.getUserId());
        sub.setSubjectPurposeOfUse("TREAT");
        sub.setOrganizationId("Edmonds Sci");
        sub.setOrganization("VA");
        sub.getSubjectPermissions().add("ETH");
        sub.getSubjectPermissions().add("HIV");
        sub.getSubjectPermissions().add("PSY");
        sub.getSubjectStructuredRole().add("MD/Allopath");
        sub.setSubjectLocality("2.16.840.1.113883.3.467");
        //generate a new message id on query
        if (action.equals(DOC_QUERY)) {
            messageId = UUID.randomUUID().toString();
        }
        sub.setMessageId(messageId);
       
        return sub;
    }
    
    private Xsparesource getXspaResource() {
        Xsparesource resp = new Xsparesource();
        resp.setResourceAction("Exec");
        resp.setResourceId(patientId);
        resp.setResourceName("NwHINDirectSend");
        resp.setResourceType("C32");
        return resp;
    }
    
    private void getDocument() {
        RetrieveDocumentSetRequest request = new RetrieveDocumentSetRequest();
        request.setDocumentUniqueId(docId);
        request.setHomeCommunityId("2.16.840.1.113883.3.467");
        request.setRepositoryUniqueId("1.3.6.1.4.1.21367.2010.1.2.1040");
        
        request.setMessageId(messageId);
        EnforcePolicy en = new EnforcePolicy();
        en.setXsparesource(getXspaResource());
        en.setXspasubject(getXspaSubject("retrieve"));
        
        request.setEnforcePolicy(en);

        
        FilterC32Service service = new FilterC32Service();
        FilterC32ServicePortType port = service.getFilterC32Port();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://xds-demo.feisystems.com/Orchestrator/services/filterc32service?wsdl");
        RetrieveDocumentSetResponse response = port.retrieveDocumentSet(request);
        
        currentDocument = response.getReturn();
        metaData = response.getMetadata();
        keyD = response.getKekEncryptionKey();
        keyM = response.getKekMaskingKey();
        
    }
    
    private String lookUpPatient() {
        RegisteryStoredQueryRequest request = new RegisteryStoredQueryRequest();
        request.setPatientId("\'"+patientId+"^^^&2.16.840.1.113883.3.467&ISO\'"); 
        EnforcePolicy en = new EnforcePolicy();
        en.setXsparesource(getXspaResource());
        en.setXspasubject(getXspaSubject("query"));
        
        request.setEnforcePolicy(en);
        
        FilterC32Service service = new FilterC32Service();
        FilterC32ServicePortType port = service.getFilterC32Port();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://xds-demo.feisystems.com/Orchestrator/services/filterc32service?wsdl");
        RegisteryStoredQueryResponse response = port.registeryStoredQuery(request);
        String res = response.getReturn();
        System.out.println("RESULTS: "+res);
        return res;
    }
    
    private AdhocQueryResponse getQueryResponse(String mxml) {
        AdhocQueryResponse obj = null;
        try {
            JAXBContext context = JAXBContext.newInstance(AdhocQueryResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(mxml);

            Object o = unmarshaller.unmarshal(sr);
            obj = (AdhocQueryResponse)o;

        }
        catch (Exception e) {
            //log.warn("",e);
            e.printStackTrace();
        }        
        return obj;
        
    }
    
    private List<ExchangeResults> processMetaData(RegistryObjectListType objList) {
        List<ExchangeResults> docInfoList = new ArrayList();
            //add all from nhinresults
        List<JAXBElement<? extends IdentifiableType>> extrinsicObjects = objList.getIdentifiable();

        if (extrinsicObjects != null && extrinsicObjects.size() > 0) {
            for (JAXBElement<? extends IdentifiableType> jaxb : extrinsicObjects) {
                ExchangeResults docInfo = new ExchangeResults();

                if (jaxb.getValue() instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType) jaxb.getValue();

                    if (extrinsicObject != null) {
                        docInfo.setPatientId(patientId);
                        docInfo.setDocName(extractDocumentTitle(extrinsicObject));
                        docInfo.setDocType(extractDocumentType(extrinsicObject));
                        //String creationTime = extractCreationTime(extrinsicObject);
                        docInfo.setDocDate(""); //(formatDate(creationTime, HL7_DATE_FORMAT, REGULAR_DATE_FORMAT));
                        docInfo.setOrgName(extractInstitution(extrinsicObject));
                        docInfo.setDocId(extractDocumentID(extrinsicObject));
                        docInfo.setRespId(extractRespositoryUniqueID(extrinsicObject));
                        docInfo.setMesgId(messageId);
                        docInfoList.add(docInfo);
                    }
                }
            }
        }
        
        
        
        return docInfoList;
    }
    
    private String extractSingleSlotValue(ExtrinsicObjectType extrinsicObject, String slotName) {
        String slotValue = null;
        for (SlotType1 slot : extrinsicObject.getSlot()) {
            if (slot != null && slot.getName().contentEquals(slotName)) {
                if (slot.getValueList().getValue().size() > 0) {
                    slotValue = slot.getValueList().getValue().get(0);
                    break;
                }
            }
        }
        return slotValue;
    }

    private String extractDocumentTitle(ExtrinsicObjectType extrinsicObject) {

        String documentTitle = null;

        if (extrinsicObject != null &&
                extrinsicObject.getName() != null) {
            List<LocalizedStringType> localizedString = extrinsicObject.getName().getLocalizedString();

            if (localizedString != null && localizedString.size() > 0) {
                documentTitle = localizedString.get(0).getValue();
            }
        }

        return documentTitle;
    }
    private String extractDocumentType(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a");

        String documentTypeCode = classification.getName().getLocalizedString().get(0).getValue();
        return documentTypeCode;
    }

    private ClassificationType extractClassification(ExtrinsicObjectType extrinsicObject, String classificationScheme) {
        ClassificationType classification = null;

        for (ClassificationType classificationItem : extrinsicObject.getClassification()) {
            if (classificationItem != null && classificationItem.getClassificationScheme().contentEquals(classificationScheme)) {
                classification = classificationItem;
                break;
            }
        }

        return classification;
    }
    
    private String extractCreationTime(ExtrinsicObjectType extrinsicObject) {
        return extractSingleSlotValue(extrinsicObject, "creationTime");
    }
    private String formatDate(String dateString, String inputFormat, String outputFormat) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
        Date date = null;

        try {
            date = inputFormatter.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return outputFormatter.format(date);
    }

    private String extractInstitution(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");

        String institution = null;

        if (classification != null && classification.getSlot() != null && !classification.getSlot().isEmpty()) {
            for (SlotType1 slot : classification.getSlot()) {
                if (slot != null && slot.getName().contentEquals("authorInstitution")) {
                    if (slot.getValueList() != null && slot.getValueList().getValue() != null && !slot.getValueList().getValue().isEmpty()) {
                        institution = slot.getValueList().getValue().get(0);
                        break;
                    }
                }
            }
        }

        return institution;
    }
    
    private String extractDocumentID(ExtrinsicObjectType extrinsicObject) {
        String documentID = null;

        ExternalIdentifierType identifier = extractIndentifierType(extrinsicObject, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");

        if (identifier != null) {
            documentID = identifier.getValue();
        }

        return documentID;
    }

    private ExternalIdentifierType extractIndentifierType(ExtrinsicObjectType extrinsicObject, String identificationScheme) {
        ExternalIdentifierType identifier = null;

        for (ExternalIdentifierType identifierItem : extrinsicObject.getExternalIdentifier()) {
            if (identifierItem != null && identifierItem.getIdentificationScheme().contentEquals(identificationScheme)) {
                identifier = identifierItem;
                break;
            }
        }

        return identifier;
    }
    private String extractRespositoryUniqueID(ExtrinsicObjectType extrinsicObject)
    {
        return extractSingleSlotValue(extrinsicObject, "repositoryUniqueId");
    }
    
	public String decryptDocument(byte[] processDocBytes, byte[] kekEncryptionKeyBytes, byte[] kekMaskingKeyBytes ) {

		Document processedDoc = null;
		String processedDocString = "";
		DESedeKeySpec desedeEncryptKeySpec;
		DESedeKeySpec desedeMaskKeySpec;
		try {

			org.apache.xml.security.Init.init();

			String processDocString = new String(processDocBytes);

			processedDoc = loadDocument(processDocString);

			desedeEncryptKeySpec =
					new DESedeKeySpec(kekEncryptionKeyBytes);
			SecretKeyFactory skfEncrypt =
					SecretKeyFactory.getInstance("DESede");
			SecretKey desedeEncryptKey = skfEncrypt.generateSecret(desedeEncryptKeySpec);			

			desedeMaskKeySpec =
					new DESedeKeySpec(kekMaskingKeyBytes);
			SecretKeyFactory skfMask =
					SecretKeyFactory.getInstance("DESede");
			SecretKey desedeMaskKey = skfMask.generateSecret(desedeMaskKeySpec);


			/*************************************************
			 * DECRYPT DOCUMENT
			 *************************************************/
			Element encryptedDataElement = (Element) processedDoc
					.getElementsByTagNameNS(
							EncryptionConstants.EncryptionSpecNS,
							EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);

			/*
			 * The key to be used for decrypting xml data would be obtained from
			 * the keyinfo of the EncrypteData using the kek.
			 */
			XMLCipher xmlCipher = XMLCipher.getInstance();
			xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
			xmlCipher.setKEK(desedeEncryptKey);

			/*
			 * The following doFinal call replaces the encrypted data with
			 * decrypted contents in the document.
			 */
			if (encryptedDataElement != null)
				xmlCipher.doFinal(processedDoc, encryptedDataElement);

			/*************************************************
			 * DECRYPT ELEMENTS
			 *************************************************/
			NodeList encryptedDataElements = processedDoc
					.getElementsByTagNameNS(
							EncryptionConstants.EncryptionSpecNS,
							EncryptionConstants._TAG_ENCRYPTEDDATA);

			while (encryptedDataElements.getLength() > 0) {
				/*
				 * The key to be used for decrypting xml data would be obtained
				 * from the keyinfo of the EncrypteData using the kek.
				 */
				XMLCipher xmlMaskCipher = XMLCipher.getInstance();
				xmlMaskCipher.init(XMLCipher.DECRYPT_MODE, null);
				xmlMaskCipher.setKEK(desedeMaskKey);

				xmlMaskCipher.doFinal(processedDoc,
						((Element) encryptedDataElements.item(0)));

				encryptedDataElements = processedDoc.getElementsByTagNameNS(
						EncryptionConstants.EncryptionSpecNS,
						EncryptionConstants._TAG_ENCRYPTEDDATA);
			}

			processedDocString = converXmlDocToString(processedDoc);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return processedDocString;
	}	

    private Document getW3CDocument(String docString) {
        Document doc = null;
        try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource source = new InputSource(new StringReader(docString));

		doc = db.parse(source);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
		return doc;
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
    
    private <T> T unmarshallFromXml(Class<T> clazz, String xml)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
		return (T) um.unmarshal(input);
    }
    private String converXmlDocToString(Document xmlDocument) {

        String xmlString = "";

        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
            xmlString = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return xmlString;
    }
    private static Document loadDocument(String xmlString) throws Exception {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource source = new InputSource(new StringReader(xmlString));

            Document doc = db.parse(source);

            return doc;
    }
    
}
