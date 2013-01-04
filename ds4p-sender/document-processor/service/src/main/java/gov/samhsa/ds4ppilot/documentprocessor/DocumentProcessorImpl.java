/*******************************************************************************
 * Open Behavioral Health Information Technology Architecture (OBHITA.org)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.samhsa.ds4ppilot.documentprocessor;

import gov.samhsa.ds4ppilot.common.beans.RuleExecutionContainer;
import gov.samhsa.ds4ppilot.common.beans.XacmlResult;
import gov.samhsa.ds4ppilot.common.exception.DS4PException;
import gov.samhsa.ds4ppilot.common.utils.EncryptTool;
import gov.samhsa.ds4ppilot.common.utils.FileHelper;
import gov.samhsa.ds4ppilot.common.utils.StringURIResolver;
import gov.samhsa.ds4ppilot.common.utils.XmlHelper;
import gov.samhsa.ds4ppilot.common.xdm.XdmZipUtils;
import gov.samhsa.ds4ppilot.documentprocessor.audit.AuditServiceImpl;
import gov.samhsa.ds4ppilot.documentprocessor.healthcareclassification.HealthcareClassificationClientImpl;
import gov.samhsa.ds4ppilot.schema.documentprocessor.ProcessDocumentResponse;
import gov.va.ds4p.cas.RuleExecutionResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class DocumentProcessorImpl.
 */
public class DocumentProcessorImpl implements DocumentProcessor {

	/** The healthcare classification client impl. */
	private final HealthcareClassificationClientImpl healthcareClassificationClientImpl;

	/** The audit service impl. */
	private final AuditServiceImpl auditServiceImpl;

	/** The de sede encrypt key. */
	private Key deSedeEncryptKey;

	/** The de sede mask key. */
	private Key deSedeMaskKey;

	/**
	 * Gets the de sede encrypt key.
	 *
	 * @return the de sede encrypt key
	 */
	public Key getDeSedeEncryptKey() {
		return deSedeEncryptKey;
	}

	/**
	 * Sets the de sede encrypt key.
	 *
	 * @param deSedeEncryptKey the new de sede encrypt key
	 */
	public void setDeSedeEncryptKey(Key deSedeEncryptKey) {
		this.deSedeEncryptKey = deSedeEncryptKey;
	}

	/**
	 * Gets the de sede mask key.
	 *
	 * @return the de sede mask key
	 */
	public Key getDeSedeMaskKey() {
		return deSedeMaskKey;
	}

	/**
	 * Sets the de sede mask key.
	 *
	 * @param deSedeMaskKey the new de sede mask key
	 */
	public void setDeSedeMaskKey(Key deSedeMaskKey) {
		this.deSedeMaskKey = deSedeMaskKey;
	}

	/**
	 * Instantiates a new document processor impl.
	 *
	 * @param healthcareClassificationClientImpl the healthcare classification client impl
	 * @param auditServiceImpl the audit service impl
	 */
	public DocumentProcessorImpl(
			HealthcareClassificationClientImpl healthcareClassificationClientImpl,
			AuditServiceImpl auditServiceImpl) {

		this.healthcareClassificationClientImpl = healthcareClassificationClientImpl;
		this.auditServiceImpl = auditServiceImpl;
	}

