package Hospital.Controllers;

import Hospital.Argument.BooleanArgument;
import Hospital.Argument.PriorityArgument;
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
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DischargePatientTest {

    WorldController wc;
    NurseController nc;
    PatientController pc;
    DoctorController dc;
    DiagnosisController diagnC;
    MedicalTestController medC;
    MedicalTestResultController mtrC;
    TreatmentController tc;
    TreatmentResultController trC;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        nc = (NurseController) wc.login(wc.getCampuses().get(0),new LoginInfo("Nurse Joy", "Nurse")); //don't search for nurse
        pc = new PatientController(wc, nc);
        diagnC = new DiagnosisController(wc, dc);
        medC = new MedicalTestController(wc, dc);
        mtrC = new MedicalTestResultController(wc, nc);
        tc = new TreatmentController(wc, dc);
        trC = new TreatmentResultController(wc, nc);
    }

    private void regPat(String name) throws SchedulableAlreadyExistsException, NotLoggedInException, NotAFactoryException, WrongArgumentListException, CannotChangeException, InvalidArgumentException {
        ArgumentList args = pc.getFactoryArguments(pc.getAvailablePatientFactories()[0]);
        args.getPublicArguments()[0].enterAnswer(name);
        pc.registerPatient(pc.getAvailablePatientFactories()[0], args);
    }

    @Test
    public void HappyTest() throws
            SchedulableAlreadyExistsException, NoPersonWithNameAndRoleException,
            ScheduleGroupUnavailable,SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, WrongArgumentListException,CannotChangeException, InvalidArgumentException, NotEnoughItemsAvailableException {
        regPat("Patrick Ient");
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
            SchedulableAlreadyExistsException, NoPersonWithNameAndRoleException,
            ScheduleGroupUnavailable, SchedulingException,
            PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, WrongArgumentListException, CannotChangeException, InvalidArgumentException, NotEnoughItemsAvailableException {
        regPat("Patrick Ient");
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
        int fact = 0;
        ArgumentList args = diagnC.getDiagnosisArguments(diagnC.getAvailableDiagnosisFactories()[fact]);
        args.getPublicArguments()[0].enterAnswer(diagnosis);
        LoginInfo doctorInfo = null;
        if (snd_op) {
            doctorInfo = new LoginInfo("Janet Fraiser", "Doctor");
            fact = 1;
        }
        diagnC.enterDiagnosis(diagnC.getAvailableDiagnosisFactories()[fact], args, doctorInfo);
    }

    @Test(expected = CannotDischargeException.class)
    public void UnapprovedDiagnosisTest() throws
            SchedulableAlreadyExistsException, NoPersonWithNameAndRoleException, ScheduleGroupUnavailable,
            SchedulingException, PatientIsCheckedInException, NotLoggedInException,
            NoOpenedPatientFileException, CannotDischargeException,
            NotAFactoryException, CannotChangeException,
            WrongArgumentListException, InvalidArgumentException, 
            PatientIsDischargedException, InvalidDiagnosisException, StockException, ItemNotReservedException, ItemNotFoundException, IllegalInfo, NotEnoughItemsAvailableException {
        regPat("Patrick Ient");
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
        regPat("Patrick Ient");
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        
        diagnose("Foo", false);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        
        PublicArgument[] args1 = new PublicArgument[2];
        args1[0] = new StringArgument("Enter the report: ").enterAnswer("report blabla");
        args1[1] = new PriorityArgument("priority").enterAnswer("high");
        DiagnosisInfo diagnosisinfo = new DiagnosisInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0));
        
        tc.makeTreatment("Surgery", new ArgumentList(args1), diagnosisinfo);
        TreatmentInfo treatmentinfo = new TreatmentInfo(dc.getUser().getOpenedPatient().getDiagnoses().get(0).getTreatment());
        PublicArgument[] args2 = new PublicArgument[2];
        args2[0] = new StringArgument("Enter the report: ").enterAnswer("reportje");
        args2[1] = new StringArgument("Enter the special aftercare: ").enterAnswer("no special aftercare");
        
        trC.enterResult(treatmentinfo, new ArgumentList(args2));
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
        regPat("Patrick Ient");
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        System.out.println(medC.getAvailableMedicalTests()[0]);
        ArgumentList args = medC.getMedicalTestArguments(medC.getAvailableMedicalTests()[0]);
        args.getPublicArguments()[0].enterAnswer("This should be an xray");
        args.getPublicArguments()[1].enterAnswer("1");
        args.getPublicArguments()[2].enterAnswer("1");
        args.getPublicArguments()[3].enterAnswer("urgent");
        String mtstring = medC.makeMedicalTest(medC.getAvailableMedicalTests()[0], args);
        MedicalTestInfo medTest = null;
        for (MedicalTestInfo medTestInfo : mtrC.getOpenMedicalTests()) {
            if (medTestInfo.advancedString().equals(mtstring)) {
                medTest = medTestInfo;
            }
        }
        
        args = mtrC.getArguments(medTest);
        args.getPublicArguments()[0].enterAnswer("1");
        args.getPublicArguments()[1].enterAnswer("Bar");
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
        regPat("Patrick Ient");
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        ArgumentList args = medC.getMedicalTestArguments(medC.getAvailableMedicalTests()[0]);
        args.getPublicArguments()[0].enterAnswer("This should be an xray");
        args.getPublicArguments()[1].enterAnswer("1");
        args.getPublicArguments()[2].enterAnswer("1");
        args.getPublicArguments()[3].enterAnswer("urgent");
        medC.makeMedicalTest(medC.getAvailableMedicalTests()[0], args);
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
        regPat("Patrick Ient");
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);

        diagnose("Foo", false);

        PublicArgument[] args = new PublicArgument[4];
        args[0] = new StringArgument("description").enterAnswer("Medication");
        args[1] = new BooleanArgument("sensitive").enterAnswer("no");
        args[2] = new StringArgument("items").enterAnswer("");
        args[3] = new PriorityArgument("priority").enterAnswer("urgent");
        System.out.println("Make");
        tc.makeTreatment(tc.getAvailableTreatments()[0], new ArgumentList(args), tc.getUntreatedDiagnoses()[0]);
        ArgumentList args2 = trC.getArguments(trC.getOpenTreatments()[0]);
        args2.getPublicArguments()[0].enterAnswer("no");
        args2.getPublicArguments()[1].enterAnswer("Bar");
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
        regPat("Patrick Ient");
        nc.checkIn("Patrick Ient", dc.getUser().getName(), wc);
        dc.consultPatientFile("Patrick Ient", wc);
        diagnose("Foo", false);
        PublicArgument[] args = new PublicArgument[4];
        args[0] = new StringArgument("description").enterAnswer("Medication");
        args[1] = new BooleanArgument("sensitive").enterAnswer("no");
        args[2] = new StringArgument("items").enterAnswer("Aspirin");
        args[3] = new PriorityArgument("priority").enterAnswer("urgent");
        tc.makeTreatment(tc.getAvailableTreatments()[0], new ArgumentList(args), tc.getUntreatedDiagnoses()[0]);
        assertFalse(dc.getUser().getOpenedPatient().isDischarged());
        dc.dischargePatient();
        assertTrue(dc.getUser().getOpenedPatient().isDischarged());
    }
}
