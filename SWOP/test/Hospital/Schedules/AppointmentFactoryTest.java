package Hospital.Schedules;

import Hospital.WareHouse.ItemInfo;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.MedicalTest.XRayScan;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import java.util.Collections;
import java.util.Arrays;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.Schedules.ConstraintSolver.AppointmentConstraintSolver;
import Hospital.World.BasicWorld;
import Hospital.World.Time;
import Hospital.World.World;
import Hospital.People.Doctor;
import Hospital.Schedules.ConstraintSolver.GetC;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import Hospital.Treatments.Medication;
import Hospital.World.Campus;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppointmentFactoryTest {

    AppointmentConstraintSolver instance;
    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    Campus campusNorth;
    Campus campusSouth;

    public AppointmentFactoryTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
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
    public void setUp() throws ArgumentIsNullException, ArgumentConstraintException, SchedulingException {

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 30), 5);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = jeroen.getSchedule();
        Appointable t = new XRayScan(3, 3, "knie");
        AppointmentCommand appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(true));
        Appointment p = new Appointment(tf, Arrays.asList(sched1, sched2), appC, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 40), 5);
        sched1 = d.getSchedule();
        sched2 = jeroen.getSchedule();
        t = new XRayScan(3, 3, "teen");
        appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(true));
        p = new Appointment(tf, Arrays.asList(sched1, sched2), appC, campusSouth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of makeAppointment method, of class AppointmentFactory.
     */
    @Test
    public void testMakeAppointment() throws Exception {
        System.out.println("makeAppointment");
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        Campus tempCampus = new Campus("abc", w);
        List<TimeFrameConstraint> tfConstraints = new ArrayList<TimeFrameConstraint>();
        List<ScheduleGroup> groups = Arrays.asList((ScheduleGroup) new SingleSchedulableGroup(ruben));
        Appointable t = new XRayScan(3, 3, "topje van m'n neus");
        AppointmentCommand appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(true));
        Appointment result = AppointmentFactory.makeAppointment(tf, tfConstraints, new GetC(tempCampus), groups, appC);
        assertEquals("Campuses are different", tempCampus, result.getCampus());
        assertEquals("Appcommand is different", appC, result.getAppCommand());
        assertEquals("TimeFrame is wrong, check solver first", tf, result.getTimeFrame());
        assertEquals("Wrong attendee", ruben.getSchedule(), result.getAttendees().get(0));
    }

    /**
     * Test of redoPreempted method, of class AppointmentFactory.
     */
    @Test
    public void testRedoPreempted() throws ArgumentIsNullException {
        Appointable t = new Medication("op een grote paddostoel", true, new ItemInfo[]{});
        AppointmentCommand appC = new AppointmentCommand(w, t, Arrays.asList((ScheduleGroup)new SingleSchedulableGroup(ruben)), new HighLowPriority(false));
        Set<AppointmentCommand> preempted = Collections.singleton(appC);
        String s = AppointmentFactory.redoPreempted(preempted);
        assertEquals("String not correct.",
                "Redo: AppointmentCommand of MedicationTreatment: op een grote paddostoel\n"
                + "No medication items!\n"
                + "Sensitive: true\n"
                + "\tAppointment from 2011/11/8 9:00 of 20 minutes, until 2011/11/8 9:20 with 2 attendees.\n", s);
    }

    /**
     * Test of undoPreempted method, of class AppointmentFactory.
     */
    @Test
    public void testUndoPreempted() throws ArgumentIsNullException, CannotDoException {
        Appointable t = new Medication("Rood met witte stippen", true, new ItemInfo[]{});
        AppointmentCommand appC = new AppointmentCommand(w, t, Arrays.asList((ScheduleGroup) new SingleSchedulableGroup(ruben)), new HighLowPriority(false));
        appC.execute();
        Set<AppointmentCommand> preempted = Collections.singleton(appC);
        String s = AppointmentFactory.undoPreempted(preempted);
        assertEquals("String not correct", "Undone:\n"
                + "AppointmentCommand of MedicationTreatment: Rood met witte stippen\n"
                + "No medication items!\n"
                + "Sensitive: true\n", s);
    }

    /**
     * Test of getPreempted method, of class AppointmentFactory.
     */
    @Test
    public void testGetPreempted() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("getPreempted");
        List<Schedule> chosenSchedules = Arrays.asList(jeroen.getSchedule());
        TimeFrame chosenTimeFrame = new TimeFrame(new Time(2011, 11, 8, 9, 33), 20);
        Set<AppointmentCommand> result = AppointmentFactory.getPreempted(chosenSchedules, chosenTimeFrame);
        assertEquals(2, result.size());
        AppointmentCommand[] apps = result.toArray(new AppointmentCommand[]{});
        assertNotSame(apps[0], apps[1]);
    }
}
