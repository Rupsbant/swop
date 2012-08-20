package Hospital.MedicalTest;

import Hospital.Argument.StringArgument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import org.junit.Test;
import static org.junit.Assert.*;

public class XRayResultTest {

    @Test
    public void arguments()
            throws ArgumentConstraintException, CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, ArgumentIsNullException,
            InvalidArgumentException {
        XRayScan xray = new XRayScan(3, 2, "Armen");
        assertFalse("Result not entered yet", xray.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) xray.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be IntegerArgument", args[0].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be StringArgument", args[1].getClass().equals(StringArgument.class));
        args[0].enterAnswer("1");
        args[1].enterAnswer("Kwaadaardig gezwel in linkerarm, voorgestelde actie: afhakken");
        xray.enterResult(args);
        assertTrue("Result was entered", xray.isResultEntered());
        assertEquals("ResultString is not parsed correctly or arguments not entered correctly",
                "Number of images taken: 1\nAbnormalities: Kwaadaardig gezwel in linkerarm, voorgestelde actie: afhakken",
                xray.getResultString());
    }

    @Test
    public void wrongarguments()
            throws CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, ArgumentIsNullException,
            InvalidArgumentException {
        XRayScan xray = null;
        try {
            xray = new XRayScan(3, 2, "Armen");
        } catch (ArgumentConstraintException e) {
            fail("should not happen");
        }
        assertFalse("Result not entered yet", xray.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) xray.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be IntegerArgument", args[0].getClass().equals(IntegerArgument.class));
        assertTrue("Wrong argument, should be StringArgument", args[1].getClass().equals(StringArgument.class));
        args[0].enterAnswer("-51");
        args[1].enterAnswer("Kwaadaardig gezwel in linkerarm, voorgestelde actie: afhakken");
        try {
            xray.enterResult(args);
            fail("argumentcontraintexception must be thrown");
        } catch (ArgumentConstraintException e) {
        }
    }

    @Test
    public void argumentsListExceptions() throws ArgumentConstraintException, WrongArgumentListException, ArgumentNotAnsweredException, ArgumentIsNullException, CannotChangeException, InvalidArgumentException {
        XRayScan xray = new XRayScan(3, 2, "Armen");
        try {
            xray.enterResult(null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "No arguments given", e.getMessage());
        }
        PublicArgument[] args = new PublicArgument[0];
        try {
            xray.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Wrong argument length: 0 should be 2", e.getMessage());
        }
        args = new PublicArgument[2];
        try {
            xray.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "Argument \"The number of images\" was null", e.getMessage());
        }
    }

    @Test
    public void argumentsTypeBreaking()
            throws ArgumentConstraintException, ArgumentNotAnsweredException,
            ArgumentIsNullException, CannotChangeException,
            InvalidArgumentException {
        XRayScan xray = new XRayScan(3, 2, "Armen");
        PublicArgument[] args = new PublicArgument[2];
        args[0] = new StringArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("abc");
        try {
            xray.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be IntegerArgument", e.getMessage());
        }

        args[0] = new IntegerArgument("Test");
        args[0].enterAnswer("3");
        args[1] = new IntegerArgument("Test2");
        try {
            xray.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be StringArgument", e.getMessage());
        }
    }

    @Test
    public void argumentNotAnswered()
            throws CannotChangeException, ArgumentConstraintException,
            ArgumentNotAnsweredException, ArgumentIsNullException,
            WrongArgumentListException, InvalidArgumentException {
        XRayScan xray = new XRayScan(3, 2, "Armen");
        PublicArgument[] args = new PublicArgument[2];

        args[0] = new IntegerArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("String");
        try {
            xray.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"The number of images\" was null", e.getMessage());
        }
        args[0] = new IntegerArgument("Test");
        args[0].enterAnswer("3");
        args[1] = new StringArgument("Test2");
        try {
            xray.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"Abnormalities.\" was null", e.getMessage());
        }
    }
}
