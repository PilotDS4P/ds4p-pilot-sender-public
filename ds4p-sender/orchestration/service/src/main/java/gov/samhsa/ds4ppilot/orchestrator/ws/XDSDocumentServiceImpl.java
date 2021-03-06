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
package gov.samhsa.ds4ppilot.orchestrator.ws;

import gov.samhsa.ds4ppilot.contract.orchestrator.XDSDocumentServicePortType;
import gov.samhsa.ds4ppilot.orchestrator.Orchestrator;
import gov.samhsa.ds4ppilot.schema.orchestrator.SaveDocumentSetToXdsRepositoryRequest;
import gov.samhsa.ds4ppilot.schema.orchestrator.SaveDocumentSetToXdsRepositoryResponse;

import javax.jws.WebService;

// TODO: Auto-generated Javadoc
/**
 * The Class XDSDocumentServiceImpl.
 */
@WebService(targetNamespace = "http://www.samhsa.gov/ds4ppilot/contract/orchestrator", portName = "XDSDocumentServicePort", serviceName = "XDSDocumentService", endpointInterface = "gov.samhsa.ds4ppilot.contract.orchestrator.XDSDocumentServicePortType")
public class XDSDocumentServiceImpl implements XDSDocumentServicePortType {

	/** The orchestrator. */
	private Orchestrator orchestrator;

	/**
	 * Instantiates a new xDS document service impl.
	 */
	public XDSDocumentServiceImpl() {
	}

	/**
	 * Instantiates a new xDS document service impl.
	 *
	 * @param orchestrator the orchestrator
	 */
	public XDSDocumentServiceImpl(Orchestrator orchestrator) {

		this.orchestrator = orchestrator;
	}

	/**
	 * Save document set to xds repository.
	 *
	 * @param parameters the parameters
	 * @return the save document set to xds repository response
	 */
	@Override
	public SaveDocumentSetToXdsRepositoryResponse saveDocumentSetToXdsRepository(
			SaveDocumentSetToXdsRepositoryRequest parameters) {
		SaveDocumentSetToXdsRepositoryResponse response = new SaveDocumentSetToXdsRepositoryResponse();

		boolean result = orchestrator.saveDocumentSetToXdsRepository(parameters
				.getDocumentSet());

		response.setReturn(result);
		return response;
	}

	/**
	 * Sets the orchestrator.
	 *
	 * @param orchestrator the new orchestrator
	 */
	public void setOrchestrator(Orchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}

	/**
	 * After properties set.
	 *
	 * @throws Exception the exception
	 */
	public void afterPropertiesSet() throws Exception {
		if (orchestrator == null) {
			throw new IllegalArgumentException(
					String.format(
							"You must set the orchestrator property of any beans of type {0}.",
							this.getClass()));
		}
	}
}
