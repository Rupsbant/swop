package Hospital.AdminTests;

import Hospital.Argument.CampusInfoArgument;
import Hospital.Controllers.ArgumentList;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Argument.StringArgument;
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
import Hospital.People.HospitalAdministrator;
import Hospital.People.LoginInfo;
import Hospital.People.Nurse;
import Hospital.People.StaffRole;
import Hospital.World.CampusInfo;
import Hospital.World.World;

public class AddStaffMemberTest {

    private World w;
    private WorldController wc;
    private AdministratorController ac;
    private StaffController staffController;

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        w = TestUtil.getWorldForTesting();
        wc = new WorldController(w);
        List<LoginInfo> logins = wc.getLogins();
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getRole().equals(StaffRole.HospitalAdministrator)) {
                ac = (AdministratorController) wc.login(wc.getCampuses().get(0), logins.get(i));
            }
        }
        staffController = new StaffController(wc, ac);
    }

    @Test
    public void addDoctor() throws NotLoggedInException, NotAFactoryException, InvalidArgumentException, WrongArgumentListException, SchedulableAlreadyExistsException, NoPersonWithNameAndRoleException, CannotChangeException {
        String staff = staffController.makeStaffMember(StaffRole.Doctor, "TestDoctor", null);
        assertEquals("Created new: TestDoctor", staff);
        Doctor d = w.getPersonByName(Doctor.class, "TestDoctor");
    }

    @Test
    public void addNurse() throws NoPersonWithNameAndRoleException, NotAFactoryException, NotLoggedInException, InvalidArgumentException, WrongArgumentListException, SchedulableAlreadyExistsException, CannotChangeException {
        CampusInfo campus = wc.getCampuses().get(1);
        String staff = staffController.makeStaffMember(StaffRole.Nurse, "testnurse", campus);
        assertEquals("Created new: testnurse", staff);
        Nurse d = w.getPersonByName(Nurse.class, "testnurse");
    }

    @Test
    public void addDoctorNullPublicArgument() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, WrongArgumentListException, CannotChangeException {
        try {
            String staff = staffController.makeStaffMember(StaffRole.Doctor, null, null);
            fail("An exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("Name is null", e.getMessage());
        }
    }

    @Test
    public void addDoctorWrongFactoryArgument() throws NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            String staff = staffController.makeStaffMember(StaffRole.HospitalAdministrator, null, null);
            fail("An exception should not have been thrown");
        } catch (ArgumentConstraintException e) {
            assertEquals("This Role cannot be created", e.getMessage());
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
