package Hospital.AdminTests;

import Hospital.Argument.CampusInfoArgument;
import Hospital.Controllers.ArgumentList;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Argument.StringArgument;
import Hospital.Argument.WorldArgument;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;
import Hospital.People.Nurse;
import Hospital.World.World;

public class AddStaffMemberTest {

    World w;
    WorldController wc;
    AdministratorController ac;
    StaffController staffController;

    public AddStaffMemberTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        w = TestUtil.getWorldForTesting();
        wc = new WorldController(w);
        List<LoginInfo> logins = wc.getLogins();
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getRole().equals("HospitalAdministrator")) {
                ac = (AdministratorController) wc.login(wc.getCampuses().get(0),logins.get(i));
            }
        }
        staffController = new StaffController(wc, ac);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addDoctor() throws NotLoggedInException, NotAFactoryException, InvalidArgumentException, WrongArgumentListException, SchedulableAlreadyExistsException, NoPersonWithNameAndRoleException, CannotChangeException {
        ArgumentList args = staffController.getStaffArguments("New Doctor");
        args.getPublicArguments()[0].enterAnswer("TestDoctor");
        String staff = staffController.makeStaffMember("New Doctor", args);
        assertEquals("TestDoctor", staff);
        Doctor d = w.getPersonByName(Doctor.class, "TestDoctor");
    }

    @Test
    public void addNurse() throws NoPersonWithNameAndRoleException, NotAFactoryException, NotLoggedInException, InvalidArgumentException, WrongArgumentListException, SchedulableAlreadyExistsException, CannotChangeException {
        ArgumentList args = staffController.getStaffArguments("New Nurse");
        args.getPublicArguments()[0].enterAnswer("testnurse");
        args.getPublicArguments()[1].enterAnswer("1");
        System.out.println(args.getPublicArguments()[1].getAnswer());
        String staff = staffController.makeStaffMember("New Nurse", args);
        assertEquals("testnurse", staff);
        Nurse d = w.getPersonByName(Nurse.class, "testnurse");
    }

    @Test
    public void getNurseArguments() throws ArgumentIsNullException, NotLoggedInException, NotAFactoryException {
        ArgumentList args = staffController.getStaffArguments("New Nurse");
        assertEquals(2, args.getPublicArguments().length);
        assertEquals("Enter the name of the nurse: ", args.getPublicArguments()[0].getQuestion());
        assertEquals("Enter the number of the campus\n1: North\n2: South\n", args.getPublicArguments()[1].getQuestion());
        assertEquals(StringArgument.class, args.getPublicArguments()[0].getClass());
        assertEquals(CampusInfoArgument.class, args.getPublicArguments()[1].getClass());
    }

    @Test
    public void getDoctorArguments() throws NotLoggedInException, NotAFactoryException, ArgumentIsNullException {
        ArgumentList args = staffController.getStaffArguments("New Doctor");
        assertEquals(1, args.getPublicArguments().length);
        assertEquals("Enter the name of the doctor: ",
                args.getPublicArguments()[0].getQuestion());
        assertEquals(StringArgument.class, args.getPublicArguments()[0].getClass());
    }

    @Test
    public void addDoctorWrongPublicArgument() throws NotAFactoryException, NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            ArgumentList args = staffController.getStaffArguments("New Doctor");
            String staff = staffController.makeStaffMember("New Doctor", args);
            fail("An exception should not have been thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals(ArgumentNotAnsweredException.class, e.getClass());
        }
    }

    @Test
    public void addDoctorNullPublicArgument() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, WrongArgumentListException, CannotChangeException {
        try {
            String staff = staffController.makeStaffMember("New Doctor", null);
            fail("An exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("ArgumentList was null", e.getMessage());
        }
    }

    @Test
    public void addDoctorWrongFactoryArgument() throws NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            ArgumentList args = staffController.getStaffArguments("New Doctor");
            args.getPublicArguments()[0].enterAnswer("testdoctor");
            String staff = staffController.makeStaffMember("test", args);
            fail("An exception should not have been thrown");
        } catch (NotAFactoryException e) {
        }
    }

    @Test
    public void addDoctorNullStaffArgument() throws NotLoggedInException, InvalidArgumentException, WrongArgumentListException, SchedulableAlreadyExistsException, CannotChangeException {

        try {
            ArgumentList args = staffController.getStaffArguments("New Doctor");
            args.getPublicArguments()[0].enterAnswer("testdoctor");
            String staff = staffController.makeStaffMember(null, args);
            fail("An exception should not have been thrown");
        } catch (NotAFactoryException e) {
        }
    }

    @Test
    public void staffControllerConstructorNullWorldArgument() {
        try {
            new StaffController(null, ac);
        } catch (ArgumentIsNullException e) {
            assertEquals("wc should not be null.", e.getMessage());
        }
    }

    @Test
    public void staffControllerConstructorNullloginArgument() {
        try {
            new StaffController(wc, null);
        } catch (ArgumentIsNullException e) {
            assertEquals("ac should not be null.", e.getMessage());
        }
    }
}
