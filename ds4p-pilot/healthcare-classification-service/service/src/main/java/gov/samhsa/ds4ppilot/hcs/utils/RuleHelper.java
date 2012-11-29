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
package gov.samhsa.ds4ppilot.hcs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

import org.apache.cxf.helpers.FileUtils;
import org.drools.compiler.DrlParser;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.xml.XmlDumper;
import org.drools.compiler.xml.XmlPackageReader;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.lang.DrlDumper;
import org.drools.lang.descr.PackageDescr;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The Class RuleHelper.
 */
public class RuleHelper {

	/**
	 * Convert DRL to XML.
	 *
	 * @param fileName the file name
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DroolsParserException the drools parser exception
	 */
	public static String convertDrlToXml(String fileName) throws IOException,
			DroolsParserException {	
		StringBuilder sb = new StringBuilder();
		String line;
		InputStream in = null;

		try {
			in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("rule1.drl");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return sb.toString();

	}

	/**
	 * Convert XML to DRL.
	 *
	 * @param xmlRule the XML rule
	 * @return the string
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String convertXmlToDrl(String xmlRule) throws SAXException,
			IOException {

		XmlPackageReader xmlPackageReader = new XmlPackageReader(null);
		PackageDescr pkgDumped = xmlPackageReader
				.read(new StringReader(xmlRule));

		DrlDumper dumper = new DrlDumper();
		String result = dumper.dump(pkgDumped);
		return result;
	}
}
