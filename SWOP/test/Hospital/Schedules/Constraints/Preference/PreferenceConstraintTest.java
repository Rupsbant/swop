package Hospital.Schedules.Constraints.Preference;

import Hospital.Exception.Scheduling.SchedulingException;
import java.util.Arrays;
import Hospital.Schedules.Schedule;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.Schedules.TimeFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PreferenceConstraintTest {

    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;

    public PreferenceConstraintTest() throws NoPersonWithNameAndRoleException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        new StayShiftPreference(null).makeThisAsPreference(d);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentConstraintException, ArgumentIsNullException, SchedulingException {
        final Campus campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        final Campus campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointment p = new Appointment(tf, Arrays.asList(sched1, sched2), null, campusSouth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 0), 20);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1, sched2), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 9, 9, 0), 20);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1, sched2), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 9, 13, 0), 20);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1, sched2), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIsAccepted() throws ArgumentIsNullException, ArgumentConstraintException {
        PreferenceConstraint instance = new PreferenceConstraint();

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 10, 0), 15);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTimeFrame(tf);
        assertTrue(instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 12, 0), 15);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTimeFrame(tf);
        assertTrue(instance.isAccepted());
    }

    @Test
    public void testIsFail() throws ArgumentIsNullException, ArgumentConstraintException {
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 10, 0), 15);
        PreferenceConstraint instance = new PreferenceConstraint();
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTimeFrame(tf);
        assertFalse(instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 12, 0), 15);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTimeFrame(tf);
        assertFalse(instance.isAccepted());
    }

    @Test
    public void testStayOnNoon() throws ArgumentIsNullException, ArgumentConstraintException {
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 11, 59), 15);
        PreferenceConstraint instance = new PreferenceConstraint();
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTimeFrame(tf);
        assertFalse(instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 11, 59), 15);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTimeFrame(tf);
        assertFalse(instance.isAccepted());
        
        tf = new TimeFrame(new Time(2011, 11, 9, 11, 59), 15);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTimeFrame(tf);
        assertFalse(instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 9, 11, 59), 15);
        instance.reset();
        instance.setDoctor(d);
        instance.setTimeFrame(tf);
        instance.setCampus(ruben.getCampus());
        assertTrue(instance.isAccepted());

    }
}
