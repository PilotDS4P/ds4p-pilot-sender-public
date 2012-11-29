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
@Table(name = "clinicaltagrule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clinicaltagrule.findAll", query = "SELECT c FROM Clinicaltagrule c"),
    @NamedQuery(name = "Clinicaltagrule.findByIdclinicaltagrule", query = "SELECT c FROM Clinicaltagrule c WHERE c.idclinicaltagrule = :idclinicaltagrule"),
    @NamedQuery(name = "Clinicaltagrule.findByLoincCode", query = "SELECT c FROM Clinicaltagrule c WHERE c.loincCode = :loincCode"),
    @NamedQuery(name = "Clinicaltagrule.findByLoincDisplayName", query = "SELECT c FROM Clinicaltagrule c WHERE c.loincDisplayName = :loincDisplayName")})
public class Clinicaltagrule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "idclinicaltagrule")
    private Integer idclinicaltagrule;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "loinc_code")
    private String loincCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "loinc_display_name")
    private String loincDisplayName;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "observation_rules")
    private String observationRules;

    public Clinicaltagrule() {
    }

    public Clinicaltagrule(Integer idclinicaltagrule) {
        this.idclinicaltagrule = idclinicaltagrule;
    }

    public Clinicaltagrule(Integer idclinicaltagrule, String loincCode, String loincDisplayName) {
        this.idclinicaltagrule = idclinicaltagrule;
        this.loincCode = loincCode;
        this.loincDisplayName = loincDisplayName;
    }

    public Integer getIdclinicaltagrule() {
        return idclinicaltagrule;
    }

    public void setIdclinicaltagrule(Integer idclinicaltagrule) {
        this.idclinicaltagrule = idclinicaltagrule;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public String getLoincDisplayName() {
        return loincDisplayName;
    }

    public void setLoincDisplayName(String loincDisplayName) {
        this.loincDisplayName = loincDisplayName;
    }

    public String getObservationRules() {
        return observationRules;
    }

    public void setObservationRules(String observationRules) {
        this.observationRules = observationRules;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idclinicaltagrule != null ? idclinicaltagrule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clinicaltagrule)) {
            return false;
        }
        Clinicaltagrule other = (Clinicaltagrule) object;
        if ((this.idclinicaltagrule == null && other.idclinicaltagrule != null) || (this.idclinicaltagrule != null && !this.idclinicaltagrule.equals(other.idclinicaltagrule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.ehtac.ds4p.jpa.Clinicaltagrule[ idclinicaltagrule=" + idclinicaltagrule + " ]";
    }
    
}
