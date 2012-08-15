package Hospital.ScenarioTest;

import Hospital.Exception.Arguments.InvalidArgumentException;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;
import Hospital.World.BasicWorld;
import Hospital.World.CampusInfo;

public class AddStaffTest {

    private WorldController wc;
    private StaffController staffController;
    private AdministratorController adminController;

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException, InvalidArgumentException, CannotChangeException, NotLoggedInException, SchedulableAlreadyExistsException {
        wc = BasicWorld.getBasicWorld();
        List<LoginInfo> logins = wc.getLogins();
        LoginInfo login = null;
        for (LoginInfo loginInfo : logins) {
            if (loginInfo.getRole().equals(StaffRole.HospitalAdministrator)) {
                login = loginInfo;
                break;
            }
        }
        adminController = (AdministratorController) wc.login(wc.getCampuses().get(0), login);
        staffController = new StaffController(wc, adminController);
        addDoctor();
    }

    private void addDoctor() throws InvalidArgumentException, CannotChangeException, NotLoggedInException, SchedulableAlreadyExistsException {
        String staff = null;
        staff = staffController.makeStaffMember(StaffRole.Doctor, "TestDoctor", null);
        assertEquals("Created new: TestDoctor", staff);
    }

    @Test(expected = SchedulableAlreadyExistsException.class)
    public void addSameDoctor() throws InvalidArgumentException, CannotChangeException, NotLoggedInException, SchedulableAlreadyExistsException {
        String staff = staffController.makeStaffMember(StaffRole.Doctor, "TestDoctor", null);
        fail("There is already a doctor with name TestDoctor. An exception shoud be thrown.");
    }

    public void addNurse(StaffController staffController) throws InvalidArgumentException, CannotChangeException, NotLoggedInException, SchedulableAlreadyExistsException {
        CampusInfo campus = wc.getCampuses().get(0);
        String staff = staffController.makeStaffMember(StaffRole.Nurse, "TestNurseABC", campus);
    }

    public void addWareHouseManager(StaffController staffController) throws InvalidArgumentException, CannotChangeException, NotLoggedInException, SchedulableAlreadyExistsException {
        CampusInfo campus = wc.getCampuses().get(1);
        String staff = staffController.makeStaffMember(StaffRole.WarehouseManager, "TestManager", campus);
    }
}
