package Hospital.Controllers;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.CannotDischargeException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Warehouse.ItemNotFoundException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Scheduling.ScheduleGroupUnavailable;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.TreatmentAlreadyAddedException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DischargePatientTest {

    private WorldController wc;
    private NurseController nc;
    private PatientController pc;
    private DoctorController dc;
    private DiagnosisController diagnC;
    private MedicalTestController medC;
    private MedicalTestResultController mtrC;
    private TreatmentController tc;
    private TreatmentResultController trC;

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0), new LoginInfo("Gregory House", StaffRole.Doctor)); //don't search for doctor
        nc = (NurseController) wc.login(wc.getCampuses().get(0), new LoginInfo("Nurse Joy", StaffRole.Nurse)); //don't search for nurse
        pc = new PatientController(wc, nc);
        diagnC = new DiagnosisController(wc, dc);
        medC = new MedicalTestController(wc, dc);
        mtrC = new MedicalTestResultController(wc, nc);
        tc = new TreatmentController(wc, dc);
        trC = new TreatmentResultController(wc, nc);
        pc.registerPatient("Patrick Ient");
    }

    @Test
    public void HappyTest() throws
            NoPersonWithNameAndRoleException,
            SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            InvalidArgumentException, NotEnoughItemsAvailableException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }

    @Test(expected = NoOpenedPatientFileException.class)
    public void NoOpenedPatientFileTest() throws
            CannotDischargeException,
            NoOpenedPatientFileException {
        dc.dischargePatient();
    }

    @Test(expected = CannotDischargeException.class)
    public void DischargeTwiceTest() throws
            NoPersonWithNameAndRoleException,
            SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            InvalidArgumentException, NotEnoughItemsAvailableException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
    }

    private void diagnose(String diagnosis, boolean snd_op) throws
            NotLoggedInException, NotAFactoryException,
            CannotChangeException, WrongArgumentListException, NoPersonWithNameAndRoleException,
            ArgumentNotAnsweredException, NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException, PatientIsDischargedException, InvalidDiagnosisException,
            StockException, ItemNotReservedException, ItemNotFoundException, IllegalInfo, InvalidArgumentException {
        LoginInfo doctorInfo = null;
        if (snd_op) {
            doctorInfo = new LoginInfo("Janet Fraiser", StaffRole.Doctor);
        }
        diagnC.enterDiagnosis(diagnosis, doctorInfo);
    }

    @Test(expected = CannotDischargeException.class)
    public void UnapprovedDiagnosisTest() throws
            SchedulableAlreadyExistsException, NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            SchedulingException, PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, CannotChangeException,
            WrongArgumentListException, InvalidArgumentException,
            PatientIsDischargedException, InvalidDiagnosisException, StockException, ItemNotReservedException, ItemNotFoundException, IllegalInfo, NotEnoughItemsAvailableException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        diagnose("Foo", true);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }

    @Test
    public void HappyWithDiagnosisTest() throws
            SchedulableAlreadyExistsException, ArgumentConstraintException,
            NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            ArgumentIsNullException, SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, CannotChangeException,
            WrongArgumentListException, ArgumentNotAnsweredException,
            PatientIsDischargedException, InvalidDiagnosisException, StockException, ItemNotReservedException, ItemNotFoundException, IllegalInfo, InvalidArgumentException, NotEnoughItemsAvailableException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);

        diagnose("Foo", false);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());

        DiagnosisInfo diagnosisinfo = new DiagnosisInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0));

        tc.makeSurgery(diagnosisinfo, "report blabla", new HighLowPriority(true));
        TreatmentInfo treatmentinfo = new TreatmentInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0).getTreatment());
        PublicArgument[] args2 = new PublicArgument[2];
        args2[0] = new StringArgument("Enter the report: ");
        args2[0].enterAnswer("reportje");
        args2[1] = new StringArgument("Enter the special aftercare: ");
        args2[1].enterAnswer("no special aftercare");

        trC.enterResult(treatmentinfo, args2);
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }

    @Test
    public void HappyWithMedicalTestTest() throws
            SchedulableAlreadyExistsException, ArgumentConstraintException,
            NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            ArgumentIsNullException, SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, WrongArgumentListException,
            ArgumentNotAnsweredException, CannotChangeException,
            IllegalInfo, InvalidArgumentException, NotEnoughItemsAvailableException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        String mtstring = medC.makeXRayScan("This should be an xray", 1, 1, new HighLowPriority(true));
        MedicalTestInfo medTest = null;
        for (MedicalTestInfo medTestInfo : mtrC.getOpenMedicalTests()) {
            if (medTestInfo.advancedString().equals(mtstring)) {
                medTest = medTestInfo;
            }
        }

        PublicArgument[] args = mtrC.getArguments(medTest);
        args[0].enterAnswer("1");
        args[1].enterAnswer("Bar");
        mtrC.enterResult(medTest, args);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }

    @Test(expected = CannotDischargeException.class)
    public void UnfinishedMedicalTestTest() throws
            SchedulableAlreadyExistsException, ArgumentConstraintException,
            NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            ArgumentIsNullException, SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, WrongArgumentListException,
            ArgumentNotAnsweredException, CannotChangeException, InvalidArgumentException, NotEnoughItemsAvailableException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        medC.makeXRayScan("This should be an xray", 1, 1, new HighLowPriority(true));
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }

    @Test
    public void HappyWithTreatmentTest() throws
            SchedulableAlreadyExistsException, ArgumentConstraintException,
            NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            ArgumentIsNullException, SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, WrongArgumentListException,
            ArgumentNotAnsweredException, CannotChangeException,
            PatientIsDischargedException, InvalidDiagnosisException,
            IllegalInfo, TreatmentAlreadyAddedException, NotEnoughItemsAvailableException, StockException, ItemNotReservedException, ItemNotFoundException, InvalidArgumentException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);

        diagnose("Foo", false);

        tc.makeMedication(tc.getUntreatedDiagnoses()[0], "Medication", false, "", new HighLowPriority(true));
        PublicArgument[] args2 = trC.getArguments(trC.getOpenTreatments()[0]);
        args2[0].enterAnswer("no");
        args2[1].enterAnswer("Bar");
        trC.enterResult(trC.getOpenTreatments()[0], args2);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }

    @Test(expected = CannotDischargeException.class)
    public void UnfinishedTreatmentTest() throws
            SchedulableAlreadyExistsException, ArgumentConstraintException,
            NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            ArgumentIsNullException, SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, WrongArgumentListException,
            ArgumentNotAnsweredException, CannotChangeException,
            PatientIsDischargedException, InvalidDiagnosisException,
            IllegalInfo, TreatmentAlreadyAddedException, NotEnoughItemsAvailableException, StockException, ItemNotReservedException, ItemNotFoundException, InvalidArgumentException {
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        diagnose("Foo", false);

        tc.makeMedication(tc.getUntreatedDiagnoses()[0], "Medication", false, "Aspirin", new HighLowPriority(true));
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }
}
