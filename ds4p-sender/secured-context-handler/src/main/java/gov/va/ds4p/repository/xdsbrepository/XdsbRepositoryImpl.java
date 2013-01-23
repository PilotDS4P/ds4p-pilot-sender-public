package gov.va.ds4p.repository.xdsbrepository;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequest;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;

import javax.xml.bind.JAXBElement;

import gov.samhsa.ds4ppilot.ws.client.XDSRepositorybWebServiceClient;


public class XdsbRepositoryImpl implements XdsbRepository {

	private String endpointAddress;

	public XdsbRepositoryImpl(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	@Override
	public ProvideAndRegisterDocumentSetResponse provideAndRegisterDocumentSetRequest(
			ProvideAndRegisterDocumentSetRequest provideAndRegisterDocumentSetRequest) {
		// TODO Auto-generated method stub
		return null;
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
