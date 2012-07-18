package Hospital.Controllers;


import Hospital.WareHouse.ItemInfo;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.TreatmentAlreadyAddedException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Argument.StringArgument;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.PriorityArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Warehouse.ItemNotFoundException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Patient.Diagnosis;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;
import Hospital.Treatments.Medication;

import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author rupsbant
 */
public class EnterTreatmentResultControllerTest {

    public WorldController wc;
    public DoctorController dc;
    public NurseController nc;
    public TreatmentController tc;
    public DiagnosisController diac;
    public TreatmentResultController trc;
    PatientController pc;
    MedicalTestController medC;
    MedicalTestResultController mtrC;

    public EnterTreatmentResultControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws NoPersonWithNameAndRoleException, NotLoggedInException, CannotChangeException,
            WrongArgumentListException, InvalidArgumentException, NoOpenedPatientFileException,
            PatientIsDischargedException, NotAFactoryException,
            ArgumentConstraintException, InvalidDiagnosisException, TreatmentAlreadyAddedException, NotEnoughItemsAvailableException, StockException, ItemNotReservedException, ItemNotFoundException {
    	wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        nc = (NurseController) wc.login(wc.getCampuses().get(0),new LoginInfo("Nurse Joy", "Nurse")); //don't search for nurse
        pc = new PatientController(wc, nc);
        medC = new MedicalTestController(wc, dc);
        mtrC = new MedicalTestResultController(wc, nc);
        tc = new TreatmentController(wc, dc);
    	wc = TestUtil.getWorldControllerForTesting();
        diac = new DiagnosisController(wc, dc);
        trc = new TreatmentResultController(wc, nc);

        initPatientFile();
        initDiagnoses();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBasic() throws NotLoggedInException, WrongArgumentListException, InvalidArgumentException,
       	IllegalInfo, CannotChangeException, ArgumentConstraintException, NoOpenedPatientFileException, NotAFactoryException, InvalidDiagnosisException, StockException, ItemNotReservedException, ItemNotFoundException {
    	DiagnosisInfo[] infos = tc.getUntreatedDiagnoses();
        PublicArgument[] args2 = new PublicArgument[4];
        args2[0] = new StringArgument("description").enterAnswer("description");
        args2[1] = new BooleanArgument("sensitive").enterAnswer("yes");
        args2[2] = new StringArgument("items").enterAnswer("");
        args2[3] = new PriorityArgument("priority").enterAnswer("urgent");
        tc.makeTreatment("Medication", new ArgumentList(args2), infos[0]);
    	assertTrue("Treatments are not correctly filtered", trc.getOpenTreatments().length == 1);
        ArgumentList args = trc.getArguments(trc.getOpenTreatments()[0]);
        assertTrue("Arguments not right type", args.getPublicArguments()[0].getClass().equals(BooleanArgument.class));
        assertTrue("Arguments not right type", args.getPublicArguments()[1].getClass().equals(StringArgument.class));
        args.getPublicArguments()[0].enterAnswer("true");
        args.getPublicArguments()[1].enterAnswer("report");
        String out = trc.enterResult(trc.getOpenTreatments()[0], args);
        assertEquals("Outputted string wrong", "MedicationTreatment: description\nNo medication items!\nSensitive: true\nAbnormal reaction: true\nReport: report\nAppointment from 2011/11/8 9:00 of 20 minutes, until 2011/11/8 9:20 with 2 attendees.", out);
    }

    @Test
    public void testBreaking() throws CannotChangeException, NotLoggedInException,
            WrongArgumentListException, InvalidArgumentException, ArgumentIsNullException,
            IllegalInfo, ArgumentConstraintException, CannotDoException, NotAFactoryException, InvalidDiagnosisException, NoOpenedPatientFileException, StockException, ItemNotReservedException, ItemNotFoundException {
    	DiagnosisInfo[] infos = tc.getUntreatedDiagnoses();
        PublicArgument[] args2 = new PublicArgument[4];
        args2[0] = new StringArgument("description").enterAnswer("description");
        args2[1] = new BooleanArgument("sensitive").enterAnswer("yes");
        args2[2] = new StringArgument("items").enterAnswer("");
        args2[3] = new PriorityArgument("priority").enterAnswer("high");
        tc.makeTreatment("Medication", new ArgumentList(args2), infos[0]);
    	ArgumentList args = trc.getArguments(trc.getOpenTreatments()[0]);
        args.getPublicArguments()[0].enterAnswer("true");
        args.getPublicArguments()[1].enterAnswer("report");
        try {
            String out = trc.enterResult(null, args);
            fail("Exception should be thrown");
        } catch (IllegalInfo ex) {
            assertEquals("Exceptionmessage wrong", "Treatment is null", ex.getMessage());
        }
        try {
            String out = trc.enterResult(trc.getOpenTreatments()[0], null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("Exceptionmessage wrong", "ArgumentList was null", ex.getMessage());
        }
        try {
            Diagnosis d = new Diagnosis("Diagnosis", new Doctor("Doktoor"));
            Medication m = new Medication("description", Boolean.TRUE,new ItemInfo[0]);
            DiagnosisInfo diagnosis = new DiagnosisInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0));
            String out = trc.enterResult(m.getInfo(), args);
            fail("Exception should be thrown");
        } catch (IllegalInfo ex) {
            assertEquals("Exceptionmessage wrong", "Treatment does not exist in world", ex.getMessage());
        }
    }

    private void initDiagnoses()
            throws NotLoggedInException, CannotChangeException, WrongArgumentListException,
            InvalidArgumentException, NoPersonWithNameAndRoleException, NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException, PatientIsDischargedException, ArgumentIsNullException,
            NotAFactoryException,
            ArgumentConstraintException {
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
