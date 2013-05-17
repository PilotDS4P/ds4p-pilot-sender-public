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
package gov.samhsa.ds4ppilot.hcs;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import gov.samhsa.ds4ppilot.hcs.audit.AuditService;
import gov.samhsa.ds4ppilot.hcs.audit.AuditServiceImpl;
import gov.samhsa.ds4ppilot.hcs.clinicallyadaptiverules.ClinicallyAdaptiveRules;
import gov.samhsa.ds4ppilot.hcs.clinicallyadaptiverules.ClinicallyAdaptiveRulesImpl;
import gov.samhsa.ds4ppilot.hcs.service.HealthcareClassificationServiceImpl;
import gov.samhsa.ds4ppilot.hcs.utils.RuleHelper;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsResponse;

import java.io.IOException;
import java.util.UUID;

import org.drools.compiler.DroolsParserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;


// TODO: Auto-generated Javadoc
/**
 * The Class HealthcareClassificationServiceTest.
 */
public class HealthcareClassificationServiceTest {
	
	/** The clinical facts. */
	private String clinicalFacts;
	
	/** The purpose of use. */
	private String purposeOfUse;
	
	/** The rule execution container. */
	private String ruleExecutionContainer;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {	
		clinicalFacts = "<FactModel><xacmlResult><pdpDecision>Permit</pdpDecision><purposeOfUse>TREAT</purposeOfUse><messageId>71fe0397-3684-4acb-9811-6416c5c77b55</messageId><homeCommunityId>2.16.840.1.113883.3.467</homeCommunityId><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law:42CFRPart2</pdpObligation><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:org:refrain-policy:NORDSLCD</pdpObligation><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:patient:redact:PSY</pdpObligation><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:patient:redact:ETH</pdpObligation></xacmlResult><ClinicalFacts><ClinicalFact><code>111880001</code><displayName>Acute HIV</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName/><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId>d11275e7-67ae-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>66214007</code><displayName>Substance Abuse Disorder</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName/><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId>e11275e7-67ae-11db-bd13-0800200c9a66b827vs52h7</observationId></ClinicalFact><ClinicalFact><code>234391009</code><displayName>Sickle Cell Anemia</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName>SNOMED</codeSystemName><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId>ab1791b0-5c71-11db-b0de-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>233604007</code><displayName>Pneumonia</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName>SNOMED</codeSystemName><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId>9d3d416d-45ab-4da1-912f-4583e0632000</observationId></ClinicalFact><ClinicalFact><code>22298006</code><displayName>Myocardial infarction</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName>SNOMED</codeSystemName><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId/></ClinicalFact><ClinicalFact><code>77386006</code><displayName>Patient currently pregnant</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName>SNOMED</codeSystemName><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId/></ClinicalFact><ClinicalFact><code>70618</code><displayName>Penicillin</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Allergies and Adverse Reactions</c32SectionTitle><c32SectionLoincCode>48765-2</c32SectionLoincCode><observationId>4adc1020-7b14-11db-9fe1-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>1191</code><displayName>Aspirin</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Allergies and Adverse Reactions</c32SectionTitle><c32SectionLoincCode>48765-2</c32SectionLoincCode><observationId>eb936011-7b17-11db-9fe1-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>2670</code><displayName>Codeine</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Allergies and Adverse Reactions</c32SectionTitle><c32SectionLoincCode>48765-2</c32SectionLoincCode><observationId>c3df3b60-7b18-11db-9fe1-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>6736007</code><displayName>moderate</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName>SNOMED</codeSystemName><c32SectionTitle>Allergies and Adverse Reactions</c32SectionTitle><c32SectionLoincCode>48765-2</c32SectionLoincCode><observationId/></ClinicalFact><ClinicalFact><code>307782</code><displayName>Albuterol 0.09 MG/ACTUAT inhalant solution</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Medications</c32SectionTitle><c32SectionLoincCode>10160-0</c32SectionLoincCode><substanceAdministrationId>cdbd33f0-6cde-11db-9fe1-0800200c9a66</substanceAdministrationId></ClinicalFact><ClinicalFact><code>309362</code><displayName>Clopidogrel 75 MG oral tablet</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Medications</c32SectionTitle><c32SectionLoincCode>10160-0</c32SectionLoincCode><substanceAdministrationId>cdbd5b05-6cde-11db-9fe1-0800200c9a66</substanceAdministrationId></ClinicalFact><ClinicalFact><code>430618</code><displayName>Metoprolol 25 MG oral tablet</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Medications</c32SectionTitle><c32SectionLoincCode>10160-0</c32SectionLoincCode><substanceAdministrationId>cdbd5b01-6cde-11db-9fe1-0800200c9a66</substanceAdministrationId></ClinicalFact><ClinicalFact><code>312615</code><displayName>Prednisone 20 MG oral tablet</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Medications</c32SectionTitle><c32SectionLoincCode>10160-0</c32SectionLoincCode><substanceAdministrationId>cdbd5b03-6cde-11db-9fe1-0800200c9a66</substanceAdministrationId></ClinicalFact><ClinicalFact><code>197454</code><displayName>Cephalexin 500 MG oral tablet</displayName><codeSystem>2.16.840.1.113883.6.88</codeSystem><codeSystemName>RxNorm</codeSystemName><c32SectionTitle>Medications</c32SectionTitle><c32SectionLoincCode>10160-0</c32SectionLoincCode><substanceAdministrationId>cdbd5b07-6cde-11db-9fe1-0800200c9a66</substanceAdministrationId></ClinicalFact><ClinicalFact><code>30313-1</code><displayName>HGB</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>107c2dc0-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>33765-9</code><displayName>WBC</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>8b3fa370-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>26515-7</code><displayName>PLT</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>80a6c740-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>2951-2</code><displayName>NA</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>a40027e1-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>2823-3</code><displayName>K</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>a40027e2-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>2075-0</code><displayName>CL</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>a40027e3-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>1963-8</code><displayName>HCO3</displayName><codeSystem>2.16.840.1.113883.6.1</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId>a40027e4-67a5-11db-bd13-0800200c9a66</observationId></ClinicalFact><ClinicalFact><code>43789009</code><displayName>CBC WO DIFFERENTIAL</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName/><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId/></ClinicalFact><ClinicalFact><code>20109005</code><displayName>LYTES</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName>SNOMED CT</codeSystemName><c32SectionTitle>Diagnostic Results</c32SectionTitle><c32SectionLoincCode>30954-2</c32SectionLoincCode><observationId/></ClinicalFact></ClinicalFacts></FactModel>";
		purposeOfUse = "TREAT";
		ruleExecutionContainer = "<ruleExecutionContainer><executionResponseList><executionResponse><c32SectionLoincCode>11450-4</c32SectionLoincCode><c32SectionTitle>Problems</c32SectionTitle><code>66214007</code><codeSystemName>SNOMED CT</codeSystemName><displayName>Substance Abuse Disorder</displayName><documentObligationPolicy>ENCRYPT</documentObligationPolicy><documentRefrainPolicy>NORDSLCD</documentRefrainPolicy><impliedConfSection>R</impliedConfSection><itemAction>REDACT</itemAction><observationId>e11275e7-67ae-11db-bd13-0800200c9a66b827vs52h7</observationId><sensitivity>ETH</sensitivity><USPrivacyLaw>42CFRPart2</USPrivacyLaw></executionResponse><executionResponse><c32SectionLoincCode>11450-4</c32SectionLoincCode><c32SectionTitle>Problems</c32SectionTitle><code>111880001</code><codeSystemName>SNOMED CT</codeSystemName><displayName>Acute HIV</displayName><documentObligationPolicy>ENCRYPT</documentObligationPolicy><documentRefrainPolicy>NORDSLCD</documentRefrainPolicy><impliedConfSection>R</impliedConfSection><itemAction>MASK</itemAction><observationId>d11275e7-67ae-11db-bd13-0800200c9a66</observationId><sensitivity>HIV</sensitivity><USPrivacyLaw>42CFRPart2</USPrivacyLaw></executionResponse></executionResponseList></ruleExecutionContainer>";
	}
	
