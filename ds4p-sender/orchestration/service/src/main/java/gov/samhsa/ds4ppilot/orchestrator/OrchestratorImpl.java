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
package gov.samhsa.ds4ppilot.orchestrator;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequest.DocumentRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import com.microsoft.schemas.message.RegistryStoredQueryResult;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import gov.samhsa.ds4ppilot.common.beans.XacmlResult;
import gov.samhsa.ds4ppilot.common.exception.DS4PException;
import gov.samhsa.ds4ppilot.orchestrator.c32getter.C32Getter;
import gov.samhsa.ds4ppilot.orchestrator.contexthandler.ContextHandler;
import gov.samhsa.ds4ppilot.orchestrator.documentprocessor.DocumentProcessor;
import gov.samhsa.ds4ppilot.orchestrator.xdsbregistry.XdsbRegistry;
import gov.samhsa.ds4ppilot.orchestrator.xdsbrepository.XdsbRepository;
import gov.samhsa.ds4ppilot.orchestrator.xdsbrepository.XdsbRepositoryImpl;
import gov.samhsa.ds4ppilot.schema.documentprocessor.ProcessDocumentResponse;
import gov.samhsa.ds4ppilot.schema.orchestrator.FilterC32Response;
import gov.samhsa.ds4ppilot.schema.orchestrator.RegisteryStoredQueryResponse;
import gov.samhsa.ds4ppilot.schema.orchestrator.RetrieveDocumentSetResponse;
import gov.va.ehtac.ds4p.ws.EnforcePolicy;
import gov.va.ehtac.ds4p.ws.EnforcePolicyResponse.Return;

/**
 * The Class OrchestratorImpl.
 */
public class OrchestratorImpl implements Orchestrator {

	/** The permit. */
	private final String PERMIT = "Permit";

	/** The context handler. */
	private ContextHandler contextHandler;

	/** The C32 getter. */
	private C32Getter c32Getter;

	/** The document processor. */
	private DocumentProcessor documentProcessor;

	/** The data handler to bytes converter. */
	private DataHandlerToBytesConverter dataHandlerToBytesConverter;

	/** The xdsbRepository. */
	private XdsbRepository xdsbRepository;

	/** The xdsbRegistry. */
	private XdsbRegistry xdsbRegistry;

	/** The subject purpose of use. */
	private String subjectPurposeOfUse; // = "TREAT";

	/** The subject locality. */
	private String subjectLocality; // = "2.16.840.1.113883.3.467";

	/** The organization. */
	private String organization; // = "SAMHSA";

	/** The organization id. */
	private String organizationId; // = "FEiSystems";

	/** The resource name. */
	private String resourceName; // = "NwHINDirectSend";

	/** The resource type. */
	private String resourceType; // = "C32";

	/** The resource action. */
	private String resourceAction; // = "Execute";

	/**
	 * Instantiates a new orchestrator impl.
	 * 
	 * @param contextHandler
	 *            the context handler
	 * @param c32Getter
	 *            the C32 getter
	 * @param documentProcessor
	 *            the document processor
	 * @param dataHandlerToBytesConverter
	 *            the data handler to bytes converter
	 */
	public OrchestratorImpl(ContextHandler contextHandler, C32Getter c32Getter,
			DocumentProcessor documentProcessor,
			DataHandlerToBytesConverter dataHandlerToBytesConverter,
			XdsbRepository xdsbRepository, XdsbRegistry xdsbRegistry) {
		super();
		this.contextHandler = contextHandler;
		this.c32Getter = c32Getter;
		this.documentProcessor = documentProcessor;
		this.dataHandlerToBytesConverter = dataHandlerToBytesConverter;
		this.xdsbRepository = xdsbRepository;
		this.xdsbRegistry = xdsbRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.samhsa.ds4ppilot.orchestrator.Orchestrator#handleC32Request(java.
	 * lang.String, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public FilterC32Response handleC32Request(String patientId,
			boolean packageAsXdm, String senderEmailAddress,
			String recipientEmailAddress) {
		StringWriter xacmlResponseXml = new StringWriter();
		byte[] processedPayload;
		FilterC32Response c32Response = new FilterC32Response();
		c32Response.setPatientId(patientId);

		Return result = null;
		try {			
			EnforcePolicy.Xspasubject xspasubject = setXspaSubject(
					recipientEmailAddress, UUID
							.randomUUID().toString());
			EnforcePolicy.Xsparesource xsparesource = setXspaResource(patientId);

			result = contextHandler.enforcePolicy(xspasubject, xsparesource);
		} catch (Exception e) {
			throw new DS4PException(e.toString(), e);
		}

		c32Response.setPdpDecision(result.getPdpDecision());

		if (result.getPdpDecision().equals(PERMIT)) {
			String originalC32 = c32Getter.getC32(patientId);

			try {
				XacmlResult xacmlResult = getXacmlResponse(result);

				JAXBContext jaxbContext = JAXBContext
						.newInstance(XacmlResult.class);
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration",
						Boolean.FALSE);
				marshaller.marshal(xacmlResult, xacmlResponseXml);

				ProcessDocumentResponse processDocumentResponse = documentProcessor
						.processDocument(originalC32,
								xacmlResponseXml.toString(), packageAsXdm,
								senderEmailAddress, recipientEmailAddress);

				processedPayload = dataHandlerToBytesConverter
						.toByteArray(processDocumentResponse
								.getProcessedDocument());

				c32Response.setMaskedDocument(processDocumentResponse
						.getMaskedDocument());
				c32Response.setFilteredStreamBody(processedPayload);
			} catch (PropertyException e) {
				throw new DS4PException(e.toString(), e);
			} catch (JAXBException e) {
				throw new DS4PException(e.toString(), e);
			} catch (IOException e) {
				throw new DS4PException(e.toString(), e);
			}
		}

		return c32Response;
	}

