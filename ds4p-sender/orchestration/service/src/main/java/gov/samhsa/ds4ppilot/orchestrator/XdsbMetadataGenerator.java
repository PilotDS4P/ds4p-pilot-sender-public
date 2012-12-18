package gov.samhsa.ds4ppilot.orchestrator;

public interface XdsbMetadataGenerator {
	public String generateMetadataXml(String document, String homeCommunityId);
}
