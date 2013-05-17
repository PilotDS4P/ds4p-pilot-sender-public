/*******************************************************************************
 * Copyright 2012 SAMHSA
 * 
 * Licensed under the Substance Abuse & Mental Health Services Administration (SAMHSA), you may not use this file except in compliance with the License.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.HealthcareClassificationService;
import gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.HealthcareClassificationServicePortType;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsRequest;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class HealthcareClassificationWebServiceClientTest.
 */
public class HealthcareClassificationWebServiceClientTest {
	
	/** The ep. */
	protected static Endpoint ep;
	
	/** The address. */
	protected static String address;

	/** The Constant returnedValueOfAssertAndExecuteClinicalFacts. */
	private static final AssertAndExecuteClinicalFactsResponse returnedValueOfAssertAndExecuteClinicalFacts = new AssertAndExecuteClinicalFactsResponse();
	
	/** The Constant ruleExecutionResponseContaine. */
	private static final String ruleExecutionResponseContaine = "RuleExecutionResponseContaine";

	/**
	 * Sets the up.
	 */
	@BeforeClass
	public static void setUp() {
		address = "http://localhost:9000/services/C32Service";
		ep = Endpoint.publish(address,
				new HealthcareClassificationServicePortTypeImpl());

		returnedValueOfAssertAndExecuteClinicalFacts
				.setRuleExecutionResponseContainer(ruleExecutionResponseContaine);
		HealthcareClassificationServicePortTypeImpl.returnedValueOfAssertAndExecuteClinicalFacts = returnedValueOfAssertAndExecuteClinicalFacts;
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
		AssertAndExecuteClinicalFactsResponse resp = createPort().assertAndExecuteClinicalFacts(new AssertAndExecuteClinicalFactsRequest());
		validateResponse(resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	/**
	 * Test ws client soap call works.
	 */
	@Test
	public void testWSClientSOAPCallWorks() {
		HealthcareClassificationWebServiceClient wsc = new HealthcareClassificationWebServiceClient(
				address);
		AssertAndExecuteClinicalFactsResponse resp = wsc.assertAndExecuteClinicalFacts(new AssertAndExecuteClinicalFactsRequest());
		validateResponse(resp);
	}

	/**
	 * Validate response.
	 *
	 * @param andExecuteClinicalFactsResponse the and execute clinical facts response
	 */
	private void validateResponse(
			AssertAndExecuteClinicalFactsResponse andExecuteClinicalFactsResponse) {
		Assert.assertEquals(
				"Returned AssertAndExecuteClinicalFactsResponse wrong",
				returnedValueOfAssertAndExecuteClinicalFacts
						.getRuleExecutionResponseContainer(),
				andExecuteClinicalFactsResponse
						.getRuleExecutionResponseContainer());
	}

	/**
	 * Creates the port.
	 *
	 * @return the healthcare classification service port type
	 */
	private HealthcareClassificationServicePortType createPort() {
		final URL WSDL_LOCATION = this.getClass().getClassLoader()
				.getResource("HealthcareClassificationService.wsdl");
		final QName SERVICE = new QName(
				"http://www.samhsa.gov/ds4ppilot/contract/HealthcareClassificationService",
				"HealthcareClassificationService");

		HealthcareClassificationServicePortType port = new HealthcareClassificationService(
				WSDL_LOCATION, SERVICE)
				.getHealthcareClassificationServicePort();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);
		return port;
	}
}
