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
package gov.samhsa.schemas.client;

import gov.va.ehtac.ds4p.ws.DS4PAudit;
import gov.va.ehtac.ds4p.ws.DS4PAuditService;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.springframework.util.StringUtils;

/**
 * The Class AuditWebServiceClient.
 */
public class AuditWebServiceClient {
	
	/** The endpoint address. */
	private String endpointAddress;

	/**
	 * Instantiates a new audit web service client.
	 *
	 * @param endpointAddress the endpoint address
	 */
	public AuditWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	/**
	 * Update authorization event with annotated document.
	 *
	 * @param messageId the message id
	 * @param document the document
	 * @return true, if successful
	 */
	public boolean updateAuthorizationEventWithAnnotatedDoc(String messageId,
			String document) {
		DS4PAudit port = createPort();

		return port.updateAuthorizationEventWithAnnotatedDoc(messageId,
				document);
	}

	/**
	 * Update authorization event with executed rule names.
	 *
	 * @param messageId the message id
	 * @param execRules the executed rule names
	 * @return true, if successful
	 */
	public boolean updateAuthorizationEventWithExecRules(String messageId,
			String execRules) {

		DS4PAudit port = createPort();

		return port.updateAuthorizationEventWithExecRules(messageId, execRules);
	}

	/**
	 * Creates the port.
	 *
	 * @return the DS4P audit
	 */
	private DS4PAudit createPort() {
		final URL WSDL_LOCATION = this.getClass().getClassLoader()
				.getResource("DS4PAuditService.wsdl");
		final QName SERVICE = new QName("http://ws.ds4p.ehtac.va.gov/",
				"DS4PAuditService");

		DS4PAudit port = new DS4PAuditService(WSDL_LOCATION, SERVICE)
				.getDS4PAuditPort();

		if (StringUtils.hasText(this.endpointAddress)) {
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		}

		return port;
	}
}
