/*
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
*/

package gov.va.ds4p.security.saml;

import com.sun.xml.wss.impl.callback.SAMLAssertionValidator;
import com.sun.xml.wss.impl.callback.SAMLAssertionValidator.SAMLValidationException;
import java.text.SimpleDateFormat;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;

/**
 *
 * @author Duane DeCouteau
 */
public class SAMLValidator implements SAMLAssertionValidator {
    
    
    private SimpleDateFormat ds = new SimpleDateFormat("MM/dd/yyyy");
    // todo move this out into config file
    
    //private XACMLContextHandler handler = new XACMLContextHandler();
    
    public void validate(Element arg0) throws SAMLValidationException {
        throw new UnsupportedOperationException("SAMLAssertionValidator : Operation Should not be Called1.");
    }

    public void validate(XMLStreamReader arg0) throws SAMLValidationException {
        throw new UnsupportedOperationException("SAMLAssertionValidator : Operation Should not be Called1.");

    }

}
