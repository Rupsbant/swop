package Hospital.Treatments;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Argument.BooleanArgument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Warehouse.ItemNotFoundException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Diagnosis;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.WareHouse.ItemReservationCommand;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.World;
import java.util.Collections;

public class CastResultTest {

    Diagnosis d;
    Cast med;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, ArgumentConstraintException, CannotChangeException, CannotDoException, NoPersonWithNameAndRoleException, NotLoggedInException, NotAFactoryException, WrongArgumentListException, ArgumentNotAnsweredException, NoOpenedPatientFileException, PatientIsDischargedException, InvalidDiagnosisException, StockException, ItemNotReservedException, ItemNotFoundException, IllegalInfo, SchedulableAlreadyExistsException, SchedulingException, PatientIsCheckedInException {
    	d = new Diagnosis("abcde", new Doctor("Doktoor"));
        med = new Cast("pijnstillers", 15);
    }
    @After
    public void tearDown() {
    }

    @Test
    public void arguments()
            throws ArgumentConstraintException, CannotChangeException, WrongArgumentListException,
            ArgumentNotAnsweredException, ArgumentIsNullException, StockException, InvalidArgumentException, CannotDoException {
        assertFalse("Result not entered yet", med.isResultEntered());
        PublicArgument[] args = (PublicArgument[]) med.getEmptyResultArgumentList();
        assertTrue("Wrong argument, should be IntegerArgument", args[0].getClass().equals(StringArgument.class));
        args[0].enterAnswer("reportje");
        med.setAppointment(new Appointment(new Time(), 15, Collections.EMPTY_LIST, null, new Campus("Cast Campus", new World())));
        ItemReservationCommand itemReservationCommand = new ItemReservationCommand(med);
        itemReservationCommand.execute();
        med.setItemReservationCommand(itemReservationCommand);
        med.enterResult(args);
        assertTrue("Result was entered", med.isResultEntered());
        assertEquals("Report: reportje", med.getResultString());
    }

    @Test
    public void argumentsListExceptions() throws ArgumentConstraintException, WrongArgumentListException, ArgumentNotAnsweredException, ArgumentIsNullException, CannotChangeException, InvalidArgumentException {
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
            assertEquals("Exceptionmessage is wrong", "Wrong argument length: 0 should be 1", e.getMessage());
        }
        args = new PublicArgument[1];
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Exceptionmessage is wrong", "Argument \"Report\" was null", e.getMessage());
        }
    }

    @Test
    public void argumentsTypeBreaking()
            throws ArgumentConstraintException, ArgumentNotAnsweredException,
            ArgumentIsNullException, CannotChangeException, InvalidArgumentException {
        PublicArgument[] args = new PublicArgument[1];
        args[0] = new IntegerArgument("Test");
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Exceptionmessage is wrong", "Argument is of the wrong type, it should be StringArgument", e.getMessage());
        }

        args[0] = new BooleanArgument("Test");
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
            throws CannotChangeException, ArgumentConstraintException,
            ArgumentNotAnsweredException, ArgumentIsNullException,
            WrongArgumentListException, InvalidArgumentException {
        PublicArgument[] args = new PublicArgument[1];
        args[0] = new StringArgument("Test");
        try {
            med.enterResult(args);
            fail("Exception should be thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals("Exceptionmessage is wrong", "Argument-answer of \"Report\" was null", e.getMessage());
        }
    }
}
