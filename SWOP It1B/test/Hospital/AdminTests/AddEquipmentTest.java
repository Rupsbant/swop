package Hospital.AdminTests;

import Hospital.Argument.CampusInfoArgument;
import Hospital.Argument.Argument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Argument.WorldArgument;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.MachineController;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import Hospital.World.World;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddEquipmentTest {

    World w;
    WorldController wc;
    AdministratorController ac;
    MachineController mc;

    public AddEquipmentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

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

    @After
    public void tearDown() {
    }

    @Test
    public void machineClassCast() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            Argument[] args = new Argument[4];
            args[0] = new IntegerArgument("kaput");
            String machine = mc.makeMachine("New XRayMachine", new ArgumentList(args));
            fail("An exception should not have been thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Argument is of the wrong type, it should be StringArgument", e.getMessage());
        }
    }

    @Test
    public void machineWrongLength() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, ArgumentNotAnsweredException, CannotChangeException {
        try {
            PublicArgument[] args = new PublicArgument[0];
            String machine = mc.makeMachine("New XRayMachine", new ArgumentList(args));
            fail("An exception should not have been thrown");
        } catch (WrongArgumentListException e) {
            assertEquals("Length of list is wrong", e.getMessage());
        }
    }

    @Test
    public void machineNotAnswered() throws NotAFactoryException, NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            ArgumentList args = mc.getMachineArguments("New XRayMachine");
            String machine = mc.makeMachine("New XRayMachine", args);
            fail("An exception should not have been thrown");
        } catch (ArgumentNotAnsweredException e) {
            assertEquals(ArgumentNotAnsweredException.class, e.getClass());
        }
    }

    @Test
    public void machineNullPublicArgument() throws NotAFactoryException, NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            String staff = mc.makeMachine("New XRayMachine", null);
            fail("An exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals("ArgumentList was null", e.getMessage());
        }
    }

    @Test
    public void machineWrongFactory() throws NotLoggedInException, WrongArgumentListException, SchedulableAlreadyExistsException, InvalidArgumentException, CannotChangeException {
        try {
            ArgumentList args = mc.getMachineArguments("XRayMachine");
            args.getPublicArguments()[0].enterAnswer("testdoctor");
            String staff = mc.makeMachine("wrong factory", args);
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
    public void machineAlreadyExists() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, IllegalArgumentException, CannotChangeException, WrongArgumentListException {
        Argument[] args = new Argument[4];
        args[0] = new StringArgument("id");
        args[1] = new StringArgument("location");
        args[2] = new CampusInfoArgument("campus").setWorld(w);
        args[3] = new WorldArgument().setWorld(w);
        ((PublicArgument) args[0]).enterAnswer("Testen van ID");
        ((PublicArgument) args[1]).enterAnswer("SOL Z");
        ((PublicArgument) args[2]).enterAnswer("1");
        String machine = mc.makeMachine("New XRayMachine", new ArgumentList(args));
        args = new Argument[4];
        args[0] = new StringArgument("id");
        args[1] = new StringArgument("location");
        args[2] = new CampusInfoArgument("campus").setWorld(w);
        args[3] = new WorldArgument().setWorld(w);
        ((PublicArgument) args[0]).enterAnswer("Testen van ID");
        ((PublicArgument) args[1]).enterAnswer("SOL K");
        ((PublicArgument) args[2]).enterAnswer("2");
        String machine2 = mc.makeMachine("New XRayMachine", new ArgumentList(args));
        fail("should throw exception");
    }

    @Test(expected = SchedulableAlreadyExistsException.class)
    public void machineAlreadyExists2() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, IllegalArgumentException, CannotChangeException, WrongArgumentListException {
        Argument[] args = new Argument[4];
        args[0] = new StringArgument("id");
        args[1] = new StringArgument("location");
        args[2] = new CampusInfoArgument("campus").setWorld(w);
        args[3] = new WorldArgument().setWorld(w);
        ((PublicArgument) args[0]).enterAnswer("Testen van ID");
        ((PublicArgument) args[1]).enterAnswer("SOL Z");
        ((PublicArgument) args[2]).enterAnswer("1");
        String machine = mc.makeMachine("New SurgicalEquipment", new ArgumentList(args));
        args = new Argument[4];
        args[0] = new StringArgument("id");
        args[1] = new StringArgument("location");
        args[2] = new CampusInfoArgument("campus").setWorld(w);
        args[3] = new WorldArgument().setWorld(w);
        ((PublicArgument) args[0]).enterAnswer("Testen van ID");
        ((PublicArgument) args[1]).enterAnswer("SOL Z");
        ((PublicArgument) args[2]).enterAnswer("1");
        String machine2 = mc.makeMachine("New BloodAnalyzer", new ArgumentList(args));
        fail("should throw exception");
    }

    @Test(expected = SchedulableAlreadyExistsException.class)
    public void machineAlreadyExists3() throws NotAFactoryException, NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException, IllegalArgumentException, CannotChangeException, WrongArgumentListException {
        Argument[] args = new Argument[4];
        args[0] = new StringArgument("id");
        args[1] = new StringArgument("location");
        args[2] = new CampusInfoArgument("campus").setWorld(w);
        args[3] = new WorldArgument().setWorld(w);
        ((PublicArgument) args[0]).enterAnswer("Testen van ID");
        ((PublicArgument) args[1]).enterAnswer("SOL Z");
        ((PublicArgument) args[2]).enterAnswer("1");
        String machine = mc.makeMachine("New UltraSoundMachine", new ArgumentList(args));
        args = new Argument[4];
        args[0] = new StringArgument("id");
        args[1] = new StringArgument("location");
        args[2] = new CampusInfoArgument("campus").setWorld(w);
        args[3] = new WorldArgument().setWorld(w);
        ((PublicArgument) args[0]).enterAnswer("Testen van ID");
        ((PublicArgument) args[1]).enterAnswer("SOL Z");
        ((PublicArgument) args[2]).enterAnswer("1");
        String machine2 = mc.makeMachine("New BloodAnalyzer", new ArgumentList(args));
        fail("should throw exception");
    }
}
