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
@Table(name = "organizational_policy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganizationalPolicy.findAll", query = "SELECT o FROM OrganizationalPolicy o"),
    @NamedQuery(name = "OrganizationalPolicy.findByIdorganizationalPolicy", query = "SELECT o FROM OrganizationalPolicy o WHERE o.idorganizationalPolicy = :idorganizationalPolicy"),
    @NamedQuery(name = "OrganizationalPolicy.findByOrganizationName", query = "SELECT o FROM OrganizationalPolicy o WHERE o.organizationName = :organizationName"),
    @NamedQuery(name = "OrganizationalPolicy.findByHomeCommunityId", query = "SELECT o FROM OrganizationalPolicy o WHERE o.homeCommunityId = :homeCommunityId"),
    @NamedQuery(name = "OrganizationalPolicy.findByApplicableUsLaw", query = "SELECT o FROM OrganizationalPolicy o WHERE o.applicableUsLaw = :applicableUsLaw")})
public class OrganizationalPolicy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idorganizational_policy")
    private Integer idorganizationalPolicy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "organization_name")
    private String organizationName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "home_community_id")
    private String homeCommunityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "applicable_us_law")
    private String applicableUsLaw;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "organizational_rules")
    private String organizationalRules;

    public OrganizationalPolicy() {
    }

    public OrganizationalPolicy(Integer idorganizationalPolicy) {
        this.idorganizationalPolicy = idorganizationalPolicy;
    }

    public OrganizationalPolicy(Integer idorganizationalPolicy, String organizationName, String homeCommunityId, String applicableUsLaw) {
        this.idorganizationalPolicy = idorganizationalPolicy;
        this.organizationName = organizationName;
        this.homeCommunityId = homeCommunityId;
        this.applicableUsLaw = applicableUsLaw;
    }

    public Integer getIdorganizationalPolicy() {
        return idorganizationalPolicy;
    }

    public void setIdorganizationalPolicy(Integer idorganizationalPolicy) {
        this.idorganizationalPolicy = idorganizationalPolicy;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    public String getApplicableUsLaw() {
        return applicableUsLaw;
    }

    public void setApplicableUsLaw(String applicableUsLaw) {
        this.applicableUsLaw = applicableUsLaw;
    }

    public String getOrganizationalRules() {
        return organizationalRules;
    }

    public void setOrganizationalRules(String organizationalRules) {
        this.organizationalRules = organizationalRules;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idorganizationalPolicy != null ? idorganizationalPolicy.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganizationalPolicy)) {
            return false;
        }
        OrganizationalPolicy other = (OrganizationalPolicy) object;
        if ((this.idorganizationalPolicy == null && other.idorganizationalPolicy != null) || (this.idorganizationalPolicy != null && !this.idorganizationalPolicy.equals(other.idorganizationalPolicy))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.ehtac.ds4p.jpa.OrganizationalPolicy[ idorganizationalPolicy=" + idorganizationalPolicy + " ]";
    }
    
}