	/**
	 * *****************************************
	 * Process document based on drools directives
	 * ******************************************.
	 *
	 * @param document the document
	 * @param enforcementPolicies the enforcement policies
	 * @param packageAsXdm the package as xdm
	 * @param senderEmailAddress the sender email address
	 * @param recipientEmailAddress the recipient email address
	 * @return the process document response
	 */
	@Override
	public ProcessDocumentResponse processDocument(String document,
			String enforcementPolicies, boolean packageAsXdm, boolean encryptDocument,
			String senderEmailAddress, String recipientEmailAddress) {
		RuleExecutionContainer ruleExecutionContainer = null;
		XacmlResult xacmlResult = null;
		ByteArrayDataSource rawData = null;
		String metadataXml = "";
		enforcementPolicies = enforcementPolicies.replace(
				" xmlns:ns2=\"http://ws.ds4p.ehtac.va.gov/\"", "");
		ProcessDocumentResponse processDocumentResponse = new ProcessDocumentResponse();

		try {
			FileHelper.writeStringToFile(document, "Original_C32.xml");

			// extract factModel
			String factModel = extractFactModel(document, enforcementPolicies);
			FileHelper.writeStringToFile(factModel, "FactModel.xml");

			// get execution response container
			String executionResponseContainer;
			executionResponseContainer = healthcareClassificationClientImpl
					.assertAndExecuteClinicalFacts(factModel);

			processDocumentResponse.setPostProcessingDirectives(executionResponseContainer);
			// unmarshall from xml to RuleExecutionContainer
			ruleExecutionContainer = unmarshallFromXml(
					RuleExecutionContainer.class, executionResponseContainer);

			FileHelper.writeStringToFile(executionResponseContainer,
					"ExecutionResponseContainer.xml");

			// unmarshall from xml to XacmlResult
			xacmlResult = unmarshallFromXml(XacmlResult.class,
					enforcementPolicies);

			// redact element
			document = redactElement(document, ruleExecutionContainer);

			// tag document
			document = tagDocument(document, executionResponseContainer,
					xacmlResult.getMessageId());

			// generate metadata xml
			metadataXml = generateMetadataXml(document, executionResponseContainer, xacmlResult.getHomeCommunityId(), senderEmailAddress,
					recipientEmailAddress);
			FileHelper.writeStringToFile(metadataXml, "metadata.xml");

			// log annoted doc
			auditServiceImpl.updateAuthorizationEventWithAnnotatedDoc(
					xacmlResult.getMessageId(), document);
			FileHelper.writeStringToFile(document, "Tagged_C32.xml");

			// mask element
			document = maskElement(document, ruleExecutionContainer);
			processDocumentResponse.setMaskedDocument(document);

			// encrypt document
			if(encryptDocument)
				document = encryptDocument(document, ruleExecutionContainer);

			byte[] documentPayload  = (packageAsXdm) ? XdmZipUtils.createXDMPackage(
					metadataXml, readFile("CCD.xsl"), document,
					readFile("INDEX.htm"), readFile("README.txt"),
					getDeSedeMaskKey().getEncoded(), getDeSedeEncryptKey()
					.getEncoded()) : document.getBytes();

					rawData = new ByteArrayDataSource(documentPayload);
					processDocumentResponse.setProcessedDocument(new DataHandler(rawData));
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);
		} catch (Exception e) {
			throw new DS4PException(e.toString(), e);
		}

