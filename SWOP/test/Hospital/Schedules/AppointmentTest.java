package Hospital.Schedules;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.Collections;

public class AppointmentTest {

    @Test
    public void collidesTest() {
        Appointment app1 = new Appointment(new Time(), 30, Collections.EMPTY_LIST, (AppointmentCommand) null, (Campus) null, null);
        Appointment app2 = new Appointment(new Time(), 30, Collections.EMPTY_LIST, (AppointmentCommand) null, (Campus) null, null);
        Appointment app3 = new Appointment(new Time(2011, 11, 8, 8, 40), 30, Collections.EMPTY_LIST, (AppointmentCommand) null, (Campus) null, null);
        Appointment app4 = new Appointment(new Time(2011, 11, 8, 8, 20), 30, Collections.EMPTY_LIST, (AppointmentCommand) null, (Campus) null, null);
        assertTrue("These two appointments collide", app1.collides(app2));
        assertTrue("These two appointments collide", app2.collides(app1));
        assertTrue("These two appointments collide", app1.collides(app4));
        assertTrue("These two appointments collide", app4.collides(app1));
        assertFalse("These two appointments don't collide", app1.collides(app3));
        assertFalse("These two appointments don't collide", app3.collides(app1));
    }
}
