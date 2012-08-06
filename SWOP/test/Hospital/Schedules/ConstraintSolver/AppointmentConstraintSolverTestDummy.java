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
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.Schedules.TimeFrame;
import Hospital.World.BasicWorld;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.World;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppointmentConstraintSolverTestDummy {

    AppointmentConstraintSolver instance;
    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    Campus campusNorth;
    Campus campusSouth;

    public AppointmentConstraintSolverTestDummy() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");

        campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {
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

    /**
     * Test of solve method, of class AppointmentConstraintSolver.
     */
    @Test
    public void testSolve() throws Exception {
        ScheduleGroup single1 = new SingleSchedulableGroup(ruben);
        SingleSchedulableGroup single2 = new SingleSchedulableGroup(d);
        List<ScheduleGroup> groups = Arrays.asList(single1, single2);
        instance.setScheduleGroups(groups);
        instance.setTimeDelay(new TimeFrame(new Time(2011, 11, 8, 9, 0), 20));
        instance.setConstaints(Collections.EMPTY_LIST);
        instance.setCampusDecider(new GetC(campusNorth));
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
        List<TimeFrameConstraint> constraints = new ArrayList<TimeFrameConstraint>();
        instance.setCampusDecider(new GetC(campusNorth));
        constraints.add(new PriorityConstraint(new HighLowPriority(false)));
        instance.setScheduleGroups(groups);
        instance.setTimeDelay(new TimeFrame(new Time(2011, 11, 8, 9, 0), 20));
        instance.setConstaints(constraints);
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
        List<TimeFrameConstraint> constraints = new ArrayList<TimeFrameConstraint>();
        constraints.add(new PriorityConstraint(new HighLowPriority(false)));
        instance.setScheduleGroups(groups);
        instance.setTimeDelay(new TimeFrame(new Time(2011, 11, 8, 9, 20), 20));
        instance.setCampusDecider(new GetC(campusNorth));
        instance.setConstaints(constraints);
        instance.solve();
        assertEquals(ruben, instance.getAttendees().get(0));
        assertEquals(d, instance.getAttendees().get(1));
        assertEquals(new TimeFrame(new Time(2011, 11, 8, 9, 50), 20), instance.getChosenTimeFrame());
        assertEquals(campusNorth, instance.getCampus());
    }
}
