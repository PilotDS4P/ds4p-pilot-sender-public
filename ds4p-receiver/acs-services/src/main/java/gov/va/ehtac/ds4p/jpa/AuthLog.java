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
@Table(name = "auth_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuthLog.findAll", query = "SELECT a FROM AuthLog a"),
    @NamedQuery(name = "AuthLog.findByMsgId", query = "SELECT a FROM AuthLog a WHERE a.msgId = :msgId"),
    @NamedQuery(name = "AuthLog.findByRequestor", query = "SELECT a FROM AuthLog a WHERE a.requestor = :requestor"),
    @NamedQuery(name = "AuthLog.findByDecision", query = "SELECT a FROM AuthLog a WHERE a.decision = :decision"),
    @NamedQuery(name = "AuthLog.findByPurposeOfUse", query = "SELECT a FROM AuthLog a WHERE a.purposeOfUse = :purposeOfUse"),
    @NamedQuery(name = "AuthLog.findByHealthcareObject", query = "SELECT a FROM AuthLog a WHERE a.healthcareObject = :healthcareObject"),
    @NamedQuery(name = "AuthLog.findByUniqueIdentifier", query = "SELECT a FROM AuthLog a WHERE a.uniqueIdentifier = :uniqueIdentifier"),
    @NamedQuery(name = "AuthLog.findByMsgDate", query = "SELECT a FROM AuthLog a WHERE a.msgDate = :msgDate"),
    @NamedQuery(name = "AuthLog.findByResponseTime", query = "SELECT a FROM AuthLog a WHERE a.responseTime = :responseTime"),
    @NamedQuery(name = "AuthLog.findByHieMsgId", query = "SELECT a FROM AuthLog a WHERE a.hieMsgId = :hieMsgId"),
    @NamedQuery(name = "AuthLog.findByServicingOrg", query = "SELECT a FROM AuthLog a WHERE a.servicingOrg = :servicingOrg")})
public class AuthLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "msg_id")
    private Integer msgId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "requestor")
    private String requestor;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "xacml_request")
    private String xacmlRequest;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "xacml_response")
    private String xacmlResponse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "decision")
    private String decision;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "obligations")
    private String obligations;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "purpose_of_use")
    private String purposeOfUse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "healthcare_object")
    private String healthcareObject;
    @Size(max = 45)
    @Column(name = "unique_identifier")
    private String uniqueIdentifier;
    @Basic(optional = false)
    @NotNull
    @Column(name = "msg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date msgDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "response_time")
    private long responseTime;
    @Size(max = 100)
    @Column(name = "hie_msg_id")
    private String hieMsgId;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "gen_drl")
    private String genDrl;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "exec_rules")
    private String execRules;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "annotated_doc")
    private String annotatedDoc;
    @Size(max = 100)
    @Column(name = "servicing_org")
    private String servicingOrg;

    public AuthLog() {
    }

    public AuthLog(Integer msgId) {
        this.msgId = msgId;
    }

    public AuthLog(Integer msgId, String requestor, String xacmlRequest, String xacmlResponse, String decision, String purposeOfUse, String healthcareObject, Date msgDate, long responseTime) {
        this.msgId = msgId;
        this.requestor = requestor;
        this.xacmlRequest = xacmlRequest;
        this.xacmlResponse = xacmlResponse;
        this.decision = decision;
        this.purposeOfUse = purposeOfUse;
        this.healthcareObject = healthcareObject;
        this.msgDate = msgDate;
        this.responseTime = responseTime;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getXacmlRequest() {
        return xacmlRequest;
    }

    public void setXacmlRequest(String xacmlRequest) {
        this.xacmlRequest = xacmlRequest;
    }

    public String getXacmlResponse() {
        return xacmlResponse;
    }

    public void setXacmlResponse(String xacmlResponse) {
        this.xacmlResponse = xacmlResponse;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getObligations() {
        return obligations;
    }

    public void setObligations(String obligations) {
        this.obligations = obligations;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getHealthcareObject() {
        return healthcareObject;
    }

    public void setHealthcareObject(String healthcareObject) {
        this.healthcareObject = healthcareObject;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getHieMsgId() {
        return hieMsgId;
    }

    public void setHieMsgId(String hieMsgId) {
        this.hieMsgId = hieMsgId;
    }

    public String getGenDrl() {
        return genDrl;
    }

    public void setGenDrl(String genDrl) {
        this.genDrl = genDrl;
    }

    public String getExecRules() {
        return execRules;
    }

    public void setExecRules(String execRules) {
        this.execRules = execRules;
    }

    public String getAnnotatedDoc() {
        return annotatedDoc;
    }

    public void setAnnotatedDoc(String annotatedDoc) {
        this.annotatedDoc = annotatedDoc;
    }

    public String getServicingOrg() {
        return servicingOrg;
    }

    public void setServicingOrg(String servicingOrg) {
        this.servicingOrg = servicingOrg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (msgId != null ? msgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuthLog)) {
            return false;
        }
        AuthLog other = (AuthLog) object;
        if ((this.msgId == null && other.msgId != null) || (this.msgId != null && !this.msgId.equals(other.msgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.ehtac.ds4p.jpa.AuthLog[ msgId=" + msgId + " ]";
    }
    
}
