/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ehtac.meaningfuluse.uts;

import UtsMetathesaurusContent.*;
import UtsMetathesaurusFinder.UiLabel;
import UtsMetathesaurusFinder.UiLabelRootSource;
import UtsMetathesaurusFinder.UtsWsFinderController;
import UtsMetathesaurusFinder.UtsWsFinderControllerImplService;
import UtsSecurity.UtsWsSecurityController;
import UtsSecurity.UtsWsSecurityControllerImplService;
import gov.va.ehtac.meaningfuluse.displayobjects.UMLSDisplayObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Duane DeCouteau
 */
public class UtsClient {
    
        private static String nwsSecurity = "https://uts.nlm.nih.gov/services/nwsSecurity?wsdl";
        private static String nwsContent = "https://uts.nlm.nih.gov/services/nwsContent?wsdl";
        private static String nwsMetadata = "https://uts.nlm.nih.gov/services/nwsMetadata?wsdl";
        private static String ticketGrantingTicket = null;
        private static String username = "ddecouteau";
	private static String password = "R0@dw0rk24";
        private static String umlsRelease = "2012AA";
        private static String serviceName = "http://umlsks.nlm.nih.gov";
        
        
	public UtsClient() {
		try {
            
                    setTicketGrantingTicket();
                    //build some ConceptDTOs and retrieve UI and Default Preferred Name

                    //use the Proxy Grant Ticket to get a Single Use Ticket
//                    String singleUseTicket1 = getSingleUseTicket();
//
//                    UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();            
//                    ConceptDTO result1 =  utsContentService.getConcept(singleUseTicket1, umlsRelease, "C0018787");
//
//                    System.out.println(result1.getUi() );
//                    System.out.println(result1.getDefaultPreferredName() );
//
//                    //use the Proxy Grant Ticket to get another Single Use Ticket
//                    String singleUseTicket2 = getSingleUseTicket();
//                    ConceptDTO result2 =  utsContentService.getConcept(singleUseTicket2, umlsRelease, "C0014591");
//                    System.out.println(result2.getUi() );
//                    System.out.println(result2.getDefaultPreferredName() );
                        
                        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

        public void setTicketGrantingTicket() {
            if (ticketGrantingTicket == null) {
                try {
                    UtsWsSecurityController port = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
                    //((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, nwsSecurity);
                    ticketGrantingTicket = port.getProxyGrantTicket(username, password);
                    System.out.println("UMLS Security Ticket: "+ticketGrantingTicket);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                //ticket is set
            }
        }
        
        private static String getSingleUseTicket() {
            String singleticket = null;
                try {
                    UtsWsSecurityController port = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
//                    UtsWsSecurityController port = service.getUtsWsSecurityControllerImplPort();
//                    ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, nwsSecurity);
                    singleticket = port.getProxyTicket(ticketGrantingTicket, serviceName);

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }            
            return singleticket;
        }
      
        public static void main(String[] args) {
            UtsClient t = new UtsClient();
            try {
                    String singleUseTicket1 = getSingleUseTicket();                
                    UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort(); 
                    
                    ConceptDTO myConcept = utsContentService.getConcept(singleUseTicket1, "2011AB", "C0004936");
                    String preferredName = myConcept.getDefaultPreferredName();
                    List<String> semanticTypes = myConcept.getSemanticTypes();
                    int numberofAtoms = myConcept.getAtomCount();
                    
                    System.out.println("Preferred Name: "+preferredName);
                    Iterator iter = semanticTypes.iterator();
                    while (iter.hasNext()) {
                        String s = (String)iter.next();
                        System.out.println("Semantic Types: "+s);
                    }
                    System.out.println("Number of Atoms: "+numberofAtoms);
                    
                    
                    
                    String singleUseTicket2 = getSingleUseTicket();                
                    
                    List<AtomDTO> myAtoms = new ArrayList<AtomDTO>();
                    UtsMetathesaurusFinder.Psf myPsf = new UtsMetathesaurusFinder.Psf();
                    myPsf.getIncludedSources().add("SNOMEDCT");
                    myPsf.setIncludeObsolete(false);
                    myPsf.setPageLn(1000);
                    myPsf.setSortOrderAscending(true);
                    //myPsf.getIncludedAdditionalRelationLabels().add("T047");
                    
                    
                    UtsWsFinderController utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();                    
                    List<UiLabelRootSource> myUiLabels = new ArrayList<UiLabelRootSource>();

                    myUiLabels = utsFinderService.findCodes(singleUseTicket2, umlsRelease, "atom", "drug abuse disorder", "approximate", myPsf);
                    
                    
                    System.out.println("Response SIZE: "+myUiLabels.size());
                    for (int i = 0; i < myUiLabels.size(); i++) {
                        UiLabelRootSource myUiLabel = myUiLabels.get(i);
                        String ui = myUiLabel.getUi();
                        String label = myUiLabel.getLabel();
                        String src = myUiLabel.getRootSource();
                        System.out.println("UI: "+ ui);
                        System.out.println("Name: "+ label);
                    }                    
//                    myPsf.getIncludedWords().add("HIV");
//                    myPsf.setCaseSensitive(false);
//                    myPsf.getIncludedTermTypes().add("FSN");
//                    myPsf.getIncludedTermTypes().add("SY");
//                    myPsf.getIncludedTermTypes().add("PT");
//                    //utsContentService.getTermAtoms(singleUseTicket2, "2011AB", username, myPsf)
//                    myAtoms = utsContentService.getConceptAtoms(singleUseTicket2, "2012AA","C0012634",myPsf);
//
//                    for (int i = 0; i < myAtoms.size(); i++) {
//                        AtomDTO myAtomDTO = myAtoms.get(i);
//
//                        String aui = myAtomDTO.getUi();
//                        String source = myAtomDTO.getRootSource();
//                        String name = myAtomDTO.getTermString().getName();
//                        String TermType = myAtomDTO.getTermType();
//                        int cvMemberCount = myAtomDTO.getCvMemberCount();
//                        System.out.println("AUI: "+aui);
//                        System.out.println("SOURCE: "+source);
//                        System.out.println("NAME: "+name);
//                        System.out.println("TERMTYPE: "+TermType);
//                        System.out.println("Member Count:"+cvMemberCount+"\n");
//                    }
                    
                    
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        public List<UMLSDisplayObject> getProblemListResults(String searchstr) {
            List<UMLSDisplayObject> res = new ArrayList();
            String sWords = searchstr + " disorder";
            try {
                    String singleUseTicket2 = getSingleUseTicket();                
                    
                    List<AtomDTO> myAtoms = new ArrayList<AtomDTO>();
                    UtsMetathesaurusFinder.Psf myPsf = new UtsMetathesaurusFinder.Psf();
                    myPsf.getIncludedSources().add("SNOMEDCT");
                    myPsf.setIncludeObsolete(false);
                    myPsf.setPageLn(5000);
                    myPsf.setSortOrderAscending(true);
                    myPsf.setCaseSensitive(false);
                    //myPsf.getIncludedAdditionalRelationLabels().add("T047");
                    
                    
                    UtsWsFinderController utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();                    
                    List<UiLabelRootSource> myUiLabels = new ArrayList<UiLabelRootSource>();

                    myUiLabels = utsFinderService.findCodes(singleUseTicket2, umlsRelease, "atom", sWords, "words", myPsf);
                    
                    
                    System.out.println("Problem List Response SIZE: "+myUiLabels.size());
                    for (int i = 0; i < myUiLabels.size(); i++) {
                        UiLabelRootSource myUiLabel = myUiLabels.get(i);
                        String ui = myUiLabel.getUi();
                        String label = myUiLabel.getLabel();
                        UMLSDisplayObject obj = new UMLSDisplayObject(ui, label);
                        if (label.indexOf("[X]") < 0) {
                            res.add(obj);
                        }
                    }                                    
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return res;
        }
        
}
