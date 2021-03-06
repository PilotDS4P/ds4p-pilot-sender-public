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
package gov.samhsa.ds4ppilot.orchestrator.ws;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.samhsa.ds4ppilot.contract.securedorchestrator.SecuredFilterC32ServicePortType;
import gov.samhsa.ds4ppilot.orchestrator.SecuredOrchestrator;
import gov.samhsa.ds4ppilot.schema.securedorchestrator.RetrieveDocumentSetRequest;
import gov.samhsa.ds4ppilot.schema.securedorchestrator.RetrieveDocumentSetResponse;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import junit.framework.Assert;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mvel2.ast.AssertNode;
import org.w3c.dom.Document;

// TODO: Auto-generated Javadoc
/**
 * The Class SecuredFilterC32ServiceImplEndpointTest.
 */
public class SecuredFilterC32ServiceImplEndpointTest {

	/** The wsdl url. */
	private static URL wsdlURL;
	
	/** The address. */
	private static String address;
	
	/** The service name. */
	private static QName serviceName;
	
	/** The port name. */
	private static QName portName;

	/** The ep. */
	private static Endpoint ep;
	
	/** The retrieve document set response. */
	private static gov.samhsa.ds4ppilot.schema.securedorchestrator.RetrieveDocumentSetResponse retrieveDocumentSetResponse;
	
	/** The retrieve document set message string. */
	private static String retrieveDocumentSetMessageString;
	
	/** The document unique id. */
	private static String documentUniqueId = "16807046.11206.4380.81335.421575012145604";


	/** The secured orchrstrator mock. */
	private static SecuredOrchestrator securedOrchrstratorMock = mock(SecuredOrchestrator.class);

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		serviceName = new QName(
				"http://www.samhsa.gov/ds4ppilot/contract/securedorchestrator",
				"SecuredFilterC32Service");
		portName = new QName(
				"http://www.samhsa.gov/ds4ppilot/contract/securedorchestrator",
				"SecuredFilterC32Port");

		address = "http://localhost:9000/services/securedfilterc32service";
		wsdlURL = new URL(address + "?wsdl");

		
		retrieveDocumentSetResponse = new RetrieveDocumentSetResponse();
		retrieveDocumentSetResponse
		.setReturn("<ns3:RetrieveDocumentSetResponse xmlns:ns2=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns4=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:ns3=\"urn:ihe:iti:xds-b:2007\"> <ns2:RegistryResponse status=\"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success\"/> <ns3:DocumentResponse> <ns3:RepositoryUniqueId>1.3.6.1.4.1.21367.2010.1.2.1040</ns3:RepositoryUniqueId> <ns3:DocumentUniqueId>$uniqueId06</ns3:DocumentUniqueId> <ns3:mimeType>text/plain</ns3:mimeType> <ns3:Document>VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4NClRoZSBxdWljayBicm93biBmb3gganVtcHMgb3ZlciB0aGUgbGF6eSBkb2cuDQpUaGUgcXVpY2sgYnJvd24gZm94IGp1bXBzIG92ZXIgdGhlIGxhenkgZG9nLg0KVGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4NClRoZSBxdWljayBicm93biBmb3gganVtcHMgb3ZlciB0aGUgbGF6eSBkb2cuDQpUaGUgcXVpY2sgYnJvd24gZm94IGp1bXBzIG92ZXIgdGhlIGxhenkgZG9nLg0KVGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4NClRoZSBxdWljayBicm93biBmb3gganVtcHMgb3ZlciB0aGUgbGF6eSBkb2cuDQpUaGUgcXVpY2sgYnJvd24gZm94IGp1bXBzIG92ZXIgdGhlIGxhenkgZG9nLg0KVGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4NClRoZSBxdWljayBicm93biBmb3gganVtcHMgb3ZlciB0aGUgbGF6eSBkb2cuDQpUaGUgcXVpY2sgYnJvd24gZm94IGp1bXBzIG92ZXIgdGhlIGxhenkgZG9nLg0KVGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4NClRoZSBxdWljayBicm93biBmb3gganVtcHMgb3ZlciB0aGUgbGF6eSBkb2cuDQpUaGUgcXVpY2sgYnJvd24gZm94IGp1bXBzIG92ZXIgdGhlIGxhenkgZG9nLg==</ns3:Document> </ns3:DocumentResponse> </ns3:RetrieveDocumentSetResponse>");

		// portName is same as the value specified in FilterC32ServiceImpl class
		// @WebService annotation
		ep = Endpoint.publish(address, new SecuredFilterC32ServiceImpl(
				securedOrchrstratorMock));
	}

	/**
	 * Tear down.
	 */
	@AfterClass
	public static void tearDown() {
		try {
			ep.stop();
		} catch (Throwable t) {
			System.out.println("Error thrown: " + t.getMessage());
		}
	}

	

	/*
	 * This test uses raw Service class for service, Dispatch<SOAPMessage> for
	 * client No wsimport/wsdl2java needed. Note works with full SOAP message
	 * (Service.Mode.MESSAGE)
	 */
	/**
	 * Secured filter c32 works with raw service and dispatch of soap message.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	@Test
	public void securedFilterC32WorksWithRawServiceAndDispatchOfSOAPMessage()
			throws Exception {
		Service jaxwsService = Service.create(wsdlURL, serviceName);
		Dispatch<SOAPMessage> disp = jaxwsService.createDispatch(portName,
				SOAPMessage.class, Service.Mode.MESSAGE);
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"securedFilterC32fullSOAPMessage.xml");
		SOAPMessage reqMsg = MessageFactory.newInstance().createMessage(null,
				is);
		when(
				securedOrchrstratorMock.retrieveDocumentSetRequest(anyString(), anyString(), anyString())).thenReturn(retrieveDocumentSetResponse);

		SOAPMessage responseSOAPMessage = disp.invoke(reqMsg);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		responseSOAPMessage.writeTo(out);
		String responseSOAPMessageString = new String(out.toByteArray());

		Assert.assertNotNull(responseSOAPMessageString);
	}	
}
