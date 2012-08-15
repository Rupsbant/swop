package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.CannotDischargeException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Command.CannotDoException;
import static org.junit.Assert.*;

import java.util.ArrayList;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Exception.*;
import Hospital.Patient.DiagnosisInfo;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;

public class DiagnosisControllerTest {

    private WorldController wc;
    private DoctorController dc;
    private DiagnosisController diag;

    @Before
    public void setUp() throws ArgumentIsNullException, NotLoggedInException, NoOpenedPatientFileException {
        try {
            wc = TestUtil.getWorldControllerForTesting();
            wc.getWorld().addSchedulable(new Doctor("Doctor 2"));
            dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doktoor", StaffRole.Doctor));
            dc.consultPatientFile("Ruben", wc);
            diag = new DiagnosisController(wc, dc);
        } catch (Exception e) {
            fail("setup fail");
        }
    }

    @Test(expected = NoOpenedPatientFileException.class)
    public void NoOpenedPatientFileTest() throws ArgumentNotAnsweredException, NoPersonWithNameAndRoleException, PatientIsDischargedException, NotLoggedInException, NotAFactoryException, ArgumentConstraintException, WrongArgumentListException, ArgumentIsNullException, NoOpenedPatientFileException, InvalidDiagnosisException, CannotDoException {
        dc.closePatientFile();
        diag.approveDiagnosis(new DiagnosisInfo(null));
        fail("exception shouldve been catched");
    }

    @Test(expected = PatientIsDischargedException.class)
    public void dischargedPatientTest() throws ArgumentNotAnsweredException, NoPersonWithNameAndRoleException, NoOpenedPatientFileException, NotLoggedInException, NotAFactoryException, ArgumentConstraintException, WrongArgumentListException, ArgumentIsNullException, CannotDischargeException, PatientIsDischargedException, InvalidDiagnosisException, CannotDoException {
        dc.dischargePatient();
        diag.approveDiagnosis(new DiagnosisInfo(null));
        fail("exception shouldve been catched");
    }

    @Test
    public void listTest() 
            throws NoPersonWithNameAndRoleException, NoOpenedPatientFileException, PatientIsDischargedException,
            NotLoggedInException, ArgumentConstraintException, ArgumentIsNullException, InvalidArgumentException {
        diag.enterDiagnosis("hoofdpijn", new LoginInfo("Doktoor", StaffRole.Doctor));
        diag.enterDiagnosis("keelpijn", new LoginInfo("Doctor 2", StaffRole.Doctor));
        diag.enterDiagnosis("arm gebroken", new LoginInfo("Doctor 2", StaffRole.Doctor));
        diag.enterDiagnosis("been gebroken", new LoginInfo("Doktoor", StaffRole.Doctor));
        diag.enterDiagnosis("kaak gebroken", new LoginInfo("Doctor 2", StaffRole.Doctor));
        DiagnosisInfo[] diaginfo = dc.getUnapprovedSecondOpinions();
        assertEquals(diaginfo.length, 2);
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doctor 2", StaffRole.Doctor));
        DiagnosisInfo[] diaginfo2 = dc.getUnapprovedSecondOpinions();
        assertEquals(diaginfo2.length, 3);
    }

    @Test
    public void getAvailableSecOpDocs() throws NotLoggedInException {
        LoginInfo[] info = diag.getAvailableSecondOpinionDoctors();
        ArrayList<String> infos = new ArrayList<String>();
        for (int i = 0; i < info.length; i++) {
            if (!info[i].getRole().equals(StaffRole.Doctor)) {
                fail("not a doctor!");
            }
            infos.add(info[i].getName());
        }
        assertEquals("wrong number avi doctors", 3, infos.size());
        assertFalse(infos.contains("Doktoor"));
        assertTrue(infos.contains("Doctor 2"));
        assertTrue(infos.contains("Gregory House"));
        assertTrue(infos.contains("Janet Fraiser"));
    }

    @Test
    public void disApproveDiagnosisTest() throws NoPersonWithNameAndRoleException, NoOpenedPatientFileException, InvalidDiagnosisException, NotLoggedInException, ArgumentConstraintException, ArgumentIsNullException, PatientIsDischargedException, InvalidArgumentException, CannotChangeException {
        dc.consultPatientFile("Ruben", wc);
        
        DoctorController dc2 = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doctor 2", StaffRole.Doctor));
        DiagnosisController diag2 = new DiagnosisController(wc, dc2);

        diag.enterDiagnosis("hoofdpijn", new LoginInfo("Doktoor", StaffRole.Doctor));
        diag.enterDiagnosis("keelpijn", new LoginInfo("Doctor 2", StaffRole.Doctor));
        diag.enterDiagnosis("arm gebroken", new LoginInfo("Doctor 2", StaffRole.Doctor));

        int lengte2 = dc.getUnapprovedSecondOpinions().length;
        int lengte1 = dc2.getUnapprovedSecondOpinions().length;
        dc2.consultPatientFile("Ruben", wc);
        
        DiagnosisInfo[] diaginfo = dc2.getUnapprovedSecondOpinions();
        diag2.disapproveDiagnosis(diaginfo[0], "details blabla");
        
        assertEquals(lengte1 - 1, dc2.getUnapprovedSecondOpinions().length);
        assertEquals(lengte2 + 1, dc.getUnapprovedSecondOpinions().length);
    }
}
