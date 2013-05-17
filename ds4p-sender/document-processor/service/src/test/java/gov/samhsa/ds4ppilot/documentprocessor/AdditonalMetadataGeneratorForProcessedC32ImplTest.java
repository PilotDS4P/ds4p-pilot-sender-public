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

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class AdditonalMetadataGeneratorForProcessedC32ImplTest.
 */
public class AdditonalMetadataGeneratorForProcessedC32ImplTest {

	/**
	 * Test generate metadata xml.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGenerateMetadataXml() throws Exception {
		// Arrange
		String ruleExecutionResponseContainer = readStringFromFile("ruleExecutionResponseContainer.xml");
		String taggedC32Doc = readStringFromFile("tagged_C32.xml");
		AdditionalMetadataGeneratorForProcessedC32Impl additionalMetadataGeneratorForProcessedC32Impl = new AdditionalMetadataGeneratorForProcessedC32Impl();
		String senderEmailAddress = "sender@sender.com";
		String recipientEmailAddress = "receiver@receiver.com";
		String purposeOfUse = "TREAT";
		String xdsDocumentEntryUniqueId = "123";

		// Act
		String result = additionalMetadataGeneratorForProcessedC32Impl
				.generateMetadataXml(UUID.randomUUID().toString(), taggedC32Doc, ruleExecutionResponseContainer,
						senderEmailAddress, recipientEmailAddress, purposeOfUse, xdsDocumentEntryUniqueId);

		// Assert
		String expectedResult = readStringFromFile("additionalMetadataGeneratedFromRuleExecutionResponseContainer.xml");
		Assert.assertNotNull(result);
		//assertTrue(result.trim().equals(expectedResult.trim()));
	}

	/**
	 * Read string from file.
	 *
	 * @param fileName the file name
	 * @return the string
	 */
	private static String readStringFromFile(String fileName) {
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName);

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		StringBuilder resultStringBuilder = new StringBuilder();
		try {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line);
				resultStringBuilder.append("\n");
			}

			br.close();

			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = resultStringBuilder.toString();
		return result;
	}
}
