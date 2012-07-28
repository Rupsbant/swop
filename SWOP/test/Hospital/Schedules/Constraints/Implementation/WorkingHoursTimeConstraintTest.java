package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.Time;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.TimeFrame;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkingHoursTimeConstraintTest {

    /**
     * Test of setValidStaff method, of class WorkingHoursTimeConstraint.
     */
    @Test
    public void testSetValidStaff() throws ArgumentConstraintException, ArgumentIsNullException {
        System.out.println("setValidStaff");
        WorkingHoursTimeConstraint instance = new WorkingHoursTimeConstraint();
        Staff n = new Nurse("name");
        instance.reset();
        assertEquals("Did not revert to null", instance.isAccepted(), null);

        TimeFrame tf = new TimeFrame(new Time(9, 9, 9, 8, 0), 15);
        instance.reset();
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("TimeFrame is not a working hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(9, 9, 9, 9, 00), 15);
        instance.reset();
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("TimeFrame is a working hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(9, 9, 9, 0, 59), 15);
        instance.reset();
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("TimeFrame is not a working hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(9, 9, 9, 17, 00), 15);
        instance.reset();
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("TimeFrame is not a working hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(9, 9, 9, 16, 46), 15);
        instance.reset();
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("TimeFrame is not a working hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(9, 9, 9, 16, 45), 15);
        instance.reset();
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("TimeFrame is a working hour.", instance.isAccepted());
    }
}
