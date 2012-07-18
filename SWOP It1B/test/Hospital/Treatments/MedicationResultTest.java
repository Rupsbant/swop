package Hospital.Treatments;

import Hospital.WareHouse.ItemInfo;
import Hospital.Controllers.TestUtil;
import Hospital.Exception.Command.CannotDoException;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.StringArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Diagnosis;
import Hospital.People.Doctor;
import Hospital.WareHouse.ItemReservationCommand;
import Hospital.World.Campus;
import Hospital.World.World;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MedicationResultTest {

    Diagnosis d;
    Medication med;

    public MedicationResultTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotChangeException, ArgumentConstraintException, CannotDoException, NoPersonWithNameAndRoleException, NotLoggedInException, NoOpenedPatientFileException, WrongArgumentListException, ArgumentNotAnsweredException, PatientIsDischargedException, NotAFactoryException {

        d = new Diagnosis("abcde", new Doctor("Doktoor"));
        med = new Medication("pijnstillers", Boolean.TRUE, new ItemInfo[0]);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void arguments()
            throws CannotChangeException, WrongArgumentListException,
            InvalidArgumentException, StockException {
        assertFalse("Result not entered yet", med.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) med.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be IntegerArgument", args[0].getClass().equals(BooleanArgument.class));
        assertTrue("Wrong argument, should be StringArgument", args[1].getClass().equals(StringArgument.class));
        args[0].enterAnswer("no");
        args[1].enterAnswer("patient is genezen");
        World world = TestUtil.getWorldForTesting();
        Campus campus = world.getCampusFromInfo(world.getCampuses().get(0));
        med.setItemReservationCommand(new ItemReservationCommand(med));
        med.enterResult(args);
        assertTrue("Result was entered", med.isResultEntered());
        assertEquals("ResultString is not parsed correctly or arguments not entered correctly",
                "Abnormal reaction: false\nReport: patient is genezen",
                med.getResultString());
    }

    @Test
    public void argumentsListExceptions() throws InvalidArgumentException, WrongArgumentListException, CannotChangeException {
        try {
            med.enterResult(null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "No arguments given", e.getMessage());
        }
        PublicArgument[] args = new PublicArgument[0];
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Wrong argument length: 0 should be 2", e.getMessage());
        }
        args = new PublicArgument[2];
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "Argument \"Abnormal Reaction\" was null", e.getMessage());
        }
    }

    @Test
    public void argumentsTypeBreaking()
            throws InvalidArgumentException, CannotChangeException {
        PublicArgument[] args = new PublicArgument[2];
        args[0] = new StringArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("abc");
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be BooleanArgument", e.getMessage());
        }

        args[0] = new BooleanArgument("Test");
        args[1] = new IntegerArgument("Test2");
        args[0].enterAnswer("no");
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be StringArgument", e.getMessage());
        }
    }

    @Test
    public void argumentNotAnswered()
            throws CannotChangeException, InvalidArgumentException, WrongArgumentListException {
        PublicArgument[] args = new PublicArgument[2];

        args[0] = new BooleanArgument("Test");
        args[1] = new StringArgument("Test2");
        args[1].enterAnswer("string");
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"Abnormal Reaction\" was null", e.getMessage());
        }
        args[0] = new BooleanArgument("Test");
        args[1] = new StringArgument("Test2");
        args[0].enterAnswer("no");
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"Report\" was null", e.getMessage());
        }
    }
}
