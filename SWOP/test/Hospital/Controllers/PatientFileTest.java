package Hospital.Controllers;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.CannotDischargeException;
import Hospital.Exception.Command.CannotDoException;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Warehouse.ItemNotFoundException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.MedicalTest.MedicalTestDummy;
import Hospital.Patient.Diagnosis;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Patient.Patient;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import Hospital.World.World;

public class PatientFileTest {

    private WorldController wc;
    private DoctorController dc;
    private World w;
    private TreatmentController tc;
    private NurseController nc;
    private TreatmentResultController trC;

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        w = wc.getWorld();
        List<LoginInfo> logins = wc.getLogins();
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getRole().equals(StaffRole.Doctor)) {
                dc = (DoctorController) wc.login(wc.getCampuses().get(0), logins.get(i));
            }
        }
        try {
            Patient p = w.getPersonByName(Patient.class, "Jeroen");
            p.addDiagnosis(new Diagnosis("hoofdpijn", dc.getUser()));
            p.addDiagnosis(new Diagnosis("keelpijn", dc.getUser()));
            p.addMedicalTest(new MedicalTestDummy("a"));
            p.addMedicalTest(new MedicalTestDummy("b"));
        } catch (NoPersonWithNameAndRoleException e1) {
            System.out.println("Jeroen is aanwezig in de test World.");
        }
        tc = new TreatmentController(wc, dc);
        nc = (NurseController) wc.login(wc.getCampuses().get(0), new LoginInfo("Nurse Joy", StaffRole.Nurse)); //don't search for nurse
        trC = new TreatmentResultController(wc, nc);
        wc = new WorldController(w);
        List<LoginInfo> l = wc.getLogins();
        LoginInfo doctor = null;
        for (LoginInfo loginInfo : l) {
            if (loginInfo.getRole().equals(StaffRole.Doctor)) {
                doctor = loginInfo;
            }
        }
        try {
            dc = (DoctorController) wc.login(wc.getCampuses().get(0), doctor);
        } catch (NoPersonWithNameAndRoleException e) {
            System.out.println("check LoginTest voor fouten");
        }
    }

    @Test
    public void testGetPatientFileList() {
        try {
            PatientFile patientfile = dc.consultPatientFile("Jeroen", wc);
            String[] patientFileList = patientfile.getPatientFileList();
            assertEquals("Diagnose details: hoofdpijn\nDoctor: Gregory House", patientFileList[0]);
            assertEquals("Diagnose details: keelpijn\nDoctor: Gregory House", patientFileList[1]);
            assertEquals("TestMedicalTest a", patientFileList[2]);
            assertEquals("TestMedicalTest b", patientFileList[3]);
        } catch (NoPersonWithNameAndRoleException e) {
            fail("person should exist");
        } catch (NotLoggedInException e) {
            fail("doctor logged In in SetUp");
        }
    }

    @Test
    public void testgetAdvancedInformation() {
        try {
            PatientFile patientfile = dc.consultPatientFile("Jeroen", wc);
            String info = patientfile.getAdvancedInformation(2);
            String expected = "TestMedicalTest a\n"
                    + "resultaat\n"
                    + "Appointment from 2011/11/8 8:00 of 10 minutes, until 2011/11/8 8:10 with 0 attendees.";
            assertEquals(expected, info);
        } catch (NoPersonWithNameAndRoleException e) {
            fail("person should exist");
        } catch (NotLoggedInException e) {
            fail("doctor logged In in SetUp");
        }
    }

    @Test
    public void testgetAdvancedInformation2() {
        try {
            PatientFile patientfile = dc.consultPatientFile("Jeroen", wc);
            String info = patientfile.getAdvancedInformation(0);
            String expected = "Diagnose details: hoofdpijn\n"
                    + "Doctor: Gregory House\n"
                    + "Untreated.";
            assertEquals(expected, info);
        } catch (NoPersonWithNameAndRoleException e) {
            fail("person should exist");
        } catch (NotLoggedInException e) {
            fail("doctor logged In in SetUp");
        }
    }

    @Test
    public void testToString() {
        try {
            PatientFile patientfile = dc.consultPatientFile("Jeroen", wc);
            String string = patientfile.toString();
            assertEquals("Jeroen", string);
        } catch (NoPersonWithNameAndRoleException e) {
            fail("person should exist");
        } catch (NotLoggedInException e) {
            fail("doctor logged In in SetUp");
        }
    }

    @Test
    public void openPatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException, ArgumentIsNullException, CannotChangeException, CannotDoException, ArgumentConstraintException, NotAFactoryException, InvalidDiagnosisException, WrongArgumentListException, ArgumentNotAnsweredException, StockException, ItemNotReservedException, ItemNotFoundException, IllegalInfo, CannotDischargeException, InvalidArgumentException {
        assertFalse("No patientFile was opened", dc.hasOpenedPatientFile());
        dc.consultPatientFile("Ruben", wc);
        assertTrue("PatientFile was opened", dc.hasOpenedPatientFile());
        assertTrue(dc.openedPatientFileNotDischarged());
        wc.getWorld().getPersonByName(Patient.class, "Ruben").dischargePatient();

        assertFalse(dc.openedPatientFileNotDischarged());
        assertFalse(dc.openedPatientHasUntreatedDiagnosis());
        Patient p = w.getPersonByName(Patient.class, "Ruben");
        p.addDiagnosis(new Diagnosis("hoofdpijn", dc.getUser()));
        assertTrue(dc.openedPatientHasUntreatedDiagnosis());

        DiagnosisInfo diagnosisinfo = new DiagnosisInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0));
        tc.makeSurgery(diagnosisinfo, "report blabla", new HighLowPriority(true));
        TreatmentInfo treatmentinfo = new TreatmentInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0).getTreatment());
        PublicArgument[] args2 = new PublicArgument[2];
        args2[0] = new StringArgument("Enter the report: ").enterAnswer("reportje");
        args2[1] = new StringArgument("Enter the special aftercare: ").enterAnswer("no special aftercare");
        trC.enterResult(treatmentinfo, new ArgumentList(args2));
        //p.getDiagnoses().get(0).setTreatment(new Medication("medicatie", true,new String[0]));
        assertFalse(dc.openedPatientHasUntreatedDiagnosis());
    }

    @Test
    public void openNullPatientFile() throws NotLoggedInException {
        assertFalse("No patientFile was opened", dc.hasOpenedPatientFile());
        try {
            dc.consultPatientFile(null, wc);
            fail("Exception should be thrown");
        } catch (NoPersonWithNameAndRoleException ex) {
            assertFalse("No patientFile was opened", dc.hasOpenedPatientFile());
        }
    }

    @Test
    public void openFalseNamePatientFile() throws NotLoggedInException {
        assertFalse("No patientFile was opened", dc.hasOpenedPatientFile());
        try {
            dc.consultPatientFile("garbage", wc);  // I hope nobody names his child this
            fail("Exception should be thrown");
        } catch (NoPersonWithNameAndRoleException ex) {
            assertFalse("No patientFile was opened", dc.hasOpenedPatientFile());
        }
    }

    @Test
    public void closePatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException {
        dc.consultPatientFile("Ruben", wc);
        assertTrue("PatientFile was opened", dc.hasOpenedPatientFile());
        dc.closePatientFile();
        assertFalse("PatientFile was closed", dc.hasOpenedPatientFile());
    }

    @Test
    public void doublyOpenPatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException {
        assertFalse("No patientFile was opened", dc.hasOpenedPatientFile());
        dc.consultPatientFile("Ruben", wc);
        assertEquals("PatientFile was opened", "Ruben", dc.getOpenedPatientName());
        dc.consultPatientFile("Jeroen", wc);
        assertEquals("PatientFile was opened", "Jeroen", dc.getOpenedPatientName());
    }
}
