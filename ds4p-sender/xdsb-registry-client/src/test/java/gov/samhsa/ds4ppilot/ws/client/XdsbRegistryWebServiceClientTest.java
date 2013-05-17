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
package gov.samhsa.ds4ppilot.ws.client;

import gov.samhsa.ds4p.xdsbregistry.DocumentRegistryService;
import ihe.iti.xds_b._2007.XDSRegistry;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


// TODO: Auto-generated Javadoc
/**
 * The Class XdsbRegistryWebServiceClientTest.
 */
public class XdsbRegistryWebServiceClientTest {

	/** The ep. */
	protected static Endpoint ep;
	
	/** The address. */
	protected static String address;
	
	/** The returned value of registry stored query. */
	private static AdhocQueryResponse returnedValueOfRegistryStoredQuery = new AdhocQueryResponse();

	/**
	 * Sets the up.
	 */
	@BeforeClass
	public static void setUp() {
		try {
			address = "http://localhost:9000/services/xdsregistryb";
			ep = Endpoint.publish(address, new XdsbRegistryServiceImpl());

			XdsbRegistryServiceImpl.returnedValueOfRegistryStoredQuery = returnedValueOfRegistryStoredQuery;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	// Test if the stub web service activate properly
	/**
	 * Test stub web service works.
	 */
	@Test
	public void testStubWebServiceWorks() {
		AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();

		Object response = createPort().registryStoredQuery(adhocQueryRequest);
		validateResponseOfRetrieveDocumentSetRequest(response);
	}

	// Test if the SOAP client calling the stub web service correctly?

	/**
	 * Test ws client soap call works_retrieve document set request.
	 */
	@Test
	public void testWSClientSOAPCallWorks_retrieveDocumentSetRequest() {
		AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();

		XdsbRegistryWebServiceClient wsc = new XdsbRegistryWebServiceClient(
				address);
		Object resp = wsc.registryStoredQuery(adhocQueryRequest);
		validateResponseOfRetrieveDocumentSetRequest(resp);
	}

	/**
	 * Validate response of retrieve document set request.
	 *
	 * @param resp the resp
	 */
	private void validateResponseOfRetrieveDocumentSetRequest(Object resp) {
		Assert.assertNotNull(resp);
	}

	/**
	 * Creates the port.
	 *
	 * @return the xDS registry
	 */
	private XDSRegistry createPort() {
		final URL WSDL_LOCATION = this.getClass().getClassLoader()
				.getResource("XDS.b_registry.net.wsdl");
		final QName SERVICE = new QName("http://samhsa.gov/ds4p/XDSbRegistry/",
				"DocumentRegistryService");

		XDSRegistry port = new DocumentRegistryService(WSDL_LOCATION, SERVICE)
				.getXDSRegistryHTTPEndpoint();

		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);

		return port;
	}
}
