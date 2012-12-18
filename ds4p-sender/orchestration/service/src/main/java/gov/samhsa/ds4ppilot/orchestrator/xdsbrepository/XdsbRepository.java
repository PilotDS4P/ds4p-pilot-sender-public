package gov.samhsa.ds4ppilot.orchestrator.xdsbrepository;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public interface XdsbRepository {

	public RegistryResponseType provideAndRegisterDocumentSetRequest(ProvideAndRegisterDocumentSetRequest provideAndRegisterDocumentSetRequest);

	public RetrieveDocumentSetResponse retrieveDocumentSetRequest(RetrieveDocumentSetRequest retrieveDocumentSetRequest);
}
