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
import java.io.File;
import java.io.FileOutputStream;

import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.EncryptionConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

/**
 * The Decrypter class reads an encrypted file from disk,
 * decrypts the contents of the file using a previously-stored
 * key, and then stores the decrypted file to disk.
 *

 */

public class Decrypter
{
   static
   {
      org.apache.xml.security.Init.init();
   }

   private static Document loadEncryptedFile(String fileName)
      throws Exception
   {
      File encryptedFile = new File(fileName);
      javax.xml.parsers.DocumentBuilderFactory dbf =
         javax.xml.parsers.DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      javax.xml.parsers.DocumentBuilder builder =
         dbf.newDocumentBuilder();
      Document document = builder.parse(encryptedFile);

      System.out.println("Encryption document loaded from: " +
                         encryptedFile.toURL().toString());
      return document;
   }

   private static SecretKey loadKeyEncryptionKey()
      throws Exception
   {
      String fileName = "keyEncryptKey";
      String jceAlgorithmName = "DESede";

      File kekFile = new File(fileName);

      DESedeKeySpec keySpec =
         new DESedeKeySpec(JavaUtils.getBytesFromFile(fileName));
      SecretKeyFactory skf =
         SecretKeyFactory.getInstance(jceAlgorithmName);
      SecretKey key = skf.generateSecret(keySpec);

      System.out.println("Key encryption key loaded from: "
                         + kekFile.toURL().toString());
      return key;
   }

   private static void writeDecryptedDocToFile(Document doc,
                                               String fileName)
      throws Exception
   {
      File encryptionFile = new File(fileName);
      FileOutputStream outStream = new FileOutputStream(encryptionFile);

      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                                    "no");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(outStream);
      transformer.transform(source, result);

      outStream.close();

      System.out.println("Decrypted data written to: " +
                         encryptionFile.toURL().toString());
   }

   private static void usage()
   {
      System.err.println("usage - java Decrypter "
                         + "infile outfile");
      System.err.println("example - java Decrypter "
                         + "encrypted.xml original.xml");
   }

   public static void main(String args[])
      throws Exception
   {
      if (args.length <2)
      {
         usage();
         System.exit(1);
      }

      // load the encrypted file into a Document
      Document document = loadEncryptedFile(args[0]);

      // get the encrypted data element
      String namespaceURI = EncryptionConstants.EncryptionSpecNS;
      String localName = EncryptionConstants._TAG_ENCRYPTEDDATA;
      Element encryptedDataElement =
         (Element)document.getElementsByTagNameNS(namespaceURI,
                                                  localName).item(0);

      // Load the key encryption key.
      Key keyEncryptKey = loadKeyEncryptionKey();

      // initialize cipher
      XMLCipher xmlCipher = XMLCipher.getInstance();
      xmlCipher.init(XMLCipher.DECRYPT_MODE, null);

      xmlCipher.setKEK(keyEncryptKey);

      // do the actual decryption
      xmlCipher.doFinal(document, encryptedDataElement);

      // write the results to a file
      writeDecryptedDocToFile(document, args[1]);
   }
}
