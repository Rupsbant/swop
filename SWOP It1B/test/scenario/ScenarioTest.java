package scenario;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Argument.PublicArgument;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import Hospital.World.BasicWorld;

public class ScenarioTest {

    WorldController w;

    @Before
    public void setUp() {
        w = BasicWorld.getBasicWorld();
    }

    @Test
    public void test() throws CannotChangeException {
        List<LoginInfo> logins = w.getLogins();
        LoginInfo login = null;
        for (LoginInfo loginInfo : logins) {
            if (loginInfo.getRole().equals("HospitalAdministrator")) {
                login = loginInfo;
                break;
            }
        }
        if (login == null) {
            fail("There is no hospital administrator");
        }
        AdministratorController adminController;
        StaffController staffController = null;
        try {
            adminController = (AdministratorController) w.login(w.getCampuses().get(0),login);
            staffController = new StaffController(w, adminController);
        } catch (NoPersonWithNameAndRoleException e) {
            fail("should not happen correct login chosen");
        } catch (ArgumentIsNullException e) {
            fail("argument can't be null");
        } catch (ArgumentConstraintException e) {
			fail("should not happen, there is at least one campus in the world");
		}
        addDoctor(staffController);
        addNurse(staffController);
        addWareHouseManager(staffController);
    }

    public void addDoctor(StaffController staffController) throws CannotChangeException {
        PublicArgument[] args = null;
        ArgumentList argL = null;
        try {
            argL = staffController.getStaffArguments("New Doctor");
            args = argL.getPublicArguments();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("");//TODO find reason
        }
        assertEquals(args.length, 1);
        //TODO check need for asserts like this one.
        try {
            args[0].enterAnswer("TestDoctor");
        } catch (IllegalArgumentException e) {
            fail("TestDoctor is a valid string.");
        } catch (CannotChangeException e) {
            fail("These arguments are changeble");
        }
        String staff = "";
        try {
            staff = staffController.makeStaffMember("New Doctor", argL);
        } catch (InvalidArgumentException ex) {
            fail();
        } catch (NotAFactoryException e) {
            fail();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (SchedulableAlreadyExistsException e) {
            fail("Schedulable Already exists");
        }
        assertEquals("TestDoctor", staff);
        System.out.println("New doctor created: " + staff);
    }

    public void addNurse(StaffController staffController) throws CannotChangeException {
        PublicArgument[] args = null;
        ArgumentList argL = null;
        try {
            argL = staffController.getStaffArguments("New Nurse");
            args = argL.getPublicArguments();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("New Nurse was a valid alias");
        }
        assertEquals(args.length, 2);
        try {
            args[0].enterAnswer("TestNurse");
            args[1].enterAnswer("1");
        } catch (IllegalArgumentException e) {
            fail("TestNurse is a valid string.");
        } catch (CannotChangeException e) {
            fail("These arguments are changeble");
        }
        String staff = "";
        try {
            staff = staffController.makeStaffMember("New Nurse", argL);
        } catch (NotAFactoryException e) {
            fail("New Nurse should be a valid alias");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (InvalidArgumentException e) {
            fail("all arguments are answered.");
        } catch (SchedulableAlreadyExistsException e) {
            fail("Nurse does not exist yet");
        }
        assertEquals("TestNurse", staff);
        System.out.println("New Nurse created: " + staff);
    }

    public void addWareHouseManager(StaffController staffController) throws CannotChangeException {
        PublicArgument[] args = null;
        ArgumentList argL = null;
        try {
            argL = staffController.getStaffArguments("New WarehouseManager");
            args = argL.getPublicArguments();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("New WarehouseManager should be a valid alias");
        }
        assertEquals(args.length, 2);
        //TODO check need for asserts like this one.
        try {
            args[0].enterAnswer("TestManager");
            args[1].enterAnswer("1");
        } catch (IllegalArgumentException e) {
            fail("TestManager is a valid string.");
        } catch (CannotChangeException e) {
            fail("These arguments are changeble");
        }
        String staff = "";
        try {
            staff = staffController.makeStaffMember("New WarehouseManager", argL);
        } catch (NotAFactoryException e) {
            fail("New WarehouseManager should be a valid alias");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (InvalidArgumentException e) {
            fail("all arguments are answered.");
        } catch (SchedulableAlreadyExistsException e) {
            fail("Manager does not exist yet");
        }
        assertEquals("TestManager", staff);
        System.out.println("New Warehouse Manager created: " + staff);
    }
}
