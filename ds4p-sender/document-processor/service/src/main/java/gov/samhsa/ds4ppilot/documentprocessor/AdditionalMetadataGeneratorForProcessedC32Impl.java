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
package gov.samhsa.ds4ppilot.documentprocessor;

import gov.samhsa.ds4ppilot.common.exception.DS4PException;
import gov.samhsa.ds4ppilot.common.utils.StringURIResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

// TODO: Auto-generated Javadoc
/**
 * The Class XdsbMetadataGeneratorImpl.
 */
public class AdditionalMetadataGeneratorForProcessedC32Impl implements
		AdditionalMetadataGeneratorForProcessedC32 {

	/** The Constant AdditonalMetadataStylesheetForProcessedC32_Xsl_File_Name. */
	private static final String AdditonalMetadataStylesheetForProcessedC32_Xsl_File_Name = "AdditonalMetadataStylesheetForProcessedC32.xsl";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.samhsa.ds4ppilot.orchestrator.XdsbMetadataGenerator#generateMetadataXml
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public String generateMetadataXml(String messageId, String document,String ruleExecutionResponseContainer,
			String senderEmailAddress, String recipientEmailAddress,
			String purposeOfUse, String xdsDocumentEntryUniqueId) {

		StringWriter stringWriter = null;
		InputStream inputStream = null;

		try {
			// add namespace execution response container for transformation
			ruleExecutionResponseContainer = ruleExecutionResponseContainer.replace(
					"<ruleExecutionContainer>",
					"<ruleExecutionContainer xmlns=\"urn:hl7-org:v3\">");

			inputStream = Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							AdditonalMetadataStylesheetForProcessedC32_Xsl_File_Name);

			StreamSource styleSheetStremSource = new StreamSource(inputStream);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Templates template = transformerFactory
					.newTemplates(styleSheetStremSource);

			Transformer transformer = template.newTransformer();
			transformer.setURIResolver(new StringURIResolver().put(
					"ruleExecutionResponseContainer",
					ruleExecutionResponseContainer));
			
			transformer.setParameter("authorTelecommunication",
					senderEmailAddress);		

			transformer
					.setParameter("intendedRecipient", recipientEmailAddress);
			
			transformer.setParameter("purposeOfUse", purposeOfUse);
			
			transformer.setParameter("privacyPoliciesExternalDocUrl", messageId);
			
			transformer.setParameter("xdsDocumentEntryUniqueId", xdsDocumentEntryUniqueId);

			stringWriter = new StringWriter();
			transformer.transform(new StreamSource(new StringReader(
					document)), new StreamResult(
					stringWriter));

			String metadataXml = stringWriter.toString();
			//System.out.println(metadataXml);

			inputStream.close();
			stringWriter.close();

			return metadataXml;

		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);

		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		}
	}
}
