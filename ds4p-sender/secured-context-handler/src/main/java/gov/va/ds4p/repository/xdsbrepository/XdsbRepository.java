package gov.va.ds4p.repository.xdsbrepository;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequest;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;

public interface XdsbRepository {

	 public ProvideAndRegisterDocumentSetResponse provideAndRegisterDocumentSetRequest(ProvideAndRegisterDocumentSetRequest provideAndRegisterDocumentSetRequest);
	 
	 public RetrieveDocumentSetResponse retrieveDocumentSetRequest(RetrieveDocumentSetRequest retrieveDocumentSetRequest);
}
