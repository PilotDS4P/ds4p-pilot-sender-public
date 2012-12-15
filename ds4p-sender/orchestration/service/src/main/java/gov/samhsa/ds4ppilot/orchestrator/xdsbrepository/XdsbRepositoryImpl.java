package gov.samhsa.ds4ppilot.orchestrator.xdsbrepository;

import gov.samhsa.ds4ppilot.ws.client.XDSRepositorybWebServiceClient;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;


public class XdsbRepositoryImpl implements XdsbRepository {

	private final String endpointAddress;

	public XdsbRepositoryImpl(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	@Override
	public RegistryResponseType provideAndRegisterDocumentSetRequest(
			ProvideAndRegisterDocumentSetRequest provideAndRegisterDocumentSetRequest) {
		RegistryResponseType registryResponse = null;

		XDSRepositorybWebServiceClient xdsRepositoryWebServiceClient = new XDSRepositorybWebServiceClient(
				endpointAddress);
		registryResponse = xdsRepositoryWebServiceClient.provideAndRegisterDocumentSetReponse(provideAndRegisterDocumentSetRequest);
		return registryResponse;
	}

	@Override
	public RetrieveDocumentSetResponse retrieveDocumentSetRequest(
			RetrieveDocumentSetRequest retrieveDocumentSetRequest) {
		RetrieveDocumentSetResponse retrieveDocumentSetRequestResponse = null;

		XDSRepositorybWebServiceClient xdsRepositoryWebServiceClient = new XDSRepositorybWebServiceClient(
				endpointAddress);
		retrieveDocumentSetRequestResponse = xdsRepositoryWebServiceClient.retrieveDocumentSetRequest(retrieveDocumentSetRequest);
		return retrieveDocumentSetRequestResponse;
	}

}
