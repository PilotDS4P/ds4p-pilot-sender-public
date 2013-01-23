/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.displayobjects;

/**
 *
 * @author Duane DeCouteau
 */
public class ExchangeResults {
    private String patientName;
    private String patientId;
    private String docId;
    private String docName;
    private String docType;
    private String docDate;
    private String orgName;
    private String mesgId;
    private String respId;
    private String currentDocument = "";
    private byte[] keyD;
    private byte[] keyM;

    public ExchangeResults() {
        
    }
    
    public ExchangeResults(String patientname, String patientid, String docid, String docname, String doctype, String docdate, String orgname, String mesgid, String respid) {
        this.patientId = patientid;
        this.patientName = patientname;
        this.docId = docid;
        this.docName = docname;
        this.docType = doctype;
        this.docDate = docdate;
        this.orgName = orgname;
        this.mesgId = mesgid;
        this.respId = respid;
    }
    /**
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the docId
     */
    public String getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * @return the mesgId
     */
    public String getMesgId() {
        return mesgId;
    }

    /**
     * @param mesgId the mesgId to set
     */
    public void setMesgId(String mesgId) {
        this.mesgId = mesgId;
    }

    /**
     * @return the respId
     */
    public String getRespId() {
        return respId;
    }

    /**
     * @param respId the respId to set
     */
    public void setRespId(String respId) {
        this.respId = respId;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the docDate
     */
    public String getDocDate() {
        return docDate;
    }

    /**
     * @param docDate the docDate to set
     */
    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the currentDocument
     */
    public String getCurrentDocument() {
        return currentDocument;
    }

    /**
     * @param currentDocument the currentDocument to set
     */
    public void setCurrentDocument(String currentDocument) {
        this.currentDocument = currentDocument;
    }

    /**
     * @return the keyD
     */
    public byte[] getKeyD() {
        return keyD;
    }

    /**
     * @param keyD the keyD to set
     */
    public void setKeyD(byte[] keyD) {
        this.keyD = keyD;
    }

    /**
     * @return the keyM
     */
    public byte[] getKeyM() {
        return keyM;
    }

    /**
     * @param keyM the keyM to set
     */
    public void setKeyM(byte[] keyM) {
        this.keyM = keyM;
    }
}
