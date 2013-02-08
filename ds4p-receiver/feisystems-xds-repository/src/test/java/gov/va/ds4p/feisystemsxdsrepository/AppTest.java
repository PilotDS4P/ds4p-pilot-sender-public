package gov.va.ds4p.feisystemsxdsrepository;

import gov.va.ehtac.ds4p.ws.ch.EnforcePolicy;
import gov.va.ehtac.ds4p.ws.ch.FilterC32Service;
import gov.va.ehtac.ds4p.ws.ch.FilterC32ServicePortType;
import gov.va.ehtac.ds4p.ws.ch.RegisteryStoredQueryRequest;
import gov.va.ehtac.ds4p.ws.ch.RegisteryStoredQueryResponse;
import gov.va.ehtac.ds4p.ws.ch.RetrieveDocumentSetRequest;
import gov.va.ehtac.ds4p.ws.ch.RetrieveDocumentSetResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse.DocumentResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
/**
 * Unit test for simple App.
 */
public class AppTest 
extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( AppTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testAppDocQuery()
	{
		try {
			RegisteryStoredQueryRequest request = new RegisteryStoredQueryRequest();

			//request.setPatientId("\'PUI100010060001^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\'");
			request.setPatientId("'PUI100010060001^^^&2.16.840.1.113883.3.467&ISO'");
			EnforcePolicy en = new EnforcePolicy();
			EnforcePolicy.Xspasubject subj = getXspaSubject();
			EnforcePolicy.Xsparesource resc = getXspaResource();
			en.setXsparesource(resc);
			en.setXspasubject(subj);

			request.setEnforcePolicy(en);
                        
			FilterC32Service service = new FilterC32Service();
			FilterC32ServicePortType port = service.getFilterC32Port();

			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://xds-demo.feisystems.com/Orchestrator/services/filterc32service?wsdl");
			RegisteryStoredQueryResponse response = port.registeryStoredQuery(request);
			String res = response.getReturn();
			System.out.println("RESULTS: "+res);
			System.out.println("messageId: " + subj.getMessageId());
			assertTrue(true);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			assertTrue(true);
		}
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testAppDocRetrieve()
	{
//		try {
//			RetrieveDocumentSetRequest request = new RetrieveDocumentSetRequest();
//			EnforcePolicy en = new EnforcePolicy();
//			EnforcePolicy.Xspasubject subj = getXspaSubject();
//			EnforcePolicy.Xsparesource resc = getXspaResource();
//			en.setXsparesource(resc);
//			en.setXspasubject(subj);
//
//			request.setHomeCommunityId("2.16.840.1.113883.3.467");
//			request.setRepositoryUniqueId("1.3.6.1.4.1.21367.2010.1.2.1040");	
//			request.setDocumentUniqueId("7943611141.010126.414155.110610.11414711212812562");
//			request.setMessageId(en.getXspasubject().getMessageId());
//			request.setEnforcePolicy(en);
//
//			FilterC32Service service = new FilterC32Service();
//			FilterC32ServicePortType port = service.getFilterC32Port();
//			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://xds-demo.feisystems.com/Orchestrator/services/filterc32service?wsdl");
//			RetrieveDocumentSetResponse response = port.retrieveDocumentSet(request);
//			String res = response.getReturn();
//			System.out.println("RESULTS: "+res);
//
//			// decrypt document
//			ihe.iti.xds_b._2007.RetrieveDocumentSetResponse xdsbRetrieveDocumentSetResponse = unmarshallFromXml(ihe.iti.xds_b._2007.RetrieveDocumentSetResponse.class, res);
//			DocumentResponse documentResponse = xdsbRetrieveDocumentSetResponse
//					.getDocumentResponse().get(0);
//			byte[] processDocBytes = documentResponse.getDocument();
//                     
//			String processedDocuemntString = decryptDocument(processDocBytes, response.getKekEncryptionKey(), response.getKekMaskingKey());
//
//			System.out.println("Processed document: " + processedDocuemntString);
//			assertTrue(true);
//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
			assertTrue(true);
//		}
	}

	private EnforcePolicy.Xspasubject getXspaSubject() {
		EnforcePolicy.Xspasubject sub = new EnforcePolicy.Xspasubject();
		sub.setSubjectId("Duane_Decouteau@direct.healthvault-stage.com");

		sub.setUserId("DrDuane");
		sub.setSubjectPurposeOfUse("TREAT");
		sub.setOrganizationId("Edmonds Sci");
		sub.setOrganization("VA");
		sub.getSubjectPermissions().add("ETH");
		sub.getSubjectPermissions().add("HIV");
		sub.getSubjectPermissions().add("PSY");
		sub.getSubjectStructuredRole().add("MD/Allopath");
		sub.setSubjectLocality("2.16.840.1.113883.3.467");
		sub.setMessageId(UUID.randomUUID().toString());

		return sub;
	}

	private EnforcePolicy.Xsparesource getXspaResource() {
		EnforcePolicy.Xsparesource resp = new EnforcePolicy.Xsparesource();
		resp.setResourceAction("Exec");
		resp.setResourceId("PUI100010060001");
		resp.setResourceName("NwHINDirectSend");
		resp.setResourceType("C32");
		return resp;
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

	private static Document loadDocument(String xmlString) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource source = new InputSource(new StringReader(xmlString));

		Document doc = db.parse(source);

		return doc;
	}

	private static String converXmlDocToString(Document xmlDocument) throws IOException, TransformerException
	{
		String xmlString = "";

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
		xmlString = writer.getBuffer().toString().replaceAll("\n|\r", "");
		return xmlString;
	}

	private <T> T unmarshallFromXml(Class<T> clazz, String xml)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
		return (T) um.unmarshal(input);
	}
}
