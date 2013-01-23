package gov.va.ds4p.registry.xdsbregistry;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import com.microsoft.schemas.message.RegistryStoredQueryResult;
import gov.samhsa.ds4ppilot.ws.client.XdsbRegistryWebServiceClient;

public class XdsbRegistryImpl implements XdsbRegistry {

	private String endpointAddress;

	public XdsbRegistryImpl(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	@Override
	public RegistryStoredQueryResult registryStoredQuery(AdhocQueryRequest input) {
		XdsbRegistryWebServiceClient client = new XdsbRegistryWebServiceClient(endpointAddress);
		RegistryStoredQueryResult result = client.registryStoredQuery(input);
		return result;
	}

}
