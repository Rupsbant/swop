package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.Constraints.Implementation.WorkingHoursTimeConstraint;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.Time;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.TimeFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkingHoursTimeConstraintTest {

    public WorkingHoursTimeConstraintTest() {
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
     * Test of setValidStaff method, of class WorkingHoursTimeConstraint.
     */
    @Test
    public void testSetValidStaff() throws ArgumentConstraintException, ArgumentIsNullException {
        System.out.println("setValidStaff");
        WorkingHoursTimeConstraint instance = new WorkingHoursTimeConstraint();
        Staff n = new Nurse("name");
        instance.resetAll();
        assertEquals("Did not revert to null", instance.acceptAll(), null);

        TimeFrame tf = new TimeFrame(new Time(9, 9, 9, 8, 0), 15);
        instance.resetAll();
        instance.setStaff(n);
        instance.setTimeFrame(tf);
        assertFalse("TimeFrame is not a working hour.", instance.acceptAll());

        tf = new TimeFrame(new Time(9, 9, 9, 9, 00), 15);
        instance.resetAll();
        instance.setStaff(n);
        instance.setTimeFrame(tf);
        assertTrue("TimeFrame is a working hour.", instance.acceptAll());

        tf = new TimeFrame(new Time(9, 9, 9, 0, 59), 15);
        instance.resetAll();
        instance.setStaff(n);
        instance.setTimeFrame(tf);
        assertFalse("TimeFrame is not a working hour.", instance.acceptAll());

        tf = new TimeFrame(new Time(9, 9, 9, 17, 00), 15);
        instance.resetAll();
        instance.setStaff(n);
        instance.setTimeFrame(tf);
        assertFalse("TimeFrame is not a working hour.", instance.acceptAll());

        tf = new TimeFrame(new Time(9, 9, 9, 16, 46), 15);
        instance.resetAll();
        instance.setStaff(n);
        instance.setTimeFrame(tf);
        assertFalse("TimeFrame is not a working hour.", instance.acceptAll());

        tf = new TimeFrame(new Time(9, 9, 9, 16, 45), 15);
        instance.resetAll();
        instance.setStaff(n);
        instance.setTimeFrame(tf);
        assertTrue("TimeFrame is a working hour.", instance.acceptAll());
    }
}
