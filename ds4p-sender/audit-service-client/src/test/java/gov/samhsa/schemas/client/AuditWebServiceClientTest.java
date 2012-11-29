package gov.samhsa.schemas.client;

import gov.va.ehtac.ds4p.ws.DS4PAudit;
import gov.va.ehtac.ds4p.ws.DS4PAuditService;

import java.net.URL;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuditWebServiceClientTest {
	protected static Endpoint ep;
	protected static String address;

	private final static boolean returnedValueOfUpdateAuthorizationEventWithAnnotatedDoc = false;
	private final static boolean returnedValueOfUpdateAuthorizationEventWithExecRules = false;

	@BeforeClass
	public static void setUp() {
		try {
			address = "http://localhost:9000/services/DS4PAuditService";
			ep = Endpoint.publish(address, new DS4PAuditImpl());
			
			DS4PAuditImpl.returnedValueOfUpdateAuthorizationEventWithAnnotatedDoc = returnedValueOfUpdateAuthorizationEventWithAnnotatedDoc;
			DS4PAuditImpl.returnedValueOfupdateAuthorizationEventWithExecRules = returnedValueOfUpdateAuthorizationEventWithExecRules;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown() {
		try {
			ep.stop();
		} catch (Throwable t) {
			System.out.println("Error thrown: " + t.getMessage());
		}
	}

	// Test if the stub web service activate properly
	@Test
	public void testStubWebServiceWorks() {
		String document = "<ClinicalDocument></ClinicalDocument>";
		String messageId = UUID.randomUUID().toString();

		boolean resp = createPort().updateAuthorizationEventWithAnnotatedDoc(
				messageId, document);
		validateResponseOfUpdateAuthorizationEventWithAnnotatedDoc(resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_updateAuthorizationEventWithAnnotatedDoc() {

		String document = "<ClinicalDocument></ClinicalDocument>";
		String messageId = UUID.randomUUID().toString();

		AuditWebServiceClient wsc = new AuditWebServiceClient(address);
		boolean resp = wsc.updateAuthorizationEventWithAnnotatedDoc(messageId,
				document);
		validateResponseOfUpdateAuthorizationEventWithAnnotatedDoc(resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_updateAuthorizationEventWithExecRules() {

		String execRules = "<ClinicalDocument></ClinicalDocument>";
		String messageId = UUID.randomUUID().toString();

		AuditWebServiceClient wsc = new AuditWebServiceClient(address);
		boolean resp = wsc.updateAuthorizationEventWithExecRules(messageId, execRules);
		Assert.assertEquals("Returned response from updateAuthorizationEventWithExecRules wrong",
				returnedValueOfUpdateAuthorizationEventWithExecRules, resp);
	}

	private void validateResponseOfUpdateAuthorizationEventWithAnnotatedDoc(
			boolean resp) {
		Assert.assertEquals("Returned response from updateAuthorizationEventWithAnnotatedDoc wrong",
				returnedValueOfUpdateAuthorizationEventWithAnnotatedDoc, resp);
	}

	private DS4PAudit createPort() {
		final URL WSDL_LOCATION = ClassLoader
				.getSystemResource("DS4PAuditService.wsdl");
		final QName SERVICE = new QName("http://ws.ds4p.ehtac.va.gov/",
				"DS4PAuditService");

		DS4PAudit port = new DS4PAuditService(WSDL_LOCATION, SERVICE)
				.getDS4PAuditPort();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);
		return port;
	}
}
