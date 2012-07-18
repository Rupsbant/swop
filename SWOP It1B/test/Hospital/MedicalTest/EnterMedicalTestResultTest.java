/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Hospital.MedicalTest;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import org.junit.Test;
import Hospital.Argument.PublicArgument;
import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.MedicalTestController;
import Hospital.Controllers.MedicalTestResultController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class EnterMedicalTestResultTest {

    public WorldController wc;
    public DoctorController dc;
    public NurseController nc;
    public MedicalTestController mc;
    public DiagnosisController diac;
    public MedicalTestResultController mrc;

    public EnterMedicalTestResultTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws NoPersonWithNameAndRoleException, NotLoggedInException, NotAFactoryException, CannotChangeException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, ArgumentIsNullException, NoOpenedPatientFileException, InvalidArgumentException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        nc = (NurseController) wc.login(wc.getCampuses().get(0),new LoginInfo("Nurse Joy", "Nurse")); //don't search for nurse
        mc = new MedicalTestController(wc, dc);
        diac = new DiagnosisController(wc, dc);

        mrc = new MedicalTestResultController(wc, nc);

        initPatientFile();
        initMedicalTests();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBasic() throws NotLoggedInException, WrongArgumentListException, ArgumentNotAnsweredException,
            ArgumentIsNullException, IllegalInfo, CannotChangeException, ArgumentConstraintException, InvalidArgumentException {
        assertTrue("Treatments are not correctly filtered", mrc.getOpenMedicalTests().length == 1);
        final ArgumentList resultArgs = mrc.getArguments(mrc.getOpenMedicalTests()[0]);
        PublicArgument[] args = resultArgs.getPublicArguments();
        assertTrue("Arguments not right type", args[0].getClass().equals(IntegerArgument.class));
        assertTrue("Arguments not right type", args[1].getClass().equals(StringArgument.class));
        args[0].enterAnswer("5");
        args[1].enterAnswer("string");
        String out = mrc.enterResult(mrc.getOpenMedicalTests()[0], resultArgs);
        String expected = "XRayTest: 0, 1, 2\n"
                + "Number of images taken: 5\n"
                + "Abnormalities: string\n"
                + "Appointment from 2011/11/8 9:00 of 15 minutes, until 2011/11/8 9:15 with 3 attendees.";
        assertEquals("Outputted string wrong", expected, out);
    }

    @Test
    public void testBreaking() throws CannotChangeException, NotLoggedInException,
            WrongArgumentListException, ArgumentNotAnsweredException, ArgumentIsNullException,
            IllegalInfo, ArgumentConstraintException, InvalidArgumentException {
        final ArgumentList resultArgs = mrc.getArguments(mrc.getOpenMedicalTests()[0]);
        PublicArgument[] args = resultArgs.getPublicArguments();
        args[0].enterAnswer("5");
        args[1].enterAnswer("string");
        try {
            String out = mrc.enterResult(null, resultArgs);
            fail("Exception should be thrown");
        } catch (IllegalInfo ex) {
            assertEquals("Exceptionmessage wrong", "MedicalTest is null", ex.getMessage());
        }
        try {
            String out = mrc.enterResult(mrc.getOpenMedicalTests()[0], null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("Exceptionmessage wrong", "ArgumentList was null", ex.getMessage());
        }
        try {
            XRayScan x = new XRayScan(1, 3, "Armen");
            String out = mrc.enterResult(x.getInfo(), resultArgs);
            fail("Exception should be thrown");
        } catch (IllegalInfo ex) {
            assertEquals("Exceptionmessage wrong", "MedicalTest not found", ex.getMessage());
        }

    }

    private void initMedicalTests() throws NotAFactoryException, CannotChangeException, NotLoggedInException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, InvalidArgumentException {

        final ArgumentList resultArgs = mc.getMedicalTestArguments("New XRayScan");
        PublicArgument[] args = resultArgs.getPublicArguments();
        args[0].enterAnswer("0");
        args[1].enterAnswer("1");
        args[2].enterAnswer("2");
        args[3].enterAnswer("high");
        mc.makeMedicalTest("New XRayScan", resultArgs);
    }

    private void initPatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException {
        dc.consultPatientFile("Ruben", wc);
    }
}