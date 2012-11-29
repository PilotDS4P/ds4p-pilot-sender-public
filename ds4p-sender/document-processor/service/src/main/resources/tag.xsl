<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sdtc="urn:hl7-org:sdtc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" exclude-result-prefixes="xs" version="2.0"
    xpath-default-namespace="urn:hl7-org:v3" xmlns:ds4p="http://www.siframework.org/ds4p">
    
    <!-- Implements document, section, and entry level confidentiality tagging per DS4P implementation guide. -->
    
    <xsl:output indent="yes"/>
    
    <!--Retrieves Drools rules execution response, parses it, and returns its document node-->
    <xsl:variable name="ruleExecutionResponseContainer" select="document('ruleExecutionResponseContainer')"/>  
 
    
    <!--Get most restrictive entry-level confidentiality code in the execution response container. This returns a double-->
    <xsl:variable name="documentConfidentialityCodeNumeric"
        select="max(ds4p:mapConfCodeFromChartoNumber($ruleExecutionResponseContainer//impliedConfSection))"/>
    
    <!--Map the most restrictive entry-level confidentiality code in the execution response container to V, R, or N-->
    <xsl:variable name="documentConfidentialityCode" select="ds4p:mapConfCodeFromNumtoChar($documentConfidentialityCodeNumeric)"/>

    <!--Get most restrictive entry-level confidentiality code in the problems section. This returns a double-->
    <xsl:variable name="problemsSectionConfidentialityCodeNumeric"
        select="max(ds4p:mapConfCodeFromChartoNumber($ruleExecutionResponseContainer//impliedConfSection[preceding-sibling::c32SectionLoincCode='11450-4']))"/>
    
    <!--Map the most restrictive entry-level confidentiality code in the problems section to V, R, or N-->    
    <xsl:variable name="problemsSectionConfidentialityCode"
        select="ds4p:mapConfCodeFromNumtoChar($problemsSectionConfidentialityCodeNumeric)"/>
    
    <!--Get most restrictive entry-level confidentiality code in the medications section. This returns a double-->
    <xsl:variable name="medicationsSectionConfidentialityCodeNumeric"
        select="max(ds4p:mapConfCodeFromChartoNumber($ruleExecutionResponseContainer//impliedConfSection[preceding-sibling::c32SectionLoincCode='10160-0']))"/>
    
    <!--Map the most restrictive entry-level confidentiality code in the medications section to V, R, or N-->    
    <xsl:variable name="medicationsSectionConfidentialityCode"
        select="ds4p:mapConfCodeFromNumtoChar($medicationsSectionConfidentialityCodeNumeric)"/>
    
    <!--Confidentiality codes for allergies and labs not implemented yet. Defaults to R-->
    <xsl:variable name="allergiesSectionConfidentialityCode"
        select="'R'"/>

    <xsl:variable name="labResultsSectionConfidentialityCode"
        select="'R'"/>
    
	<!-- mypolicies.xacml is just a default. Dynamic parameters values can be passed in at runtime -->
    <xsl:param name="privacyPoliciesExternalDocUrl" as="xs:string" select="'mypolicies.xacml'"/>
    
    <!--This function maps a numeric confidentiality code to V, N, or R-->
    <xsl:function name="ds4p:mapConfCodeFromNumtoChar" as="xs:string">
        <xsl:param name="confidentialityCodeNumeric" />
        <xsl:choose>
            <xsl:when test="$confidentialityCodeNumeric = 3">V</xsl:when>
            <xsl:when test="$confidentialityCodeNumeric = 2">R</xsl:when>
            <xsl:when test="$confidentialityCodeNumeric = 1">N</xsl:when>
            <xsl:otherwise>R</xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <!--This function maps a char (V, N, or R) confidentiality code to numeric.-->
    <xsl:function name="ds4p:mapConfCodeFromChartoNumber" as="xs:integer">
        <xsl:param name="confidentialityCodeChar" />
        <xsl:choose>
            <xsl:when test="$confidentialityCodeChar = 'V'">3</xsl:when>
            <xsl:when test="$confidentialityCodeChar = 'R'">2</xsl:when>
            <xsl:when test="$confidentialityCodeChar = 'N'">1</xsl:when>
            <xsl:otherwise>2</xsl:otherwise>
        </xsl:choose>
    </xsl:function>
   


    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!--Document-level ConfidentialityCode code-->
    <xsl:template match="ClinicalDocument/confidentialityCode">
        <confidentialityCode code="{$documentConfidentialityCode}"
            codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
    </xsl:template>

    <!--Section-level confidentialityCode code is already specified in C32-->
    <xsl:template match="section/confidentialityCode">
        <xsl:choose>
            <!--Problems section-level documentConfidentialityCode code-->
            <xsl:when test="../code[@code='11450-4']">
                <confidentialityCode code="{$problemsSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>

            <!--Allergies section-level documentConfidentialityCode code-->
            <xsl:when test="../code[@code='48765-2']">
                <confidentialityCode code="{$allergiesSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>
            <!--Medications section-level documentConfidentialityCode code-->
            <xsl:when test="../code[@code='10160-0']">
                <confidentialityCode code="{$medicationsSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>
            <!--Lab Results section-level documentConfidentialityCode code-->
            <xsl:when test="../code[@code='30954-2']">
                <confidentialityCode code="{$labResultsSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>

            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!--Section-level confidentialityCode code is not already specified in C32-->
    <xsl:template match="section/code">
        <xsl:choose>
            <!--Problems section-level documentConfidentialityCode code-->
            <xsl:when test="not(following-sibling::confidentialityCode) and @code='11450-4'">
                <xsl:copy-of select="."/>
                <confidentialityCode code="{$problemsSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>

            <!--Allergies section-level documentConfidentialityCode code-->
            <xsl:when test="not(following-sibling::confidentialityCode) and @code='48765-2'">
                <xsl:copy-of select="."/>
                <confidentialityCode code="{$allergiesSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>

            <!--Medications section-level documentConfidentialityCode code-->
            <xsl:when test="not(following-sibling::confidentialityCode) and @code='10160-0'">
                <xsl:copy-of select="."/>
                <confidentialityCode code="{$medicationsSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>

            <!--Lab results section-level documentConfidentialityCode code-->
            <xsl:when test="not(following-sibling::confidentialityCode) and @code='30954-2'">
                <xsl:copy-of select="."/>
                <confidentialityCode code="{$labResultsSectionConfidentialityCode}"
                    codeSystem="2.16.840.1.113883.5.25" xmlns="urn:hl7-org:v3"/>
            </xsl:when>

            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!--Tag Acute HIV, Sickle Cell Anemia, and Substance Abuse Disorder entries : 
        entry-level tagging is pointing to external XACML document per DS4P IG.-->
    <xsl:template match="observation">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xsl:if
                test="value[@code='111880001'] or value[@code='66214007'] or value[@code='234391009']">
                <reference typeCode="REFR" xmlns="urn:hl7-org:v3">
                    <seperatableInd value="false"/>
                    <externalDocument>
                        <id root="b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3"/>
                        <text mediaType="text/xml">
                            <reference value="{$privacyPoliciesExternalDocUrl}"/>
                        </text>
                    </externalDocument>
                </reference>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <!--Tag anti-depressant entry : 
        entry-level tagging is pointing to external XACML document per DS4P IG.-->
    <xsl:template match="substanceAdministration">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xsl:if
                test="descendant::code[@code='993536']">
                <reference typeCode="REFR" xmlns="urn:hl7-org:v3">
                    <seperatableInd value="false"/>
                    <externalDocument>
                        <id root="b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3"/>
                        <text mediaType="text/xml">
                            <reference value="{$privacyPoliciesExternalDocUrl}"/>
                        </text>
                    </externalDocument>
                </reference>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    


</xsl:stylesheet>
