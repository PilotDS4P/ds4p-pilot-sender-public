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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Duane DeCouteau
 */
@Entity
@Table(name = "snomedproblemlist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Snomedproblemlist.findAll", query = "SELECT s FROM Snomedproblemlist s"),
    @NamedQuery(name = "Snomedproblemlist.findByIdsnomedproblemlist", query = "SELECT s FROM Snomedproblemlist s WHERE s.idsnomedproblemlist = :idsnomedproblemlist"),
    @NamedQuery(name = "Snomedproblemlist.findBySnomedCid", query = "SELECT s FROM Snomedproblemlist s WHERE s.snomedCid = :snomedCid"),
    @NamedQuery(name = "Snomedproblemlist.findBySnomedFsn", query = "SELECT s FROM Snomedproblemlist s WHERE s.snomedFsn = :snomedFsn"),
    @NamedQuery(name = "Snomedproblemlist.findBySnomedConceptStatus", query = "SELECT s FROM Snomedproblemlist s WHERE s.snomedConceptStatus = :snomedConceptStatus"),
    @NamedQuery(name = "Snomedproblemlist.findByUmlsCui", query = "SELECT s FROM Snomedproblemlist s WHERE s.umlsCui = :umlsCui"),
    @NamedQuery(name = "Snomedproblemlist.findByOccurrence", query = "SELECT s FROM Snomedproblemlist s WHERE s.occurrence = :occurrence"),
    @NamedQuery(name = "Snomedproblemlist.findByUsageType", query = "SELECT s FROM Snomedproblemlist s WHERE s.usageType = :usageType"),
    @NamedQuery(name = "Snomedproblemlist.findByFirstInUsage", query = "SELECT s FROM Snomedproblemlist s WHERE s.firstInUsage = :firstInUsage"),
    @NamedQuery(name = "Snomedproblemlist.findByIsRetiredFromSubset", query = "SELECT s FROM Snomedproblemlist s WHERE s.isRetiredFromSubset = :isRetiredFromSubset"),
    @NamedQuery(name = "Snomedproblemlist.findByLastInSubset", query = "SELECT s FROM Snomedproblemlist s WHERE s.lastInSubset = :lastInSubset"),
    @NamedQuery(name = "Snomedproblemlist.findByReplacedBySnomedCid", query = "SELECT s FROM Snomedproblemlist s WHERE s.replacedBySnomedCid = :replacedBySnomedCid"),
    @NamedQuery(name = "Snomedproblemlist.findByLikeName", query = "SELECT s FROM Snomedproblemlist s WHERE s.snomedFsn LIKE :snomedFsn")})
public class Snomedproblemlist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "idsnomedproblemlist")
    private Integer idsnomedproblemlist;
    @Size(max = 45)
    @Column(name = "snomed_cid")
    private String snomedCid;
    @Size(max = 200)
    @Column(name = "snomed_fsn")
    private String snomedFsn;
    @Size(max = 45)
    @Column(name = "snomed_concept_status")
    private String snomedConceptStatus;
    @Size(max = 45)
    @Column(name = "umls_cui")
    private String umlsCui;
    @Size(max = 45)
    @Column(name = "occurrence")
    private String occurrence;
    @Size(max = 45)
    @Column(name = "usage_type")
    private String usageType;
    @Size(max = 45)
    @Column(name = "first_in_usage")
    private String firstInUsage;
    @Size(max = 45)
    @Column(name = "is_retired_from_subset")
    private String isRetiredFromSubset;
    @Size(max = 45)
    @Column(name = "last_in_subset")
    private String lastInSubset;
    @Size(max = 45)
    @Column(name = "replaced_by_snomed_cid")
    private String replacedBySnomedCid;

    public Snomedproblemlist() {
    }

    public Snomedproblemlist(Integer idsnomedproblemlist) {
        this.idsnomedproblemlist = idsnomedproblemlist;
    }

    public Integer getIdsnomedproblemlist() {
        return idsnomedproblemlist;
    }

    public void setIdsnomedproblemlist(Integer idsnomedproblemlist) {
        this.idsnomedproblemlist = idsnomedproblemlist;
    }

    public String getSnomedCid() {
        return snomedCid;
    }

    public void setSnomedCid(String snomedCid) {
        this.snomedCid = snomedCid;
    }

    public String getSnomedFsn() {
        return snomedFsn;
    }

    public void setSnomedFsn(String snomedFsn) {
        this.snomedFsn = snomedFsn;
    }

    public String getSnomedConceptStatus() {
        return snomedConceptStatus;
    }

    public void setSnomedConceptStatus(String snomedConceptStatus) {
        this.snomedConceptStatus = snomedConceptStatus;
    }

    public String getUmlsCui() {
        return umlsCui;
    }

    public void setUmlsCui(String umlsCui) {
        this.umlsCui = umlsCui;
    }

    public String getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        this.occurrence = occurrence;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getFirstInUsage() {
        return firstInUsage;
    }

    public void setFirstInUsage(String firstInUsage) {
        this.firstInUsage = firstInUsage;
    }

    public String getIsRetiredFromSubset() {
        return isRetiredFromSubset;
    }

    public void setIsRetiredFromSubset(String isRetiredFromSubset) {
        this.isRetiredFromSubset = isRetiredFromSubset;
    }

    public String getLastInSubset() {
        return lastInSubset;
    }

    public void setLastInSubset(String lastInSubset) {
        this.lastInSubset = lastInSubset;
    }

    public String getReplacedBySnomedCid() {
        return replacedBySnomedCid;
    }

    public void setReplacedBySnomedCid(String replacedBySnomedCid) {
        this.replacedBySnomedCid = replacedBySnomedCid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsnomedproblemlist != null ? idsnomedproblemlist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Snomedproblemlist)) {
            return false;
        }
        Snomedproblemlist other = (Snomedproblemlist) object;
        if ((this.idsnomedproblemlist == null && other.idsnomedproblemlist != null) || (this.idsnomedproblemlist != null && !this.idsnomedproblemlist.equals(other.idsnomedproblemlist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.ehtac.ds4p.jpa.Snomedproblemlist[ idsnomedproblemlist=" + idsnomedproblemlist + " ]";
    }
    
}
