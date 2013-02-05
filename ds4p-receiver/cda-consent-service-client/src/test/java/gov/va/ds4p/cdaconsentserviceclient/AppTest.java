package gov.va.ds4p.cdaconsentserviceclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    
    private String patientName = "Asample Patientone";
    private String patientId = "PUI100010060001";
    private String patientIDType = "ehr";
    private String patientDateOfBirth = "19960101";
    private String patientGender = "F";
    private String intendedPOU = "TREAT";
    private List<String> allowedPOU = new ArrayList();
    private String primaryRecipient = "Duane_Decouteau@direct.healthvault-stage.com";
    private List<String> allowedRecipients = new ArrayList();;
    private String authorization = "Permit";
    private List<String> maskingActions = new ArrayList();
    private List<String> redactActions = new ArrayList();
    private String homeCommunityId = "2.16.840.1.113883.3.467";
    
    public AppTest( String testName )
    {
        super( testName );
        allowedPOU = Arrays.asList(new String[]{"TREAT", "ETREAT"});
        allowedRecipients = Arrays.asList(new String[]{"Duane_Decouteau@direct.healthvault-stage.com", "dr.taylor@aero.org"});
        maskingActions = Arrays.asList(new String[]{"HIV"});
        redactActions = Arrays.asList(new String[] {"ETH","PSY"});
        

    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        CDAR2ConsentDirectiveGenerator g = new CDAR2ConsentDirectiveGenerator();
        String res = g.generatePatientConsentDirective(patientName, patientId, patientIDType, patientGender, patientDateOfBirth, authorization, intendedPOU, allowedPOU, primaryRecipient, allowedRecipients, maskingActions, redactActions, homeCommunityId);
        System.out.println("RESULTS: "+res);
        assertTrue( true );
    }
}
