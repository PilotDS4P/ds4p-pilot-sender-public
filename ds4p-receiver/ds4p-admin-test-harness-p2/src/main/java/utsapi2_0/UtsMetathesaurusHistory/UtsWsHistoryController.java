/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */

package UtsMetathesaurusHistory;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "UtsWsHistoryController", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UtsWsHistoryController {


    /**
     * 
     * @param ticket
     * @param atomId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.AtomMovementDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAtomMovements", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetAtomMovements")
    @ResponseWrapper(localName = "getAtomMovementsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetAtomMovementsResponse")
    public List<AtomMovementDTO> getAtomMovements(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "atomId", targetNamespace = "")
        String atomId)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param conceptId
     * @param version
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getBequeathedToConceptCuis", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetBequeathedToConceptCuis")
    @ResponseWrapper(localName = "getBequeathedToConceptCuisResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetBequeathedToConceptCuisResponse")
    public List<String> getBequeathedToConceptCuis(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "conceptId", targetNamespace = "")
        String conceptId)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param fromVersion
     * @param conceptId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.ConceptBequeathalDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getConceptBequeathals", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetConceptBequeathals")
    @ResponseWrapper(localName = "getConceptBequeathalsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetConceptBequeathalsResponse")
    public List<ConceptBequeathalDTO> getConceptBequeathals(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "conceptId", targetNamespace = "")
        String conceptId,
        @WebParam(name = "fromVersion", targetNamespace = "")
        String fromVersion)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param fromVersion
     * @param conceptId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.ConceptDeathDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getConceptDeletions", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetConceptDeletions")
    @ResponseWrapper(localName = "getConceptDeletionsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetConceptDeletionsResponse")
    public List<ConceptDeathDTO> getConceptDeletions(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "conceptId", targetNamespace = "")
        String conceptId,
        @WebParam(name = "fromVersion", targetNamespace = "")
        String fromVersion)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param fromVersion
     * @param conceptId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.ConceptMergeDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getConceptMerges", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetConceptMerges")
    @ResponseWrapper(localName = "getConceptMergesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetConceptMergesResponse")
    public List<ConceptMergeDTO> getConceptMerges(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "conceptId", targetNamespace = "")
        String conceptId,
        @WebParam(name = "fromVersion", targetNamespace = "")
        String fromVersion)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param termId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.TermMergeDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTermMerges", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetTermMerges")
    @ResponseWrapper(localName = "getTermMergesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetTermMergesResponse")
    public List<TermMergeDTO> getTermMerges(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "termId", targetNamespace = "")
        String termId)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param conceptId
     * @param version
     * @return
     *     returns java.lang.String
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedToConceptCui", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetMergedToConceptCui")
    @ResponseWrapper(localName = "getMergedToConceptCuiResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetMergedToConceptCuiResponse")
    public String getMergedToConceptCui(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "conceptId", targetNamespace = "")
        String conceptId)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param termId
     * @param version
     * @return
     *     returns java.lang.String
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedToTermUi", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetMergedToTermUi")
    @ResponseWrapper(localName = "getMergedToTermUiResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetMergedToTermUiResponse")
    public String getMergedToTermUi(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "termId", targetNamespace = "")
        String termId)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param atomId
     * @param version
     * @return
     *     returns java.lang.String
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMovedToConceptCui", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetMovedToConceptCui")
    @ResponseWrapper(localName = "getMovedToConceptCuiResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetMovedToConceptCuiResponse")
    public String getMovedToConceptCui(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "atomId", targetNamespace = "")
        String atomId)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param source
     * @param ticket
     * @param conceptId
     * @param saui
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.SourceAtomChangeDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSourceAtomChanges", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetSourceAtomChanges")
    @ResponseWrapper(localName = "getSourceAtomChangesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetSourceAtomChangesResponse")
    public List<SourceAtomChangeDTO> getSourceAtomChanges(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "conceptId", targetNamespace = "")
        String conceptId,
        @WebParam(name = "source", targetNamespace = "")
        String source,
        @WebParam(name = "saui", targetNamespace = "")
        String saui)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param fromVersion
     * @param termId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.TermDeathDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTermDeletions", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetTermDeletions")
    @ResponseWrapper(localName = "getTermDeletionsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetTermDeletionsResponse")
    public List<TermDeathDTO> getTermDeletions(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "termId", targetNamespace = "")
        String termId,
        @WebParam(name = "fromVersion", targetNamespace = "")
        String fromVersion)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param fromVersion
     * @param termStringId
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusHistory.TermStringDeathDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTermStringDeletions", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetTermStringDeletions")
    @ResponseWrapper(localName = "getTermStringDeletionsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusHistory.GetTermStringDeletionsResponse")
    public List<TermStringDeathDTO> getTermStringDeletions(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "termStringId", targetNamespace = "")
        String termStringId,
        @WebParam(name = "fromVersion", targetNamespace = "")
        String fromVersion)
        throws UtsFault_Exception
    ;

}
