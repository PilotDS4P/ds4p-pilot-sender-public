/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.va.ehtac.ds4p.ws;

import gov.va.ehtac.ds4p.jpa.AuthLog;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author duanedecouteau
 */
@WebService()
public class DS4PAudit {
    String jndiERR = "DS4PAudit:JNDI Resource Error";
    String connectionERR = "DS4PAudit:Connection Error";
    String baseERR = "DS4PAuditPolicyWS";


    //for jpa stuff
    EntityManagerFactory emf = null;

    private EntityManagerFactory getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("DS4PACSServicesPU");
        }
        return emf;
    }
    protected void persist(Object object) {
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            em.persist(object);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
            //throw new RuntimeException(e);
        }
    }

    protected void update(Object object) {
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            em.merge(object);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
            //throw new RuntimeException(e);
        }
    }

    protected void delete(Object object) {
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            em.remove(object);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
            //throw new RuntimeException(e);
        }
    }

    protected void flush() {
        try {
            EntityManager em = getEntityManager().createEntityManager();
            em.setFlushMode(FlushModeType.COMMIT);
            em.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "saveAuthorizationEvent")
    public Boolean saveAuthorizationEvent(@WebParam(name = "authobj")
    AuthLog authobj) {
        Boolean res = new Boolean(true);
        try {
            persist(authobj);
        }
        catch (Exception ex) {
            res = new Boolean(false);
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllAuthorizationEvent")
    public List<gov.va.ehtac.ds4p.jpa.AuthLog> getAllAuthorizationEvent() {
        List<AuthLog> res = new ArrayList();
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findAll");
            res = q.getResultList();
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAuthorizationByUId")
    public List<gov.va.ehtac.ds4p.jpa.AuthLog> getAuthorizationByUId(@WebParam(name = "uniqueId")
    String uniqueId) {
        List<AuthLog> res = null;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByUniqueIdentifier");
            q.setParameter("uniqueIdentifier", uniqueId);
            res = q.getResultList();
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAuthorizationByHealthCareObject")
    public List<gov.va.ehtac.ds4p.jpa.AuthLog> getAuthorizationByHealthCareObject(@WebParam(name = "healthcareobject")
    String healthcareobject) {
        List<AuthLog> res = null;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHealthcareObject");
            q.setParameter("healthcareObject", healthcareobject);
            res = q.getResultList();
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAuthorizationByStartEndDates")
    public List<gov.va.ehtac.ds4p.jpa.AuthLog> getAuthorizationByStartEndDates(@WebParam(name = "startdate")
    String startdate, @WebParam(name = "enddate")
    String enddate) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAuthorizationEventByHIEMsgId")
    public AuthLog getAuthorizationEventByHIEMsgId(@WebParam(name = "hie_msg_id") String hie_msg_id) {
        AuthLog res = null;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHieMsgId");
            q.setParameter("hieMsgId", hie_msg_id);
            res = (AuthLog)q.getSingleResult();
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
        }
        return res;
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateAuthorizationEventWithDRL")
    public Boolean updateAuthorizationEventWithDRL(@WebParam(name = "hie_msg_id") String hie_msg_id, @WebParam(name = "genDrl") String genDrl) {
        Boolean res = Boolean.TRUE;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHieMsgId");
            q.setParameter("hieMsgId", hie_msg_id);
            AuthLog obj = (AuthLog)q.getSingleResult();
            obj.setGenDrl(genDrl);
            update(obj);
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
            res = Boolean.FALSE;
        }
        return res;
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateAuthorizationEventWithExecRules")
    public Boolean updateAuthorizationEventWithExecRules(@WebParam(name = "hie_msg_id") String hie_msg_id, @WebParam(name = "execRules") String execRules) {
        Boolean res = Boolean.TRUE;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHieMsgId");
            q.setParameter("hieMsgId", hie_msg_id);
            AuthLog obj = (AuthLog)q.getSingleResult();
            obj.setExecRules(execRules);
            update(obj);
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
            res = Boolean.FALSE;
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateAuthorizationEventWithAnnotatedDoc")
    public Boolean updateAuthorizationEventWithAnnotatedDoc(@WebParam(name = "hie_msg_id") String hie_msg_id, @WebParam(name = "doc") String doc) {
        Boolean res = Boolean.TRUE;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHieMsgId");
            q.setParameter("hieMsgId", hie_msg_id);
            AuthLog obj = (AuthLog)q.getSingleResult();
            obj.setAnnotatedDoc(doc);
            update(obj);
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
            res = Boolean.FALSE;
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getObligationsByMessageId")
    public List<String> getObligationsByMessageId(@WebParam(name = "messageId") String messageId) {
        List<String> res = new ArrayList();
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHieMsgId");
            q.setParameter("hieMsgId", messageId);
            AuthLog obj = (AuthLog)q.getSingleResult();
            String oList = obj.getObligations();
            oList = oList.replaceAll("\n", " ");
            StringTokenizer st = new StringTokenizer(oList);
            try {
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token != null || token.length() > 1) {
                        res.add(token);
                    }
                }
            }
            catch (Exception stx) {
                stx.printStackTrace();
            }
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
        }        
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getPurposeOfUseByMessageId")
    public String getPurposeOfUseByMessageId(@WebParam(name = "messageId") String messageId) {
        String res = "";
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("AuthLog.findByHieMsgId");
            q.setParameter("hieMsgId", messageId);
            AuthLog obj = (AuthLog)q.getSingleResult();
            res = obj.getPurposeOfUse();
            t.commit();
        }
        catch (Exception ex) {
            System.err.println(baseERR + ex.getMessage());
        }        
        return res;
    }
    
    
    
}
