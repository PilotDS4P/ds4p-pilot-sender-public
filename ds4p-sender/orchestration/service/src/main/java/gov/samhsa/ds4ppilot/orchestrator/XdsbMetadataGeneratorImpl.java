package gov.samhsa.ds4ppilot.orchestrator;

import gov.samhsa.ds4ppilot.common.exception.DS4PException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XdsbMetadataGeneratorImpl implements XdsbMetadataGenerator {
	@Override
	public String generateMetadataXml(String document, String homeCommunityId) {

		StringWriter stringWriter = null;
		InputStream inputStream = null;

		try {
			inputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("XdsbMetadata.xsl");

			StreamSource styleSheetStremSource = new StreamSource(inputStream);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Templates template = transformerFactory
					.newTemplates(styleSheetStremSource);

			Transformer transformer = template.newTransformer();

			transformer.setParameter("homeCommunityId", homeCommunityId);

			stringWriter = new StringWriter();
			transformer.transform(new StreamSource(new StringReader(document)),
					new StreamResult(stringWriter));

			String metadataXml = stringWriter.toString();

			inputStream.close();
			stringWriter.close();

			return metadataXml;

		} catch (IOException e) {
			throw new DS4PException(e.toString(), e);

		} catch (TransformerException e) {
			throw new DS4PException(e.toString(), e);
		}
	}

	public static void main(String[] args) {
		XdsbMetadataGeneratorImpl xdsbMetadataGeneratorImpl = new XdsbMetadataGeneratorImpl();
		InputStream is = xdsbMetadataGeneratorImpl.getClass().getClassLoader()
				.getResourceAsStream("c32.xml");

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
				c32Document.toString(), "homeCommunityId");

		System.out.println(meta);
	}
}
