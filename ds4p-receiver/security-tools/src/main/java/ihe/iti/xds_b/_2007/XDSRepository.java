package ihe.iti.xds_b._2007;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * This class was generated by Apache CXF 2.6.0
 * 2012-12-04T11:19:24.307-07:00
 * Generated source version: 2.6.0
 * 
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "XDSRepository")
@XmlSeeAlso({ObjectFactory.class, oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory.class, oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory.class, oasis.names.tc.ebxml_regrep.xsd.lcm._3.ObjectFactory.class, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface XDSRepository {

    @WebResult(name = "RetrieveDocumentSetResponse", targetNamespace = "urn:ihe:iti:xds-b:2007", partName = "RetrieveDocumentSetResult")
    @Action(input = "urn:ihe:iti:2007:RetrieveDocumentSet", output = "urn:ihe:iti:2007:RetrieveDocumentSetResponse")
    @WebMethod(operationName = "RetrieveDocumentSet", action = "urn:ihe:iti:2007:RetrieveDocumentSet")
    public RetrieveDocumentSetResponse retrieveDocumentSet(
        @WebParam(partName = "input", name = "RetrieveDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007")
        RetrieveDocumentSetRequest input
    );

    @WebResult(name = "ProvideAndRegisterDocumentSetResponse", targetNamespace = "urn:ihe:iti:xds-b:2007", partName = "ProvideAndRegisterDocumentSetResult")
    @Action(input = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b", output = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse")
    @WebMethod(operationName = "ProvideAndRegisterDocumentSet", action = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b")
    public ProvideAndRegisterDocumentSetResponse provideAndRegisterDocumentSet(
        @WebParam(partName = "input", name = "ProvideAndRegisterDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007")
        ProvideAndRegisterDocumentSetRequest input
    );
}
