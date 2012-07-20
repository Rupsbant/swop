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
import Hospital.Schedules.TimeFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PriorityConstraintTest {

    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;

    public PriorityConstraintTest() throws NoPersonWithNameAndRoleException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {
        final Campus campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        final Campus campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 30), 20);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointable t = new XRayScan(3, 3, "hoofd en schouders");
        AppointmentCommand appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(true));
        Appointment p = new Appointment(tf, Arrays.asList(sched1, sched2), appC, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 10), 20);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        t = new XRayScan(3, 3, "knie en teen");
        appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(false));
        p = new Appointment(tf, Arrays.asList(sched1, sched2), appC, campusSouth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setValidSchedulable method, of class PriorityConstraint.
     */
    @Test
    public void testPreemption() throws ArgumentIsNullException, ArgumentConstraintException {
        PriorityConstraint instance = new PriorityConstraint(new HighLowPriority(true));

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 20), 20);
        Schedulable s = ruben;
        instance.setValidSchedulable(tf, s);
        assertFalse("Appointments are same priority", instance.acceptAll());

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 0), 20);
        instance.resetAll();
        instance.setValidSchedulable(tf, s);
        assertTrue("This appointment is more urgent", instance.acceptAll());
    }

    @Test
    public void testNoPreemption() throws ArgumentIsNullException, ArgumentConstraintException {
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 20), 20);
        Schedulable s = ruben;
        PriorityConstraint instance = new PriorityConstraint(new HighLowPriority(false));
        instance.setValidSchedulable(tf, s);
        assertFalse("Appointments is of lower priority", instance.acceptAll());

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 0), 20);
        instance.resetAll();
        assertEquals(null, instance.acceptAll());
        instance.setValidSchedulable(tf, s);
        assertFalse("Appointments are of the same priority", instance.acceptAll());
    }
}
