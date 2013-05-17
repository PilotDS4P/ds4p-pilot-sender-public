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
package gov.samhsa.ds4ppilot.documentprocessor.ws;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.samhsa.ds4ppilot.contract.documentprocessor.ProcessDocumentService;
import gov.samhsa.ds4ppilot.contract.documentprocessor.ProcessDocumentServicePortType;
import gov.samhsa.ds4ppilot.documentprocessor.DocumentProcessor;
import gov.samhsa.ds4ppilot.schema.documentprocessor.ProcessDocumentRequest;
import gov.samhsa.ds4ppilot.schema.documentprocessor.ProcessDocumentResponse;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentProcessorImplEndpointTest.
 */
public class DocumentProcessorImplEndpointTest {
	
	/** The ep. */
	private static Endpoint ep;

	/** The process document response. */
	private static ProcessDocumentResponse processDocumentResponse;
	
	/** The wsdl url. */
	private static URL wsdlURL;
	
	/** The address. */
	private static String address;

	/** The service name. */
	private static QName serviceName;

	/** The document processor mock. */
	private static DocumentProcessor documentProcessorMock = mock(DocumentProcessor.class);

	static {
		serviceName = new QName(
				"http://www.samhsa.gov/ds4ppilot/contract/documentprocessor",
				"ProcessDocumentService");
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		address = "http://localhost:9000/services/processDocumentservice";
		wsdlURL = new URL(address + "?wsdl");
		processDocumentResponse = new ProcessDocumentResponse();

		processDocumentResponse
		.setMaskedDocument("<ClinicalDocument></ClinicalDocument>");
		processDocumentResponse.setProcessedDocument(null);

		ep = Endpoint.publish(address, new ProcessDocumentServiceImpl(documentProcessorMock));
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

	/**
	 * Process document works with generated service and sei.
	 *
	 * @throws MalformedURLException the malformed url exception
	 */
	@Test
	public void processDocumentWorksWithGeneratedServiceAndSei()
			throws MalformedURLException {		
		ProcessDocumentService service = new ProcessDocumentService(wsdlURL, serviceName);
		ProcessDocumentServicePortType port = service.getProcessDocumentPort();
		ProcessDocumentRequest request = new ProcessDocumentRequest();

		when(documentProcessorMock.processDocument(null, null, false, false,
				null, null, null))
				.thenReturn(processDocumentResponse);

		ProcessDocumentResponse response = port.processDocument(request);

		validateResponse(response);
	}

	/**
	 * Validate response.
	 *
	 * @param resp the resp
	 */
	private void validateResponse(ProcessDocumentResponse resp) {
		System.out.println("resp.getMaskedDocument(): "
				+ resp.getMaskedDocument());
		Assert.assertEquals("<ClinicalDocument></ClinicalDocument>",
				processDocumentResponse.getMaskedDocument(),
				resp.getMaskedDocument());
	}
}
