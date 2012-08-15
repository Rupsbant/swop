package Hospital.Controllers;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;

public class NurseControllerTest {

    private WorldController wc;
    private NurseController nc;

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        List<LoginInfo> logins = wc.getLogins();
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getRole().equals(StaffRole.Nurse)) {
                nc = (NurseController) wc.login(wc.getCampuses().get(0), logins.get(i));
            }
        }
    }

    @Test
    public void getDoctorsTest() {
        String[] doc = nc.getDoctors(wc);
        ArrayList<String> docs = new ArrayList<String>(Arrays.asList(doc));
        assertEquals("wrong number of doctors", 3, docs.size());
        assertTrue(docs.contains("Doktoor"));
        assertTrue(docs.contains("Gregory House"));
        assertTrue(docs.contains("Janet Fraiser"));
        assertFalse(docs.contains("Gergorie House"));
        assertFalse(docs.contains("Doktor"));
        assertFalse(docs.contains("Janet Freiser"));
    }
}
