package Hospital.Controllers;

import Hospital.Exception.Arguments.WrongArgumentListException;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Patient.Patient;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;

public class CheckInPatientTest {

    private WorldController wc;
    private NurseController nc;
    private PatientController pc;

    @Before
    public void setUp() throws NoPersonWithNameAndRoleException, InvalidArgumentException, NotLoggedInException, NotAFactoryException, CannotChangeException, SchedulableAlreadyExistsException, WrongArgumentListException {
        wc = TestUtil.getWorldControllerForTesting();
        nc = (NurseController) wc.login(wc.getCampuses().get(0),new LoginInfo("Nurse Joy", StaffRole.Nurse));
        pc = new PatientController(wc, nc);
        ArgumentList args = pc.getFactoryArguments(pc.getAvailablePatientFactories()[0]);
        ArgumentList args2 = pc.getFactoryArguments(pc.getAvailablePatientFactories()[0]);
        args.getPublicArguments()[0].enterAnswer("Patient1");
        args2.getPublicArguments()[0].enterAnswer("Patient2");
        pc.registerPatient(pc.getAvailablePatientFactories()[0], args);
        pc.registerPatient(pc.getAvailablePatientFactories()[0], args2);
    }

    @Test
    public void checkInTest() throws NoPersonWithNameAndRoleException, InvalidArgumentException, SchedulingException, PatientIsCheckedInException, NotLoggedInException, NotEnoughItemsAvailableException {
        String result1 = null, result2 = null;
        result1 = nc.checkIn("Patient1", "Doktoor", wc);
        result2 = nc.checkIn("Patient2", "Doktoor", wc);
        assertEquals("Appointment from 2011/11/8 9:00 of 30 minutes, until 2011/11/8 9:30 with 2 attendees.", result1);
        assertEquals("Appointment from 2011/11/8 9:30 of 30 minutes, until 2011/11/8 10:00 with 2 attendees.", result2);
        assertFalse(wc.getWorld().getPersonByName(Patient.class, "Patient1").isDischarged());
        assertFalse(wc.getWorld().getPersonByName(Patient.class, "Patient2").isDischarged());
        assertEquals(nc.getCampusController().getCampus(),wc.getWorld().getPersonByName(Patient.class, "Patient1").getCampus());
        assertEquals(nc.getCampusController().getCampus(),wc.getWorld().getPersonByName(Patient.class, "Patient2").getCampus());
    }

    @Test
    public void checkInWrongPatientTest() throws InvalidArgumentException, SchedulingException, PatientIsCheckedInException, NotLoggedInException, NotEnoughItemsAvailableException {
        String result1;
        try {
            result1 = nc.checkIn("blabla", "Doktoor", wc);
        } catch (NoPersonWithNameAndRoleException e) {
        }
    }

    @Test
    public void checkInWrongDoctorTest() throws InvalidArgumentException, SchedulingException, PatientIsCheckedInException, NotLoggedInException, NotEnoughItemsAvailableException {
        String result1;
        try {
            result1 = nc.checkIn("Patien1", "blabla", wc);
        } catch (NoPersonWithNameAndRoleException e) {
        }
    }

    @Test
    public void checkInWrongWorldControllerTest() throws NoPersonWithNameAndRoleException, InvalidArgumentException, SchedulingException, PatientIsCheckedInException, NotLoggedInException, NotEnoughItemsAvailableException {
        String result1;
        try {
            result1 = nc.checkIn("Patient1", "Doktoor", null);
        } catch (ArgumentIsNullException e) {
        }
    }
}
