package Hospital.Treatments;

import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.DiagnosisController;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.TreatmentController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.*;
import Hospital.People.LoginInfo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreatmentFactoryTest {

    WorldController wc;
    DoctorController dc;
    TreatmentController tc;
    DiagnosisController diac;

    public TreatmentFactoryTest() {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException, CannotChangeException, WrongArgumentListException, ArgumentNotAnsweredException, PatientIsDischargedException, NotAFactoryException, ArgumentConstraintException, InvalidArgumentException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        tc = new TreatmentController(wc, dc);
        diac = new DiagnosisController(wc, dc);
        initPatientFile();
        initDiagnoses();
    }

    @Test
    public void findTreatments() throws ArgumentIsNullException, NotLoggedInException, NoOpenedPatientFileException {
        DiagnosisInfo[] infos = tc.getUntreatedDiagnoses();
        assertEquals("Length wrong", 2, infos.length);
        assertEquals("DiagnosisStringWrong", "Diagnose details: abdce\nDoctor: Gregory House", infos[0].toString());
        assertEquals("DiagnosisStringWrong", "Diagnose details: abdce2\nDoctor: Gregory House", infos[1].toString());
    }

    @Test
    public void getArguments() throws NotLoggedInException, NotAFactoryException {
        PublicArgument[] args = tc.getTreatmentArguments("Medication").getPublicArguments();
        assertEquals("Wrong length", 4, args.length);
        assertEquals("Wrong type, first", StringArgument.class, args[0].getClass());
        assertEquals("Wrong type, second", BooleanArgument.class, args[1].getClass());
        assertEquals("Wrong question, first", "Please enter a description: ", args[0].getQuestion());
        assertEquals("Wrong question, second", "Is the medication sensitive?: ", args[1].getQuestion());
    }

    @Test
    public void breakFactoryName() throws NotLoggedInException {
        try {
            tc.getTreatmentArguments(null);
            fail("throw exception");
        } catch (NotAFactoryException ex) {
        }
        try {
            tc.getTreatmentArguments("qdsfhjkldfhsjkdfhqsdkl"); //I hope they never create this treatment...
            fail("throw exception");
        } catch (NotAFactoryException ex) {
        }
    }

    private void initDiagnoses()
            throws NotLoggedInException, CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, NoPersonWithNameAndRoleException, NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException, PatientIsDischargedException, ArgumentIsNullException,
            NotAFactoryException,
            ArgumentConstraintException, InvalidArgumentException {
        ArgumentList args = diac.getDiagnosisArguments("Diagnosis");
        args.getPublicArguments()[0].enterAnswer("abdce");
        diac.enterDiagnosis("Diagnosis", args, null);
        args = diac.getDiagnosisArguments("Diagnosis");
        args.getPublicArguments()[0].enterAnswer("abdce2");
        diac.enterDiagnosis("Diagnosis", args, null);
    }

    private void initPatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException {
        dc.consultPatientFile("Ruben", wc);
    }
}
