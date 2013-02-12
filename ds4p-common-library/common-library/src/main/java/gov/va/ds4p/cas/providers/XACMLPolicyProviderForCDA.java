/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.cas.providers;

import gov.va.ds4p.policy.reference.OrganizationPolicy;
import gov.va.ds4p.policy.reference.OrganizationTaggingRules;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class XACMLPolicyProviderForCDA {
    StringBuffer buffer = new StringBuffer();
    //for demonstration just worry about the following org obligations    
    private static final String ORG_PRIVACY_LAW_TYPE = "USPrivacyLaw";
    private static final String ORG_REFRAIN_POLICY_TYPE = "RefrainPolicy";
    private static final String ORG_DOCUMENT_HANDLING_TYPE = "DocumentHandling";

    private List<String> orgLaw = new ArrayList();
    private List<String> refrainPolicy = new ArrayList();
    private List<String> documentHandling = new ArrayList();
    
    public XACMLPolicyProviderForCDA() {
        
        
        
    }
    
    public String createPatientConsentXACMLPolicy(OrganizationPolicy orgPolicy, String patientId,  
                                     String authorization, List<String> allowedPOU, List<String> allowedRecipients, 
                                     List<String> redactActions, List<String> maskingActions) {
        String res = "";
        createPolicySetHeader(patientId, orgPolicy.getHomeCommunityId());
        createPolicyHeader();
        createPolicyTarget();
        createRuleAuthorization();
        createRuleAllowedPOU(allowedPOU);
        createRuleAllowedRecipients(allowedRecipients);
        createRuleRequiredSensitivityPermissions(maskingActions);
        createEndPolicy();
        
        //add data segmentation directives
        if (!redactActions.isEmpty()) {
            createObligationPolicyRedact(redactActions);
        }
        if (!maskingActions.isEmpty()) {
            createObligationPolicyMask(maskingActions);
        }
        
        //add in organizational polices
        orgLaw.clear();
        refrainPolicy.clear();
        documentHandling.clear();
        getOrganizationalPolicyTags(orgPolicy, ORG_PRIVACY_LAW_TYPE);
        getOrganizationalPolicyTags(orgPolicy, ORG_REFRAIN_POLICY_TYPE);
        getOrganizationalPolicyTags(orgPolicy, ORG_DOCUMENT_HANDLING_TYPE);
        if (!orgLaw.isEmpty()) {
            createObligationPolicyUSPrivacyLaw();
        }
        if (!refrainPolicy.isEmpty()) {
            createObligationPolicyRefrain();
        }
        if (!documentHandling.isEmpty()) {
            createObligationPolicyDocumentHandling();
        }
        
        //Emergency Treatment Policy
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
        buffer.append("<!-- DISCLOSURE AUTHORIZATION POLICY -->");
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
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:patient:authorization\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
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
        buffer.append("<!-- EMERGENCY TREATMENT OVERRIDE POLICY -->");        
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
    
    private void createObligationPolicyRedact(List<String> redactCodes) {
        String resourceName = "DS4PRedactAuthorization";
        buffer.append("<!-- DATA REDACTION POLICY -->");        
        buffer.append("<Policy PolicyId=\"urn:gov:hhs:onc:ds4p:redact\" RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides\">");
        buffer.append("<Target>");
        buffer.append("<Resources>");
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+resourceName+"</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        buffer.append("</Resources>");
        buffer.append("</Target>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:RedactAllowRule\" Effect=\"Permit\">");
        buffer.append("<Description>Determine is Redaction is allowed for specific data sensitivity</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:redactauthorization\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter = redactCodes.iterator();
        while (iter.hasNext()) {
            String redact = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+redact+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:RedactDenyRule\" Effect=\"Deny\">");
        buffer.append("<Description>Determine is Redaction is not allowed for specific data sensitivity</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:redactauthorization\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter2 = redactCodes.iterator();
        while (iter2.hasNext()) {
            String redact = (String)iter2.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+redact+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("</Policy>");      
    }
    
    private void createObligationPolicyMask(List<String> maskCodes) {
        String resourceName = "DS4PMaskAuthorization";
        buffer.append("<!-- DATA MASKING POLICY -->");
        buffer.append("<Policy PolicyId=\"urn:gov:hhs:onc:ds4p:redact\" RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides\">");
        buffer.append("<Target>");
        buffer.append("<Resources>");
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+resourceName+"</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        buffer.append("</Resources>");
        buffer.append("</Target>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:MaskAllowRule\" Effect=\"Permit\">");
        buffer.append("<Description>Determine is Masking is allowed for specific data sensitivity</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:maskauthorization\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter = maskCodes.iterator();
        while (iter.hasNext()) {
            String mask = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+mask+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:MaskDenyRule\" Effect=\"Deny\">");
        buffer.append("<Description>Determine is Masking is not allowed for specific data sensitivity</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:maskauthorization\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter2 = maskCodes.iterator();
        while (iter2.hasNext()) {
            String mask = (String)iter2.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+mask+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("</Policy>");      
    }
    
    private void createObligationPolicyUSPrivacyLaw() {
        String resourceName = "DS4PUSPrivacyLaw";
        buffer.append("<!-- ORGANIZATION US PRIVACY LAW POLICY -->");        
        buffer.append("<Policy PolicyId=\"urn:gov:hhs:onc:ds4p:usprivacylaw\" RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides\">");
        buffer.append("<Target>");
        buffer.append("<Resources>");
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+resourceName+"</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        buffer.append("</Resources>");
        buffer.append("</Target>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:USPrivacyLawAllowRule\" Effect=\"Permit\">");
        buffer.append("<Description>Determine if US Privacy is required</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter = orgLaw.iterator();
        while (iter.hasNext()) {
            String law = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+law+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:USPrivacyLawDenyRule\" Effect=\"Deny\">");
        buffer.append("<Description>Determine if US Privacy Law is not required</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter2 = orgLaw.iterator();
        while (iter2.hasNext()) {
            String law2 = (String)iter2.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+law2+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("</Policy>");              
    }
    
    private void createObligationPolicyRefrain() {
        String resourceName = "DS4PRefrainPolicy";
        buffer.append("<!-- ORGANIZATION REFRAIN POLICY -->");        
        buffer.append("<Policy PolicyId=\"urn:gov:hhs:onc:ds4p:refrainpolicy\" RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides\">");
        buffer.append("<Target>");
        buffer.append("<Resources>");
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+resourceName+"</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        buffer.append("</Resources>");
        buffer.append("</Target>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:RefrainPolicyAllowRule\" Effect=\"Permit\">");
        buffer.append("<Description>Determine refrain policy allowed</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:org:refrain-policy\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter = refrainPolicy.iterator();
        while (iter.hasNext()) {
            String refrain = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+refrain+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:RefrainPolicyDenyRule\" Effect=\"Deny\">");
        buffer.append("<Description>Determine refrain policy is denied</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:org:us-privacy-law\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter2 = refrainPolicy.iterator();
        while (iter2.hasNext()) {
            String refrain2 = (String)iter2.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+refrain2+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("</Policy>");                      
    }
    
    private void createObligationPolicyDocumentHandling() {
        String resourceName = "DS4PDocumentHandling";
        buffer.append("<!-- ORGANIZATION DOCUMENT HANDLING POLICY -->");        
        buffer.append("<Policy PolicyId=\"urn:gov:hhs:onc:ds4p:refrainpolicy\" RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides\">");
        buffer.append("<Target>");
        buffer.append("<Resources>");
        buffer.append("<Resource>");
        buffer.append("<ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">");
        buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+resourceName+"</AttributeValue>");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:gov:hhs:fha:nhinc:service-type\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("</ResourceMatch>");
        buffer.append("</Resource>");
        buffer.append("</Resources>");
        buffer.append("</Target>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:RefrainPolicyAllowRule\" Effect=\"Permit\">");
        buffer.append("<Description>Determine document handling policy is allowed</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:org:document-handling\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter = documentHandling.iterator();
        while (iter.hasNext()) {
            String dh = (String)iter.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+dh+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
        buffer.append("</Condition>");
        buffer.append("</Rule>");
        
        buffer.append("<Rule RuleId=\"urn:gov:hhs:onc:ds4p:anyServiceType:RefrainPolicyDenyRule\" Effect=\"Deny\">");
        buffer.append("<Description>Determine document handling policy is denied</Description>");
        buffer.append("<Target/>");
        
        buffer.append("<Condition>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:not\">");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of\">");
        buffer.append("<ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:2.0:resource:org:document-handling\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>");
        buffer.append("<Apply FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:string-bag\">");
        Iterator iter2 = documentHandling.iterator();
        while (iter2.hasNext()) {
            String dh2 = (String)iter2.next();
            buffer.append("<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"+dh2+"</AttributeValue>");
        }
        buffer.append("</Apply>");
        buffer.append("</Apply>");
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
    
    private void getOrganizationalPolicyTags(OrganizationPolicy orgPolicy, String tagType) {
        try {
            Iterator iter = orgPolicy.getOrganizationTaggingRules().iterator();
            while (iter.hasNext()) {
                OrganizationTaggingRules r = (OrganizationTaggingRules)iter.next();
                if (ORG_PRIVACY_LAW_TYPE.equals(tagType)) {
                    String law = r.getActUSPrivacyLaw().getCode();
                    if (!orgLaw.contains(law)) {
                        orgLaw.add(law);
                    }
                }
                else if (ORG_REFRAIN_POLICY_TYPE.equals(tagType)) {
                    String ref = r.getRefrainPolicy().getCode();
                    if (!refrainPolicy.contains(ref)) {
                        refrainPolicy.add(ref);
                    }
                }
                else if (ORG_DOCUMENT_HANDLING_TYPE.equals(tagType)) {
                    String dh = r.getOrgObligationPolicyDocument().getObligationPolicy().getCode();
                    if (!documentHandling.contains(dh)) {
                        documentHandling.add(dh);
                    }
                }
                else {
                    // no match
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }                
    }
    
}
