package Hospital.Treatments;

import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.TreatmentController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Diagnosis;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MedicationFactoryTest {

    WorldController wc;
    DoctorController dc;
    TreatmentController tc;
    DiagnosisController diac;

    public MedicationFactoryTest() {
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
    public void testCreate() throws CannotChangeException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentIsNullException, ArgumentConstraintException, InvalidArgumentException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";

        MedicationFactory med = new MedicationFactory();
        Diagnosis dia = new Diagnosis("De patient leeft.",new Doctor("Doktoor"));
        PublicArgument[] args = new PublicArgument[3];
        args[0] = new StringArgument("blabla").enterAnswer(STRING);
        args[1] = new BooleanArgument("blabla").enterAnswer("true");
        args[2] = new StringArgument("blabla").enterAnswer("ActivatedCarbon Aspirin");
        Treatment t = med.make(args);
        Medication medication = (Medication) t;
        assertEquals(medication.getSensitive(), true);
        assertEquals(medication.getDescription(), STRING);
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
