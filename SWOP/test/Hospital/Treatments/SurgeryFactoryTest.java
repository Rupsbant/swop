package Hospital.Treatments;

import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.InvalidArgumentException;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Hospital.Argument.Argument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.TreatmentController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Diagnosis;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;

public class SurgeryFactoryTest {
	WorldController wc;
    DoctorController dc;
    TreatmentController tc;
    DiagnosisController diac;

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
    public void testCreate() throws CannotChangeException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentIsNullException, ArgumentConstraintException, InvalidArgumentException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";
        SurgeryFactory med = new SurgeryFactory();
        Diagnosis dia = new Diagnosis("De patient leeft.",new Doctor("Doktoor"));
        Argument[] args = med.getEmptyArgumentList();
        ((PublicArgument) args[0]).enterAnswer(STRING);
        Treatment t = med.make(args);
        Surgery medication = (Surgery) t;
        assertEquals(medication.getDescription(), STRING);
    }

    @Test
    public void getArguments() throws NotLoggedInException, NotAFactoryException {
        PublicArgument[] args = tc.getTreatmentArguments("Surgery").getPublicArguments();
        assertEquals("Wrong length", 2, args.length);
        assertEquals("Wrong type, first", StringArgument.class, args[0].getClass());
        assertEquals("Wrong question, first", "Please enter a description: ", args[0].getQuestion());
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