		return processDocumentResponse;
	}

	/**
	 * *****************************************
	 * Extract clinical facts and xacml result
	 * ******************************************.
	 *
	 * @param document the document
	 * @param enforcementPolicies the enforcement policies
	 * @return the string
	 */
	public String extractFactModel(String document, String enforcementPolicies) {
		StringWriter writer = new StringWriter();
		InputStream in = null;
		String factModel = null;

		try {
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("extractClinicalFacts.xsl");

			StreamSource source = new StreamSource(new StringReader(document));
			StreamSource style = new StreamSource(in);

			String xacmlResult = enforcementPolicies;

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer t = factory.newTransformer(style);

			t.setParameter("xacmlResult", xacmlResult);

			t.setOutputProperty(OutputKeys.INDENT, "no");
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
			t.transform(source, new StreamResult(writer));

			factModel = writer.toString();

			factModel = factModel.replaceAll("&lt;", "<");
			factModel = factModel.replaceAll("&gt;", ">");

			in.close();
			writer.close();
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);

		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		}

		return factModel;
	}

	/**
	 * *****************************************
	 * Generate metadata xml
	 * ******************************************.
	 *
	 * @param document the document
	 * @param executionResponseContainer the execution response container
	 * @param homeCommunityId the home community id
	 * @param senderEmailAddress the sender email address
	 * @param recipientEmailAddress the recipient email address
	 * @return the string
	 */
	public String generateMetadataXml(String document, String executionResponseContainer, String homeCommunityId,
			String senderEmailAddress, String recipientEmailAddress) {
		StringWriter out = new StringWriter();
		InputStream in = null;
		String metadataXml = null;

		try {

			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("metadata.xsl");

			// add namespace execution response container for transformation
			executionResponseContainer = executionResponseContainer.replace(
					"<ruleExecutionContainer>",
					"<ruleExecutionContainer xmlns=\"urn:hl7-org:v3\">");

			StreamSource style = new StreamSource(in);

			TransformerFactory factory = TransformerFactory.newInstance();
			Templates template = factory.newTemplates(style);

			Transformer t = template.newTransformer();
			t.setURIResolver(new StringURIResolver().put(
					"ruleExecutionResponseContainer",
					executionResponseContainer));
			t.setParameter("homeCommunityId", homeCommunityId);
			t.setParameter("authorTelecommunication", senderEmailAddress);
			t.setParameter("intendedRecipient", recipientEmailAddress);

			t.transform(new StreamSource(new StringReader(document)),
					new StreamResult(out));

			metadataXml = out.toString();

			out.close();
			in.close();
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);

		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		}

		return metadataXml;
	}

	/**
	 * *****************************************
	 * Tag document
	 * ******************************************.
	 *
	 * @param document the document
	 * @param executionResponseContainer the execution response container
	 * @param messageId the message id
	 * @return the string
	 */
	public String tagDocument(String document,
			String executionResponseContainer, String messageId) {
		StringWriter out = new StringWriter();
		InputStream in = null;
		String taggedDocument = null;

		try {
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("tag.xsl");

			// add namespace execution response container for transformation
			executionResponseContainer = executionResponseContainer.replace(
					"<ruleExecutionContainer>",
					"<ruleExecutionContainer xmlns=\"urn:hl7-org:v3\">");

			StreamSource style = new StreamSource(in);

			TransformerFactory factory = TransformerFactory.newInstance();
			Templates template = factory.newTemplates(style);

			Transformer t = template.newTransformer();
			t.setURIResolver(new StringURIResolver().put(
					"ruleExecutionResponseContainer",
					executionResponseContainer));
			t.setParameter("privacyPoliciesExternalDocUrl", messageId);

			t.transform(new StreamSource(new StringReader(document)),
					new StreamResult(out));

			taggedDocument = out.toString();

			out.close();
			in.close();
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);

		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		}

		return taggedDocument;
	}

	/**
	 * *****************************************
	 * Encrypt document
	 * ******************************************.
	 *
	 * @param document the document
	 * @param ruleExecutionContainer the rule execution container
	 * @return the string
	 */
	public String encryptDocument(String document,
			RuleExecutionContainer ruleExecutionContainer) {

		Document xmlDocument = null;
		String xmlString = null;
		boolean encryptDoc = false;

		for (RuleExecutionResponse response : ruleExecutionContainer
				.getExecutionResponseList()) {
			if (response.getDocumentObligationPolicy().equals("ENCRYPT")) {
				encryptDoc = true;
				break;
			}
		}

		if (encryptDoc) {

			try {
				xmlDocument = XmlHelper.loadDocument(document);

				/*
				 * Get a key to be used for encrypting the element. Here we are
				 * generating an AES key.
				 */
				Key aesSymmetricKey = EncryptTool.generateDataEncryptionKey();

				/*
				 * Get a key to be used for encrypting the symmetric key. Here
				 * we are generating a DESede key.
				 */
				deSedeEncryptKey = EncryptTool.generateKeyEncryptionKey();

				String algorithmURI = XMLCipher.TRIPLEDES_KeyWrap;

				XMLCipher keyCipher = XMLCipher.getInstance(algorithmURI);
				keyCipher.init(XMLCipher.WRAP_MODE, deSedeEncryptKey);
				EncryptedKey encryptedKey = keyCipher.encryptKey(xmlDocument,
						aesSymmetricKey);

				byte[] keyBytes = deSedeEncryptKey.getEncoded();

				// encrypt the contents of the document element
				Element rootElement = xmlDocument.getDocumentElement();

				encryptElement(xmlDocument, aesSymmetricKey, encryptedKey,
						rootElement);

				// Output encrypted doc to file
				FileHelper.writeDocToFile(xmlDocument, "Encrypted_C32.xml");

				xmlString = XmlHelper.converXmlDocToString(xmlDocument);
			} catch (XMLEncryptionException e) {
				throw new DS4PException(e.toString(), e);
			} catch (IOException e) {
				throw new DS4PException(e.toString(), e);
			} catch (TransformerException e) {
				throw new DS4PException(e.toString(), e);
			} catch (Exception e) {
				throw new DS4PException(e.toString(), e);
			}
		}
		return xmlString;
	}

	/**
	 * *****************************************
	 * Mask element
	 * ******************************************.
	 *
	 * @param document the document
	 * @param ruleExecutionContainer the rule execution container
	 * @return the string
	 */
	public String maskElement(String document,
			RuleExecutionContainer ruleExecutionContainer) {

		Document xmlDocument = null;
		String xmlString = null;

		try {
			xmlDocument = XmlHelper.loadDocument(document);

			/*
			 * Get a key to be used for encrypting the element. Here we are
			 * generating an AES key.
			 */
			Key aesSymmetricKey = EncryptTool.generateDataEncryptionKey();

			/*
			 * Get a key to be used for encrypting the symmetric key. Here we
			 * are generating a DESede key.
			 */
			deSedeMaskKey = EncryptTool.generateKeyEncryptionKey();
			byte[] keyBytes = deSedeMaskKey.getEncoded();

			String algorithmURI = XMLCipher.TRIPLEDES_KeyWrap;

			XMLCipher keyCipher = XMLCipher.getInstance(algorithmURI);
			keyCipher.init(XMLCipher.WRAP_MODE, deSedeMaskKey);
			EncryptedKey encryptedKey = keyCipher.encryptKey(xmlDocument,
					aesSymmetricKey);

			for (RuleExecutionResponse response : ruleExecutionContainer
					.getExecutionResponseList()) {
				if (response.getItemAction().equals("MASK")) {
					String observationId = response.getObservationId();
					String displayName = response.getDisplayName();

					// mask display Name
					String xPathExprDisplayName = "//hl7:td[.='%']/parent::hl7:tr";
					xPathExprDisplayName = xPathExprDisplayName.replace("%",
							displayName);

					Element elementToBeEncrypted = getElement(xmlDocument,
							xPathExprDisplayName);

					encryptElement(xmlDocument, aesSymmetricKey, encryptedKey,
							elementToBeEncrypted);

					// mask element contents
					String xPathExprObservationId = "//hl7:id[@root='%']/ancestor::hl7:entry";
					xPathExprObservationId = xPathExprObservationId.replace(
							"%", observationId);

					elementToBeEncrypted = getElement(xmlDocument,
							xPathExprObservationId);

					encryptElement(xmlDocument, aesSymmetricKey, encryptedKey,
							elementToBeEncrypted);
				}
			}

			FileHelper.writeDocToFile(xmlDocument, "Masked_C32.xml");
			xmlString = XmlHelper.converXmlDocToString(xmlDocument);

		} catch (XMLEncryptionException e) {
			throw new DS4PException(e.toString(), e);
		} catch (XPathExpressionException e) {
			throw new DS4PException(e.toString(), e);
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);
		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		} catch (Exception e) {
			throw new DS4PException(e.toString(), e);
		}
		return xmlString;
	}

	/**
	 * *****************************************
	 * Redact element
	 * ******************************************.
	 *
	 * @param document the document
	 * @param ruleExecutionContainer the rule execution container
	 * @return the string
	 */
	public String redactElement(String document,
			RuleExecutionContainer ruleExecutionContainer) {

		Document xmlDocument = null;
		String xmlString = null;

		try {
			xmlDocument = XmlHelper.loadDocument(document);

			for (RuleExecutionResponse response : ruleExecutionContainer
					.getExecutionResponseList()) {
				if (response.getItemAction().equals("REDACT")) {
					String observationId = response.getObservationId();
					String displayName = response.getDisplayName();

					// redact display Name
					String xPathExprDisplayName = "//hl7:td[.='%']/parent::hl7:tr";
					xPathExprDisplayName = xPathExprDisplayName.replace("%",
							displayName);

					Element elementToBeRedacted = getElement(xmlDocument,
							xPathExprDisplayName);

					elementToBeRedacted.getParentNode().removeChild(
							elementToBeRedacted);

					// mask element contents
					String xPathExprObservationId = "//hl7:id[@root='%']/ancestor::hl7:entry";
					xPathExprObservationId = xPathExprObservationId.replace(
							"%", observationId);

					elementToBeRedacted = getElement(xmlDocument,
							xPathExprObservationId);

					elementToBeRedacted.getParentNode().removeChild(
							elementToBeRedacted);

					xmlDocument.normalize();
				}
			}

			FileHelper.writeDocToFile(xmlDocument, "Redacted_C32.xml");
			xmlString = XmlHelper.converXmlDocToString(xmlDocument);
		} catch (XPathExpressionException e) {
			throw new DS4PException(e.toString(), e);
		} catch (XMLEncryptionException e) {
			throw new DS4PException(e.toString(), e);
		} catch (DOMException e) {
			throw new DS4PException(e.toString(), e);
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);
		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		} catch (Exception e) {
			throw new DS4PException(e.toString(), e);
		}

		return xmlString;
	}

	/**
	 * *********************************************************************
	 * Get element
	 * **********************************************************************.
	 *
	 * @param xmlDocument the xml document
	 * @param xPathExprDisplayName the x path expr display name
	 * @return the element
	 * @throws XPathExpressionException the x path expression exception
	 * @throws XMLEncryptionException the xML encryption exception
	 * @throws Exception the exception
	 */
	public Element getElement(Document xmlDocument, String xPathExprDisplayName)
			throws XPathExpressionException, XMLEncryptionException, Exception {

		NamespaceContext ctx = new NamespaceContext() {
			@Override
			public String getNamespaceURI(String prefix) {
				String uri;
				if (prefix.equals("hl7"))
					uri = "urn:hl7-org:v3";
				else if (prefix.equals("xenc"))
					uri = "http://www.w3.org/2001/04/xmlenc#";
				else
					uri = null;
				return uri;
			}

			@Override
			public Iterator getPrefixes(String val) {
				return null;
			}

			@Override
			public String getPrefix(String uri) {
				return null;
			}
		};

		// Create XPath instance
		XPathFactory xpathFact = XPathFactory.newInstance();
		XPath xpath = xpathFact.newXPath();
		xpath.setNamespaceContext(ctx);

		// Evaluate XPath expression against parsed document
		Node node = (Node) xpath.evaluate(xPathExprDisplayName, xmlDocument,
				XPathConstants.NODE);

		return (Element) node;
	}

	/**
	 * *********************************************************************
	 * Encrypt element
	 * **********************************************************************.
	 *
	 * @param xmlDocument the xml document
	 * @param encryptSymmetricKey the encrypt symmetric key
	 * @param encryptedKey the encrypted key
	 * @param element the element
	 * @throws XMLEncryptionException the xML encryption exception
	 * @throws Exception the exception
	 */
	private void encryptElement(Document xmlDocument, Key encryptSymmetricKey,
			EncryptedKey encryptedKey, Element element)
					throws XMLEncryptionException, Exception {

		String algorithmURI = XMLCipher.AES_128;

		XMLCipher xmlCipher = XMLCipher.getInstance(algorithmURI);
		xmlCipher.init(XMLCipher.ENCRYPT_MODE, encryptSymmetricKey);

		/*
		 * Setting keyinfo inside the encrypted data being prepared.
		 */
		EncryptedData encryptedData = xmlCipher.getEncryptedData();
		KeyInfo keyInfo = new KeyInfo(xmlDocument);
		keyInfo.add(encryptedKey);
		encryptedData.setKeyInfo(keyInfo);

		xmlCipher.doFinal(xmlDocument, element, true);
	}

	/**
	 * *****************************************
	 * Unmarshall from xml to generic object.
	 *
	 * @param <T> the generic type
	 * @param clazz the clazz
	 * @param xml the xml
	 * @return the t
	 * @throws JAXBException ******************************************
	 */
	private <T> T unmarshallFromXml(Class<T> clazz, String xml)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
		return (T) um.unmarshal(input);
	}

	/**
	 * *****************************************
	 * Read a file and return string
	 * ******************************************.
	 *
	 * @param filename the filename
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String readFile(String filename) throws IOException {
		byte[] bytes;

		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		bytes = new byte[is.available()];
		is.read(bytes);

		is.close();

		return new String(bytes);
	}
}
