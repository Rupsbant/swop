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

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.*;
import Hospital.Patient.DiagnosisInfo;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;

public class DiagnosisControllerTest {

    WorldController wc;
    DoctorController dc;
    DiagnosisController diag;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NotLoggedInException, NoOpenedPatientFileException {
        try {
            wc = TestUtil.getWorldControllerForTesting();
            wc.getWorld().addSchedulable(new Doctor("Doctor 2"));
            dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doktoor", "Doctor"));
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
    public void listTest() throws ArgumentNotAnsweredException, NoPersonWithNameAndRoleException, NoOpenedPatientFileException, PatientIsDischargedException, NotLoggedInException, NotAFactoryException, ArgumentConstraintException, WrongArgumentListException, ArgumentIsNullException, IllegalArgumentException, CannotChangeException, InvalidArgumentException {
        PublicArgument[] args1 = new PublicArgument[1];
        PublicArgument[] args2 = new PublicArgument[1];
        PublicArgument[] args3 = new PublicArgument[1];
        PublicArgument[] args4 = new PublicArgument[1];
        PublicArgument[] args5 = new PublicArgument[1];
        args1[0] = new StringArgument("details").enterAnswer("hoofdpijn");
        args2[0] = new StringArgument("details").enterAnswer("keelpijn");
        args3[0] = new StringArgument("details").enterAnswer("arm gebroken");
        args4[0] = new StringArgument("details").enterAnswer("been gebroken");
        args5[0] = new StringArgument("details").enterAnswer("kaak gebroken");
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args1), new LoginInfo("Doktoor", "Doctor"));
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args2), new LoginInfo("Doctor 2", "Doctor"));
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args3), new LoginInfo("Doctor 2", "Doctor"));
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args4), new LoginInfo("Doktoor", "Doctor"));
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args5), new LoginInfo("Doctor 2", "Doctor"));
        DiagnosisInfo[] diaginfo = dc.getUnapprovedSecondOpinions();
        assertEquals(diaginfo.length, 2);
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doctor 2", "Doctor"));
        DiagnosisInfo[] diaginfo2 = dc.getUnapprovedSecondOpinions();
        assertEquals(diaginfo2.length, 3);
    }

    @Test
    public void getAvailableSecOpDocs() throws NotLoggedInException {
        LoginInfo[] info = diag.getAvailableSecondOpinionDoctors();
        ArrayList<String> infos = new ArrayList<String>();
        for (int i = 0; i < info.length; i++) {
            if (!info[i].getRole().equals("Doctor")) {
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
    public void disApproveDiagnosisTest() throws CannotChangeException, WrongArgumentListException, ArgumentNotAnsweredException, NoPersonWithNameAndRoleException, NoOpenedPatientFileException, InvalidDiagnosisException, NotLoggedInException, NotAFactoryException, ArgumentConstraintException, ArgumentIsNullException, PatientIsDischargedException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);

        PublicArgument[] args1 = new PublicArgument[1];
        PublicArgument[] args2 = new PublicArgument[1];
        PublicArgument[] args3 = new PublicArgument[1];
        PublicArgument[] args4 = new PublicArgument[1];
        args1[0] = new StringArgument("details").enterAnswer("hoofdpijn");
        args2[0] = new StringArgument("details").enterAnswer("keelpijn");
        args3[0] = new StringArgument("details").enterAnswer("arm gebroken");
        args4[0] = new StringArgument("details").enterAnswer("been gebroken");
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args1), new LoginInfo("Doktoor", "Doctor"));
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args2), new LoginInfo("Doctor 2", "Doctor"));
        diag.enterDiagnosis("Diagnosis with SecondOpinion", new ArgumentList(args3), new LoginInfo("Doctor 2", "Doctor"));

        int lengte2 = dc.getUnapprovedSecondOpinions().length;
        
        DoctorController dc2 = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doctor 2", "Doctor"));
        DiagnosisController diag2 = new DiagnosisController(wc, dc2);
        int lengte1 = dc2.getUnapprovedSecondOpinions().length;
        dc2.consultPatientFile("Ruben", wc);
        DiagnosisInfo[] diaginfo = dc2.getUnapprovedSecondOpinions();
        diag2.disapproveDiagnosis(diaginfo[0], "details blabla");
        
        assertEquals(lengte1 - 1, dc2.getUnapprovedSecondOpinions().length);
        dc2 = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Doktoor", "Doctor"));
        assertEquals(lengte2 + 1, dc2.getUnapprovedSecondOpinions().length);
    }
}
