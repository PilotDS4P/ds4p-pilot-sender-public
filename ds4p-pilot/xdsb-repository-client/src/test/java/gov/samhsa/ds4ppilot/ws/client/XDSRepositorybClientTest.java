package gov.samhsa.ds4ppilot.ws.client;


import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest.DocumentRequest;
import ihe.iti.xds_b._2007.XDSRepository;

import java.net.URL;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class XDSRepositorybClientTest  {

	protected static Endpoint ep;
	protected static String address;
	private  static ihe.iti.xds_b._2007.RetrieveDocumentSetResponse returnedValueOfRetrieveDocumentSet;

	@BeforeClass
	public static void setUp() {
		try {
			address = "http://localhost:9000/services/xdsrepositoryb";
			ep = Endpoint.publish(address, new XdsRepositorybImpl());
			
			XdsRepositorybImpl.returnedValueOfRetrieveDocumentSet = returnedValueOfRetrieveDocumentSet;	

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		RetrieveDocumentSetRequest retrieveDocumentSet = new RetrieveDocumentSetRequest();		
		DocumentRequest documentRequest = new DocumentRequest();
		documentRequest.setHomeCommunityId( "HC" );
		documentRequest.setRepositoryUniqueId("1.3.6.1.4.1.21367.2010.1.2.1040");
		documentRequest.setDocumentUniqueId("$uniqueId06");		
		retrieveDocumentSet.getDocumentRequest().add(documentRequest);
		
		Object response =  createPort().retrieveDocumentSet(retrieveDocumentSet);
		validateResponseOfRetrieveDocumentSetRequest(response);
	}

	// Test if the SOAP client calling the stub web service correctly?
	@Test
	public void testWSClientSOAPCallWorks_retrieveDocumentSetRequest() {
		RetrieveDocumentSetRequest retrieveDocumentSet = new RetrieveDocumentSetRequest();		
		DocumentRequest documentRequest = new DocumentRequest();
		documentRequest.setHomeCommunityId( "HC" );
		documentRequest.setRepositoryUniqueId("1.3.6.1.4.1.21367.2010.1.2.1040");
		documentRequest.setDocumentUniqueId("$uniqueId06");		
		retrieveDocumentSet.getDocumentRequest().add(documentRequest);
		
		XDSRepositorybWebServiceClient wsc = new XDSRepositorybWebServiceClient(address);
		Object resp = wsc.retrieveDocumentSetRequest(retrieveDocumentSet);
		validateResponseOfRetrieveDocumentSetRequest(resp);
	}	

	private void validateResponseOfRetrieveDocumentSetRequest(
			Object resp) {
		Assert.assertNotNull(resp);
	}

	private XDSRepository createPort() {
		final URL WSDL_LOCATION = this.getClass().getClassLoader()
				.getResource("XDS.b_repository.net.wsdl");
		final QName SERVICE = new  QName("http://tempuri.org/", "DocumentRepositoryService");
		
		XDSRepository port = new org.tempuri.DocumentRepositoryService(WSDL_LOCATION, SERVICE).getXDSRepositoryHTTPEndpoint();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);
		return port;
	}
}


