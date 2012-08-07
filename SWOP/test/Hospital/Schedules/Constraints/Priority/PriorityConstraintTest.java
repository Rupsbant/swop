package Hospital.Schedules.Constraints.Priority;

import Hospital.MedicalTest.XRayScan;
import Hospital.Schedules.Appointable;
import Hospital.Schedules.AppointmentCommand;
import java.util.Collections;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.Arrays;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Schedules.Schedulable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PriorityConstraintTest {

    private World w = BasicWorld.getWorldForTesting();
    private Patient ruben;
    private Doctor d;

    public PriorityConstraintTest() throws NoPersonWithNameAndRoleException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {
        final Campus campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        final Campus campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));

        Time tf = new Time(2011, 11, 8, 9, 30);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointable t = new XRayScan(3, 3, "hoofd en schouders");
        AppointmentCommand appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(true));
        Appointment p = new Appointment(tf, 20, Arrays.asList(sched1, sched2), appC, campusNorth, new HighLowPriority(true));
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new Time(2011, 11, 8, 13, 10);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        t = new XRayScan(3, 3, "knie en teen");
        appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(false));
        p = new Appointment(tf, 20, Arrays.asList(sched1, sched2), appC, campusSouth, new HighLowPriority(false));
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);
    }

    /**
     * Test of setValidSchedulable method, of class PriorityConstraint.
     */
    @Test
    public void testPreemption() throws ArgumentIsNullException, ArgumentConstraintException {
        PriorityConstraint instance = new PriorityConstraint(new HighLowPriority(true));

        Time tf = new Time(2011, 11, 8, 9, 20);
        Schedulable s = ruben;
        instance.setSchedulable(s);
        instance.setTime(tf, 20);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(new Time(2011, 11, 8, 9, 50), instance.isAccepted());
        //assertFalse("Appointments are same priority", instance.isAccepted());

        tf = new Time(2011, 11, 8, 13, 0);
        instance.reset();
        instance.setSchedulable(s);
        instance.setTime(tf, 20);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("This appointment is more urgent", instance.isAccepted());
    }

    @Test
    public void testNoPreemption() throws ArgumentIsNullException, ArgumentConstraintException {
        Time tf = new Time(2011, 11, 8, 9, 20);
        Schedulable s = ruben;
        PriorityConstraint instance = new PriorityConstraint(new HighLowPriority(false));
        instance.setSchedulable(s);
        instance.setTime(tf, 20);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(new Time(2011, 11, 8, 9, 50), instance.isAccepted());
        //assertFalse("Appointments is of lower priority", instance.isAccepted());

        tf = new Time(2011, 11, 8, 13, 0);
        instance.reset();
        assertEquals(null, instance.isAccepted());
        instance.setSchedulable(s);
        instance.setTime(tf, 20);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(new Time(2011, 11, 8, 13, 30), instance.isAccepted());
        //assertFalse("Appointments are of the same priority", instance.isAccepted());
    }
}
