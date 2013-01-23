package gov.va.ds4p.registry.xdsbregistry;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import com.microsoft.schemas.message.RegistryStoredQueryResult;

public interface XdsbRegistry {

	public RegistryStoredQueryResult registryStoredQuery(
	        AdhocQueryRequest input
	    );
}
