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
package gov.samhsa.schemas.client;

import gov.va.ehtac.ds4p.ws.DS4PAudit;
import gov.va.ehtac.ds4p.ws.DS4PAuditService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	private final static List<String> returnedValueOfGetObligationsByMessageId = new ArrayList<String>();
	private final static String returnedValueOfGetPurposeOfUseByMessageId = "";

	@BeforeClass
	public static void setUp() {
		try {
			address = "http://localhost:9000/services/DS4PAuditService";
			ep = Endpoint.publish(address, new DS4PAuditImpl());

			returnedValueOfGetObligationsByMessageId
					.add("urn:oasis:names:tc:xspa:2.0:resource:patient:mask:HIV");
			DS4PAuditImpl.returnedValueOfGetObligationsByMessageId = returnedValueOfGetObligationsByMessageId;
			DS4PAuditImpl.returnedValueOfGetPurposeOfUseByMessageId = returnedValueOfGetPurposeOfUseByMessageId;
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
		boolean resp = wsc.updateAuthorizationEventWithExecRules(messageId,
				execRules);
		Assert.assertEquals(
				"Returned response from updateAuthorizationEventWithExecRules wrong",
				returnedValueOfUpdateAuthorizationEventWithExecRules, resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_getObligationsByMessageId() {

		String messageId = UUID.randomUUID().toString();

		AuditWebServiceClient wsc = new AuditWebServiceClient(address);
		List<String> resp = wsc.getObligationsByMessageId(messageId);
		Assert.assertEquals("Retruned response from getObligationsByMessageId",
				returnedValueOfGetObligationsByMessageId.get(0),
				resp.get(0));
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_getPurposeOfUseByMessageId() {

		String messageId = UUID.randomUUID().toString();

		AuditWebServiceClient wsc = new AuditWebServiceClient(address);
		String resp = wsc.getPurposeOfUseByMessageId(messageId);
		Assert.assertEquals(
				"Retruned response from getPurposeOfUseByMessageId",
				returnedValueOfGetPurposeOfUseByMessageId, resp);
	}

	private void validateResponseOfUpdateAuthorizationEventWithAnnotatedDoc(
			boolean resp) {
		Assert.assertEquals(
				"Returned response from updateAuthorizationEventWithAnnotatedDoc wrong",
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
