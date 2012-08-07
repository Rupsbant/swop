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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PreferenceConstraintTest {

    private World w = BasicWorld.getWorldForTesting();
    private Patient ruben;
    private Patient jeroen;
    private Doctor d;

    public PreferenceConstraintTest() throws NoPersonWithNameAndRoleException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        new StayShiftPreference(null).makeThisAsPreference(d);
    }

    @Before
    public void setUp() throws ArgumentConstraintException, ArgumentIsNullException, SchedulingException {
        final Campus campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        final Campus campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));

        Time tf = new Time(2011, 11, 8, 9, 0);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointment p = new Appointment(tf, 20, Arrays.asList(sched1, sched2), null, campusSouth, null);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new Time(2011, 11, 8, 13, 0);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, 20, Arrays.asList(sched1, sched2), null, campusNorth, null);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new Time(2011, 11, 9, 9, 0);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, 20, Arrays.asList(sched1, sched2), null, campusNorth, null);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new Time(2011, 11, 9, 13, 0);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, 20, Arrays.asList(sched1, sched2), null, campusNorth, null);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);
    }

    @Test
    public void testIsAccepted() throws ArgumentIsNullException, ArgumentConstraintException {
        PreferenceConstraint instance = new PreferenceConstraint();

        Time tf = new Time(2011, 11, 8, 10, 0);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());

        tf = new Time(2011, 11, 8, 12, 0);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());
    }

    @Test
    public void testIsFail() throws ArgumentIsNullException, ArgumentConstraintException {
        Time tf = new Time(2011, 11, 8, 10, 0);
        PreferenceConstraint instance = new PreferenceConstraint();
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());

        tf = new Time(2011, 11, 8, 12, 0);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
    }

    @Test
    public void testStayOnNoon() throws ArgumentIsNullException, ArgumentConstraintException {
        Time tf = new Time(2011, 11, 8, 11, 59);
        PreferenceConstraint instance = new PreferenceConstraint();
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());

        tf = new Time(2011, 11, 8, 11, 59);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        
        tf = new Time(2011, 11, 9, 11, 59);
        instance.reset();
        instance.setDoctor(d);
        instance.setCampus(jeroen.getCampus());
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());

        tf = new Time(2011, 11, 9, 11, 59);
        instance.reset();
        instance.setDoctor(d);
        instance.setTime(tf, 15);
        instance.setCampus(ruben.getCampus());
        assertEquals(tf, instance.isAccepted());

    }
}
