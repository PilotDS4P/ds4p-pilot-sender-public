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
package ihe.iti.xds_b._2007;

import javax.xml.bind.annotation.XmlRegistry;


// TODO: Auto-generated Javadoc
/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ihe.iti.xds_b._2007 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ihe.iti.xds_b._2007
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetRequest.Document }
     *
     * @return the document
     */
    public ProvideAndRegisterDocumentSetRequest.Document createProvideAndRegisterDocumentSetRequestDocument() {
        return new ProvideAndRegisterDocumentSetRequest.Document();
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetResponse }.
     *
     * @return the provide and register document set response
     */
    public ProvideAndRegisterDocumentSetResponse createProvideAndRegisterDocumentSetResponse() {
        return new ProvideAndRegisterDocumentSetResponse();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetResponse }.
     *
     * @return the retrieve document set response
     */
    public RetrieveDocumentSetResponse createRetrieveDocumentSetResponse() {
        return new RetrieveDocumentSetResponse();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetRequest.DocumentRequest }
     *
     * @return the document request
     */
    public RetrieveDocumentSetRequest.DocumentRequest createRetrieveDocumentSetRequestDocumentRequest() {
        return new RetrieveDocumentSetRequest.DocumentRequest();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetResponse.DocumentResponse }
     *
     * @return the document response
     */
    public RetrieveDocumentSetResponse.DocumentResponse createRetrieveDocumentSetResponseDocumentResponse() {
        return new RetrieveDocumentSetResponse.DocumentResponse();
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetRequest }.
     *
     * @return the provide and register document set request
     */
    public ProvideAndRegisterDocumentSetRequest createProvideAndRegisterDocumentSetRequest() {
        return new ProvideAndRegisterDocumentSetRequest();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetRequest }.
     *
     * @return the retrieve document set request
     */
    public RetrieveDocumentSetRequest createRetrieveDocumentSetRequest() {
        return new RetrieveDocumentSetRequest();
    }

}
