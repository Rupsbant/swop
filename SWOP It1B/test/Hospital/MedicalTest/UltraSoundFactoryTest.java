package Hospital.MedicalTest;

import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.InvalidArgumentException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Argument.BooleanArgument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PriorityArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.MedicalTestController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;

public class UltraSoundFactoryTest {

    public WorldController wc;
    public DoctorController dc;
    public MedicalTestController mc;

    public UltraSoundFactoryTest() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        mc = new MedicalTestController(wc, dc);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void UltraSoundAvailable() {
        assertTrue("UltraSoundScan was not added", Arrays.asList(mc.getAvailableMedicalTests()).contains("New UltraSoundScan"));
    }

    @Test
    public void UltraSoundArguments() throws NotLoggedInException, NotAFactoryException {
        PublicArgument[] factoryArguments = mc.getMedicalTestArguments("New UltraSoundScan").getPublicArguments();
        assertEquals("Length os arguments wrong, should be 4", 4, factoryArguments.length);
        assertEquals("First argumentType wrong", StringArgument.class, factoryArguments[0].getClass());
        assertEquals("Second argumentType wrong", BooleanArgument.class, factoryArguments[1].getClass());
        assertEquals("Third argumentType wrong", BooleanArgument.class, factoryArguments[2].getClass());
        assertEquals("First Argumentquestion wrong", "Enter the focus: ", factoryArguments[0].getQuestion());
        assertEquals("Second Argumentquestion wrong", "Record video?: ", factoryArguments[1].getQuestion());
        assertEquals("Third Argumentquestion wrong", "Record images?: ", factoryArguments[2].getQuestion());
    }

    @Test
    public void Happy() throws CannotChangeException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        final ArgumentList ultraSoundArgs = mc.getMedicalTestArguments("New UltraSoundScan");
        PublicArgument[] factoryArguments = ultraSoundArgs.getPublicArguments();

        dc.consultPatientFile("Ruben", wc);
        factoryArguments[0].enterAnswer("0");
        factoryArguments[1].enterAnswer("false");
        factoryArguments[2].enterAnswer("true");
        factoryArguments[3].enterAnswer("high");
        String out = mc.makeMedicalTest("New UltraSoundScan", ultraSoundArgs);
        String expected = "UltraSoundScan: 0, false, true\nNo result entered\nAppointment from 2011/11/8 9:00 of 30 minutes, until 2011/11/8 9:30 with 3 attendees.";
        assertEquals("Outputted String wrong", expected, out);
    }

    @Test
    public void NullListTest() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, NoPersonWithNameAndRoleException, ArgumentIsNullException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New UltraSoundScan", null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "ArgumentList was null", ex.getMessage());
        }
    }

    @Test
    public void WrongLength() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(new PublicArgument[2+1]));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Length of ArgumentList is wrong, should be 3", ex.getMessage());
        }
        try {
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(new PublicArgument[4+1]));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Length of ArgumentList is wrong, should be 3", ex.getMessage());
        }
    }

    @Test
    public void NullArgument() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, CannotChangeException, WrongArgumentListException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(new PublicArgument[3+1]));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Focus\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Record Video\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("IntQue").enterAnswer("yes");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Record Images\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("IntQue").enterAnswer("yes");
            args[2] = new BooleanArgument("abc").enterAnswer("yes");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"the priority\" was null", ex.getMessage());
        }
    }

    @Test
    public void WrongType() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, CannotChangeException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new IntegerArgument("IntQue").enterAnswer("3");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be StringArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new StringArgument("Que").enterAnswer("no");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be BooleanArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("IntQue").enterAnswer("yes");
            args[2] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be BooleanArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("IntQue").enterAnswer("true");
            args[2] = new BooleanArgument("Que").enterAnswer("true");
            args[3] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be PriorityArgument", ex.getMessage());
        }
    }

    @Test
    public void NotAnswered() throws NotAFactoryException, NotLoggedInException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, CannotChangeException, NoPersonWithNameAndRoleException, WrongArgumentListException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("IntQue");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"Focus\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("Que");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"Record Video\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("IntQue").enterAnswer("true");
            args[2] = new BooleanArgument("Que");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"Record Images\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new BooleanArgument("IntQue").enterAnswer("true");
            args[2] = new BooleanArgument("Que").enterAnswer("yes");
            args[3] = new PriorityArgument("question");
            String out = mc.makeMedicalTest("New UltraSoundScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"the priority\" was null", ex.getMessage());
        }
    }
}
