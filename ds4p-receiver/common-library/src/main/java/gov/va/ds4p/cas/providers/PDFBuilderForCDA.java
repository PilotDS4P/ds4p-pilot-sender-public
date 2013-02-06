/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.cas.providers;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gov.va.ds4p.policy.reference.OrganizationPolicy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class PDFBuilderForCDA {
    
    private static Document document = null;
    
    public PDFBuilderForCDA(OrganizationPolicy orgPolicy, String patientId,  
                                     String authorization, List<String> allowedPOU, List<String> allowedRecipients, 
                                     List<String> redactActions, List<String> maskingActions) {
        
    }

    
    
    public static class PdfConsentBuilder {
        private final ByteArrayOutputStream os = new ByteArrayOutputStream();

        public PdfConsentBuilder() {
            String vaheader = "Department of Veterans Affairs";
            String header = "Request for and Authorization to Release Protected Diagnoses to Nationwide Health Information Network";
            String privacyAct = "Privacy Act: The execution of this form does not authorize the release of information other than that specifically described below. The information requested on "+
                        "this form is solicited under Title 38, U.S.C. 7332. Your disclosure of the information requested on this form is voluntary. However, if the information including "+
                        "Social Security Number (SSN) (the SSN will be used to locate records for release) is not furnished completely and accurately, Department of Veterans Affairs will "+
                        "be unable to comply with your request to participate in the NHIN program. The Veterans Health Administration may not condition treatment on signing of this "+
                        "form. Failure to furnish the information will not have any effect on any other benefits to which you may be entitled.";
            String infoRequested = "Information Requested:";
            String infoRequested2 = "Pertinent health information that may include but is not limited to the following: ";
            String infoRequested3 = "Medications, Vitals, Problem List, Health Summary, Progress Notes";


            String requestAndAuthorize = "I request and authorize my VA health care facility to release my protected health information (PHI) for treatment purposes to the communities that are "+
                                 "participating in the Nationwide Health Information Network (NHIN). This information may consist of the diagnosis of Sickle Cell Anemia, the treatment of or "+
                                 "referral for Drug Abuse, treatment of or referral for Alcohol Abuse or the treatment of or testing for infection with Human Immunodeficiency Virus. This "+
                                 "authorization includes the diagnoses that I may have upon signing of the authorization and the diagnoses that I may acquire in the future.";

            String forPeriodOfLBL = "This authorization will remain in effect until I revoke my authorization or for the period of five years whichever is sooner.";

            String certificationLBL = "AUTHORIZATION: I certify that this request has been made freely, voluntarily and without ";
            String certificationLBL2 = "coercion and that the information given above is accurate and complete to the best of my knowledge.";


            try {
              Font tmbase = new Font(FontFamily.TIMES_ROMAN, 10);
              Font tmsmall = new Font(FontFamily.TIMES_ROMAN, 8);

              document = new Document(PageSize.A4, 25, 25, 25, 25);
              PdfWriter.getInstance(document, os);
              document.open();
              document.add(new Paragraph(vaheader, tmbase));
              document.add(new Paragraph(header, tmbase));
              document.add(new Paragraph("-"));
              document.add(new Paragraph(privacyAct, tmsmall));
              document.add(new Paragraph("-"));
//              document.add(new Paragraph("Patient Full Name Last: "+XSPAAdminSessionAttributes.getPatientLastName() +" First: "+XSPAAdminSessionAttributes.getPatientFirstName()+" Middle:", tmbase));
//              document.add(new Paragraph("Last four digits of SSN: "+XSPAAdminSessionAttributes.getPatientSSN(), tmbase));
//              document.add(new Paragraph("Requestor: "+XSPAAdminSessionAttributes.getRequestorName(), tmbase));
              document.add(new Paragraph("-"));
              document.add(new Paragraph(infoRequested, tmbase));
              document.add(new Paragraph(infoRequested2, tmbase));
              document.add(new Paragraph(infoRequested3, tmbase));
              document.add(new Paragraph("-"));
              document.add(new Paragraph(requestAndAuthorize, tmbase));
              document.add(new Paragraph(forPeriodOfLBL, tmbase));
              document.add(new Paragraph("-"));
              document.add(new Paragraph(certificationLBL, tmbase));
              document.add(new Paragraph(certificationLBL2, tmbase));
              document.add(new Paragraph("-"));
//              document.add(new Paragraph("Signature: End-User Agreement Dated: "+XSPAAdminSessionAttributes.getDirectiveSigned().toString(), tmbase));
//             }
            
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (document != null) {
                    document.close();
                }
            }

        }

        //@Override
        public InputStream getStream() {
            return new ByteArrayInputStream(os.toByteArray());
        }
    }
    
    
}
