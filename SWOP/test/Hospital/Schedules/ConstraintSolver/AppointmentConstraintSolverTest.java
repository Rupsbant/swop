package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Appointable;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.MedicalTest.XRayScan;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import Hospital.Schedules.Constraints.Priority.PriorityConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.Schedules.TimeFrame;
import Hospital.World.BasicWorld;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.World;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppointmentConstraintSolverTest {

    AppointmentConstraintSolver instance;
    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    Campus campusNorth;
    Campus campusSouth;

    public AppointmentConstraintSolverTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
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
        instance = new Solver();

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 30), 20);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointable t = new XRayScan(3, 3, "knie");
        AppointmentCommand appC = new AppointmentCommand(w, t, Collections.EMPTY_LIST, new HighLowPriority(true));
        Appointment p = new Appointment(tf, Arrays.asList(sched1, sched2), appC, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 10), 20);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
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
     * Test of solve method, of class AppointmentConstraintSolver.
     */
    @Test
    public void testSolve() throws Exception {
        ScheduleGroup single1 = new SingleSchedulableGroup(ruben);
        SingleSchedulableGroup single2 = new SingleSchedulableGroup(d);
        List<ScheduleGroup> groups = Arrays.asList(single1, single2);
        instance.setScheduleGroups(groups);
        instance.setFirstTimeFrame(new TimeFrame(new Time(2011, 11, 8, 9, 0), 20));
        instance.setConstaints(new GetC(campusNorth));
        instance.solve();
        assertEquals(ruben, instance.getAttendees().get(0));
        assertEquals(d, instance.getAttendees().get(1));
        assertEquals(new TimeFrame(new Time(2011, 11, 8, 9, 0), 20), instance.getChosenTimeFrame());
        assertEquals(campusNorth, instance.getCampus());
    }

    @Test
    public void testConstraintNoCollision() throws Exception {
        ScheduleGroup single1 = new SingleSchedulableGroup(ruben);
        SingleSchedulableGroup single2 = new SingleSchedulableGroup(d);
        List<ScheduleGroup> groups = Arrays.asList(single1, single2);
        TimeFrameConstraint constraint = new GetC(campusNorth);
        constraint.addConstraintList(new PriorityConstraint(new HighLowPriority(false)));
        instance.setScheduleGroups(groups);
        instance.setFirstTimeFrame(new TimeFrame(new Time(2011, 11, 8, 9, 0), 20));
        instance.setConstaints(constraint);
        instance.solve();
        assertEquals(ruben, instance.getAttendees().get(0));
        assertEquals(d, instance.getAttendees().get(1));
        assertEquals(new TimeFrame(new Time(2011, 11, 8, 9, 0), 20), instance.getChosenTimeFrame());
        assertEquals(campusNorth, instance.getCampus());
    }

    @Test
    public void testConstraintCollision() throws Exception {
        ScheduleGroup single1 = new SingleSchedulableGroup(ruben);
        SingleSchedulableGroup single2 = new SingleSchedulableGroup(d);
        List<ScheduleGroup> groups = Arrays.asList(single1, single2);
        TimeFrameConstraint constraint = new GetC(campusNorth);
        constraint.addConstraintList(new PriorityConstraint(new HighLowPriority(false)));
        instance.setScheduleGroups(groups);
        instance.setFirstTimeFrame(new TimeFrame(new Time(2011, 11, 8, 9, 20), 20));
        instance.setConstaints(constraint);
        instance.solve();
        assertEquals(ruben, instance.getAttendees().get(0));
        assertEquals(d, instance.getAttendees().get(1));
        assertEquals(new TimeFrame(new Time(2011, 11, 8, 9, 50), 20), instance.getChosenTimeFrame());
        assertEquals(campusNorth, instance.getCampus());
    }
}