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

public class ChangeLocationPreferenceTest {

    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    private Campus campusNorth;
    private Campus campusSouth;

    public ChangeLocationPreferenceTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
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
        Schedule sched1 = d.getSchedule();

        /**
         * Day one
         */
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 30), 20);
        makeAppointment(tf, sched1, campusNorth);

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 10), 20);
        makeAppointment(tf, sched1, campusSouth);

        tf = new TimeFrame(new Time(2011, 11, 8, 14, 10), 20);
        makeAppointment(tf, sched1, campusNorth);

        tf = new TimeFrame(new Time(2011, 11, 8, 15, 10), 20);
        makeAppointment(tf, sched1, campusNorth);

        /**
         * Day two
         */
        tf = new TimeFrame(new Time(2011, 11, 9, 9, 30), 20);
        makeAppointment(tf, sched1, campusNorth);

        tf = new TimeFrame(new Time(2011, 11, 9, 13, 10), 20);
        makeAppointment(tf, sched1, campusSouth);

        tf = new TimeFrame(new Time(2011, 11, 9, 14, 10), 20);
        makeAppointment(tf, sched1, campusNorth);

        tf = new TimeFrame(new Time(2011, 11, 9, 15, 10), 20);
        makeAppointment(tf, sched1, campusSouth);

        tf = new TimeFrame(new Time(2011, 11, 9, 16, 10), 20);
        makeAppointment(tf, sched1, campusNorth);

        /**
         * Day three
         */
        tf = new TimeFrame(new Time(2011, 11, 10, 9, 30), 20);
        makeAppointment(tf, sched1, campusNorth);

        tf = new TimeFrame(new Time(2011, 11, 10, 13, 10), 20);
        makeAppointment(tf, sched1, campusSouth);

        tf = new TimeFrame(new Time(2011, 11, 10, 14, 10), 20);
        makeAppointment(tf, sched1, campusNorth);

        tf = new TimeFrame(new Time(2011, 11, 10, 15, 10), 20);
        makeAppointment(tf, sched1, campusSouth);

        tf = new TimeFrame(new Time(2011, 11, 10, 16, 10), 20);
        makeAppointment(tf, sched1, campusSouth);
    }

    private void makeAppointment(TimeFrame tf, Schedule sched1, final Campus campusNorth) throws ArgumentIsNullException, SchedulingException {
        sched1 = d.getSchedule();
        Appointment p = new Appointment(tf, Arrays.asList(sched1), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of makeThisAsPreference method, of class ChangeLocationPreference.
     */
    @Test
    public void testMakeThisAsPreference() throws ArgumentConstraintException, ArgumentIsNullException {
        System.out.println("makeThisAsPreference");
        HasPreference d = new Doctor("abc");
        ChangeLocationPreference instance = new ChangeLocationPreference(null);
        assertEquals(null, d.getPreference());
        instance.makeThisAsPreference(d);
        assertEquals(ChangeLocationPreference.class, d.getPreference().getClass());
    }

    @Test
    public void testCanAddAppointmentDayOne() throws ArgumentIsNullException, ArgumentConstraintException {
        ChangeLocationPreference instance = new ChangeLocationPreference(d);
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        boolean result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 50), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 50), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 14, 30), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 8, 14, 30), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);
    }

    @Test
    public void testCanAddAppointmentDayTwo() throws ArgumentIsNullException, ArgumentConstraintException {
        ChangeLocationPreference instance = new ChangeLocationPreference(d);
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 9, 9, 0), 20);
        boolean result = instance.canAddAppointment(tf, campusSouth);
        assertFalse(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 9, 0), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 9, 50), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 9, 50), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 17, 10), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertFalse(result);

        tf = new TimeFrame(new Time(2011, 11, 9, 17, 10), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);
    }

    @Test
    public void testCanAddAppointmentDayThree() throws ArgumentIsNullException, ArgumentConstraintException {
        ChangeLocationPreference instance = new ChangeLocationPreference(d);
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 10, 9, 0), 20);
        boolean result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 10, 9, 0), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 10, 9, 50), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 10, 9, 50), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 10, 15, 30), 20);
        result = instance.canAddAppointment(tf, campusSouth);
        assertTrue(result);

        tf = new TimeFrame(new Time(2011, 11, 10, 15, 30), 20);
        result = instance.canAddAppointment(tf, campusNorth);
        assertFalse(result);
    }
}
