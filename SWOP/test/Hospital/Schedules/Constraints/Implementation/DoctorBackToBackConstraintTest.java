package Hospital.Schedules.Constraints.Implementation;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Time;
import Hospital.People.Doctor;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import java.util.Arrays;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Patient.Patient;
import Hospital.World.Campus;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DoctorBackToBackConstraintTest {

    private World w = BasicWorld.getWorldForTesting();
    private Patient ruben;
    private Doctor d;
    private Campus campusNorth;
    private Campus campusSouth;

    public DoctorBackToBackConstraintTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
        campusSouth = w.getCampusFromInfo(w.getCampuses().get(1));
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 30), 20);
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointment p = new Appointment(tf, Arrays.asList(sched1, sched2), null, campusNorth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        tf = new TimeFrame(new Time(2011, 11, 8, 13, 10), 20);
        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(tf, Arrays.asList(sched1, sched2), null, campusSouth);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);
    }

    /**
     * Test of resetValid method, of class DoctorBackToBackConstraint.
     */
    @Test
    public void testResetValid() throws ArgumentIsNullException, ArgumentConstraintException {
        DoctorBackToBackConstraint instance = new DoctorBackToBackConstraint();
        instance.reset();
        assertTrue(instance.isAccepted() == null);
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 10, 0), 15);
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTimeFrame(tf);

        assertTrue(instance.isAccepted() != null);
        instance.reset();
        assertTrue(instance.isAccepted() == null);
    }

    /**
     * Test of isAccepted method, of class DoctorBackToBackConstraint.
     */
    @Test
    public void testIsAccepted() throws ArgumentIsNullException, ArgumentConstraintException {
        DoctorBackToBackConstraint instance = new DoctorBackToBackConstraint();
        Map<TimeFrame, TimeFrame> tfList = new TreeMap<TimeFrame, TimeFrame>();
        instance.setDoctor(d);
        instance.setCampus(campusNorth);
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 0), 15), new TimeFrame(new Time(2011, 11, 8, 9, 0), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 1), 15), new TimeFrame(new Time(2011, 11, 8, 9, 2), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 50), 15), new TimeFrame(new Time(2011, 11, 8, 9, 50), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 51), 15), new TimeFrame(new Time(2011, 11, 8, 9, 52), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 10, 0), 15), new TimeFrame(new Time(2011, 11, 8, 10, 0), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 45), 15), new TimeFrame(new Time(2011, 11, 8, 13, 45), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 46), 15), new TimeFrame(new Time(2011, 11, 8, 13, 47), 15));

        
        for (Entry<TimeFrame, TimeFrame> e : tfList.entrySet()) {
            instance.setTimeFrame(e.getKey());
            assertEquals(e.getValue(), instance.isAccepted());
        }
        instance.setCampus(campusSouth);
        tfList.clear();
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 30), 15), new TimeFrame(new Time(2011, 11, 8, 13, 30), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 45), 15), new TimeFrame(new Time(2011, 11, 8, 13, 46), 15));
        
        for (Entry<TimeFrame, TimeFrame> e : tfList.entrySet()) {
            instance.setTimeFrame(e.getKey());
            assertEquals(e.getValue(), instance.isAccepted());
        }
    }
}
