/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Hospital.Schedules.Constraints.Preference;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.World.BasicWorld;
import Hospital.World.Time;
import Hospital.World.World;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupsbant
 */
public class ChangeLocationPreference2Test {

    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    private Campus campusNorth;

    public ChangeLocationPreference2Test() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of canAddAppointment method, of class ChangeLocationPreference.
     */
    @Test
    public void testCanAddAppointment() throws ArgumentIsNullException, ArgumentConstraintException {
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        StayShiftPreference instance = new StayShiftPreference(d);
        boolean result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);
    }
}
