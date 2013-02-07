/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ds4p.cas.providers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gov.va.ds4p.policy.reference.HumanReadibleText;
import gov.va.ds4p.policy.reference.OrganizationPolicy;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Duane DeCouteau
 */
public class PDFBuilderForCDA {
    
    private Document document = null;
    private String organizationName;    
    private String oAddress;
    private String oCity;
    private String oState;
    private String oZip;
    private String oPhone;
    
    private String patientId;
    private String patientName;
    private String pAddress;
    private String pCity;
    private String pState;
    private String pZip;
    private String pPhone;
    private String pDoB;
    
    private String privacyLaw;
    private String privacyAct;
    private String privacyStatement;
    private List<String> recipients;
    private List<String> mask;
    private List<String> redact;
    private List<String> pous;
    private String patientAuthorization;
    private Font tmBig = new Font(FontFamily.TIMES_ROMAN, 14);
    private Font tmbase = new Font(FontFamily.TIMES_ROMAN, 10);
    private Font tmsmall = new Font(FontFamily.TIMES_ROMAN, 8);

    
    public PDFBuilderForCDA(OrganizationPolicy orgPolicy, String patientid, String patientname, 
                                     String authorization, List<String> allowedPOU, List<String> allowedRecipients, 
                                     List<String> redactActions, List<String> maskingActions) {
        document = null;
        organizationName = orgPolicy.getOrgName();
        oAddress = orgPolicy.getOrganizationConsentPolicyInfo().getAddr().getStreetAddressLine();
        oCity = orgPolicy.getOrganizationConsentPolicyInfo().getAddr().getCity();
        oState = orgPolicy.getOrganizationConsentPolicyInfo().getAddr().getState();
        oZip = orgPolicy.getOrganizationConsentPolicyInfo().getAddr().getPostalCode();
        
        patientId = patientid;
        patientName = patientname;
        pAddress = orgPolicy.getOrganizationConsentPolicyInfo().getDefaultPatientDemographics().getAddr().getStreetAddressLine();
        pCity = orgPolicy.getOrganizationConsentPolicyInfo().getDefaultPatientDemographics().getAddr().getCity();
        pState = orgPolicy.getOrganizationConsentPolicyInfo().getDefaultPatientDemographics().getAddr().getState();
        pZip = orgPolicy.getOrganizationConsentPolicyInfo().getDefaultPatientDemographics().getAddr().getPostalCode();
        pPhone = orgPolicy.getOrganizationConsentPolicyInfo().getDefaultPatientDemographics().getPatientTelcom();
        
        privacyLaw = orgPolicy.getUsPrivacyLaw();
        HumanReadibleText hText = orgPolicy.getOrganizationConsentPolicyInfo().getHumanReadibleText();
        privacyStatement = hText.getDisplayText();
        
        recipients = allowedRecipients;
        
        mask = maskingActions;
        redact = redactActions;
        pous = allowedPOU;
        patientAuthorization = authorization;
    }

