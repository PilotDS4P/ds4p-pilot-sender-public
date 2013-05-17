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

import gov.samhsa.schema.ds4p.xdsbregistry.message.RegistryRequest;
import ihe.iti.xds_b._2007.XDSRegistry;

import java.util.logging.Logger;
import javax.jws.WebService;
import org.hl7.v3.PRPAIN201302UV;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

// TODO: Auto-generated Javadoc
/**
 * This class was generated by Apache CXF 2.6.0 2012-11-21T08:25:58.844-05:00
 * Generated source version: 2.6.0
 * 
 */

@WebService(serviceName = "DocumentRegistryService", portName = "XDSRegistry_HTTP_Endpoint", targetNamespace = "http://samhsa.gov/ds4p/XDSbRegistry/", wsdlLocation = "classpath:XDS.b_registry.net.wsdl", endpointInterface = "ihe.iti.xds_b._2007.XDSRegistry")
public class XdsbRegistryServiceImpl implements XDSRegistry {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(XdsbRegistryServiceImpl.class
			.getName());
	
	/** The returned value of registry stored query. */
	protected  static AdhocQueryResponse returnedValueOfRegistryStoredQuery;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ihe.iti.xds_b._2007.XDSRegistry#patientRegistryRecordRevised(com.microsoft
	 * .schemas.message.RegistryRequest input )*
	 */
	/**
	 * Patient registry record revised.
	 *
	 * @param input the input
	 * @return the object
	 */
	@Override
	public Object patientRegistryRecordRevised(
			RegistryRequest input) {
		LOG.info("Executing operation patientRegistryRecordRevised");
		System.out.println(input);
		try {
			RegistryRequest _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ihe.iti.xds_b._2007.XDSRegistry#updateDocumentSet(com.microsoft.schemas
	 * .message.RegistryRequest input )*
	 */
	/**
	 * Update document set.
	 *
	 * @param input the input
	 * @return the object
	 */
	@Override
	public Object updateDocumentSet(
			RegistryRequest input) {
		LOG.info("Executing operation updateDocumentSet");
		System.out.println(input);
		try {
			RegistryRequest _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ihe.iti.xds_b._2007.XDSRegistry#registryStoredQuery(oasis.names.tc.
	 * ebxml_regrep.xsd.query._3.AdhocQueryRequest input )*
	 */
	/**
	 * Registry stored query.
	 *
	 * @param input the input
	 * @return the adhoc query response
	 */
	@Override
	public AdhocQueryResponse registryStoredQuery(
			oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest input) {
		LOG.info("Executing operation registryStoredQuery");
		System.out.println(input);
		try {
			AdhocQueryResponse _return = returnedValueOfRegistryStoredQuery;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ihe.iti.xds_b._2007.XDSRegistry#deleteDocumentSet(com.microsoft.schemas
	 * .message.RegistryRequest input )*
	 */
	/**
	 * Delete document set.
	 *
	 * @param input the input
	 * @return the object
	 */
	@Override
	public Object deleteDocumentSet(
			RegistryRequest input) {
		LOG.info("Executing operation deleteDocumentSet");
		System.out.println(input);
		try {
			RegistryRequest _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ihe.iti.xds_b._2007.XDSRegistry#patientRegistryRecordAdded(com.microsoft
	 * .schemas.message.RegistryRequest input )*
	 */
	/**
	 * Patient registry record added.
	 *
	 * @param input the input
	 * @return the object
	 */
	@Override
	public Object patientRegistryRecordAdded(
			PRPAIN201302UV input) {
		LOG.info("Executing operation patientRegistryRecordAdded");
		System.out.println(input);
		try {
			RegistryRequest _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ihe.iti.xds_b._2007.XDSRegistry#patientRegistryDuplicatesResolved(com
	 * .microsoft.schemas.message.RegistryRequest input )*
	 */
	/**
	 * Patient registry duplicates resolved.
	 *
	 * @param input the input
	 * @return the object
	 */
	@Override
	public Object patientRegistryDuplicatesResolved(
			RegistryRequest input) {
		LOG.info("Executing operation patientRegistryDuplicatesResolved");
		System.out.println(input);
		try {
			RegistryRequest _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ihe.iti.xds_b._2007.XDSRegistry#registerDocumentSet(com.microsoft.schemas
	 * .message.RegistryRequest input )*
	 */
	/**
	 * Register document set.
	 *
	 * @param input the input
	 * @return the object
	 */
	@Override
	public Object registerDocumentSet(
			RegistryRequest input) {
		LOG.info("Executing operation registerDocumentSet");
		System.out.println(input);
		try {
			RegistryRequest _return = null;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