	@Override
	public RetrieveDocumentSetResponse retrieveDocumentSetRequest(
			String homeCommunityId, String repositoryUniqueId,
			String documentUniqueId) {
		RetrieveDocumentSetResponse response = new RetrieveDocumentSetResponse();
		RetrieveDocumentSetRequest retrieveDocumentSet = new RetrieveDocumentSetRequest();
		DocumentRequest documentRequest = new DocumentRequest();
		documentRequest.setHomeCommunityId(homeCommunityId);
		documentRequest.setRepositoryUniqueId(repositoryUniqueId);
		documentRequest.setDocumentUniqueId(documentUniqueId);
		retrieveDocumentSet.getDocumentRequest().add(documentRequest);

		Return result = null;
		try {
			EnforcePolicy.Xspasubject xspasubject = setXspaSubject(
					"Duane_Decouteau@direct.healthvault-stage.com", UUID
							.randomUUID().toString());
			EnforcePolicy.Xsparesource xsparesource = setXspaResource("PUI100010060001");

			result = contextHandler.enforcePolicy(xspasubject, xsparesource);

			ihe.iti.xds_b._2007.RetrieveDocumentSetResponse retrieveDocumentSetResponse = null;

			if (result.getPdpDecision().equals(PERMIT)) {

				retrieveDocumentSetResponse = xdsbRepository
						.retrieveDocumentSetRequest(retrieveDocumentSet);

				String xmlResponse = marshall(retrieveDocumentSetResponse,
						retrieveDocumentSetResponse.getClass());
				response.setReturn(xmlResponse);

			}
		} catch (Throwable e) {
			throw new DS4PException(e.toString(), e);
		}

		return response;
	}

	@Override
	public RegisteryStoredQueryResponse registeryStoredQueryRequest(
			String patientId)  {
		AdhocQueryRequest registryStoredQuery = new AdhocQueryRequest();

		ResponseOptionType responseOptionType = new ResponseOptionType();
		responseOptionType.setReturnComposedObjects(true);
		responseOptionType.setReturnType("LeafClass");
		registryStoredQuery.setResponseOption(responseOptionType);

		AdhocQueryType adhocQueryType = new AdhocQueryType();
		adhocQueryType.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d"); // FindDocuments
																				// by
																				// patientId
		registryStoredQuery.setAdhocQuery(adhocQueryType);

		SlotType1 patientIdSlotType = new SlotType1();
		patientIdSlotType.setName("$XDSDocumentEntryPatientId");
		ValueListType patientIdValueListType = new ValueListType();
		patientIdValueListType.getValue().add(
				"'24d3b01495f14e9^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO'"); // PatientId
		patientIdSlotType.setValueList(patientIdValueListType);
		adhocQueryType.getSlot().add(patientIdSlotType);

		SlotType1 statusSlotType = new SlotType1();
		statusSlotType.setName("$XDSDocumentEntryStatus");
		ValueListType statusValueListType = new ValueListType();
		statusValueListType.getValue().add(
				"('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')");
		statusSlotType.setValueList(statusValueListType);
		adhocQueryType.getSlot().add(statusSlotType);

		RegistryStoredQueryResult result = xdsbRegistry
				.registryStoredQuery(registryStoredQuery);

		RegisteryStoredQueryResponse response = new RegisteryStoredQueryResponse();

		Return enforcePolicyResult = null;
		try {

			EnforcePolicy.Xspasubject xspasubject = setXspaSubject(
					"Duane_Decouteau@direct.healthvault-stage.com", UUID
							.randomUUID().toString());
			EnforcePolicy.Xsparesource xsparesource = setXspaResource("PUI100010060001");

			enforcePolicyResult = contextHandler.enforcePolicy(xspasubject, xsparesource);

			if (enforcePolicyResult.getPdpDecision().equals(PERMIT)) {
				String xmlResponse = marshall(
						result,
						result.getClass(),
						oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType.class);
				response.setReturn(xmlResponse);
			}
		} catch (Throwable e) {
			throw new DS4PException(e.toString(), e);
		}

		return response;
	}

