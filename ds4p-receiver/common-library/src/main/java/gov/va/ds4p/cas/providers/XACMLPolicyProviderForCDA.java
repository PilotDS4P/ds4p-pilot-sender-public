/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.cas.providers;

import gov.va.ds4p.policy.reference.OrganizationPolicy;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class XACMLPolicyProviderForCDA {
    StringBuffer buffer = new StringBuffer();
    
    public XACMLPolicyProviderForCDA() {
        
        
        
    }
    
    public String createPatientConsentXACMLPolicy(OrganizationPolicy orgPolicy, String patientId,  
                                     String authorization, List<String> allowedPOU, List<String> allowedRecipients, 
                                     List<String> maskingActions) {
        String res = "";
        createPolicySetHeader(patientId, orgPolicy.getHomeCommunityId());
        createPolicyHeader();
        createPolicyTarget();
        createRuleAuthorization();
        createRuleAllowedPOU(allowedPOU);
        createRuleAllowedRecipients(allowedRecipients);
        createRuleRequiredSensitivityPermissions(maskingActions);
        createEndPolicy();
        createRuleEmergency();
        createEndPolicySet();
        res = buffer.toString();
        return res;
    }
    
    private void createPolicySetHeader(String patientId, String homeCommunityId) {
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<PolicySet xmlns=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\" ");
        buffer.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        buffer.append("xsi:schemaLocation=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os http://docs.oasis-open.org/xacml/access_control-xacml-2.0-policy-schema-os.xsd\" ");
        buffer.append("PolicySetId=\""+patientId+"-"+homeCommunityId+"\" ");
        buffer.append("PolicyCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides\">");
        buffer.append("<Target/>");
    }
    
    private void createPolicyHeader() {
        buffer.append("<Policy PolicyId=\"urn:oasis:names:tc:xspa:1.0:nwhin:exchange:query\" ");
        buffer.append("RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides\">");
        buffer.append("<Description>Denies the request if authorization does not exist.</Description>");
    }
    
    private void createPolicyTarget() {
        buffer.append("<Target>");
        buffer.append("<Resources>");
        
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">DocumentQuery</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">DocumentRetrieve</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        
        
        buffer.append("</Resources>");
        buffer.append("</Target>");  
    }
    
    private void createRuleAuthorization() {
        buffer.append("<Rule RuleId=\"urn:gov:hhs:fha:nhinc:docquery:notauthorized\" Effect=\"Permit\">");
        buffer.append("<Description>If request is to disclose then permit.</Description>");
        buffer.append("<Target/>");
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Disclose</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:org:mitre:resource:patient:authorization\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
    }
    
    private void createRuleAllowedPOU(List<String> allowedPOUs) {
        buffer.append("<Rule RuleId=\"urn:gov:hhs:fha:nhinc:docquery:allowedpous\" Effect=\"Deny\">");
        buffer.append("<Description>If request in not in list then deny.</Description>");
        buffer.append("<Target/>");
        buffer.append("<Condition>");

        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        
        Iterator iter = allowedPOUs.iterator();
        while (iter.hasNext()) {
            String pou = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+pou+"</AttributeValue>");
        }
        
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");        
    }
    
    private void createRuleAllowedRecipients(List<String> allowedRecipients) {
        buffer.append("<Rule RuleId=\"urn:gov:hhs:fha:nhinc:docquery:allowedrecipients\" Effect=\"Deny\">");
        buffer.append("<Description>If request in not in list then deny.</Description>");
        buffer.append("<Target/>");
        buffer.append("<Condition>");

        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:subject:subject-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        
        Iterator iter = allowedRecipients.iterator();
        while (iter.hasNext()) {
            String recipient = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+recipient+"</AttributeValue>");
        }
        
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");        
    }
    
    private void createRuleRequiredSensitivityPermissions(List<String> maskingActions) {
        buffer.append("<Rule RuleId=\"urn:gov:hhs:fha:nhinc:docquery:requiredpermissions\" Effect=\"Deny\">");
        buffer.append("<Description>If request in not in list then deny.</Description>");
        buffer.append("<Target/>");
        buffer.append("<Condition>");
        
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-subset\">");

        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        
        Iterator iter = maskingActions.iterator();
        while (iter.hasNext()) {
            String mask = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+mask+"</AttributeValue>");
        }
        
        buffer.append("</Apply>");
        buffer.append("<SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:subject:sensitivity:privileges\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");

        
        
        buffer.append("</Condition>");
        buffer.append("</Rule>");                
    }
    
    private void createRuleEmergency() {
        buffer.append("<Policy PolicyId=\"urn:gov:hhs:fha:nhinc:anyServiceType:EmergencyPolicy\" RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:permit-overrides\">");
        buffer.append("<Target/>");
        buffer.append("<Rule RuleId=\"urn:gov:hhs:fha:nhinc:anyServiceType:EmergencyRule\" Effect=\"Permit\">");
        buffer.append("<Description>Purpose of Use is Emergency so Permit All</Description>");
        buffer.append("<Target/>");
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">ETREAT</AttributeValue>");
        buffer.append("<SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        buffer.append("</Policy>");      
    }
    
    private void createEndPolicy() {
        buffer.append("</Policy>");
    }
    
    private void createEndPolicySet() {
        buffer.append("</PolicySet>");
    }
    
}
