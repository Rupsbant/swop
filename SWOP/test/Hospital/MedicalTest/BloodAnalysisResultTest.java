package Hospital.MedicalTest;

import Hospital.Exception.Arguments.InvalidArgumentException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.WrongArgumentListException;

public class BloodAnalysisResultTest {

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
            throws ArgumentConstraintException, CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        BloodAnalysis blood = new BloodAnalysis("Armen", 5);
        assertFalse("Result not entered yet", blood.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) blood.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be IntegerArgument", args[0].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be IntegerArgument", args[1].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be IntegerArgument", args[2].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be IntegerArgument", args[3].getClass().equals(IntegerArgument.class));
        args[0].enterAnswer("1");
        args[1].enterAnswer("5");
        args[2].enterAnswer("6");
        args[3].enterAnswer("2");
        blood.enterResult(args);
        assertTrue("Result was entered", blood.isResultEntered());
        assertEquals("ResultString is not parsed correctly or arguments not entered correctly",
                "Amount of blood withdrawn: 1\nRed cell count: 5\nWhite cell count: 6\nPlatelet count: 2",
                blood.getResultString());
    }

    @Test
    public void wrongarguments()
            throws CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        BloodAnalysis blood = null;
        try {
            blood = new BloodAnalysis("Armen", 5);
        } catch (ArgumentConstraintException e1) {
            fail("should not happen");
        }
        assertFalse("Result not entered yet", blood.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) blood.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be IntegerArgument", args[0].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be IntegerArgument", args[1].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be IntegerArgument", args[2].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be IntegerArgument", args[3].getClass().equals(IntegerArgument.class));
        args[0].enterAnswer("1");
        args[1].enterAnswer("5");
        args[2].enterAnswer("-6");
        args[3].enterAnswer("2");
        try {
            blood.enterResult(args);
            fail("argumentcontraintexception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void argumentsListExceptions() throws ArgumentConstraintException, WrongArgumentListException, ArgumentNotAnsweredException, ArgumentIsNullException, CannotChangeException, InvalidArgumentException {
        BloodAnalysis blood = new BloodAnalysis("Armen", 5);
        try {
            blood.enterResult(null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "No arguments given", e.getMessage());
        }
        PublicArgument[] args = new PublicArgument[0];
        try {
            blood.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Wrong argument length: 0 should be 4", e.getMessage());
        }
    }

    @Test
    public void argumentsTypeBreaking()
            throws ArgumentConstraintException, ArgumentNotAnsweredException,
            ArgumentIsNullException, CannotChangeException, InvalidArgumentException {
        BloodAnalysis blood = new BloodAnalysis("Armen", 5);
        PublicArgument[] args = new PublicArgument[4];
        args[0] = new StringArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("abc");
        try {
            blood.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be IntegerArgument", e.getMessage());
        }

        args[0] = new IntegerArgument("Test");
        args[1] = new IntegerArgument("Test2");
        args[2] = new StringArgument("Test3");
        args[3] = new IntegerArgument("Test4");
        args[0].enterAnswer("3");
        args[1].enterAnswer("5");
        args[2].enterAnswer("15");
        args[3].enterAnswer("35");
        try {
            blood.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be IntegerArgument", e.getMessage());
        }
    }

    @Test
    public void argumentNotAnswered()
            throws CannotChangeException, ArgumentConstraintException,
            ArgumentNotAnsweredException, ArgumentIsNullException,
            WrongArgumentListException, InvalidArgumentException {
        BloodAnalysis blood = new BloodAnalysis("Armen", 5);
        PublicArgument[] args = new PublicArgument[4];

        args[0] = new IntegerArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("string");
        try {
            blood.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
        }
        args[0] = new IntegerArgument("Test");
        args[1] = new IntegerArgument("Test2");
        args[0].enterAnswer("3");
        try {
            blood.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
        }
    }
}
