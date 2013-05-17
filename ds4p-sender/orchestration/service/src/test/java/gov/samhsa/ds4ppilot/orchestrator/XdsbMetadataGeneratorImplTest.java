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

import gov.samhsa.ds4ppilot.common.exception.DS4PException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;

import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class XdsbMetadataGeneratorImplTest.
 */
public class XdsbMetadataGeneratorImplTest {

	/**
	 * Testgenerate metadata xml_ c32.
	 */
	@Test
	public void testgenerateMetadataXml_C32() {
		testgenerateMetadataXml("remC32.xml");
	}

	/**
	 * Testgenerate metadata xml_ cda r2.
	 */
	@Test
	public void testgenerateMetadataXml_CdaR2() {
		testgenerateMetadataXml("cdaR2Consent.xml");
	}

	/**
	 * Testgenerate metadata xml.
	 *
	 * @param fileName the file name
	 */
	private void testgenerateMetadataXml(String fileName) {
		XdsbMetadataGeneratorImpl xdsbMetadataGeneratorImpl = new XdsbMetadataGeneratorImpl(
				new UniqueOidProviderImpl());
		InputStream is = xdsbMetadataGeneratorImpl.getClass().getClassLoader()
				.getResourceAsStream(fileName);

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		StringBuilder c32Document = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				c32Document.append(line);
			}

			br.close();
			is.close();
		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);
		}

		String meta = xdsbMetadataGeneratorImpl.generateMetadataXml(
				c32Document.toString(), "1.1.1.1.1");

		System.out.println(meta);

		SubmitObjectsRequest submitObjectsRequest = xdsbMetadataGeneratorImpl
				.generateMetadata(c32Document.toString(), "1.1.1.1.1");

		System.out.println("Generated SubmitObjectsRequest");

		try {
			System.out.println(marshall(submitObjectsRequest));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Marshall.
	 *
	 * @param obj the obj
	 * @return the string
	 * @throws Throwable the throwable
	 */
	private static String marshall(Object obj) throws Throwable {
		final JAXBContext context = JAXBContext.newInstance(obj.getClass());

		// Create the marshaller, this is the nifty little thing that will
		// actually transform the object into XML
		final Marshaller marshaller = context.createMarshaller();

		// Create a stringWriter to hold the XML
		final StringWriter stringWriter = new StringWriter();

		// Marshal the javaObject and write the XML to the stringWriter
		marshaller.marshal(obj, stringWriter);

		return stringWriter.toString();
	}
}
