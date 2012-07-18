package Hospital.Schedules;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.World.Time;

public class ScheduleTest {

    @Test
    public void addAppointmentNullArgumentTest() {
        Schedule schedule = new Schedule();
        try {
            schedule.addAppointment(null);
            fail("This exception should have been thrown");
        } catch (SchedulingException e) {
            fail("This exception should not have been thrown");
        } catch (ArgumentIsNullException e) {
            assertEquals(ArgumentIsNullException.class, e.getClass());
        }
    }

    @Test
    public void removeAppointmentNullArgumentTest() {
        Schedule schedule = new Schedule();
        try {
            schedule.removeAppointment(null);
            fail("Exception should be thrown");
        } catch (ArgumentIsNullException ex){
        } catch(Exception e){
            e.printStackTrace();
            fail("This exception should not have been thrown"+e);
        }

    }

    public void removeAppointmentWrongArgumentTest() {
        //TODO
    }

    @Test
    public void isFreeNullArgumentTest() {
        Schedule schedule = new Schedule();
        try {
            schedule.isFree(new TimeFrame(null, 5));
        } catch (ArgumentIsNullException e) {
            assertEquals(ArgumentIsNullException.class, e.getClass());
            assertEquals("Starttime of appointment is null", e.getMessage());
        } catch (ArgumentConstraintException e) {
            fail("This exception should not have been thrown");
        }
    }

    @Test
    public void isFreeWrongArgumentTest() {
        Schedule schedule = new Schedule();
        try {
            schedule.isFree(new TimeFrame(new Time(), -5));
        } catch (ArgumentConstraintException e) {
            assertEquals(ArgumentConstraintException.class, e.getClass());
            assertEquals("Length of appointment is negative", e.getMessage());
        } catch (ArgumentIsNullException e) {
            fail("This exception should not have been thrown");
        }
    }
}
