/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Duane DeCouteau
 */
@Entity
@Table(name = "directprocessing")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Directprocessing.findAll", query = "SELECT d FROM Directprocessing d"),
    @NamedQuery(name = "Directprocessing.findByIddirectprocessing", query = "SELECT d FROM Directprocessing d WHERE d.iddirectprocessing = :iddirectprocessing"),
    @NamedQuery(name = "Directprocessing.findByProviderId", query = "SELECT d FROM Directprocessing d WHERE d.providerId = :providerId"),
    @NamedQuery(name = "Directprocessing.findByUniquePatientId", query = "SELECT d FROM Directprocessing d WHERE d.uniquePatientId = :uniquePatientId"),
    @NamedQuery(name = "Directprocessing.findByPatientName", query = "SELECT d FROM Directprocessing d WHERE d.patientName = :patientName"),
    @NamedQuery(name = "Directprocessing.findByPatientHomeCommunity", query = "SELECT d FROM Directprocessing d WHERE d.patientHomeCommunity = :patientHomeCommunity"),
    @NamedQuery(name = "Directprocessing.findByDateProcessed", query = "SELECT d FROM Directprocessing d WHERE d.dateProcessed = :dateProcessed"),
    @NamedQuery(name = "Directprocessing.findBySendingProviderId", query = "SELECT d FROM Directprocessing d WHERE d.sendingProviderId = :sendingProviderId"),
    @NamedQuery(name = "Directprocessing.findByOriginatingFacility", query = "SELECT d FROM Directprocessing d WHERE d.originatingFacility = :originatingFacility"),
    @NamedQuery(name = "Directprocessing.findByConfidentiality", query = "SELECT d FROM Directprocessing d WHERE d.confidentiality = :confidentiality"),
    @NamedQuery(name = "Directprocessing.findByRefrainPolicy", query = "SELECT d FROM Directprocessing d WHERE d.refrainPolicy = :refrainPolicy"),
    @NamedQuery(name = "Directprocessing.findByFacilityType", query = "SELECT d FROM Directprocessing d WHERE d.facilityType = :facilityType"),
    @NamedQuery(name = "Directprocessing.findByFaciltyTypeCode", query = "SELECT d FROM Directprocessing d WHERE d.faciltyTypeCode = :faciltyTypeCode"),
    @NamedQuery(name = "Directprocessing.findByPou", query = "SELECT d FROM Directprocessing d WHERE d.pou = :pou")})
public class Directprocessing implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "iddirectprocessing")
    private Integer iddirectprocessing;
    @Size(max = 200)
    @Column(name = "provider_id")
    private String providerId;
    @Lob
    @Column(name = "xdmfile")
    private byte[] xdmfile;
    @Lob
    @Column(name = "dockey")
    private byte[] dockey;
    @Lob
    @Column(name = "dockeyMasking")
    private byte[] dockeyMasking;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "cdaxsl")
    private String cdaxsl;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "encrypteddocument")
    private String encrypteddocument;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "metadata")
    private String metadata;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "document")
    private String document;
    @Size(max = 200)
    @Column(name = "uniquePatientId")
    private String uniquePatientId;
    @Size(max = 200)
    @Column(name = "patientName")
    private String patientName;
    @Size(max = 100)
    @Column(name = "patientHomeCommunity")
    private String patientHomeCommunity;
    @Column(name = "dateProcessed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateProcessed;
    @Size(max = 200)
    @Column(name = "sendingProviderId")
    private String sendingProviderId;
    @Size(max = 200)
    @Column(name = "originatingFacility")
    private String originatingFacility;
    @Size(max = 45)
    @Column(name = "confidentiality")
    private String confidentiality;
    @Size(max = 45)
    @Column(name = "refrainPolicy")
    private String refrainPolicy;
    @Size(max = 200)
    @Column(name = "facilityType")
    private String facilityType;
    @Size(max = 45)
    @Column(name = "faciltyTypeCode")
    private String faciltyTypeCode;
    @Size(max = 45)
    @Column(name = "pou")
    private String pou;

    public Directprocessing() {
    }

    public Directprocessing(Integer iddirectprocessing) {
        this.iddirectprocessing = iddirectprocessing;
    }

    public Integer getIddirectprocessing() {
        return iddirectprocessing;
    }

    public void setIddirectprocessing(Integer iddirectprocessing) {
        this.iddirectprocessing = iddirectprocessing;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public byte[] getXdmfile() {
        return xdmfile;
    }

    public void setXdmfile(byte[] xdmfile) {
        this.xdmfile = xdmfile;
    }

    public byte[] getDockey() {
        return dockey;
    }

    public void setDockey(byte[] dockey) {
        this.dockey = dockey;
    }

    public byte[] getDockeyMasking() {
        return dockeyMasking;
    }

    public void setDockeyMasking(byte[] dockeyMasking) {
        this.dockeyMasking = dockeyMasking;
    }

    public String getCdaxsl() {
        return cdaxsl;
    }

    public void setCdaxsl(String cdaxsl) {
        this.cdaxsl = cdaxsl;
    }

    public String getEncrypteddocument() {
        return encrypteddocument;
    }

    public void setEncrypteddocument(String encrypteddocument) {
        this.encrypteddocument = encrypteddocument;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getUniquePatientId() {
        return uniquePatientId;
    }

    public void setUniquePatientId(String uniquePatientId) {
        this.uniquePatientId = uniquePatientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientHomeCommunity() {
        return patientHomeCommunity;
    }

    public void setPatientHomeCommunity(String patientHomeCommunity) {
        this.patientHomeCommunity = patientHomeCommunity;
    }

    public Date getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(Date dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public String getSendingProviderId() {
        return sendingProviderId;
    }

    public void setSendingProviderId(String sendingProviderId) {
        this.sendingProviderId = sendingProviderId;
    }

    public String getOriginatingFacility() {
        return originatingFacility;
    }

    public void setOriginatingFacility(String originatingFacility) {
        this.originatingFacility = originatingFacility;
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getRefrainPolicy() {
        return refrainPolicy;
    }

    public void setRefrainPolicy(String refrainPolicy) {
        this.refrainPolicy = refrainPolicy;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getFaciltyTypeCode() {
        return faciltyTypeCode;
    }

    public void setFaciltyTypeCode(String faciltyTypeCode) {
        this.faciltyTypeCode = faciltyTypeCode;
    }

    public String getPou() {
        return pou;
    }

    public void setPou(String pou) {
        this.pou = pou;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddirectprocessing != null ? iddirectprocessing.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Directprocessing)) {
            return false;
        }
        Directprocessing other = (Directprocessing) object;
        if ((this.iddirectprocessing == null && other.iddirectprocessing != null) || (this.iddirectprocessing != null && !this.iddirectprocessing.equals(other.iddirectprocessing))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.ehtac.ds4p.jpa.Directprocessing[ iddirectprocessing=" + iddirectprocessing + " ]";
    }
    
}
