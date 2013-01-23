/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */

package UtsMetathesaurusMetaData;

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
@WebService(name = "UtsWsMetadataController", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UtsWsMetadataController {


    /**
     * 
     * @param ticket
     * @param language
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.LanguageDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getLanguage", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetLanguage")
    @ResponseWrapper(localName = "getLanguageResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetLanguageResponse")
    public LanguageDTO getLanguage(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "language", targetNamespace = "")
        String language)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param source
     * @param ticket
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SourceDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSource", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSource")
    @ResponseWrapper(localName = "getSourceResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceResponse")
    public SourceDTO getSource(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "source", targetNamespace = "")
        String source)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param atn
     * @param ticket
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.AttributeNameDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAttributeName", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAttributeName")
    @ResponseWrapper(localName = "getAttributeNameResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAttributeNameResponse")
    public AttributeNameDTO getAttributeName(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "atn", targetNamespace = "")
        String atn)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param rsab
     * @param ticket
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.RootSourceDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRootSource", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetRootSource")
    @ResponseWrapper(localName = "getRootSourceResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetRootSourceResponse")
    public RootSourceDTO getRootSource(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rsab", targetNamespace = "")
        String rsab)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param relationLabel
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.RelationLabelDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRelationLabel", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetRelationLabel")
    @ResponseWrapper(localName = "getRelationLabelResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetRelationLabelResponse")
    public RelationLabelDTO getRelationLabel(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "relationLabel", targetNamespace = "")
        String relationLabel)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rela
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.AdditionalRelationLabelDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAdditionalRelationLabel", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAdditionalRelationLabel")
    @ResponseWrapper(localName = "getAdditionalRelationLabelResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAdditionalRelationLabelResponse")
    public AdditionalRelationLabelDTO getAdditionalRelationLabel(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rela", targetNamespace = "")
        String rela)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param tty
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.TermTypeDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTermType", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetTermType")
    @ResponseWrapper(localName = "getTermTypeResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetTermTypeResponse")
    public TermTypeDTO getTermType(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "tty", targetNamespace = "")
        String tty)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.ContactInformationDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAcquisitionContactInformation", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAcquisitionContactInformation")
    @ResponseWrapper(localName = "getAcquisitionContactInformationResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAcquisitionContactInformationResponse")
    public ContactInformationDTO getAcquisitionContactInformation(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param charset
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.CharacterSetDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCharacterSet", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetCharacterSet")
    @ResponseWrapper(localName = "getCharacterSetResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetCharacterSetResponse")
    public CharacterSetDTO getCharacterSet(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "charset", targetNamespace = "")
        String charset)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.ContactInformationDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getContentContactInformation", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetContentContactInformation")
    @ResponseWrapper(localName = "getContentContactInformationResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetContentContactInformationResponse")
    public ContactInformationDTO getContentContactInformation(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param coc
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.CooccurrenceTypeDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCooccurrenceType", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetCooccurrenceType")
    @ResponseWrapper(localName = "getCooccurrenceTypeResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetCooccurrenceTypeResponse")
    public CooccurrenceTypeDTO getCooccurrenceType(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "coc", targetNamespace = "")
        String coc)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param rsab
     * @param ticket
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SourceDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCurrentVersionSource", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetCurrentVersionSource")
    @ResponseWrapper(localName = "getCurrentVersionSourceResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetCurrentVersionSourceResponse")
    public SourceDTO getCurrentVersionSource(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rsab", targetNamespace = "")
        String rsab)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param generalMetadata
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.GeneralMetadataEntryDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getGeneralMetadataEntry", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetGeneralMetadataEntry")
    @ResponseWrapper(localName = "getGeneralMetadataEntryResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetGeneralMetadataEntryResponse")
    public List<GeneralMetadataEntryDTO> getGeneralMetadataEntry(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "generalMetadata", targetNamespace = "")
        String generalMetadata)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param identifier
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.IdentifierTypeDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getIdentifierType", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetIdentifierType")
    @ResponseWrapper(localName = "getIdentifierTypeResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetIdentifierTypeResponse")
    public IdentifierTypeDTO getIdentifierType(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "identifier", targetNamespace = "")
        String identifier)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.ContactInformationDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getLicenseContactInformation", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetLicenseContactInformation")
    @ResponseWrapper(localName = "getLicenseContactInformationResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetLicenseContactInformationResponse")
    public ContactInformationDTO getLicenseContactInformation(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param rsab
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRootSourceSynonymousNames", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetRootSourceSynonymousNames")
    @ResponseWrapper(localName = "getRootSourceSynonymousNamesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetRootSourceSynonymousNamesResponse")
    public List<String> getRootSourceSynonymousNames(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rsab", targetNamespace = "")
        String rsab)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param atn
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SourceAttributeNameDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSourceAttributeName", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceAttributeName")
    @ResponseWrapper(localName = "getSourceAttributeNameResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceAttributeNameResponse")
    public SourceAttributeNameDTO getSourceAttributeName(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource,
        @WebParam(name = "atn", targetNamespace = "")
        String atn)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param citation
     * @param ticket
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SourceCitationDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSourceCitation", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceCitation")
    @ResponseWrapper(localName = "getSourceCitationResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceCitationResponse")
    public SourceCitationDTO getSourceCitation(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "citation", targetNamespace = "")
        String citation)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rela
     * @param relationLabel
     * @param rootSource
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SourceRelationLabelDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSourceRelationLabel", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceRelationLabel")
    @ResponseWrapper(localName = "getSourceRelationLabelResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceRelationLabelResponse")
    public SourceRelationLabelDTO getSourceRelationLabel(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource,
        @WebParam(name = "relationLabel", targetNamespace = "")
        String relationLabel,
        @WebParam(name = "rela", targetNamespace = "")
        String rela)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param tty
     * @param rootSource
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SourceTermTypeDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSourceTermType", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceTermType")
    @ResponseWrapper(localName = "getSourceTermTypeResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSourceTermTypeResponse")
    public SourceTermTypeDTO getSourceTermType(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource,
        @WebParam(name = "tty", targetNamespace = "")
        String tty)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param atn
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceAttributeNameDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSubEquivalentAttributeNames", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSubEquivalentAttributeNames")
    @ResponseWrapper(localName = "getSubEquivalentAttributeNamesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSubEquivalentAttributeNamesResponse")
    public List<SourceAttributeNameDTO> getSubEquivalentAttributeNames(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "atn", targetNamespace = "")
        String atn,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param subheading
     * @param version
     * @return
     *     returns UtsMetathesaurusMetaData.SubheadingDTO
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSubheading", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSubheading")
    @ResponseWrapper(localName = "getSubheadingResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetSubheadingResponse")
    public SubheadingDTO getSubheading(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "subheading", targetNamespace = "")
        String subheading)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.AdditionalRelationLabelDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllAdditionalRelationLabels", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllAdditionalRelationLabels")
    @ResponseWrapper(localName = "getAllAdditionalRelationLabelsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllAdditionalRelationLabelsResponse")
    public List<AdditionalRelationLabelDTO> getAllAdditionalRelationLabels(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.AttributeNameDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllAttributeNames", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllAttributeNames")
    @ResponseWrapper(localName = "getAllAttributeNamesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllAttributeNamesResponse")
    public List<AttributeNameDTO> getAllAttributeNames(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.CharacterSetDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllCharacterSets", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllCharacterSets")
    @ResponseWrapper(localName = "getAllCharacterSetsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllCharacterSetsResponse")
    public List<CharacterSetDTO> getAllCharacterSets(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.CooccurrenceTypeDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllCooccurrenceTypes", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllCooccurrenceTypes")
    @ResponseWrapper(localName = "getAllCooccurrenceTypesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllCooccurrenceTypesResponse")
    public List<CooccurrenceTypeDTO> getAllCooccurrenceTypes(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param atn
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceAttributeNameDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllEquivalentAttributeNames", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllEquivalentAttributeNames")
    @ResponseWrapper(localName = "getAllEquivalentAttributeNamesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllEquivalentAttributeNamesResponse")
    public List<SourceAttributeNameDTO> getAllEquivalentAttributeNames(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "atn", targetNamespace = "")
        String atn,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.LanguageDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllLanguages", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllLanguages")
    @ResponseWrapper(localName = "getAllLanguagesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllLanguagesResponse")
    public List<LanguageDTO> getAllLanguages(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.RelationLabelDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllRelationLabels", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllRelationLabels")
    @ResponseWrapper(localName = "getAllRelationLabelsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllRelationLabelsResponse")
    public List<RelationLabelDTO> getAllRelationLabels(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.RootSourceDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllRootSources", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllRootSources")
    @ResponseWrapper(localName = "getAllRootSourcesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllRootSourcesResponse")
    public List<RootSourceDTO> getAllRootSources(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceAttributeNameDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSourceAttributeNames", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceAttributeNames")
    @ResponseWrapper(localName = "getAllSourceAttributeNamesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceAttributeNamesResponse")
    public List<SourceAttributeNameDTO> getAllSourceAttributeNames(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceCitationDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSourceCitations", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceCitations")
    @ResponseWrapper(localName = "getAllSourceCitationsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceCitationsResponse")
    public List<SourceCitationDTO> getAllSourceCitations(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceRelationLabelDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSourceRelationLabels", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceRelationLabels")
    @ResponseWrapper(localName = "getAllSourceRelationLabelsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceRelationLabelsResponse")
    public List<SourceRelationLabelDTO> getAllSourceRelationLabels(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSources", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSources")
    @ResponseWrapper(localName = "getAllSourcesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourcesResponse")
    public List<SourceDTO> getAllSources(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceTermTypeDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSourceTermTypes", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceTermTypes")
    @ResponseWrapper(localName = "getAllSourceTermTypesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSourceTermTypesResponse")
    public List<SourceTermTypeDTO> getAllSourceTermTypes(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SubheadingDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSubheadings", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSubheadings")
    @ResponseWrapper(localName = "getAllSubheadingsResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSubheadingsResponse")
    public List<SubheadingDTO> getAllSubheadings(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param atn
     * @param ticket
     * @param rootSource
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceAttributeNameDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllSuperAttributeNames", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSuperAttributeNames")
    @ResponseWrapper(localName = "getAllSuperAttributeNamesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllSuperAttributeNamesResponse")
    public List<SourceAttributeNameDTO> getAllSuperAttributeNames(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "atn", targetNamespace = "")
        String atn,
        @WebParam(name = "rootSource", targetNamespace = "")
        String rootSource)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.TermTypeDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllTermTypes", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllTermTypes")
    @ResponseWrapper(localName = "getAllTermTypesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetAllTermTypesResponse")
    public List<TermTypeDTO> getAllTermTypes(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version)
        throws UtsFault_Exception
    ;

    /**
     * 
     * @param rsab
     * @param ticket
     * @param version
     * @return
     *     returns java.util.List<UtsMetathesaurusMetaData.SourceDTO>
     * @throws UtsFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getVersionedSources", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetVersionedSources")
    @ResponseWrapper(localName = "getVersionedSourcesResponse", targetNamespace = "http://webservice.uts.umls.nlm.nih.gov/", className = "UtsMetathesaurusMetaData.GetVersionedSourcesResponse")
    public List<SourceDTO> getVersionedSources(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket,
        @WebParam(name = "version", targetNamespace = "")
        String version,
        @WebParam(name = "rsab", targetNamespace = "")
        String rsab)
        throws UtsFault_Exception
    ;

}
