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
package gov.samhsa.ds4ppilot.hcs.service;

import gov.samhsa.ds4ppilot.hcs.audit.AuditService;
import gov.samhsa.ds4ppilot.hcs.beans.ClinicalFact;
import gov.samhsa.ds4ppilot.hcs.beans.FactModel;
import gov.samhsa.ds4ppilot.common.beans.RuleExecutionContainer;
import gov.samhsa.ds4ppilot.common.exception.DS4PException;
import gov.samhsa.ds4ppilot.hcs.clinicallyadaptiverules.ClinicallyAdaptiveRules;
import gov.samhsa.ds4ppilot.schema.healthcareclassificationservice.AssertAndExecuteClinicalFactsResponse;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.rule.Rule;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * The Class HealthcareClassificationServiceImpl.
 */
public class HealthcareClassificationServiceImpl implements
		HealthcareClassificationService {

	/** The knowledge session. */
	private StatefulKnowledgeSession session;
	
	/** The knowledge base. */
	private KnowledgeBase knowledgeBase;
	
	/** The clinically adaptive rules. */
	private ClinicallyAdaptiveRules clinicallyAdaptiveRules;
	
	/** The audit service. */
	private AuditService auditService;
	
	/** The fired rule names. */
	private String firedRuleNames = "";

	/**
	 * Instantiates a new healthcare classification service implementation.
	 *
	 * @param clinicallyAdaptiveRules the clinically adaptive rules
	 * @param auditService the audit service
	 */
	public HealthcareClassificationServiceImpl(
			ClinicallyAdaptiveRules clinicallyAdaptiveRules,
			AuditService auditService) {
		super();
		this.clinicallyAdaptiveRules = clinicallyAdaptiveRules;
		this.auditService = auditService;
	}

	/**
	 * Creates the stateful knowledge session.
	 *
	 * @param purposeOfUse the purpose of use
	 * @param pdpObligations the PDP obligations
	 * @param communityId the community id
	 * @param messageId the message id
	 */
	private void createStatefulKnowledgeSession(String purposeOfUse,
			List<String> pdpObligations, String communityId, String messageId) {

		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
					.newKnowledgeBuilder();

			String casRules = clinicallyAdaptiveRules
					.getCASRuleSetStringByPOUObligationsAndHomeCommunityId(
							purposeOfUse, pdpObligations, communityId,
							messageId);
			
			kbuilder.add(
					ResourceFactory.newByteArrayResource(casRules.getBytes()),
					ResourceType.DRL);

			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					System.err.println(error);
				}
			}

			knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
			knowledgeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());

			session = knowledgeBase.newStatefulKnowledgeSession();
			session.setGlobal("ruleExecutionContainer",
					new RuleExecutionContainer());
		} catch (Exception e) {
			throw new DS4PException(e.toString(), e);
		}

	}

	/* (non-Javadoc)
	 * @see gov.samhsa.ds4ppilot.hcs.service.HealthcareClassificationService#assertAndExecuteClinicalFacts(String)
	 */
	@Override
	public AssertAndExecuteClinicalFactsResponse assertAndExecuteClinicalFacts(
			String factModelXmlString) {
		RuleExecutionContainer executionResponseContainer = null;
		FactModel factModel = new FactModel();
		AssertAndExecuteClinicalFactsResponse assertAndExecuteResponse = new AssertAndExecuteClinicalFactsResponse();

		StringWriter executionResponseContainerXML = new StringWriter();

		System.out.println("factModelXmlString: " + factModelXmlString);

		try {
			// unmarshall factmodel
			JAXBContext jaxbContext = JAXBContext.newInstance(FactModel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ByteArrayInputStream input = new ByteArrayInputStream(
					factModelXmlString.getBytes());
			factModel = (FactModel) jaxbUnmarshaller.unmarshal(input);

			createStatefulKnowledgeSession(factModel.getXacmlResult()
					.getSubjectPurposeOfUse(), factModel.getXacmlResult()
					.getPdpObligations(), factModel.getXacmlResult()
					.getHomeCommunityId(), factModel.getXacmlResult()
					.getMessageId());

			session.insert(factModel.getXacmlResult());
			for (ClinicalFact clinicalFact : factModel.getClinicalFactList()) {
				session.insert(clinicalFact);
			}

			session.addEventListener(new DefaultAgendaEventListener() {
				@Override
				public void afterActivationFired(AfterActivationFiredEvent event) {
					super.afterActivationFired(event);
					final Rule rule = event.getActivation().getRule();
					addRuleName(rule.getName());

				}
			});

			session.fireAllRules();

			// log fired rules
			auditService.updateAuthorizationEventWithExecRules(factModel
					.getXacmlResult().getMessageId(), firedRuleNames);

			executionResponseContainer = (RuleExecutionContainer) session
					.getGlobal("ruleExecutionContainer");

			// Marshal rule execution response
			jaxbContext = JAXBContext.newInstance(RuleExecutionContainer.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration",
					Boolean.FALSE);
			marshaller.marshal(executionResponseContainer,
					executionResponseContainerXML);
		} catch (PropertyException e) {
			throw new DS4PException(e.toString(), e);
		} catch (JAXBException e) {
			throw new DS4PException(e.toString(), e);
		} catch (Exception e) {
			throw new DS4PException(e.toString(), e);
		} finally {
			firedRuleNames = "";
			session.dispose();
		}
		assertAndExecuteResponse
				.setRuleExecutionResponseContainer(executionResponseContainerXML
						.toString());

		return assertAndExecuteResponse;
	}

	/**
	 * Adds the rule name.
	 *
	 * @param ruleName the rule name
	 */
	private void addRuleName(String ruleName) {
		firedRuleNames = (!firedRuleNames.equals("")) ? firedRuleNames + ", "
				+ ruleName : ruleName;
	}

}
