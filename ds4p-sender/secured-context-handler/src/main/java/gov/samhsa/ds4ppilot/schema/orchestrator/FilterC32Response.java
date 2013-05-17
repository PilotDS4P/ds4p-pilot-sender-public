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
package gov.samhsa.ds4ppilot.schema.orchestrator;

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
 *         &lt;element name="patientId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pdpDecision" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="filteredStreamBody" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="maskedDocument" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "patientId",
    "pdpDecision",
    "filteredStreamBody",
    "maskedDocument"
})
@XmlRootElement(name = "FilterC32Response")
public class FilterC32Response {

    /** The patient id. */
    @XmlElement(required = true)
    protected String patientId;
    
    /** The pdp decision. */
    @XmlElement(required = true)
    protected String pdpDecision;
    
    /** The filtered stream body. */
    @XmlElement(required = true)
    protected byte[] filteredStreamBody;
    
    /** The masked document. */
    @XmlElement(required = true)
    protected String maskedDocument;

    /**
     * Gets the value of the patientId property.
     *
     * @return the patient id
     * possible object is
     * {@link String }
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the value of the patientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientId(String value) {
        this.patientId = value;
    }

    /**
     * Gets the value of the pdpDecision property.
     *
     * @return the pdp decision
     * possible object is
     * {@link String }
     */
    public String getPdpDecision() {
        return pdpDecision;
    }

    /**
     * Sets the value of the pdpDecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdpDecision(String value) {
        this.pdpDecision = value;
    }

    /**
     * Gets the value of the filteredStreamBody property.
     *
     * @return the filtered stream body
     * possible object is
     * byte[]
     */
    public byte[] getFilteredStreamBody() {
        return filteredStreamBody;
    }

    /**
     * Sets the value of the filteredStreamBody property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFilteredStreamBody(byte[] value) {
        this.filteredStreamBody = ((byte[]) value);
    }

    /**
     * Gets the value of the maskedDocument property.
     *
     * @return the masked document
     * possible object is
     * {@link String }
     */
    public String getMaskedDocument() {
        return maskedDocument;
    }

    /**
     * Sets the value of the maskedDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskedDocument(String value) {
        this.maskedDocument = value;
    }

}
