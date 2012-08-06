package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.Time;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkingHoursTimeConstraintTest {

    /**
     * Test of setValidStaff method, of class WorkingHoursTimeConstraint.
     */
    @Test
    public void testSetValidStaff() throws ArgumentConstraintException, ArgumentIsNullException {
        WorkingHoursTimeConstraint instance = new WorkingHoursTimeConstraint();
        Time tf = new Time(9, 9, 9, 8, 0);
        instance.setTime(tf, 15);
        instance.reset();
        assertEquals("Did not revert to null", instance.isAccepted(), null);

        tf = new Time(9, 9, 9, 8, 0);
        instance.reset();
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(new Time(9, 9, 9, 9, 0), instance.isAccepted());
        //assertFalse("Time is not a working hour.", instance.isAccepted());

        tf = new Time(9, 9, 9, 9, 00);
        instance.reset();
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Time is a working hour.", instance.isAccepted());

        tf = new Time(9, 9, 9, 0, 59);
        instance.reset();
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("Time is not a working hour.", instance.isAccepted());

        tf = new Time(9, 9, 9, 17, 00);
        instance.reset();
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(new Time(9, 9, 10, 9, 0), instance.isAccepted());
        //assertFalse("Time is not a working hour.", instance.isAccepted());

        tf = new Time(9, 9, 9, 16, 46);
        instance.reset();
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(new Time(9, 9, 10, 9, 0), instance.isAccepted());
        //assertFalse("Time is not a working hour.", instance.isAccepted());

        tf = new Time(9, 9, 9, 16, 45);
        instance.reset();
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Time is a working hour.", instance.isAccepted());
    }
}
