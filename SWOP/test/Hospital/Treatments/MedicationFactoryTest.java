package Hospital.Treatments;

import Hospital.People.StaffRole;
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
import Hospital.People.LoginInfo;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MedicationFactoryTest {

    private WorldController wc;
    private DoctorController dc;
    private TreatmentController tc;
    private DiagnosisController diac;

    public MedicationFactoryTest() {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException, CannotChangeException, WrongArgumentListException, ArgumentNotAnsweredException, PatientIsDischargedException, NotAFactoryException, ArgumentConstraintException, InvalidArgumentException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", StaffRole.Doctor)); //don't search for doctor
        tc = new TreatmentController(wc, dc);
        diac = new DiagnosisController(wc, dc);
        initPatientFile();
        initDiagnoses();
    }

    @Test
    public void testCreate() throws CannotChangeException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentIsNullException, ArgumentConstraintException, InvalidArgumentException {
        String STRING = "10x 500mg Ibuprofen bij elke maaltijd";

        Medication medication = new Medication(STRING, Boolean.TRUE, null);
        assertEquals(medication.getSensitive(), true);
        assertEquals(medication.getDescription(), STRING);
    }

    private void initDiagnoses()
            throws NotLoggedInException, CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, NoPersonWithNameAndRoleException, NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException, PatientIsDischargedException, ArgumentIsNullException,
            NotAFactoryException,
            ArgumentConstraintException, InvalidArgumentException {
        diac.enterDiagnosis("abcde", null);
        diac.enterDiagnosis("abdce2", null);
    }

    private void initPatientFile() throws NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException {
        dc.consultPatientFile("Ruben", wc);
    }
}