    public String getPDFConsentDirective() {
        String res = "";
        try {
            PdfConsentBuilder builder = new PdfConsentBuilder();
            res = builder.getDocumentString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
    
    public class PdfConsentBuilder {
        private final ByteArrayOutputStream os = new ByteArrayOutputStream();

        public PdfConsentBuilder() {

            String header = "Request for an Authorization to Release Protected Health Information (PHI)";
            if (privacyLaw.equals("Title38Section7338")) {
                privacyAct = "Privacy Act: The execution of this form does not authorize the release of information other than that specifically described below. The information requested on "+
                        "this form is solicited under Title 38, U.S.C. 7332. Your disclosure of the information requested on this form is voluntary. However, if the information including "+
                        "Social Security Number (SSN) (the SSN will be used to locate records for release) is not furnished completely and accurately, Department of Veterans Affairs will "+
                        "be unable to comply with your request to participate in the NHIN program. The Veterans Health Administration may not condition treatment on signing of this "+
                        "form. Failure to furnish the information will not have any effect on any other benefits to which you may be entitled.";
            }
            else if (privacyLaw.equals("42CFRPart2")) {
                privacyAct = "Privacy Act: 42 CFR Part 2 protects clients who have applied for, participated in, or received an interview, counseling, or any other service from a "+ 
                             "federally assisted alcohol or drug abuse program, identified as an alcohol or drug client during an evaluation of eligibility for treatment. "+
                             "Programs may not disclose any identifying information unless the patient has given consent or otherwise specifically permitted "+
                             "under 42 CFR Part 2.";
            }
            else {
                privacyAct = privacyStatement;
            }
            String infoRequested = "Information Requested:";
            String infoRequested2 = "Pertinent health information that may include but is not limited to the following: ";
            String infoRequested3 = "Medications, Vitals, Problem List, Health Summary, Progress Notes";


            String forPeriodOfLBL = "This authorization will remain in effect until I revoke my authorization or for the period of five years whichever is sooner.";

            String certificationLBL = "AUTHORIZATION: I certify that this request has been made freely, voluntarily and without ";
            String certificationLBL2 = "coercion and that the information given above is accurate and complete to the best of my knowledge.";
            
            String documentDate = "Date: "+getCurrentDate();
            
            String disclosureAction = "Disclosure Action Requested: "+patientAuthorization;

            try {

              document = new Document(PageSize.A4, 25, 25, 25, 25);
              PdfWriter.getInstance(document, os);

              document.open();
              
              addHeaderImage();
              document.add(getNamesTable());
              document.add(new Paragraph(documentDate, tmbase));
              Paragraph headerP = new Paragraph(header, tmbase);
              headerP.setSpacingBefore(1);
              headerP.setSpacingAfter(1);
              document.add(headerP);

              document.add(getPrivacyStatement());

              document.add(new Paragraph(infoRequested, tmbase));
              document.add(new Paragraph(infoRequested2, tmbase));
              Paragraph infoP = new Paragraph(infoRequested3, tmbase);
              infoP.setSpacingAfter(1);
              document.add(infoP);
              
              Paragraph disclosure = new Paragraph(disclosureAction, tmbase);
              disclosure.setSpacingAfter(1);
              document.add(disclosure);
              
              document.add(getAuthorizedRecipients());
              
              document.add(getAllowedPOUs());
              
              if (patientAuthorization.equals("Permit") || patientAuthorization.equals("Disclose")) {
                  document.add(getRedactActions());
                  document.add(getMaskingActions());
              }
              Paragraph period = new Paragraph(forPeriodOfLBL, tmbase);
              period.setSpacingAfter(1);
              document.add(period);
              document.add(new Paragraph(certificationLBL, tmsmall));
              document.add(new Paragraph(certificationLBL2, tmsmall));
            
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
        
        private void addHeaderImage() {
            try {
                URL imageURL = getClass().getResource("/images/logo.png");                 
                Image logo = Image.getInstance(imageURL);
                document.add(logo);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        private Paragraph getPrivacyStatement() {
            Paragraph p = new Paragraph(privacyAct, tmbase);
            p.setSpacingAfter(1);
            p.setSpacingAfter(1);
            return p;
        }
        
        private PdfPTable getNamesTable() {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(patientName);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);            
            table.addCell(organizationName);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);            
            table.addCell(pAddress);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);                        
            table.addCell(oAddress);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);            
            table.addCell(pCity +", "+pState+"  "+pZip);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);                                    
            table.addCell(oCity+", "+oState+"  "+oZip);
            PdfPCell mCell = new PdfPCell(new Phrase("Phone: "+pPhone+"  DoB: "+pDoB));
            mCell.setColspan(2);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);                        
            table.addCell(mCell);
            return table;
        }
        
        private PdfPTable getAuthorizedRecipients() {
            PdfPTable table = new PdfPTable(1);
            table.setSpacingAfter(1);
            table.setSpacingBefore(1);
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
            table.setWidthPercentage(40);
            table.addCell("Authorized Recipient(s)");
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            Iterator iter = recipients.iterator();
            while (iter.hasNext()) {
                String r = (String)iter.next();
                table.addCell(r);
            }
            return table;
        }
        
        private PdfPTable getMaskingActions() {
            PdfPTable table = new PdfPTable(1);
            table.setSpacingAfter(1);
            table.setSpacingBefore(1);            
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
            table.setWidthPercentage(40);
            table.addCell("Data Sensitivities Requiring Masking(Encryption)");
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            Iterator iter = mask.iterator();
            while (iter.hasNext()) {
                String r = (String)iter.next();
                table.addCell(r);
            }           
            return table;
        }
        
        private PdfPTable getRedactActions() {
            PdfPTable table = new PdfPTable(1);
            table.setSpacingAfter(1);
            table.setSpacingBefore(1);            
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
            table.setWidthPercentage(40);
            table.addCell("Data Sensitivities Requiring Redaction(Removal From Record)");
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            Iterator iter = redact.iterator();
            while (iter.hasNext()) {
                String r = (String)iter.next();
                table.addCell(r);
            }           
            return table;            
        }
        
        private PdfPTable getAllowedPOUs() {
            PdfPTable table = new PdfPTable(1);
            table.setSpacingAfter(1);
            table.setSpacingBefore(1);            
            table.getDefaultCell().setBackgroundColor(BaseColor.CYAN);
            table.setWidthPercentage(40);
            table.addCell("Intended Purpose(s) of Use");
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            Iterator iter = pous.iterator();
            while (iter.hasNext()) {
                String r = (String)iter.next();
                table.addCell(r);
            }           
            return table;            
        }
        
        private String getCurrentDate() {
            String res = "";
                Date dt = new Date();
                SimpleDateFormat sdt = new SimpleDateFormat("M dd, yyyy");
                try {
                    res = sdt.format(dt);
                }
                catch (Exception ex2) {
                    ex2.printStackTrace();
                }

            return res;        
        }
        

        //@Override
        public String getDocumentString() {
            return new String(os.toByteArray());
        }
    }
    
    
}
