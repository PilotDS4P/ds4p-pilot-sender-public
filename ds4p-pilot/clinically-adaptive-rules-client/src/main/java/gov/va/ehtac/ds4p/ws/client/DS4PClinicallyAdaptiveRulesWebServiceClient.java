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
package gov.va.ehtac.ds4p.ws.client;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.springframework.util.StringUtils;

import gov.va.ehtac.ds4p.ws.Clinicaltagrule;
import gov.va.ehtac.ds4p.ws.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.ds4p.ws.OrganizationalPolicy;

/**
 * The Class DS4PClinicallyAdaptiveRulesWebServiceClient.
 */
public class DS4PClinicallyAdaptiveRulesWebServiceClient {

	/** The endpoint address. */
	private String endpointAddress;

	/**
	 * Instantiates a new d s4 p clinically adaptive rules web service client.
	 *
	 * @param endpointAddress the endpoint address
	 */
	public DS4PClinicallyAdaptiveRulesWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	/**
	 * Gets the all organizational policy.
	 *
	 * @return the all organizational policy
	 */
	public List<OrganizationalPolicy> getAllOrganizationalPolicy() {
		DS4PClinicallyAdaptiveRulesInterface port = createPort();

		return port.getAllOrganizationalPolicy();
	}

	/**
	 * Sets the clinical domain tagging rule.
	 *
	 * @param taggingRule the tagging rule
	 * @return the boolean
	 */
	public Boolean setClinicalDomainTaggingRule(Clinicaltagrule taggingRule) {
		DS4PClinicallyAdaptiveRulesInterface port = createPort();

		return port.setClinicalDomainTaggingRule(taggingRule);
	}

	/**
	 * Gets the CAS rule set string by pou obligations and home community id.
	 *
	 * @param pou the pou
	 * @param obligations the obligations
	 * @param homeCommunityId the home community id
	 * @param messageId the message id
	 * @return the CAS rule set string by pou obligations and home community id
	 */
	public String getCASRuleSetStringByPOUObligationsAndHomeCommunityId(
			String pou, List<String> obligations, String homeCommunityId,
			String messageId) {
		DS4PClinicallyAdaptiveRulesInterface port = createPort();

		String value = port.getCASRuleSetStringByPOUObligationsAndHomeCommunityId(pou,
				obligations, homeCommunityId, messageId);
		return value;
	}

	/**
	 * Gets the clinical domain rule.
	 *
	 * @param domainLoincCode the domain loinc code
	 * @return the clinical domain rule
	 */
	public Clinicaltagrule getClinicalDomainRule(String domainLoincCode) {
		DS4PClinicallyAdaptiveRulesInterface port = createPort();

		return port.getClinicalDomainRule(domainLoincCode);
	}

	/**
	 * Sets the organizational policy.
	 *
	 * @param policy the policy
	 * @return the boolean
	 */
	public Boolean setOrganizationalPolicy(OrganizationalPolicy policy) {
		DS4PClinicallyAdaptiveRulesInterface port = createPort();

		return port.setOrganizationalPolicy(policy);
	}

	/**
	 * Gets the organizational policy.
	 *
	 * @param homeCommunityId the home community id
	 * @return the organizational policy
	 */
	public OrganizationalPolicy getOrganizationalPolicy(String homeCommunityId) {
		DS4PClinicallyAdaptiveRulesInterface port = createPort();

		return port.getOrganizationalPolicy(homeCommunityId);
	}

	/**
	 * Creates the port.
	 *
	 * @return the DS4P clinically adaptive rules interface
	 */
	private DS4PClinicallyAdaptiveRulesInterface createPort() {
		final URL WSDL_LOCATION = this.getClass().getClassLoader()
				.getResource("DS4PClinicallyAdaptiveRulesInterface.wsdl");
		final QName SERVICE = new QName("http://ws.ds4p.ehtac.va.gov/",
				"DS4PClinicallyAdaptiveRulesInterface");

		DS4PClinicallyAdaptiveRulesInterface port = new DS4PClinicallyAdaptiveRulesInterface_Service(
				WSDL_LOCATION, SERVICE)
				.getDS4PClinicallyAdaptiveRulesInterfacePort();

		if (StringUtils.hasText(this.endpointAddress)) {
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		}
		return port;
	}
}
