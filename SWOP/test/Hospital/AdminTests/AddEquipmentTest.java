package Hospital.AdminTests;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.MachineController;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Machine.Location;
import Hospital.People.LoginInfo;
import Hospital.World.World;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddEquipmentTest {

    private World w;
    private WorldController wc;
    private AdministratorController ac;
    private MachineController mc;

    @Before
    public void setUp() throws InvalidArgumentException, NoPersonWithNameAndRoleException {
        w = TestUtil.getWorldForTesting();
        wc = new WorldController(w);
        List<LoginInfo> logins = wc.getLogins();
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getRole().equals("HospitalAdministrator")) {
                ac = (AdministratorController) wc.login(wc.getCampuses().get(0), logins.get(i));
            }
        }
        mc = new MachineController(wc, ac);
        w.addCampus("campus test");
    }

    @Test
    public void machineWrongFactory() throws NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            String staff = mc.makeMachine("wrong factory", null, null, null);
            fail("An exception should not have been thrown");
        } catch (NotAFactoryException e) {
        }
    }

    @Test
    public void machineNullStaffArgument() throws NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            StaffController staffController = new StaffController(wc, ac);
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
            new MachineController(null, ac);
        } catch (ArgumentIsNullException e) {
            assertEquals("wc should not be null.", e.getMessage());
        }
    }

    @Test
    public void staffControllerConstructorNullloginArgument() {
        try {
            new MachineController(wc, null);
        } catch (ArgumentIsNullException e) {
            assertEquals("ac should not be null.", e.getMessage());
        }
    }

    @Test(expected = SchedulableAlreadyExistsException.class)
    public void machineAlreadyExists() 
            throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, 
            InvalidArgumentException, IllegalArgumentException, CannotChangeException, WrongArgumentListException {
        mc.makeMachine("New XRayMachine", "ID 1", new Location("Location 1"), null);
        mc.makeMachine("New SurgicalEquipment", "ID 1", new Location("Location 1"), null);
        fail("should throw exception");
    }
}
