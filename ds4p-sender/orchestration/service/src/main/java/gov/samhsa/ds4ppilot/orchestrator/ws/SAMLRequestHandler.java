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

import gov.samhsa.ds4ppilot.common.exception.DS4PException;
import gov.va.ds4p.security.saml.SAMLValidator;

import java.util.Set;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.springframework.remoting.soap.SoapFaultException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.wss.impl.callback.SAMLAssertionValidator.SAMLValidationException;

// TODO: Auto-generated Javadoc
/**
 * The Class SAMLRequestHandler.
 */
public class SAMLRequestHandler implements SOAPHandler<SOAPMessageContext> {

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		SAMLValidator samlValidator = new SAMLValidator();
		String WS_SECURITY_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		
		if (!outboundProperty.booleanValue()) {		
			SOAPMessage soapMessage = smc.getMessage();
			
			try {
				// get SOAP Header and Message
				SOAPHeader soapheader = smc.getMessage().getSOAPHeader();				

				if (soapheader == null) {
					generateAndThrowSOAPFault(soapMessage, "SAMLRequestHandler.handleMessage: No SOAP header.");
				}

				// check for wsse:security element under SOAP Header
				NodeList nodelist = soapheader
						.getElementsByTagName("wsse:Security");

				if (nodelist == null
						|| !nodelist.item(0).getLocalName().equals("Security")
						|| !WS_SECURITY_URI.equals(nodelist.item(0)
								.getNamespaceURI())) {
					generateAndThrowSOAPFault(soapMessage,
							"SAMLRequestHandler.handleMessage: No Security tag in header block.");
				}

				Node wsseNode = nodelist.item(0);
				for (Node childNode = wsseNode.getFirstChild(); childNode != null;) {
					Node node = childNode.getNextSibling();
					
					// check for SAML assertion under wsse:security element
					if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals("Assertion")) {
						samlValidator.validate((Element) node);
						break;
					}
					childNode = node;
				}

			} catch (Exception e) {
				generateAndThrowSOAPFault(soapMessage,
						e.getMessage());
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Generate and throw soap fault.
	 *
	 * @param msg the msg
	 * @param reason the reason
	 */
	private void generateAndThrowSOAPFault(SOAPMessage msg, String reason) {
		try {
			SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
			SOAPFault soapFault = soapBody.addFault();
			soapFault.setFaultString(reason);
			throw new SOAPFaultException(soapFault);
		} catch (SOAPException e) {
		}
	}

}
