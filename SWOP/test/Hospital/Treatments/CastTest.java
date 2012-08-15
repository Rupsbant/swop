package Hospital.Treatments;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
import Hospital.People.StaffRole;

public class CastTest {

    private WorldController wc;
    private DoctorController dc;
    private TreatmentController tc;
    private DiagnosisController diac;

    @Before
    public void setUp()
            throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException,
            CannotChangeException, WrongArgumentListException, InvalidArgumentException, PatientIsDischargedException, NotAFactoryException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", StaffRole.Doctor)); //don't search for doctor
        tc = new TreatmentController(wc, dc);
        diac = new DiagnosisController(wc, dc);
        initPatientFile();
        initDiagnoses();
    }

    @Test
    public void testCreate() throws CannotChangeException, InvalidArgumentException, WrongArgumentListException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";
        Diagnosis dia = new Diagnosis("De patient leeft.", new Doctor("Doktoor"));
        Cast cast = new Cast(STRING, 17);
        assertEquals(cast.getBodyPart(), STRING);
        assertEquals(cast.getDuration(), 17);
    }

    @Test
    public void testCreateInvalid() throws CannotChangeException, InvalidArgumentException, WrongArgumentListException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";
        Diagnosis dia = new Diagnosis("De patient leeft.", new Doctor("Doktoor"));
        try {
            Cast c = new Cast(STRING, -11);
            fail("Exception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    private void initDiagnoses()
            throws NotLoggedInException, CannotChangeException, WrongArgumentListException,
            InvalidArgumentException, NoPersonWithNameAndRoleException, NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException, PatientIsDischargedException, NotAFactoryException {
        diac.enterDiagnosis("abcde", null);
        diac.enterDiagnosis("abdce2", null);
    }

    private void initPatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException {
        dc.consultPatientFile("Ruben", wc);
    }
}
