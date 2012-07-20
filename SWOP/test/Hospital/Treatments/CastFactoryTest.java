package Hospital.Treatments;

import Hospital.Controllers.ArgumentList;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Hospital.Argument.Argument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
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
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Diagnosis;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;

public class CastFactoryTest {

    WorldController wc;
    DoctorController dc;
    TreatmentController tc;
    DiagnosisController diac;

    @Before
    public void setUp()
            throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException,
            CannotChangeException, WrongArgumentListException, InvalidArgumentException, PatientIsDischargedException, NotAFactoryException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        tc = new TreatmentController(wc, dc);
        diac = new DiagnosisController(wc, dc);
        initPatientFile();
        initDiagnoses();
    }

    @Test
    public void testCreate() throws CannotChangeException, InvalidArgumentException, WrongArgumentListException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";
        CastFactory med = new CastFactory();
        Diagnosis dia = new Diagnosis("De patient leeft.", new Doctor("Doktoor"));
        Argument[] args = med.getEmptyArgumentList();
        ((PublicArgument) args[0]).enterAnswer(STRING);
        ((PublicArgument) args[1]).enterAnswer("17");
        Treatment t = med.make(args);
        Cast cast = (Cast) t;
        assertEquals(cast.getBodyPart(), STRING);
        assertEquals(cast.getDuration(), 17);
    }

    @Test
    public void testCreateInvalid() throws CannotChangeException, InvalidArgumentException, WrongArgumentListException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";
        CastFactory med = new CastFactory();
        Diagnosis dia = new Diagnosis("De patient leeft.", new Doctor("Doktoor"));
        Argument[] args = med.getEmptyArgumentList();
        ((PublicArgument) args[0]).enterAnswer(STRING);
        ((PublicArgument) args[1]).enterAnswer("-2");
        Treatment t;
        try {
            med.make(args);
            fail("Exception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void getArguments() throws NotLoggedInException, NotAFactoryException {
        PublicArgument[] args = tc.getTreatmentArguments("Cast").getPublicArguments();
        assertEquals("Wrong length", 3, args.length);
        assertEquals("Wrong type, first", StringArgument.class, args[0].getClass());
        assertEquals("Wrong type, second", IntegerArgument.class, args[1].getClass());
        assertEquals("Wrong question, first", "Please enter the body part: ", args[0].getQuestion());
        assertEquals("Wrong question, second", "Please enter the duration in days: ", args[1].getQuestion());
    }

    private void initDiagnoses()
            throws NotLoggedInException, CannotChangeException, WrongArgumentListException,
            InvalidArgumentException, NoPersonWithNameAndRoleException, NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException, PatientIsDischargedException, NotAFactoryException {
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
