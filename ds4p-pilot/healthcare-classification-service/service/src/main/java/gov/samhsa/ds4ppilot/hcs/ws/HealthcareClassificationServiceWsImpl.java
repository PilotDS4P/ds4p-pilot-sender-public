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
package gov.samhsa.ds4ppilot.hcs.ws;

import gov.samhsa.ds4ppilot.hcs.service.HealthcareClassificationService;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsRequest;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsResponse;

import javax.jws.WebService;
import gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.*;

/**
 * The Class HealthcareClassificationServiceWsImpl.
 */
@WebService(targetNamespace = "http://www.samhsa.gov/ds4ppilot/contract/HealthcareClassificationService", 
portName = "HealthcareClassificationServicePort", serviceName = "HealthcareClassificationService", 
endpointInterface = "gov.samhsa.ds4ppilot.contract.healthcareclassificationservice.HealthcareClassificationServicePortType")
public class HealthcareClassificationServiceWsImpl implements
		HealthcareClassificationServicePortType {

	/** The healthcare classification service. */
	private HealthcareClassificationService healthcareClassificationService;

	/**
	 * Instantiates a new healthcare classification service ws implementation.
	 */
	public HealthcareClassificationServiceWsImpl() {
	}

	/**
	 * Instantiates a new healthcare classification service ws implementation.
	 *
	 * @param healthcareClassificationService the healthcare classification service
	 */
	public HealthcareClassificationServiceWsImpl(
			HealthcareClassificationService healthcareClassificationService) {

		this.healthcareClassificationService = healthcareClassificationService;
	}

	/**
	 * Assert and execute clinical facts.
	 *
	 * @param parameters the parameters
	 * @return the assert and execute clinical facts response
	 */
	@Override
	public AssertAndExecuteClinicalFactsResponse assertAndExecuteClinicalFacts(
			AssertAndExecuteClinicalFactsRequest parameters) {
		AssertAndExecuteClinicalFactsResponse response = new AssertAndExecuteClinicalFactsResponse();

		response = healthcareClassificationService
				.assertAndExecuteClinicalFacts(parameters
						.getClinicalFactXmlString());
		return response;
	}
	
	/**
	 * Gets the healthcare classification service.
	 *
	 * @return the healthcare classification service
	 */
	public HealthcareClassificationService getHealthcareClassificationService() {
		return healthcareClassificationService;
	}

	/**
	 * Sets the healthcare classification service.
	 *
	 * @param healthcareClassificationService the new healthcare classification service
	 */
	public void setHealthcareClassificationService(
			HealthcareClassificationService healthcareClassificationService) {
		this.healthcareClassificationService = healthcareClassificationService;
	}
}
