package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;

import Hospital.People.StaffRole;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegisterNewPatientTest {

    private WorldController wc;
    private NurseController nc;
    private PatientController pc;

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException, NotLoggedInException, InvalidArgumentException, SchedulableAlreadyExistsException {
        wc = TestUtil.getWorldControllerForTesting();
        nc = (NurseController) wc.login(wc.getCampuses().get(0), new LoginInfo("Nurse Joy", StaffRole.Nurse));
        pc = new PatientController(wc, nc);
        pc.registerPatient("Patrick Ient");
    }

    @Test
    public void HappyTest() {
        assertTrue(wc.getPatients().contains("Patrick Ient"));
    }

    @Test(expected = NotLoggedInException.class)
    public void LoggedInTest() throws NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException {
        nc.logout();
        pc.registerPatient("name");
    }

    @Test(expected = SchedulableAlreadyExistsException.class)
    public void DoublePatientTest() throws SchedulableAlreadyExistsException, ArgumentConstraintException, WrongArgumentListException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        pc.registerPatient("Patrick Ient");
    }

    @Test(expected = ArgumentConstraintException.class)
    public void EmptyNameTest() throws SchedulableAlreadyExistsException, ArgumentConstraintException, WrongArgumentListException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        pc.registerPatient("");
    }

    //TODO: not enough food test
}
