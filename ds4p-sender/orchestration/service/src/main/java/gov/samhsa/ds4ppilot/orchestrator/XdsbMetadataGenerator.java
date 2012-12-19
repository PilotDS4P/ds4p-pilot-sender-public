package gov.samhsa.ds4ppilot.orchestrator;

/**
 * The Interface XdsbMetadataGenerator.
 */
public interface XdsbMetadataGenerator {

	/**
	 * Generate metadata xml.
	 * 
	 * @param document
	 *            the document
	 * @param homeCommunityId
	 *            the home community id
	 * @return the string
	 */
	public String generateMetadataXml(String document, String homeCommunityId);
}
