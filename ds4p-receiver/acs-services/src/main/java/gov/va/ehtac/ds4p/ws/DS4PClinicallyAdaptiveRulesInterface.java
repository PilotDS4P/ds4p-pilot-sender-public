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

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.*;

import gov.va.ehtac.ds4p.jpa.OrganizationalPolicy;
import gov.va.ehtac.ds4p.jpa.Clinicaltagrule;
import gov.va.ehtac.ds4p.ruleprocessing.RuleGenerator;
import java.util.ArrayList;

/**
 *
 * @author Duane DeCouteau
 */
@WebService(serviceName = "DS4PClinicallyAdaptiveRulesInterface")
public class DS4PClinicallyAdaptiveRulesInterface {
    //for jpa stuff
    EntityManagerFactory emf = null;
    
    private DS4PAudit audit = new DS4PAudit();

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
    @WebMethod(operationName = "setOrganizationalPolicy")
    public Boolean setOrganizationalPolicy(@WebParam(name = "policy")
    OrganizationalPolicy policy) {
        Boolean res = Boolean.TRUE;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("OrganizationalPolicy.findByIdorganizationalPolicy");
            q.setParameter("idorganizationalPolicy", (policy.getIdorganizationalPolicy()));
            OrganizationalPolicy p = (OrganizationalPolicy)q.getSingleResult();
            p.setHomeCommunityId(policy.getHomeCommunityId());
            p.setOrganizationName(policy.getOrganizationName());
            p.setApplicableUsLaw(policy.getApplicableUsLaw());
            p.setOrganizationalRules(policy.getOrganizationalRules());
            update(p);
            t.commit();
            em.close();
        }
        catch (Exception ex) {
            System.err.println("DS4PClinicallyAdaptiveRulesInterface:setOrganizationalPolicy "+ex.getMessage());            
            res = Boolean.FALSE;
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getOrganizationalPolicy")
    public OrganizationalPolicy getOrganizationalPolicy(@WebParam(name = "homeCommunityId")
    String homeCommunityId) {
        OrganizationalPolicy res = null;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("OrganizationalPolicy.findByHomeCommunityId");
            q.setParameter("homeCommunityId", homeCommunityId);
            res = (OrganizationalPolicy)q.getSingleResult();
            t.commit();
            em.close();
        }
        catch (Exception ex) {
            System.err.println("DS4PClinicallyAdaptiveRulesInterface:getOrganizationalPolicy "+ex.getMessage());
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllOrganizationalPolicy")
    public List<OrganizationalPolicy> getAllOrganizationalPolicy() {
        List<OrganizationalPolicy> res = new ArrayList();
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("OrganizationalPolicy.findAll");
            res = q.getResultList();
            t.commit();
            em.close();
        }
        catch (Exception ex) {
            System.err.println("DS4PClinicallyAdaptiveRulesInterface:getAllOrganizationalPolicy "+ex.getMessage());
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getClinicalDomainRule")
    public Clinicaltagrule getClinicalDomainRule(@WebParam(name = "domainLoincCode") String domainLoincCode) {
        Clinicaltagrule res = null;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("Clinicaltagrule.findByLoincCode");
            q.setParameter("loincCode", domainLoincCode);
            res = (Clinicaltagrule)q.getSingleResult();
            t.commit();
            em.close();
        }
        catch (Exception ex) {
            System.err.println("DS4PClinicallyAdaptiveRulesInterface:getClinicalDomainRule "+ex.getMessage());
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "setClinicalDomainTaggingRule")
    public Boolean setClinicalDomainTaggingRule(@WebParam(name = "taggingRule") Clinicaltagrule taggingRule) {
        Boolean res = Boolean.TRUE;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("Clinicaltagrule.findByIdclinicaltagrule");
            q.setParameter("idclinicaltagrule", (taggingRule.getIdclinicaltagrule()));
            Clinicaltagrule p = (Clinicaltagrule)q.getSingleResult();
            p.setLoincCode(taggingRule.getLoincCode());
            p.setLoincDisplayName(taggingRule.getLoincDisplayName());
            p.setObservationRules(taggingRule.getObservationRules());
            update(p);
            t.commit();
            em.close();
        }
        catch (Exception ex) {
            System.err.println("DS4PClinicallyAdaptiveRulesInterface:setOrganizationalPolicy "+ex.getMessage());            
            res = Boolean.FALSE;
            ex.printStackTrace();
        }
        return res;
    }


    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCASRuleSetStringByPOUObligationsAndHomeCommunityId")
    public String getCASRuleSetStringByPOUObligationsAndHomeCommunityId(@WebParam(name = "pou") String pou, @WebParam(name = "obligations") List<String> obligations, @WebParam(name = "homeCommunityId") String homeCommunityId, @WebParam(name = "messageId") String messageId) {
        String res = "";
        System.out.println("xSystem MessageId: "+messageId);
        RuleGenerator rg = new RuleGenerator();
        res = rg.GenerateRule(pou, obligations, homeCommunityId);
        try {
            Boolean b = audit.updateAuthorizationEventWithDRL(messageId, res);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(res);
        return res;
    }


    
}
