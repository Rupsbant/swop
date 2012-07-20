package Hospital.ScenarioTest;

import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.InvalidArgumentException;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Argument.PublicArgument;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import Hospital.World.BasicWorld;

public class AddStaffTest {

    WorldController w;

    @Before
    public void setUp() {
        w = BasicWorld.getBasicWorld();
    }

    @Test
    public void test() throws InvalidArgumentException, CannotChangeException {
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
            adminController = (AdministratorController) w.login(w.getCampuses().get(0), login);
            staffController = new StaffController(w, adminController);
        } catch (NoPersonWithNameAndRoleException e) {
            fail("should not happen correct login chosen");
        } catch (ArgumentIsNullException e) {
            fail("argument can't be null");
        }
        addDoctor(staffController);
        addSameDoctor(staffController);
        addNurse(staffController);
        addWareHouseManager(staffController);
    }

    private void addSameDoctor(StaffController staffController) throws InvalidArgumentException, CannotChangeException {
        if (staffController == null) {
            fail("staffController is null. There is something wrong in the test.");
        }
        ArgumentList args = null;
        try {
            args = staffController.getStaffArguments("New Doctor");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("The factory \"New Doctor\" should exist.");
        }
        assertEquals(args.getPublicArguments().length, 1);
        //TODO check need for asserts like this one.
        try {
            args.getPublicArguments()[0].enterAnswer("TestDoctor");
        } catch (IllegalArgumentException e) {
            fail("TestDoctor is a valid string.");
        } catch (CannotChangeException e) {
            fail("These arguments are changeble");
        }
        String staff = null;
        try {
            staff = staffController.makeStaffMember("New Doctor", args);
            fail("There is already a doctor with name TestDoctor. An exception shoud be thrown.");
        } catch (NotAFactoryException e) {
            fail("The factory \"New Doctor\" should exist.");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (ArgumentNotAnsweredException e) {
            fail("All arguments are answered. Should not happen.");
        } catch (WrongArgumentListException e) {
            fail("The correct argument list is used.(from getStaffArguments(\"New Doctor\").");
        } catch (SchedulableAlreadyExistsException e) {
            System.out.println("New doctor (TestDoctor) is not created.");
        } catch (ArgumentIsNullException e) {
            fail("Should not happen non of the arguments is null.");
        } catch (ArgumentConstraintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void addDoctor(StaffController staffController) throws InvalidArgumentException, CannotChangeException {
        if (staffController == null) {
            fail("staffController is null. There is something wrong in the test.");
        }
        PublicArgument[] args = null;
        ArgumentList argL = null;
        try {
            argL = staffController.getStaffArguments("New Doctor");
            args = argL.getPublicArguments();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("The factory \"New Doctor\" should exist.");
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
        String staff = null;
        try {
            staff = staffController.makeStaffMember("New Doctor", argL);
        } catch (NotAFactoryException e) {
            fail("The factory \"New Doctor\" should exist.");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (ArgumentNotAnsweredException e) {
            fail("All arguments are answered. Should not happen.");
        } catch (WrongArgumentListException e) {
            fail("The correct argument list is used.(from getStaffArguments(\"New Doctor\").");
        } catch (SchedulableAlreadyExistsException e) {
            fail("BasicWorld shoud not have a doctor named TestDoctor.");
        } catch (ArgumentIsNullException e) {
            fail("Should not happen non of the arguments is null.");
        } catch (ArgumentConstraintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("TestDoctor", staff);
        System.out.println("New doctor created:" + staff);
    }

    public void addNurse(StaffController staffController) throws InvalidArgumentException, CannotChangeException {
        PublicArgument[] args = null;
        ArgumentList argL = null;
        try {
            argL = staffController.getStaffArguments("New Nurse");
            args = argL.getPublicArguments();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("The factory \"New Nurse\" should exist.");
        }
        assertEquals(args.length, 2);
        //TODO check need for asserts like this one.
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
            fail("The factory \"New Nurse\" should exist.");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (ArgumentNotAnsweredException e) {
            fail("all arguments are answered.");
        } catch (WrongArgumentListException e) {
            fail("The correct argument list is used.(from getStaffArguments(\"New Nurse\").");
        } catch (SchedulableAlreadyExistsException e) {
            fail("BasicWorld shoud not have a nurse named TestNurse.");
        } catch (ArgumentIsNullException e) {
            fail("Should not happen non of the arguments is null.");
        } catch (ArgumentConstraintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("TestNurse", staff);
        System.out.println("New Nurse created: " + staff);
    }

    public void addWareHouseManager(StaffController staffController) throws InvalidArgumentException, CannotChangeException {
        PublicArgument[] args = null;
        ArgumentList argL = null;
        try {
            argL = staffController.getStaffArguments("New WarehouseManager");
            args = argL.getPublicArguments();
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (NotAFactoryException e) {
            fail("The factory \"New WarehouseManager\" should exist.");
        }
        assertEquals(args.length, 2);
        //TODO check need for asserts like this one.
        try {
            args[0].enterAnswer("TestManager");
            args[1].enterAnswer("2");
        } catch (IllegalArgumentException e) {
            fail("TestManager is a valid string.");
        } catch (CannotChangeException e) {
            fail("These arguments are changeble");
        }
        String staff = "";
        try {
            staff = staffController.makeStaffMember("New WarehouseManager", argL);
        } catch (NotAFactoryException e) {
            fail("The factory \"New WarehouseManager\" should exist.");
        } catch (NotLoggedInException e) {
            fail("The hospital administrator should be logged in.");
        } catch (ArgumentNotAnsweredException e) {
            fail("all arguments are answered.");
        } catch (WrongArgumentListException e) {
            fail("The correct argument list is used.(from getStaffArguments(\"New WarehouseManager\").");
        } catch (SchedulableAlreadyExistsException e) {
            fail("BasicWorld shoud not have a warehouse manager named TestManager.");
        } catch (ArgumentIsNullException e) {
            fail("Should not happen non of the arguments is null.");
        } catch (ArgumentConstraintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("TestManager", staff);
        System.out.println("New Warehouse Manager created: " + staff);
    }
}
