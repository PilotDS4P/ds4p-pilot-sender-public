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
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="kekMaskingKey" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="kekEncryptionKey" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="metadata" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "_return",
    "kekMaskingKey",
    "kekEncryptionKey",
    "metadata"
})
@XmlRootElement(name = "RetrieveDocumentSetResponse")
public class RetrieveDocumentSetResponse {

    /** The _return. */
    @XmlElement(name = "return", required = true)
    protected String _return;
    
    /** The kek masking key. */
    @XmlElement(required = true)
    protected byte[] kekMaskingKey;
    
    /** The kek encryption key. */
    @XmlElement(required = true)
    protected byte[] kekEncryptionKey;
    
    /** The metadata. */
    @XmlElement(required = true)
    protected String metadata;

    /**
     * Gets the value of the return property.
     *
     * @return the return
     * possible object is
     * {@link String }
     */
    public String getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturn(String value) {
        this._return = value;
    }

    /**
     * Gets the value of the kekMaskingKey property.
     *
     * @return the kek masking key
     * possible object is
     * byte[]
     */
    public byte[] getKekMaskingKey() {
        return kekMaskingKey;
    }

    /**
     * Sets the value of the kekMaskingKey property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKekMaskingKey(byte[] value) {
        this.kekMaskingKey = ((byte[]) value);
    }

    /**
     * Gets the value of the kekEncryptionKey property.
     *
     * @return the kek encryption key
     * possible object is
     * byte[]
     */
    public byte[] getKekEncryptionKey() {
        return kekEncryptionKey;
    }

    /**
     * Sets the value of the kekEncryptionKey property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKekEncryptionKey(byte[] value) {
        this.kekEncryptionKey = ((byte[]) value);
    }

    /**
     * Gets the value of the metadata property.
     *
     * @return the metadata
     * possible object is
     * {@link String }
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * Sets the value of the metadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadata(String value) {
        this.metadata = value;
    }

}
