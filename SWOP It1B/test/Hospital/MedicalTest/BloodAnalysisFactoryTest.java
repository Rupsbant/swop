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

public class BloodAnalysisFactoryTest {

    public WorldController wc;
    public DoctorController dc;
    public MedicalTestController mc;

    public BloodAnalysisFactoryTest() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
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
    public void XRayAvailable() {
        assertTrue("BloodAnalysis was not added", Arrays.asList(mc.getAvailableMedicalTests()).contains("New BloodAnalysis"));
    }

    @Test
    public void XRayArguments() throws NotLoggedInException, NotAFactoryException {
        PublicArgument[] factoryArguments = mc.getMedicalTestArguments("New BloodAnalysis").getPublicArguments();
        assertEquals("Length of arguments wrong, should be 3", 3, factoryArguments.length);
        assertEquals("First argumentType wrong", StringArgument.class, factoryArguments[0].getClass());
        assertEquals("Second argumentType wrong", IntegerArgument.class, factoryArguments[1].getClass());
        assertEquals("First Argumentquestion wrong", "Enter the focus: ", factoryArguments[0].getQuestion());
        assertEquals("Second Argumentquestion wrong", "Enter number of analyses: ", factoryArguments[1].getQuestion());
    }

    @Test
    public void Happy() throws CannotChangeException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        final ArgumentList argL = mc.getMedicalTestArguments("New BloodAnalysis");
        PublicArgument[] factoryArguments = argL.getPublicArguments();
        dc.consultPatientFile("Ruben", wc);
        factoryArguments[0].enterAnswer("been");
        factoryArguments[1].enterAnswer("15");
        factoryArguments[2].enterAnswer("urgent");
        String out = mc.makeMedicalTest("New BloodAnalysis", argL);
        String expected = "BloodAnalysis: been, 15\nNo result entered\nAppointment from 2011/11/8 9:00 of 45 minutes, until 2011/11/8 9:45 with 3 attendees.";
        assertEquals("Outputted String wrong", expected, out);
    }

    @Test
    public void WrongArguments() throws CannotChangeException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, WrongArgumentListException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        final ArgumentList argL = mc.getMedicalTestArguments("New BloodAnalysis");
        PublicArgument[] factoryArguments = argL.getPublicArguments();
        dc.consultPatientFile("Ruben", wc);
        factoryArguments[0].enterAnswer("been");
        factoryArguments[1].enterAnswer("-5");
        try {
            String out = mc.makeMedicalTest("New BloodAnalysis", argL);
            fail("argumentcontratintexception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void NullListTest() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, NoPersonWithNameAndRoleException, ArgumentIsNullException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New BloodAnalysis", null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "ArgumentList was null", ex.getMessage());
        }
    }

    @Test
    public void WrongLength() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New BloodAnalysis", new ArgumentList(new PublicArgument[4]));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Length of ArgumentList is wrong, should be 2", ex.getMessage());
        }
        try {
            String out = mc.makeMedicalTest("New BloodAnalysis", new ArgumentList(new PublicArgument[2]));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Length of ArgumentList is wrong, should be 2", ex.getMessage());
        }
    }

    @Test
    public void NullArgument() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, CannotChangeException, WrongArgumentListException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New BloodAnalysis", new ArgumentList(new PublicArgument[3]));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Focus\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[3];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New BloodAnalysis", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Number of analyses\" was null", ex.getMessage());
        }
    }

    @Test
    public void WrongType() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, CannotChangeException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            PublicArgument[] args = new PublicArgument[3];
            args[0] = new IntegerArgument("IntQue").enterAnswer("3");
            args[2] = new PriorityArgument("priority").enterAnswer("high");
            String out = mc.makeMedicalTest("New BloodAnalysis", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be StringArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[3];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New BloodAnalysis", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be IntegerArgument", ex.getMessage());
        }
    }

    @Test
    public void NotAnswered() throws NotAFactoryException, NotLoggedInException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, CannotChangeException, NoPersonWithNameAndRoleException, WrongArgumentListException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("IntQue");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"BodyPart\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("Que");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"Number of images\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("IntQue").enterAnswer("3");
            args[2] = new IntegerArgument("Que");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"Zoom\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("IntQue").enterAnswer("3");
            args[2] = new IntegerArgument("Que").enterAnswer("3");
            args[3] = new PriorityArgument("Que");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"the priority\" was null", ex.getMessage());
        }
    }
}
