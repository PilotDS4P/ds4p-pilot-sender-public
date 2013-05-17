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

import gov.samhsa.schemas.c32service.C32Service;
import gov.samhsa.schemas.c32service.IC32Service;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class C32WebServiceClientTest {

	protected static Endpoint ep;
	protected static String address;
	
	private static final String returnedValueOfGetC32 = "C32";

	@BeforeClass
	public static void setUp() {
		address = "http://localhost:9000/services/C32Service";
		ep = Endpoint.publish(address, new IC32ServiceImpl());
		IC32ServiceImpl.returnedValueOfGetC32 = returnedValueOfGetC32;
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
		final String patientId = "";
		
		String resp = createPort().getC32(patientId);
		validateResponse(resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks() {
		final String patientId = "";

		C32WebServiceClient wsc = new C32WebServiceClient(address);
		String resp = wsc.getC32(patientId);
		validateResponse(resp);
	}

	private void validateResponse(String resp) {
		Assert.assertEquals("Returned C32 wrong", returnedValueOfGetC32, resp);
	}

	private IC32Service createPort() {
		final URL WSDL_LOCATION = ClassLoader
				.getSystemResource("C32Service.wsdl");
		final QName SERVICE = new QName("http://schemas.samhsa.gov/c32service",
				"C32Service");

		IC32Service port = new C32Service(WSDL_LOCATION, SERVICE)
				.getBasicHttpBindingIC32Service();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);
		return port;
	}
}
