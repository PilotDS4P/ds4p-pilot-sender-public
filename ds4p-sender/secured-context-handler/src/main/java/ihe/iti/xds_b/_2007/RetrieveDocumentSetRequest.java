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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


// TODO: Auto-generated Javadoc
/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocumentRequest" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                   &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                   &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "documentRequest"
})
@XmlRootElement(name = "RetrieveDocumentSetRequest")
public class RetrieveDocumentSetRequest {

    /** The document request. */
    @XmlElement(name = "DocumentRequest", required = true)
    protected List<RetrieveDocumentSetRequest.DocumentRequest> documentRequest;

    /**
     * Gets the value of the documentRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getDocumentRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     *
     * @return the document request
     * {@link RetrieveDocumentSetRequest.DocumentRequest }
     */
    public List<RetrieveDocumentSetRequest.DocumentRequest> getDocumentRequest() {
        if (documentRequest == null) {
            documentRequest = new ArrayList<RetrieveDocumentSetRequest.DocumentRequest>();
        }
        return this.documentRequest;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "homeCommunityId",
        "repositoryUniqueId",
        "documentUniqueId"
    })
    public static class DocumentRequest {

        /** The home community id. */
        @XmlElement(name = "HomeCommunityId")
        protected String homeCommunityId;
        
        /** The repository unique id. */
        @XmlElement(name = "RepositoryUniqueId", required = true)
        protected String repositoryUniqueId;
        
        /** The document unique id. */
        @XmlElement(name = "DocumentUniqueId", required = true)
        protected String documentUniqueId;

        /**
         * Gets the value of the homeCommunityId property.
         *
         * @return the home community id
         * possible object is
         * {@link String }
         */
        public String getHomeCommunityId() {
            return homeCommunityId;
        }

        /**
         * Sets the value of the homeCommunityId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHomeCommunityId(String value) {
            this.homeCommunityId = value;
        }

        /**
         * Gets the value of the repositoryUniqueId property.
         *
         * @return the repository unique id
         * possible object is
         * {@link String }
         */
        public String getRepositoryUniqueId() {
            return repositoryUniqueId;
        }

        /**
         * Sets the value of the repositoryUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRepositoryUniqueId(String value) {
            this.repositoryUniqueId = value;
        }

        /**
         * Gets the value of the documentUniqueId property.
         *
         * @return the document unique id
         * possible object is
         * {@link String }
         */
        public String getDocumentUniqueId() {
            return documentUniqueId;
        }

        /**
         * Sets the value of the documentUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentUniqueId(String value) {
            this.documentUniqueId = value;
        }

    }

}
