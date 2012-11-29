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

import gov.va.ds4p.cas.constants.DS4PConstants;
import gov.va.ehtac.ds4p.jpa.Directprocessing;
import gov.va.ehtac.ds4p.xdm.XDMProcessing;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.*;

/**
 *
 * @author Duane DeCouteau
 */
@WebService(serviceName = "NwHINDirectDocumentProcessing")
public class NwHINDirectDocumentProcessing {
    String jndiERR = "NwHINDirectDocumentProcessing:JNDI Resource Error";
    String connectionERR = "NwHINDirectDocumentProcessing:Connection Error";
    String baseERR = "NwHINDirectDocumentProcessing";
    NwHINDirectAuthorizationServices pep = new NwHINDirectAuthorizationServices();

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
    @WebMethod(operationName = "persistXDMObject")
    public Boolean persistXDMObject(@WebParam(name = "xdmobjects") Directprocessing xdmobjects) {
        Boolean res = new Boolean(true);
        try {
            persist(xdmobjects);
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
    @WebMethod(operationName = "getXDMObject")
    public Directprocessing getXDMObject(@WebParam(name = "id") Integer id) {
        Directprocessing res = null;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("Directprocessing.findByIddirectprocessing");
            q.setParameter("iddirectprocessing", id);
            res = (Directprocessing)q.getSingleResult();
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
    @WebMethod(operationName = "getXDMObjectListByProviderId")
    public List<Directprocessing> getXDMObjectListProviderId(@WebParam(name = "providerId") String providerId) {
        List<Directprocessing> res = new ArrayList();
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("Directprocessing.findByProviderId");
            q.setParameter("providerId", providerId);
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
    @WebMethod(operationName = "updateXDMObject")
    public Boolean updateXDMObject(@WebParam(name = "xdmobject") Directprocessing xdmobject) {
        Boolean res = Boolean.TRUE;
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("Directprocessing.findByIddirectprocessing");
            q.setParameter("iddirectprocessing", xdmobject.getIddirectprocessing());
            Directprocessing obj = (Directprocessing)q.getSingleResult();
            obj.setCdaxsl(xdmobject.getCdaxsl());
            obj.setDockey(xdmobject.getDockey());
            obj.setDockeyMasking(xdmobject.getDockeyMasking());
            obj.setDocument(xdmobject.getDocument());
            obj.setEncrypteddocument(xdmobject.getEncrypteddocument());
            obj.setMetadata(xdmobject.getMetadata());
            obj.setPatientHomeCommunity(xdmobject.getPatientHomeCommunity());
            obj.setPatientName(xdmobject.getPatientName());
            obj.setProviderId(xdmobject.getProviderId());
            obj.setUniquePatientId(xdmobject.getUniquePatientId());
            obj.setXdmfile(xdmobject.getXdmfile());
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
    @WebMethod(operationName = "getXDMObjectListByPatientId")
    public List<Directprocessing> getXDMObjectListByPatientId(@WebParam(name = "patientId") String patientId) {
        List<Directprocessing> res = new ArrayList();
        try {
            EntityManager em = getEntityManager().createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Query q = em.createNamedQuery("Directprocessing.findByUniquePatientId");
            q.setParameter("uniquePatientId", patientId);
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
    @WebMethod(operationName = "unpackXDMPackage")
    public Boolean unpackXDMPackage(@WebParam(name = "payload") byte[] payload) {
        //TODO write your implementation code here:
        XDMProcessing xdm = new XDMProcessing();
        Directprocessing dp = xdm.processFile(payload);
        Boolean res = new Boolean(true);
        try {
            persist(dp);
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
    @WebMethod(operationName = "unpackEnforceXDMPackage")
    public Boolean unpackEnforceXDMPackage(@WebParam(name = "payload") byte[] payload, @WebParam(name = "providerId") String providerId, @WebParam(name = "recipientId") String recipientId, 
                                @WebParam(name = "organizationName") String organizationName, 
                                @WebParam(name = "organizationId") String organizationId, 
                                @WebParam(name = "role") String role, 
                                @WebParam(name = "purposeOfUse") String purposeOfUse, 
                                @WebParam(name = "servicePermissions") List<String> servicePermissions, 
                                @WebParam(name = "sensitivityPrivileges") List<String> sensitivityPrivileges, 
                                @WebParam(name = "intendedPurposeOfUse") String intendedPurposeOfUse, 
                                @WebParam(name = "intendedRecipient") String intendedRecipient, 
                                @WebParam(name = "requiredPermission") List<String> requiredPermission, 
                                @WebParam(name = "requiredSensitivityPrivileges") List<String> requiredSensitivityPrivileges, 
                                @WebParam(name = "requestedResource") String requestedResource, 
                                @WebParam(name = "uniquePatientId") String uniquePatientId) {
        //determine if its ok to recieve
        Boolean res = Boolean.TRUE;
        requestedResource = "NwHINDirectReceive";
        requiredPermission = new ArrayList();
        requiredSensitivityPrivileges = new ArrayList();
        requiredPermission.add("NwHINDirectReceive");
        String decision = pep.enforceDirectPolicy(providerId, recipientId, organizationName, organizationId, role, purposeOfUse, servicePermissions, sensitivityPrivileges, intendedPurposeOfUse, intendedRecipient, requiredPermission, requiredSensitivityPrivileges, requestedResource, uniquePatientId);
        if (decision.equals(DS4PConstants.PERMIT)) {
            XDMProcessing xdm = new XDMProcessing();
            Directprocessing dp = xdm.processFile(payload);
            //now that metadata is available determine if its ok to collect information
            requestedResource = "NwHINDirectCollect";
            requiredPermission.clear();
            requiredPermission.add("NwHINDirectCollect");
            requiredSensitivityPrivileges = xdm.getReqSensitivity();
            organizationName = dp.getPatientHomeCommunity();
            organizationId = dp.getOriginatingFacility();
            intendedPurposeOfUse = dp.getPou();
            intendedRecipient = dp.getProviderId();
            uniquePatientId = dp.getUniquePatientId();
            //get decision
            decision = pep.enforceDirectPolicy(providerId, recipientId, organizationName, organizationId, role, purposeOfUse, servicePermissions, sensitivityPrivileges, intendedPurposeOfUse, intendedRecipient, requiredPermission, requiredSensitivityPrivileges, requestedResource, uniquePatientId);
            if (decision.equals(DS4PConstants.PERMIT)) {
                try {
                    persist(dp);
                }
                catch (Exception ex) {
                    res = new Boolean(false);
                    ex.printStackTrace();
                }   
            }
            else {
                res = Boolean.FALSE;
            }
        } 
        else {
            res = Boolean.FALSE;
        }
        return res;
    }
    
}
