<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sdtc="urn:hl7-org:sdtc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="xs sdtc" version="2.0"
	xpath-default-namespace="" xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0"
	xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0"
	xmlns="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">

	<!-- Generate additional metadata for processed C32 document. -->

	<xsl:output indent="yes" omit-xml-declaration="yes" />
	
	<xsl:param name="authorTelecommunication" as="xs:string"
		select="''" />

	<xsl:param name="intendedRecipient" as="xs:string"
		select="''" />
		
		<xsl:param name="purposeOfUse" as="xs:string"
		select="''" />

	<xsl:param name="XDSDocumentEntry_uniqueId" as="xs:string"
		select="'{$XDSDocumentEntry_uniqueId}'"></xsl:param>
		
	<xsl:template match="/">


		<lcm:SubmitObjectsRequest xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0"
			xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0" xmlns="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">

			<RegistryObjectList>
				<ExtrinsicObject>
				
				<Slot name="authorTelecommunication">
						<ValueList>
							<Value>
								<xsl:value-of select="concat('^^Internet^', $authorTelecommunication)" />
							</Value>
						</ValueList>
					</Slot>

					<Slot name="intendedRecipient">
						<ValueList>
							<Value>
								<xsl:value-of select="concat('^^Internet^', $intendedRecipient)" />
							</Value>
						</ValueList>
					</Slot>

					<Slot name="urn:siframework.org:ds4p:purposeofuse">
						<ValueList>
							<Value>
								<xsl:value-of select=" $purposeOfUse" />
							</Value>
						</ValueList>
					</Slot>

					<Slot name="urn:siframework.org:ds4p:obligationpolicy">
						<ValueList>
							<xsl:for-each select="//executionResponse[itemAction != 'REDACT']">
								<Value>
									<xsl:value-of select="documentObligationPolicy" />
								</Value>
							</xsl:for-each>
						</ValueList>
					</Slot>

					<Slot name="urn:siframework.org:ds4p:refrainpolicy">
						<ValueList>
							<xsl:for-each select="//executionResponse[itemAction != 'REDACT']">
								<Value >
									<xsl:value-of select="documentRefrainPolicy" />
								</Value>
							</xsl:for-each>
						</ValueList>
					</Slot>

					<!-- <Slot name="urn:siframework.org:ds4p:sensitivitypolicy">
						<ValueList>
							<xsl:for-each select="//executionResponse[itemAction != 'REDACT']">
								<Value observationId="{observationId}">
									<xsl:value-of select="sensitivity" />
								</Value>
							</xsl:for-each>
						</ValueList>
					</Slot> -->

					<Slot name="urn:siframework.org:ds4p:usprivacylaw">
						<ValueList>
							<xsl:for-each select="//executionResponse[itemAction != 'REDACT']">
								<Value >
									<xsl:value-of select="USPrivacyLaw" />
								</Value>
							</xsl:for-each>
						</ValueList>
					</Slot>

					<ExternalIdentifier id="ei02"
						identificationScheme="urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab"
						value="{$XDSDocumentEntry_uniqueId}">
						<Name>
							<LocalizedString value="XDSDocumentEntry.uniqueId" />
						</Name>
					</ExternalIdentifier>

				</ExtrinsicObject>

			</RegistryObjectList>
		</lcm:SubmitObjectsRequest>

	</xsl:template>
</xsl:stylesheet>
