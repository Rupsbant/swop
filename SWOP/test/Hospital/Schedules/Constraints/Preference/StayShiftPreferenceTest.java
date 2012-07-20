package Hospital.Schedules.Constraints.Preference;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.World.Time;
import java.util.Arrays;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.People.Doctor;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class StayShiftPreferenceTest {

    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    private Campus campusNorth;
    private Campus campusSouth;

    public StayShiftPreferenceTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 30), 20);
        Schedule sched1 = d.getSchedule();
        Appointment p = new Appointment(tf, Arrays.asList(sched1), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 10), 20);
        sched1 = d.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1), null, campusSouth);
        ScheduleTestUtil.addAppointment(sched1, p);

        tf = new TimeFrame(new Time(2011, 11, 9, 9, 30), 20);
        sched1 = d.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);

        tf = new TimeFrame(new Time(2011, 11, 9, 13, 10), 20);
        sched1 = d.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of makeThisAsPreference method, of class StayShiftPreference.
     */
    @Test
    public void testMakeThisAsPreference() throws ArgumentConstraintException, ArgumentIsNullException {
        System.out.println("makeThisAsPreference");
        StayShiftPreference instance = new StayShiftPreference(null);
        ChangeLocationPreference temp = new ChangeLocationPreference(null);
        temp.makeThisAsPreference(d);
        assertEquals(ChangeLocationPreference.class, d.getPreference().getClass());
        instance.makeThisAsPreference(d);
        assertEquals(StayShiftPreference.class, d.getPreference().getClass());
    }

    /**
     * Test of canAddAppointment method, of class StayShiftPreference.
     */
    @Test
    public void testCanAddAppointment() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("canAddAppointment");
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 11, 10), 20);
        StayShiftPreference instance = new StayShiftPreference(d);
        boolean result = instance.canAddAppointment(tf, campusSouth);
        assertFalse(result);

        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 14, 10), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertFalse(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 14, 10), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 11, 50), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertFalse(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 11, 50), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertFalse(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 11, 50), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertFalse(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 11, 50), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

    }
}
