package gov.samhsa.ds4ppilot.documentprocessor;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class AdditonalMetadataGeneratorForProcessedC32ImplTest {

	@Test
	public void testGenerateMetadataXml() throws Exception {
		// Arrange
		String ruleExecutionResponseContainer = readStringFromFile("ruleExecutionResponseContainer.xml");
		AdditionalMetadataGeneratorForProcessedC32Impl additionalMetadataGeneratorForProcessedC32Impl = new AdditionalMetadataGeneratorForProcessedC32Impl();
		String senderEmailAddress = "sender@sender.com";
		String recipientEmailAddress = "";

		// Act
		String result = additionalMetadataGeneratorForProcessedC32Impl
				.generateMetadataXml(ruleExecutionResponseContainer,
						senderEmailAddress, recipientEmailAddress);

		// Assert
		String expectedResult = readStringFromFile("additionalMetadataGeneratedFromRuleExecutionResponseContainer.xml");
		assertTrue(result.trim().equals(expectedResult.trim()));
	}

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
