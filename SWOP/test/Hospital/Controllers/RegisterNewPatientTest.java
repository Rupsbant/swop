package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegisterNewPatientTest {

    WorldController wc;
    NurseController nc;
    PatientController pc;

    public RegisterNewPatientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        nc = (NurseController) wc.login(wc.getCampuses().get(0),new LoginInfo("Nurse Joy", "Nurse"));
        pc = new PatientController(wc, nc);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void HappyTest() throws ArgumentConstraintException, NotLoggedInException, NotAFactoryException, SchedulableAlreadyExistsException, WrongArgumentListException, ArgumentNotAnsweredException, ArgumentIsNullException, CannotChangeException, InvalidArgumentException {
        ArgumentList args = pc.getFactoryArguments(pc.getAvailablePatientFactories()[0]);
        args.getPublicArguments()[0].enterAnswer("Patrick Ient");

        pc.registerPatient(pc.getAvailablePatientFactories()[0], args);
        assertTrue(wc.getPatients().contains("Patrick Ient"));
    }

    @Test(expected = NotLoggedInException.class)
    public void LoggedInTest() throws NotLoggedInException {
        nc.logout();
        try {
            ArgumentList args = pc.getFactoryArguments(pc.getAvailablePatientFactories()[0]);
        } catch (NotAFactoryException nafe) {
            fail("Not a factory!");
        }
    }

    @Test(expected = SchedulableAlreadyExistsException.class)
    public void DoublePatientTest() throws SchedulableAlreadyExistsException, ArgumentConstraintException, WrongArgumentListException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        regPat("Patrick Ient");
        regPat("Patrick Ient");
    }

    @Test(expected = ArgumentConstraintException.class)
    public void EmptyNameTest() throws SchedulableAlreadyExistsException, ArgumentConstraintException, WrongArgumentListException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        regPat("");
    }

    private void regPat(String name) throws SchedulableAlreadyExistsException, ArgumentConstraintException, WrongArgumentListException, NotLoggedInException, NotAFactoryException, ArgumentNotAnsweredException, ArgumentIsNullException, InvalidArgumentException {
        ArgumentList args = null;
        try {
            args = pc.getFactoryArguments(pc.getAvailablePatientFactories()[0]);
        } catch (NotLoggedInException nlie) {
            fail("Should have been logged in.");
        } catch (NotAFactoryException nafe) {
            fail("Not a factory!");
        }
        try {
            args.getPublicArguments()[0].enterAnswer(name);
        } catch (CannotChangeException cce) {
            fail("Argument couldn't be changed.");
        }
        pc.registerPatient(pc.getAvailablePatientFactories()[0], args);
    }
    //TODO: not enough food test
}
