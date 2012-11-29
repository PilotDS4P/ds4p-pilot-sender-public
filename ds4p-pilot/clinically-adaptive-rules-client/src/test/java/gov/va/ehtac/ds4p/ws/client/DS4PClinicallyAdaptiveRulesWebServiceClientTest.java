package gov.va.ehtac.ds4p.ws.client;

import gov.va.ehtac.ds4p.ws.Clinicaltagrule;
import gov.va.ehtac.ds4p.ws.DS4PClinicallyAdaptiveRulesInterface;
import gov.va.ehtac.ds4p.ws.DS4PClinicallyAdaptiveRulesInterface_Service;
import gov.va.ehtac.ds4p.ws.OrganizationalPolicy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DS4PClinicallyAdaptiveRulesWebServiceClientTest {

	protected static Endpoint ep;
	protected static String address;

	private static final List<OrganizationalPolicy> returnedValueOfGetAllOrganizationalPolicy = new ArrayList<OrganizationalPolicy>();
	private static final String applicableLaw = "Applicable Law";

	private static final Boolean returnedValueOfSetClinicalDomainTaggingRule = false;
	private static final String returnedValueOfGetCASRuleSetStringByPOUObligationsAndHomeCommunityId = "CASRuleSetString";
	
	private static final Clinicaltagrule returnedValueOfGetClinicalDomainRule = new Clinicaltagrule();
	private static final String loincDisplayName = "LoincDisplayName";
	
	private static final Boolean returnedValueOfSetOrganizationalPolicy = false;;
	private static final OrganizationalPolicy returnedValueOfGetOrganizationalPolicy = new OrganizationalPolicy();

	@BeforeClass
	public static void setUp() {
		address = "http://localhost:9000/services/DS4PClinicallyAdaptiveRulesService";
		ep = Endpoint.publish(address,
				new DS4PClinicallyAdaptiveRulesInterfaceImpl());

		OrganizationalPolicy organizationalPolicy = new OrganizationalPolicy();
		organizationalPolicy.setApplicableUsLaw(applicableLaw);
		returnedValueOfGetAllOrganizationalPolicy.add(organizationalPolicy);
		DS4PClinicallyAdaptiveRulesInterfaceImpl.returnedValueOfGetAllOrganizationalPolicy = returnedValueOfGetAllOrganizationalPolicy;

		DS4PClinicallyAdaptiveRulesInterfaceImpl.returnedValueOfSetClinicalDomainTaggingRule = returnedValueOfSetClinicalDomainTaggingRule;

		DS4PClinicallyAdaptiveRulesInterfaceImpl.returnedValueOfGetCASRuleSetStringByPOUObligationsAndHomeCommunityId = returnedValueOfGetCASRuleSetStringByPOUObligationsAndHomeCommunityId;
		
		returnedValueOfGetClinicalDomainRule.setLoincDisplayName(loincDisplayName);
		DS4PClinicallyAdaptiveRulesInterfaceImpl.returnedValueOfGetClinicalDomainRule = returnedValueOfGetClinicalDomainRule;
		
		DS4PClinicallyAdaptiveRulesInterfaceImpl.returnedValueOfSetOrganizationalPolicy = returnedValueOfSetOrganizationalPolicy;
		
		returnedValueOfGetOrganizationalPolicy.setApplicableUsLaw(applicableLaw);
		DS4PClinicallyAdaptiveRulesInterfaceImpl.returnedValueOfGetOrganizationalPolicy = returnedValueOfGetOrganizationalPolicy;
		
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
		List<OrganizationalPolicy> resp = createPort()
				.getAllOrganizationalPolicy();
		validateResponseOfGetAllOrganizationalPolicy(resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_getAllOrganizationalPolicy() {
		DS4PClinicallyAdaptiveRulesWebServiceClient wsc = new DS4PClinicallyAdaptiveRulesWebServiceClient(
				address);
		List<OrganizationalPolicy> resp = wsc.getAllOrganizationalPolicy();
		validateResponseOfGetAllOrganizationalPolicy(resp);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_setClinicalDomainTaggingRule() {
		DS4PClinicallyAdaptiveRulesWebServiceClient wsc = new DS4PClinicallyAdaptiveRulesWebServiceClient(
				address);
		Clinicaltagrule clinicaltagrule = new Clinicaltagrule();
		Boolean resp = wsc.setClinicalDomainTaggingRule(clinicaltagrule);
		Assert.assertEquals(
				"Returned response from setClinicalDomainTaggingRule wrong",
				returnedValueOfSetClinicalDomainTaggingRule, resp);
	}

	@Test
	public void testWSClientSOAPCallWorks_getCASRuleSetStringByPOUObligationsAndHomeCommunityId() {
		DS4PClinicallyAdaptiveRulesWebServiceClient wsc = new DS4PClinicallyAdaptiveRulesWebServiceClient(
				address);
		String pou = null;
		List<String> obligations = null;
		String homeCommunityId = null;
		String messageId = null;
		String resp = wsc.getCASRuleSetStringByPOUObligationsAndHomeCommunityId(pou, obligations, homeCommunityId, messageId);
		Assert.assertEquals(
				"Returned response from getCASRuleSetStringByPOUObligationsAndHomeCommunityId wrong",
				returnedValueOfGetCASRuleSetStringByPOUObligationsAndHomeCommunityId, resp);
	}
	
	@Test
	public void testWSClientSOAPCallWorks_getClinicalDomainRule() {
		DS4PClinicallyAdaptiveRulesWebServiceClient wsc = new DS4PClinicallyAdaptiveRulesWebServiceClient(
				address);
		String domainLoincCode = null;
		Clinicaltagrule resp = wsc.getClinicalDomainRule(domainLoincCode);
		Assert.assertEquals(
				"Returned response from getClinicalDomainRule wrong",
				loincDisplayName, resp.getLoincDisplayName());
	}
	
	@Test
	public void testWSClientSOAPCallWorks_setOrganizationalPolicy() {
		DS4PClinicallyAdaptiveRulesWebServiceClient wsc = new DS4PClinicallyAdaptiveRulesWebServiceClient(
				address);
		OrganizationalPolicy organizationalPolicy = null;
		Boolean resp = wsc.setOrganizationalPolicy(organizationalPolicy);
		Assert.assertEquals(
				"Returned response from setOrganizationalPolicy wrong",
				returnedValueOfSetOrganizationalPolicy, resp);
	}
	
	@Test
	public void testWSClientSOAPCallWorks_getOrganizationalPolicy() {
		DS4PClinicallyAdaptiveRulesWebServiceClient wsc = new DS4PClinicallyAdaptiveRulesWebServiceClient(
				address);
		String homeCommunityId = null;
		OrganizationalPolicy resp = wsc.getOrganizationalPolicy(homeCommunityId);
		Assert.assertEquals(
				"Returned response from getOrganizationalPolicy wrong",
				returnedValueOfGetOrganizationalPolicy.getApplicableUsLaw(), resp.getApplicableUsLaw());
	}

	private void validateResponseOfGetAllOrganizationalPolicy(
			List<OrganizationalPolicy> resp) {
		Assert.assertEquals(
				"Returned List<OrganizationalPolicy> from getAllOrganizationalPolicy wrong",
				returnedValueOfGetAllOrganizationalPolicy.size(), resp.size());
		Assert.assertEquals(
				"Returned List<OrganizationalPolicy> from getAllOrganizationalPolicy wrong",
				returnedValueOfGetAllOrganizationalPolicy.get(0)
						.getApplicableUsLaw(), applicableLaw);
	}

	private DS4PClinicallyAdaptiveRulesInterface createPort() {
		final URL WSDL_LOCATION = ClassLoader
				.getSystemResource("DS4PClinicallyAdaptiveRulesInterface.wsdl");
		final QName SERVICE = new QName("http://ws.ds4p.ehtac.va.gov/",
				"DS4PClinicallyAdaptiveRulesInterface");

		DS4PClinicallyAdaptiveRulesInterface port = new DS4PClinicallyAdaptiveRulesInterface_Service(
				WSDL_LOCATION, SERVICE)
				.getDS4PClinicallyAdaptiveRulesInterfacePort();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);
		return port;
	}
}
