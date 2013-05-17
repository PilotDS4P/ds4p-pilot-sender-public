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


import gov.samhsa.ds4ppilot.schema.securedorchestrator.RegisteryStoredQueryResponse;
import gov.samhsa.ds4ppilot.schema.securedorchestrator.RetrieveDocumentSetResponse;

// TODO: Auto-generated Javadoc
/**
 * The Interface SecuredOrchestrator.
 */
public interface SecuredOrchestrator {
	
	/**
	 * Retrieve document set request.
	 *
	 * @param documentUniqueId the document unique id
	 * @param messageId the message id
	 * @param intendedRecipient the intended recipient
	 * @return the retrieve document set response
	 */
	public RetrieveDocumentSetResponse retrieveDocumentSetRequest(String documentUniqueId, String messageId, String intendedRecipient);

	/**
	 * Registery stored query request.
	 *
	 * @param patientId the patient id
	 * @param messageId the message id
	 * @return the registery stored query response
	 */
	public RegisteryStoredQueryResponse registeryStoredQueryRequest(String patientId, String messageId);
}
