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
package gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.springframework.util.StringUtils;

import gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.HealthcareClassificationService;
import gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.HealthcareClassificationServicePortType;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsRequest;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class HealthcareClassificationWebServiceClient.
 */
public class HealthcareClassificationWebServiceClient {
	
	/** The endpoint address. */
	private String endpointAddress;

	/**
	 * Instantiates a new healthcare classification web service client.
	 *
	 * @param endpointAddress the endpoint address
	 */
	public HealthcareClassificationWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	/**
	 * Assert and execute clinical facts.
	 *
	 * @param andExecuteClinicalFactsRequest the and execute clinical facts request
	 * @return the assert and execute clinical facts response
	 */
	public AssertAndExecuteClinicalFactsResponse assertAndExecuteClinicalFacts(
			AssertAndExecuteClinicalFactsRequest andExecuteClinicalFactsRequest) {
		HealthcareClassificationServicePortType port;
		if (StringUtils.hasText(this.endpointAddress)) {
			port = createPort(endpointAddress);
		} else {
			// Using default endpoint address defined in the wsdl:port of wsdl
			// file
			port = createPort();
		}

		return assertAndExecuteClinicalFacts(port,
				andExecuteClinicalFactsRequest);
	}

	/**
	 * Assert and execute clinical facts.
	 *
	 * @param port the port
	 * @param request the request
	 * @return the assert and execute clinical facts response
	 */
	private AssertAndExecuteClinicalFactsResponse assertAndExecuteClinicalFacts(
			HealthcareClassificationServicePortType port,
			AssertAndExecuteClinicalFactsRequest request) {
		return port.assertAndExecuteClinicalFacts(request);
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
		return port;
	}

	/**
	 * Creates the port.
	 *
	 * @param endpointAddress the endpoint address
	 * @return the healthcare classification service port type
	 */
	private HealthcareClassificationServicePortType createPort(
			String endpointAddress) {
		HealthcareClassificationServicePortType port = createPort();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpointAddress);
		return port;
	}
}
