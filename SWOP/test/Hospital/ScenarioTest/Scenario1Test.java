package Hospital.ScenarioTest;

import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Controllers.LoginController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.PatientController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.People.LoginInfo;
import Hospital.World.BasicWorld;

public class Scenario1Test {

    private WorldController w;

    @Before
    public void setUp() {
        w = BasicWorld.getBasicWorld();
    }

    @Test
    public void test() throws InvalidArgumentException {
        //login "Nurse Joy"
        NurseController nurseController = (NurseController) login("Nurse Joy");
        //create new patient
        String patient = registerNewPatient(nurseController);
        //check in patient with doctor Gregory House
        checkInPatient(patient, nurseController, "Gregory House");
        //logout "Nurse Joy"
        logout();
    }

    private void logout() {
    }

    private void checkInPatient(String patient, NurseController nurseController, String doctor) {
        try {
            String app = nurseController.checkIn(patient, doctor, w);
            System.out.println("The patient is checked in an an appointment is made. " + app);
        } catch (NotEnoughItemsAvailableException ex) {
            fail("Doctor " + doctor + " should be in basic world");
        } catch (NoPersonWithNameAndRoleException e) {
            fail("Doctor " + doctor + " should be in basic world");
        } catch (ArgumentIsNullException e) {
            fail("The arguments should not be null");
        } catch (ArgumentConstraintException e) {
            fail("");//TODO why?
        } catch (SchedulingException e) {
            fail("The patient should be able to schedule an appointment with the doctor.");
        } catch (PatientIsCheckedInException e) {
            fail("The patient should not be checked in.");
        } catch (NotLoggedInException e) {
            fail("The nurse should be logged in.");
        }
    }

    private String registerNewPatient(NurseController nurseController) throws InvalidArgumentException {
        PatientController patientController = null;
        try {
            patientController = new PatientController(w, nurseController);
        } catch (ArgumentIsNullException e) {
            fail("The arguments should not be null. Check this test.");
        }
        String patient = null;
        try {
            patient = patientController.registerPatient("TestPatient1");
            System.out.println("New patient added to the system. The patients name is " + patient + ".");
        } catch (NotLoggedInException e) {
            fail("The nurse should be logged in. This should not happen.");
        } catch (SchedulableAlreadyExistsException e) {
            fail("Basicworld should not contain a patient with name TestPatient1.");
        } catch (InvalidArgumentException e) {
            fail("The argumentList should be correct. Got from getFactoryArguments.");
        }
        return patient;
    }

    private LoginController login(String name) {
        List<LoginInfo> logins = w.getLogins();
        LoginInfo login = null;
        for (LoginInfo loginInfo : logins) {
            if (loginInfo.getName().equals(name)) {
                System.out.println("Person found:" + loginInfo);
                login = loginInfo;
                break;
            }
        }
        if (login == null) {
            fail("There is person with " + name + " as name.");
        }
        LoginController loginController = null;
        try {
            loginController = w.login(w.getCampuses().get(0), login);
            System.out.println("Login of person succesfull.");
        } catch (NoPersonWithNameAndRoleException e) {
            fail("should not happen correct login chosen");
        } catch (ArgumentIsNullException e) {
            fail("argument can't be null");
        } catch (ArgumentConstraintException e) {
            fail("should not happen, there is a campus in the world");
        }
        if (loginController == null) {
            fail("Should not be null if login was succesfull.");
        }
        return loginController;
    }
}
