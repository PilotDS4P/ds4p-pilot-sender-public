package gov.samhsa.ds4ppilot.orchestrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.samhsa.ds4ppilot.common.exception.DS4PException;
import gov.samhsa.ds4ppilot.orchestrator.audit.AuditServiceImpl;
import gov.samhsa.ds4ppilot.orchestrator.c32getter.C32Getter;
import gov.samhsa.ds4ppilot.orchestrator.c32getter.C32GetterImpl;
import gov.samhsa.ds4ppilot.orchestrator.contexthandler.ContextHandler;
import gov.samhsa.ds4ppilot.orchestrator.contexthandler.ContextHandlerImpl;
import gov.samhsa.ds4ppilot.orchestrator.documentprocessor.DocumentProcessor;
import gov.samhsa.ds4ppilot.orchestrator.documentprocessor.DocumentProcessorImpl;
import gov.samhsa.ds4ppilot.orchestrator.xdsbregistry.XdsbRegistry;
import gov.samhsa.ds4ppilot.orchestrator.xdsbregistry.XdsbRegistryImpl;
import gov.samhsa.ds4ppilot.orchestrator.xdsbrepository.XdsbRepository;
import gov.samhsa.ds4ppilot.orchestrator.xdsbrepository.XdsbRepositoryImpl;
import gov.samhsa.ds4ppilot.schema.documentprocessor.ProcessDocumentResponse;
import gov.samhsa.ds4ppilot.schema.orchestrator.FilterC32Response;
import gov.samhsa.ds4ppilot.schema.orchestrator.RegisteryStoredQueryResponse;
import gov.samhsa.ds4ppilot.ws.client.XdsbRegistryWebServiceClient;
import gov.va.ehtac.ds4p.ws.EnforcePolicy;
import gov.va.ehtac.ds4p.ws.EnforcePolicy.Xsparesource;
import gov.va.ehtac.ds4p.ws.EnforcePolicy.Xspasubject;
import gov.va.ehtac.ds4p.ws.EnforcePolicyResponse.Return;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.hl7.v3.Device;
import org.hl7.v3.Id;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent.Subject1;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent.Subject1.Patient;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent.Subject1.Patient.PatientPerson;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent.Subject1.Patient.PatientPerson.Addr;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent.Subject1.Patient.PatientPerson.BirthTime;
import org.hl7.v3.PatientIdentityFeedRequestType.ControlActProcess.Subject.RegistrationEvent.Subject1.Patient.PatientPerson.Name;
import org.hl7.v3.PatientIdentityFeedRequestType.Receiver;
import org.hl7.v3.PatientIdentityFeedRequestType.Sender;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class SecuredOrchestratorImplTest {

	private static boolean packageXdm;
	private static String patientIdDeny;
	private static String patientIdPermit;
	private static String senderEmailAddress;
	private static String reciepientEmailAddress;

	private final static String PERMIT = "Permit";

	@Before
	public void setUp() {
		patientIdPermit = "PUI100010060001";
		patientIdDeny = "PUI100010060006";
		packageXdm = true;
		senderEmailAddress = "leo.smith@direct.obhita-stage.org";
		reciepientEmailAddress = "Duane_Decouteau@direct.healthvault-stage.com";
	}

	

	@Ignore("This test should be configured to run as an integration test.")
	@Test
	public void testSamlRetrieveDocumentSetRequest() {
		final String xdsbRepositoryEndpointAddress = "http://xds-demo.feisystems.com:8080/axis2/services/xdsrepositoryb";

		final String contextHandlerEndpointAddress = "http://174.78.146.228:8080/DS4PACSServices/DS4PContextHandler";
		ContextHandlerImpl contextHandler = new ContextHandlerImpl(
				contextHandlerEndpointAddress);
		
		final String endpointAddressForAuditServcie = "http://174.78.146.228:8080/DS4PACSServices/DS4PAuditService?wsdl";
		gov.samhsa.ds4ppilot.orchestrator.audit.AuditServiceImpl auditService = new AuditServiceImpl(endpointAddressForAuditServcie);

		final String endpointAddress = "http://localhost/Rem.Web/C32Service.svc";
		C32GetterImpl c32Getter = new C32GetterImpl(endpointAddress);

		final String documentProcessorEndpointAddress = "http://localhost:90/DocumentProcessor/services/processdocumentservice";
		DocumentProcessorImpl documentProcessor = new DocumentProcessorImpl(
				documentProcessorEndpointAddress);

		DataHandlerToBytesConverter dataHandlerToBytesConverter = new DataHandlerToBytesConverterImpl();

		XdsbRepositoryImpl xdsbRepository = new XdsbRepositoryImpl(
				xdsbRepositoryEndpointAddress);

		final String xdsbRegistryEndpointAddress = "http://xds-demo.feisystems.com:8080/axis2/services/xdsregistryb";
		XdsbRegistryImpl xdsbRegistry = new XdsbRegistryImpl(
				xdsbRegistryEndpointAddress);

		SecuredOrchestratorImpl securedOrchestrator = new SecuredOrchestratorImpl(contextHandler,
				c32Getter, documentProcessor, auditService, dataHandlerToBytesConverter,
				xdsbRepository, xdsbRegistry);

		securedOrchestrator.setSubjectPurposeOfUse("TREAT");
		securedOrchestrator.setSubjectLocality("2.16.840.1.113883.3.467");
		securedOrchestrator.setOrganization("SAMHSA");
		securedOrchestrator.setOrganizationId("FEiSystems");

		securedOrchestrator.setResourceName("NwHINDirectSend");
		securedOrchestrator.setResourceType("C32");
		securedOrchestrator.setResourceAction("Execute");

		gov.samhsa.ds4ppilot.schema.securedorchestrator.RetrieveDocumentSetResponse response = securedOrchestrator
				.retrieveDocumentSetRequest("16807046.11206.4380.81335.421575012145604"/*"88101412251.133129.4131014.8141111.159001521200914"*/, 
						UUID.randomUUID().toString());

		assertNotNull(response);
	}

	@Ignore("This test should be configured to run as an integration test.")
	@Test
	public void testSamlRegisteryStoredQueryRequest() {
		final String xdsbRepositoryEndpointAddress = "http://xds-demo.feisystems.com:8080/axis2/services/xdsrepositoryb";
		
		final String endpointAddressForAuditServcie = "http://174.78.146.228:8080/DS4PACSServices/DS4PAuditService";
		gov.samhsa.ds4ppilot.orchestrator.audit.AuditServiceImpl auditService = new AuditServiceImpl(endpointAddressForAuditServcie);

		final String contextHandlerEndpointAddress = "http://174.78.146.228:8080/DS4PACSServices/DS4PContextHandler";
		ContextHandlerImpl contextHandler = new ContextHandlerImpl(
				contextHandlerEndpointAddress);

		final String endpointAddress = "http://localhost/Rem.Web/C32Service.svc";
		C32GetterImpl c32Getter = new C32GetterImpl(endpointAddress);

		final String documentProcessorEndpointAddress = "http://localhost:90/DocumentProcessor/services/processdocumentservice";
		DocumentProcessorImpl documentProcessor = new DocumentProcessorImpl(
				documentProcessorEndpointAddress);

		DataHandlerToBytesConverter dataHandlerToBytesConverter = new DataHandlerToBytesConverterImpl();

		XdsbRepositoryImpl xdsbRepository = new XdsbRepositoryImpl(
				xdsbRepositoryEndpointAddress);

		final String xdsbRegistryEndpointAddress = "http://xds-demo.feisystems.com:8080/axis2/services/xdsregistryb";
		XdsbRegistryImpl xdsbRegistry = new XdsbRegistryImpl(
				xdsbRegistryEndpointAddress);

		SecuredOrchestratorImpl securedOrchestrator = new SecuredOrchestratorImpl(contextHandler,
				c32Getter, documentProcessor, auditService, dataHandlerToBytesConverter,
				xdsbRepository, xdsbRegistry);

		securedOrchestrator.setSubjectPurposeOfUse("TREAT");
		securedOrchestrator.setSubjectLocality("2.16.840.1.113883.3.467");
		securedOrchestrator.setOrganization("SAMHSA");
		securedOrchestrator.setOrganizationId("FEiSystems");

		securedOrchestrator.setResourceName("NwHINDirectSend");
		securedOrchestrator.setResourceType("C32");
		securedOrchestrator.setResourceAction("Execute");		

		gov.samhsa.ds4ppilot.schema.securedorchestrator.RegisteryStoredQueryResponse response = securedOrchestrator
				.registeryStoredQueryRequest(
						"PUI100010060001^^^&2.16.840.1.113883.3.467&ISO", UUID.randomUUID().toString());

		assertNotNull(response);

	}	
	

	@SuppressWarnings("unused")
	private static void displayC32(String xml) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		Document xmlDocument = null;
		try {
			xmlDocument = loadXmlFrom(xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(
					new OutputStreamWriter(System.out, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\n\r");
	}

	private static Document loadXmlFrom(String xml) throws Exception {
		InputSource is = new InputSource(new StringReader(xml));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		return doc;
	}

	/**
	 * @param c32Response
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void writePackageToFile(FilterC32Response c32Response)
			throws IOException, FileNotFoundException {

		byte[] bytes = c32Response.getFilteredStreamBody();

		FileOutputStream fos = new FileOutputStream("xdm.zip");
		try {
			fos.write(bytes);
		} finally {
			fos.close();
		}
	}

	private String getXmlFromXmlFile(String xmlFileNameInResources) {
		InputStream in = null;
		BufferedReader br = null;
		StringBuilder c32Document = new StringBuilder();

		try {
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(xmlFileNameInResources);

			br = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = br.readLine()) != null) {
				c32Document.append(line);
			}
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);
		} finally {
			try {
				br.close();
				in.close();
			} catch (IOException e) {
				// do nothing here
			}
		}

		return c32Document.toString();
	}
}
