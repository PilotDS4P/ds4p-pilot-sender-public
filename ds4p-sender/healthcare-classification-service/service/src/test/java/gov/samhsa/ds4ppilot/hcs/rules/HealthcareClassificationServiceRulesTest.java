package gov.samhsa.ds4ppilot.hcs.rules;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import gov.samhsa.ds4ppilot.common.beans.RuleExecutionContainer;
import gov.samhsa.ds4ppilot.common.beans.XacmlResult;
import gov.samhsa.ds4ppilot.hcs.beans.ClinicalFact;
import gov.samhsa.ds4ppilot.hcs.beans.FactModel;
import gov.samhsa.ds4ppilot.hcs.utils.RuleHelper;
import junit.framework.Assert;

import org.drools.FactHandle;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.common.DefaultFactHandle;
import org.drools.definition.rule.Rule;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class HealthcareClassificationServiceRulesTest {
	private KnowledgeBase knowledgeBase;
	private KnowledgeBuilder kbuilder;
	private StatefulKnowledgeSession knowledgeSession;
	private String clinicalFacts;
	private FactModel factModel = new FactModel();

	@Before
	public void setup() {
		clinicalFacts = "<FactModel><xacmlResult><pdpDecision>Permit</pdpDecision><purposeOfUse>TREAT</purposeOfUse><messageId>71fe0397-3684-4acb-9811-6416c5c77b55</messageId><homeCommunityId>2.16.840.1.113883.3.467</homeCommunityId><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law:42CFRPart2</pdpObligation><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:org:refrain-policy:NORDSLCD</pdpObligation><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:patient:redact:PSY</pdpObligation><pdpObligation>urn:oasis:names:tc:xspa:2.0:resource:patient:redact:ETH</pdpObligation></xacmlResult>"
				+ "<ClinicalFacts><ClinicalFact><code>111880001</code><displayName>Acute HIV</displayName><codeSystem>2.16.840.1.113883.6.96</codeSystem><codeSystemName/><c32SectionTitle>Problems</c32SectionTitle><c32SectionLoincCode>11450-4</c32SectionLoincCode><observationId>d11275e7-67ae-11db-bd13-0800200c9a66</observationId></ClinicalFact></ClinicalFacts></FactModel>";

		try {
			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			kbuilder.add(ResourceFactory.newByteArrayResource(RuleHelper.convertDrlToXml("rule1.drl").getBytes()), ResourceType.DRL);

			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					System.err.println(error);
				}
			}

			// load up the knowledge base
			knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
			knowledgeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());

			// create the session
			knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
			knowledgeSession.setGlobal("ruleExecutionContainer",
					new RuleExecutionContainer());

			// insert facts
			factModel = unmarshallFromXml(FactModel.class, clinicalFacts);

			knowledgeSession.insert(factModel.getXacmlResult());
			for (ClinicalFact clinicalFact : factModel.getClinicalFactList()) {
				knowledgeSession.insert(clinicalFact);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testRulesFired() throws Exception {

		// ***********************************************************
		// execute the scenario to be tested
		// ***********************************************************
		int rulesFired = knowledgeSession.fireAllRules();

		// ***********************************************************
		// verify the results
		// ***********************************************************
		Assert.assertEquals(1, rulesFired);
	}

	@Test
	public void testRulesFiredListener() throws Exception {

		// ***********************************************************
		// create the mock listeners and add them to the session
		// ***********************************************************
		AgendaEventListener agendaEventListener = mock(AgendaEventListener.class);
		knowledgeSession.addEventListener(agendaEventListener);

		// ***********************************************************
		// execute the scenario to be tested
		// ***********************************************************
		knowledgeSession.fireAllRules();

		// ***********************************************************
		// verify the results
		// ***********************************************************
		verify(agendaEventListener, times(1)).afterActivationFired(
				(AfterActivationFiredEvent) Mockito.anyObject());
	}

	@Test
	public void testCheckBindingValues() throws Exception {
		// ***********************************************************
		// create the mock listeners and add them to the session
		// ***********************************************************
		AgendaEventListener agendaEventListener = mock(AgendaEventListener.class);
		knowledgeSession.addEventListener(agendaEventListener);

		// ***********************************************************
		// execute the scenario to be tested
		// ***********************************************************
		knowledgeSession.fireAllRules();

		// ***********************************************************
		// verify the results
		// ***********************************************************
		// create and argument captor for AfterActivationFiredEvent
		ArgumentCaptor<AfterActivationFiredEvent> argumentCaptor =  ArgumentCaptor.forClass(AfterActivationFiredEvent.class);
				
		// check that the method was called twice and capture the arguments
		verify(agendaEventListener).afterActivationFired(
				argumentCaptor.capture());

		// check the rule name for the first rule to fire
		AfterActivationFiredEvent first = argumentCaptor.getValue();
		Assert.assertEquals(first.getActivation().getRule().getName(),
				"Clinical Rule Acute HIV NO PATIENT CONSTRAINTS");

		// check value of C32SectionLoincCode
		FactHandle factHandle = ((List<FactHandle>) first.getActivation()
				.getFactHandles()).get(0);
		KnowledgeRuntime knowledgeRuntime = first.getKnowledgeRuntime();
		ClinicalFact fact = (ClinicalFact) knowledgeRuntime
				.getObject(factHandle);

		Assert.assertEquals(fact.getC32SectionLoincCode(), "11450-4");
	}

	@After
	public void tearDown() {
		if (knowledgeSession != null) {
			knowledgeSession.dispose();
		}
	}

	private <T> T unmarshallFromXml(Class<T> clazz, String xml)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
		return (T) um.unmarshal(input);
	}
}
