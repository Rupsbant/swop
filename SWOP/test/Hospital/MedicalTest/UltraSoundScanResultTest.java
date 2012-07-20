package Hospital.MedicalTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Argument.Argument;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.WrongArgumentListException;

public class UltraSoundScanResultTest {

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
    public void arguments()
            throws InvalidArgumentException, CannotChangeException, WrongArgumentListException {
        UltraSoundScan ultra = new UltraSoundScan("been", true, false);
        assertFalse("Result not entered yet", ultra.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) ultra.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be StringArgument", args[0].getClass().equals(StringArgument.class));
        assertTrue("Wrong argument, should be StringArgument", args[1].getClass().equals(StringArgument.class));
        args[0].enterAnswer("1");
        args[1].enterAnswer("benign");
        ultra.enterResult(args);
        assertTrue("Result was entered", ultra.isResultEntered());
        assertEquals("ResultString is not parsed correctly or arguments not entered correctly",
                "Scan information: 1\nNature of scanned mass: benign",
                ultra.getResultString());
    }

    @Test
    public void wrongarguments()
            throws CannotChangeException, WrongArgumentListException,
            InvalidArgumentException {
        UltraSoundScan ultra = new UltraSoundScan("been", true, false);
        assertFalse("Result not entered yet", ultra.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) ultra.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be StringArgument", args[0].getClass().equals(StringArgument.class));
        assertTrue("Wrong argument, should be StringArgument", args[1].getClass().equals(StringArgument.class));
        args[0].enterAnswer("1");
        args[1].enterAnswer("benig");
        try {
            ultra.enterResult(args);
            fail("argumentconstraintexception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void argumentsListExceptions() 
            throws InvalidArgumentException, WrongArgumentListException, CannotChangeException {
        UltraSoundScan ultra = new UltraSoundScan("been", true, false);
        try {
            ultra.enterResult(null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "No arguments given", e.getMessage());
        }
        PublicArgument[] args = new PublicArgument[0];
        try {
            ultra.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Wrong argument length: 0 should be 2", e.getMessage());
        }
    }

    @Test
    public void argumentsTypeBreaking()
            throws InvalidArgumentException, CannotChangeException {
        UltraSoundScan ultra = new UltraSoundScan("been", true, false);
        PublicArgument[] args = new PublicArgument[2];
        args[0] = new IntegerArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("abc");
        try {
            ultra.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be StringArgument", e.getMessage());
        }

        args[0] = new IntegerArgument("Test");
        args[1] = new IntegerArgument("Test2");
        args[0].enterAnswer("3");
        try {
            ultra.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be StringArgument", e.getMessage());
        }
    }

    @Test
    public void argumentNotAnswered()
            throws CannotChangeException, InvalidArgumentException, WrongArgumentListException {

        PublicArgument[] args = new PublicArgument[2];
        UltraSoundScan ultra = new UltraSoundScan("been", true, false);
        args[0] = new StringArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("25");
        try {
            ultra.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"ScanInformation\" was null", e.getMessage());
        }
        args[0] = new StringArgument("Test");
        args[1] = new StringArgument("Test2");
        args[0].enterAnswer("3");
        try {
            ultra.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"The nature of the mass\" was null", e.getMessage());
        }
    }

    @Test
    public void ultrasoundFactoryTest() 
            throws WrongArgumentListException, InvalidArgumentException, CannotChangeException {
        UltraSoundScanFactory fact = new UltraSoundScanFactory();
        assertEquals(fact.getEmptyArgumentList().length, 3);
        Argument[] args = new Argument[3];
        args[0] = new StringArgument("string").enterAnswer("blabla");
        args[1] = new BooleanArgument("integer").enterAnswer("true");
        args[2] = new BooleanArgument("integer").enterAnswer("false");
        fact.make(args);
    }
}
