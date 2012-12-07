package gov.samhsa.ds4ppilot.orchestrator.xdsbregistry;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;


public interface XdsbRegistry {

	public AdhocQueryResponse registryStoredQuery(
	        AdhocQueryRequest input
	    );
}