	/**
	 * Gets the subject purpose of use.
	 * 
	 * @return the subject purpose of use
	 */
	public String getSubjectPurposeOfUse() {
		return subjectPurposeOfUse;
	}

	/**
	 * Sets the subject purpose of use.
	 * 
	 * @param subjectPurposeOfUse
	 *            the new subject purpose of use
	 */
	public void setSubjectPurposeOfUse(String subjectPurposeOfUse) {
		this.subjectPurposeOfUse = subjectPurposeOfUse;
	}

	/**
	 * Gets the subject locality.
	 * 
	 * @return the subject locality
	 */
	public String getSubjectLocality() {
		return subjectLocality;
	}

	/**
	 * Sets the subject locality.
	 * 
	 * @param subjectLocality
	 *            the new subject locality
	 */
	public void setSubjectLocality(String subjectLocality) {
		this.subjectLocality = subjectLocality;
	}

	/**
	 * Gets the organization.
	 * 
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * Sets the organization.
	 * 
	 * @param organization
	 *            the new organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Gets the organization id.
	 * 
	 * @return the organization id
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * Sets the organization id.
	 * 
	 * @param organizationId
	 *            the new organization id
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * Gets the resource name.
	 * 
	 * @return the resource name
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Sets the resource name.
	 * 
	 * @param resourceName
	 *            the new resource name
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * Gets the resource type.
	 * 
	 * @return the resource type
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * Sets the resource type.
	 * 
	 * @param resourceType
	 *            the new resource type
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * Gets the resource action.
	 * 
	 * @return the resource action
	 */
	public String getResourceAction() {
		return resourceAction;
	}

	/**
	 * Sets the resource action.
	 * 
	 * @param resourceAction
	 *            the new resource action
	 */
	public void setResourceAction(String resourceAction) {
		this.resourceAction = resourceAction;
	}

	/**
	 * @return
	 */
	private EnforcePolicy.Xsparesource setXspaResource(String patientId) {
		EnforcePolicy.Xsparesource xsparesource = new EnforcePolicy.Xsparesource();
		xsparesource.setResourceId(patientId);
		xsparesource.setResourceName(resourceName);
		xsparesource.setResourceType(resourceType);
		xsparesource.setResourceAction(resourceAction);
		return xsparesource;
	}

	/**
	 * @return
	 */
	private EnforcePolicy.Xspasubject setXspaSubject(
			String recipientEmailAddress, String messageId) {
		EnforcePolicy.Xspasubject xspasubject = new EnforcePolicy.Xspasubject();
		xspasubject.setSubjectPurposeOfUse(subjectPurposeOfUse);
		xspasubject.setSubjectLocality(subjectLocality);
		xspasubject.setSubjectEmailAddress(recipientEmailAddress);
		xspasubject.setSubjectId(recipientEmailAddress);
		xspasubject.setOrganization(organization);
		xspasubject.setOrganizationId(organizationId);
		xspasubject.setMessageId(messageId);
		return xspasubject;
	}

	/**
	 * Gets the XACML response.
	 * 
	 * @param result
	 *            the result
	 * @return the XACML response
	 */
	private XacmlResult getXacmlResponse(Return result) {
		XacmlResult xacmlResult = new XacmlResult();
		xacmlResult.setHomeCommunityId(result.getHomeCommunityId());
		xacmlResult.setMessageId(result.getMessageId());
		xacmlResult.setPdpDecision(result.getPdpDecision());
		xacmlResult.setPdpObligations(result.getPdpObligation());
		xacmlResult.setSubjectPurposeOfUse(result.getPurposeOfUse());
		return xacmlResult;
	}

	private String marshall(Object obj, Class<?>... classesToBeBound)
			throws Throwable {
		JAXBContext context = JAXBContext.newInstance(classesToBeBound);

		Marshaller marshaller = context.createMarshaller();
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(obj, stringWriter);

		System.out.println(stringWriter.toString());

		return stringWriter.toString();
	}

	/**
	 * Gets the C32.
	 * 
	 * @param patientId
	 *            the patient id
	 * @return the c32
	 */
	@SuppressWarnings("unused")
	private String getC32(String patientId) {
		InputStream in = null;
		BufferedReader br = null;
		StringBuilder c32Document = new StringBuilder();

		try {
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("c32.xml");

			br = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = br.readLine()) != null) {
				c32Document.append(line);
			}
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);
		} finally {
			try {
				br.close();
				in.close();
			} catch (IOException e) {
				// do nothing here
			}
		}

		return c32Document.toString();
	}
}