	//@Ignore("This test should be configured to run as an integration test.")
	/**
	 * Assert and execute clinical facts test.
	 */
	@Test
	public void AssertAndExecuteClinicalFactsTest() {
		final String endpointAddressForClinicallyAdaptiveRules = "http://174.78.146.228:8080/DS4PACSServices/DS4PClinicallyAdaptiveRulesInterface";
		final String endpointAddressForAuditServcie = "http://174.78.146.228:8080/DS4PACSServices/DS4PAuditService";
		HealthcareClassificationServiceImpl droolsAgentService = new HealthcareClassificationServiceImpl(new ClinicallyAdaptiveRulesImpl(endpointAddressForClinicallyAdaptiveRules), new AuditServiceImpl(endpointAddressForAuditServcie));		
		AssertAndExecuteClinicalFactsResponse response = droolsAgentService.assertAndExecuteClinicalFacts(clinicalFacts);
		String ruleExecutionContainerXML = response.getRuleExecutionResponseContainer();
		System.out.println("\n\n" + ruleExecutionContainerXML);
		
		Assert.assertNotNull(ruleExecutionContainerXML); 
	}	
	
	
	/**
	 * Test healthcare classification_works when at least one obligation exists.
	 *
	 * @throws DroolsParserException the drools parser exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testHealthcareClassification_worksWhenAtLeastOneObligationExists() throws DroolsParserException, IOException
	{
		AuditService auditServiceMock = mock(AuditService.class);
		ClinicallyAdaptiveRules clinicallyAdaptiveRulesMock = mock(ClinicallyAdaptiveRules.class);
		HealthcareClassificationServiceImpl droolsAgentService = new HealthcareClassificationServiceImpl(clinicallyAdaptiveRulesMock, auditServiceMock); 
		
		when(auditServiceMock.updateAuthorizationEventWithExecRules(anyString(), anyString())).thenReturn(true);
		when(clinicallyAdaptiveRulesMock.getCASRuleSetStringByPOUObligationsAndHomeCommunityId(anyString(), Matchers.anyListOf(String.class), anyString(), anyString())).thenReturn(RuleHelper.convertDrlToXml("rule1.drl"));
		
		AssertAndExecuteClinicalFactsResponse response = droolsAgentService.assertAndExecuteClinicalFacts(clinicalFacts);
		
		Assert.assertEquals(response.getRuleExecutionResponseContainer(), ruleExecutionContainer);
	}	
}
