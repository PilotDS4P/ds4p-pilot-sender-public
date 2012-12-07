/*******************************************************************************
 * Open Behavioral Health Information Technology Architecture (OBHITA.org)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.samhsa.ds4ppilot.ws.client;

import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.springframework.util.StringUtils;

import gov.samhsa.ds4p.xdsbregistry.DocumentRegistryService;
import ihe.iti.xds_b._2007.XDSRegistry;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;


/**
 * The Class DS4PClinicallyAdaptiveRulesWebServiceClient.
 */
public class XdsbRegistryWebServiceClient {

	/** The endpoint address. */
	private String endpointAddress;

	
	public XdsbRegistryWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	
	public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest registryStoredQuery) {
		XDSRegistry port = createPort();

		return port.registryStoredQuery(registryStoredQuery);
	}
	
	private XDSRegistry createPort() {
		final URL WSDL_LOCATION = this.getClass().getClassLoader()
				.getResource("XDS.b_registry.net.wsdl");
		final QName SERVICE =  new QName("http://samhsa.gov/ds4p/XDSbRegistry/", "DocumentRegistryService");

		XDSRegistry port = new DocumentRegistryService(WSDL_LOCATION, SERVICE).getXDSRegistryHTTPEndpoint();		
		
		if (StringUtils.hasText(this.endpointAddress)) {
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress); 					
		}

		return port;
	}
	
	public static void main(String[] args) throws Throwable {
		final String demoEndpoint = "http://xds-demo.feisystems.com:8080/axis2/services/xdsregistryb";
		final String javaVmEndpoint = "http://192.168.223.134:8080/axis2/services/xdsregistryb";
		final String dotnetVmEndpoint = "http://192.168.223.138:8080/xdsservice/xdsregistry";
		XdsbRegistryWebServiceClient xdsService = new XdsbRegistryWebServiceClient(demoEndpoint);
		
		AdhocQueryRequest registryStoredQuery = new AdhocQueryRequest();
		
		ResponseOptionType responseOptionType = new ResponseOptionType();
		responseOptionType.setReturnComposedObjects(true);
		responseOptionType.setReturnType("LeafClass");
		registryStoredQuery.setResponseOption(responseOptionType);
		
		AdhocQueryType adhocQueryType = new AdhocQueryType();
		adhocQueryType.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d"); // FindDocuments by patientId
		registryStoredQuery.setAdhocQuery(adhocQueryType);
		
		SlotType1 patientIdSlotType = new SlotType1();
		patientIdSlotType.setName("$XDSDocumentEntryPatientId");
		ValueListType patientIdValueListType = new ValueListType();
		patientIdValueListType.getValue().add("'24d3b01495f14e9^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO'"); // PatientId
		patientIdSlotType.setValueList(patientIdValueListType);
		adhocQueryType.getSlot().add(patientIdSlotType);
		
		SlotType1 statusSlotType = new SlotType1();
		statusSlotType.setName("$XDSDocumentEntryStatus");
		ValueListType statusValueListType = new ValueListType();
		statusValueListType.getValue().add("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')"); 
		statusSlotType.setValueList(statusValueListType);
		adhocQueryType.getSlot().add(statusSlotType);
		
		marshall(registryStoredQuery);
		
		Object obj = xdsService.registryStoredQuery(registryStoredQuery);
		
		System.out.println(obj.getClass().toString());
		
		marshall(obj);
	}
	
	public static void marshall(Object obj) throws Throwable {
        // =============================================================================================================
        // Setup JAXB
        // =============================================================================================================
 
        // Create a JAXB context passing in the class of the object we want to marshal/unmarshal
        //final JAXBContext context = JAXBContext.newInstance(obj.getClass(), oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType.class);
		final JAXBContext context = JAXBContext.newInstance(obj.getClass());
 
        // =============================================================================================================
        // Marshalling OBJECT to XML
        // =============================================================================================================
 
        // Create the marshaller, this is the nifty little thing that will actually transform the object into XML
        final Marshaller marshaller = context.createMarshaller();
 
        // Create a stringWriter to hold the XML
        final StringWriter stringWriter = new StringWriter();
 
        // Marshal the javaObject and write the XML to the stringWriter
        marshaller.marshal(obj, stringWriter);
 
        // Print out the contents of the stringWriter
        System.out.println(stringWriter.toString());

    }
}
