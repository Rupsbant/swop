package Hospital.MedicalTest;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PriorityArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import java.util.Arrays;
import Hospital.Controllers.MedicalTestController;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.People.LoginInfo;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 * Deze testklasse zal testen of de XRay klasse goed werkt.
 * Deze zal kijken of ze toegevoegd wordt aan wereld
 * Of deze tot de juiste groep van testen behoort
 * Of deze de argumenten juist aanvaard
 * @author SWOP-12
 */
public class XRayFactoryTest {

    public WorldController wc;
    public DoctorController dc;
    public MedicalTestController mc;

    public XRayFactoryTest() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
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
        assertTrue("XRayTest was not added", Arrays.asList(mc.getAvailableMedicalTests()).contains("New XRayScan"));
    }

    @Test
    public void XRayArguments() throws NotLoggedInException, NotAFactoryException {
        PublicArgument[] factoryArguments = mc.getMedicalTestArguments("New XRayScan").getPublicArguments();
        assertEquals("Length os arguments wrong, should be 4", 4, factoryArguments.length);
        assertEquals("First argumentType wrong", StringArgument.class, factoryArguments[0].getClass());
        assertEquals("Second argumentType wrong", IntegerArgument.class, factoryArguments[1].getClass());
        assertEquals("Third argumentType wrong", IntegerArgument.class, factoryArguments[2].getClass());
        assertEquals("First Argumentquestion wrong", "Enter the body part: ", factoryArguments[0].getQuestion());
        assertEquals("Second Argumentquestion wrong", "Enter number of images: ", factoryArguments[1].getQuestion());
        assertEquals("Third Argumentquestion wrong", "Enter zoom: ", factoryArguments[2].getQuestion());
    }

    @Test
    public void Happy() throws CannotChangeException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        final ArgumentList XRayTestArgs = mc.getMedicalTestArguments("New XRayScan");
        PublicArgument[] factoryArguments = XRayTestArgs.getPublicArguments();

        dc.consultPatientFile("Ruben", wc);
        factoryArguments[0].enterAnswer("0");
        factoryArguments[1].enterAnswer("1");
        factoryArguments[2].enterAnswer("2");
        factoryArguments[3].enterAnswer("urgent");
        String out = mc.makeMedicalTest("New XRayScan", XRayTestArgs);
        String expected = "XRayTest: 0, 1, 2\n"
                + "No result entered\n"
                + "Appointment from 2011/11/8 9:00 of 15 minutes, until 2011/11/8 9:15 with 3 attendees.";
        assertEquals("Outputted String wrong", expected, out);
    }

    @Test
    public void wrongarguments() throws CannotChangeException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, WrongArgumentListException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        final ArgumentList xrayTestArgs = mc.getMedicalTestArguments("New XRayScan");
        PublicArgument[] factoryArguments = xrayTestArgs.getPublicArguments();

        dc.consultPatientFile("Ruben", wc);
        factoryArguments[0].enterAnswer("blabla");
        factoryArguments[1].enterAnswer("-2");
        factoryArguments[2].enterAnswer("2");
        String out;
        try {
            out = mc.makeMedicalTest("New XRayScan", xrayTestArgs);
            fail("argumentcontraintexception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void wrongarguments2() throws CannotChangeException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, WrongArgumentListException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        final ArgumentList XRayTestArgs = mc.getMedicalTestArguments("New XRayScan");
        PublicArgument[] factoryArguments = XRayTestArgs.getPublicArguments();

        dc.consultPatientFile("Ruben", wc);
        factoryArguments[0].enterAnswer("blabla");
        factoryArguments[1].enterAnswer("2");
        factoryArguments[2].enterAnswer("4");
        String out;
        try {
            out = mc.makeMedicalTest("New XRayScan", XRayTestArgs);
            fail("argumentcontraintexception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void NullListTest() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, WrongArgumentListException, ArgumentConstraintException, NoOpenedPatientFileException, NoPersonWithNameAndRoleException, ArgumentIsNullException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New XRayScan", null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "ArgumentList was null", ex.getMessage());
        }
    }

    @Test
    public void WrongLength() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(new PublicArgument[3]));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Length of ArgumentList is wrong, should be 3", ex.getMessage());
        }
        try {
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(new PublicArgument[5]));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Length of ArgumentList is wrong, should be 3", ex.getMessage());
        }
    }

    @Test
    public void NullArgument() throws NotAFactoryException, NotLoggedInException, ArgumentNotAnsweredException, ArgumentConstraintException, NoOpenedPatientFileException, ArgumentIsNullException, NoPersonWithNameAndRoleException, CannotChangeException, WrongArgumentListException, InvalidArgumentException {
        dc.consultPatientFile("Ruben", wc);
        try {
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(new PublicArgument[4]));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"BodyPart\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Number of images\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("IntQue").enterAnswer("3");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument \"Zoom\" was null", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("IntQue").enterAnswer("3");
            args[2] = new IntegerArgument("IntQue").enterAnswer("3");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
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
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be StringArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be IntegerArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("IntQue").enterAnswer("3");
            args[2] = new StringArgument("Que").enterAnswer("Ans");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (WrongArgumentListException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument is of the wrong type, it should be IntegerArgument", ex.getMessage());
        }
        try {
            PublicArgument[] args = new PublicArgument[4];
            args[0] = new StringArgument("Que").enterAnswer("Ans");
            args[1] = new IntegerArgument("IntQue").enterAnswer("3");
            args[2] = new IntegerArgument("Que").enterAnswer("3");
            args[3] = new StringArgument("Tekst").enterAnswer("Tekst");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
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
            args[3] = new PriorityArgument("question");
            String out = mc.makeMedicalTest("New XRayScan", new ArgumentList(args));
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException ex) {
            assertEquals("ExceptionMessage is wrong", "Argument-answer of \"the priority\" was null", ex.getMessage());
        }
    }
}
