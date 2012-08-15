package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import java.util.List;

import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;
import Hospital.World.CampusInfo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupsbant
 */
public class LoginTest {

    private WorldController wc;

    @Before
    public void setUp() throws ArgumentIsNullException {
        wc = TestUtil.getWorldControllerForTesting();
    }

    public LoginInfo adminFoundTest() {
        List<LoginInfo> l = wc.getLogins();
        assertTrue("No persons in login", !l.isEmpty());
        LoginInfo admin = null;
        for (LoginInfo loginInfo : l) {
            if (loginInfo.getRole().equals(StaffRole.HospitalAdministrator)) {
                admin = loginInfo;
            }
        }
        assertNotNull("HospitalAdministrator not available", admin);
        assertEquals(admin.getRole(), StaffRole.HospitalAdministrator);
        return admin;
    }

    @Test
    public void loginTest() {
        LoginController adminLogin;
        try {
            adminLogin = wc.login(wc.getCampuses().get(0), adminFoundTest());
            assertEquals(adminLogin.getRole(), StaffRole.HospitalAdministrator);
            assertEquals(wc.getCampuses().get(0), adminLogin.getCampusController().getCampus().getCampusInfo());
            assertTrue("LoginFlag doesn't work", adminLogin.loggedIn());
            adminLogin.checkLoggedIn();
        } catch (Exception e) {
            fail("This exception should not have been thrown");
        }
    }

    @Test
    public void logoutTest() {
        LoginController adminLogin;
        try {
            adminLogin = wc.login(wc.getCampuses().get(0), adminFoundTest());
            adminLogin.logout();
            try {
                adminLogin.checkLoggedIn();
                fail("This exception should have been thrown");
            } catch (NotLoggedInException ex) {
            }
            assertFalse("This flag should be false", adminLogin.loggedIn());
        } catch (Exception e) {
            fail("This exception should not have been thrown");
        }
    }

    @Test
    public void loginTestNullArgument() {
        LoginController adminLogin;
        try {
            adminLogin = wc.login(wc.getCampuses().get(0), null);
            fail("The login methode should throw an exception");
        } catch (NoPersonWithNameAndRoleException e) {
            fail("This exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals(ArgumentIsNullException.class, e.getClass());
            assertEquals("Person is null.", e.getMessage());
        } catch (ArgumentConstraintException e) {
            fail("it should not have failed on the campus, but on the user.");
        }
    }

    @Test
    public void loginTestNullCampus() {
        LoginController adminLogin;
        try {
            adminLogin = wc.login(null, adminFoundTest());
            fail("The login methode should throw an exception");
        } catch (NoPersonWithNameAndRoleException e) {
            fail("This exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            fail("Person should not be null");
        } catch (ArgumentConstraintException e) {
            ;
        }
    }

    @Test
    public void loginTestInvalidCampus() {
        LoginController adminLogin;
        try {
            adminLogin = wc.login(new CampusInfo("Valhalla"), adminFoundTest());
            fail("The login methode should throw an exception");
        } catch (NoPersonWithNameAndRoleException e) {
            fail("This exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            fail("Person should not be null");
        } catch (ArgumentConstraintException e) {
            ;
        }
    }

    @Test
    public void loginTestWrongNameArgument() {
        LoginController adminLogin;
        try {
            LoginInfo info = new LoginInfo("Test", adminFoundTest().getRole());
            adminLogin = wc.login(wc.getCampuses().get(0), info);
            fail("The login methode should throw an exception");
        } catch (NoPersonWithNameAndRoleException e) {
            assertEquals(NoPersonWithNameAndRoleException.class, e.getClass());
        } catch (ArgumentIsNullException e) {
            fail("This exception should not have been thrown");
        } catch (ArgumentConstraintException e) {
            fail("it should not have failed on the campus, but on the user.");
        }
    }

    @Test
    public void loginTestWrongRoleArgument() {
        LoginController adminLogin;
        LoginInfo info;
        try {
            info = new LoginInfo(adminFoundTest().getName(), StaffRole.WarehouseManager);
            adminLogin = wc.login(wc.getCampuses().get(0), info);
            fail("The login methode should throw an exception");
        } catch (ArgumentIsNullException e1) {
            fail("This exception should not have been thrown");
        } catch (NoPersonWithNameAndRoleException e) {
            assertEquals(NoPersonWithNameAndRoleException.class, e.getClass());
        } catch (ArgumentConstraintException e) {
            fail("it should not have failed on the campus, but on the user.");
        }
    }

    @Test
    public void loginInfoConstructorTest() {
        try {
            new LoginInfo("sdlmqfj", null);
            fail("This exception should have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals(ArgumentIsNullException.class, e.getClass());
            assertEquals("Empty role", e.getMessage());
        }
        try {
            new LoginInfo(null, StaffRole.Doctor);
            fail("This exception should have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals(ArgumentIsNullException.class, e.getClass());
            assertEquals("Empty name", e.getMessage());
        }

    }
}