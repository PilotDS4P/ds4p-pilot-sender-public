package gov.samhsa.ds4ppilot.orchestrator.xdsbregistry;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import gov.samhsa.ds4ppilot.ws.client.XdsbRegistryWebServiceClient;

public class XdsbRegistryImpl implements XdsbRegistry {

	private String endpointAddress;

	public XdsbRegistryImpl(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	@Override
	public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest input) {
		XdsbRegistryWebServiceClient client = new XdsbRegistryWebServiceClient(endpointAddress);
		AdhocQueryResponse result = client.registryStoredQuery(input);
		return result;
	}
}
