/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.ds4p.kairon;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Duane DeCouteau
 */
public class KaironPolicyObject {
	private String fname;
	private String lname;
	private String query;
	private String purpose;
	private String email;
	private String pid;
	private String rid ;
	private String tid;
	private String status;
	private String sender;
	private List<String> topicListRedact = new ArrayList();
        private List<String> topicListMask = new ArrayList();
	
	private JSONObject jsonPolicy = null;

        public KaironPolicyObject() {
            
        }
        
        public KaironPolicyObject(String jsonStr) {
            try {
                JSONObject jsonObj =  (JSONObject) new JSONTokener(jsonStr).nextValue();

                JSONObject policyWrapper = null;
                if (jsonObj != null)
                            policyWrapper = jsonObj;

                if (policyWrapper != null)
                {
                        jsonPolicy = policyWrapper.getJSONObject("PolicyQuery");
                }
                String patientAcctID = jsonPolicy.getString("PatientAcctID");
                if (patientAcctID != null)
                        pid = patientAcctID;

                query = jsonPolicy.getString("Query");
                rid = jsonPolicy.getString("Recipient");					
                purpose = jsonPolicy.getString("Purpose");
                sender = jsonPolicy.getString("Sender");
                try {
                    JSONArray jArray = jsonPolicy.getJSONArray("sensitivityCodeMASK");
                    topicListMask.clear();
                    for (int i = 0; i < jArray.length(); i++) {
                        String s = (String)jArray.get(i);
                        topicListMask.add(s);
                    }
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                try {
                    JSONArray jArray = jsonPolicy.getJSONArray("sensitivityCodesREDACT");
                    topicListRedact.clear();
                    for (int i = 0; i < jArray.length(); i++) {
                        String s = (String)jArray.get(i);
                        topicListRedact.add(s);
                    }
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                
                status = jsonPolicy.getString("status");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return the rid
     */
    public String getRid() {
        return rid;
    }

    /**
     * @param rid the rid to set
     */
    public void setRid(String rid) {
        this.rid = rid;
    }

    /**
     * @return the tid
     */
    public String getTid() {
        return tid;
    }

    /**
     * @param tid the tid to set
     */
    public void setTid(String tid) {
        this.tid = tid;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }


    /**
     * @return the jsonPolicy
     */
    public JSONObject getJsonPolicy() {
        return jsonPolicy;
    }

    /**
     * @param jsonPolicy the jsonPolicy to set
     */
    public void setJsonPolicy(JSONObject jsonPolicy) {
        this.jsonPolicy = jsonPolicy;
    }

    /**
     * @return the topicListRedact
     */
    public List<String> getTopicListRedact() {
        return topicListRedact;
    }

    /**
     * @param topicListRedact the topicListRedact to set
     */
    public void setTopicListRedact(List<String> topicListRedact) {
        this.topicListRedact = topicListRedact;
    }

    /**
     * @return the topicListMask
     */
    public List<String> getTopicListMask() {
        return topicListMask;
    }

    /**
     * @param topicListMask the topicListMask to set
     */
    public void setTopicListMask(List<String> topicListMask) {
        this.topicListMask = topicListMask;
    }
}
