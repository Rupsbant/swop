package Hospital.Schedules.Constraints.Implementation;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
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
        Schedule sched1 = d.getSchedule();
        Schedule sched2 = ruben.getSchedule();
        Appointment p = new Appointment(new Time(2011, 11, 8, 9, 30), 20, Arrays.asList(sched1, sched2), null, campusNorth, null);
        ScheduleTestUtil.addAppointment(sched1, p);
        ScheduleTestUtil.addAppointment(sched2, p);

        sched1 = d.getSchedule();
        sched2 = ruben.getSchedule();
        p = new Appointment(new Time(2011, 11, 8, 13, 10), 20, Arrays.asList(sched1, sched2), null, campusSouth, null);
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
        instance.setDoctor(d);
        instance.setCampus(ruben.getCampus());
        instance.setTime(new Time(2011, 11, 8, 10, 0), 15);

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
        instance.setDoctor(d);
        instance.setCampus(campusNorth);
        Map<Time, Time> tfList = new TreeMap<Time, Time>();
        tfList.put(new Time(2011, 11, 8, 9, 0), new Time(2011, 11, 8, 9, 0));
        tfList.put(new Time(2011, 11, 8, 9, 1), new Time(2011, 11, 8, 9, 2));
        tfList.put(new Time(2011, 11, 8, 9, 50), new Time(2011, 11, 8, 9, 50));
        tfList.put(new Time(2011, 11, 8, 9, 51), new Time(2011, 11, 8, 9, 52));
        tfList.put(new Time(2011, 11, 8, 10, 0), new Time(2011, 11, 8, 10, 0));
        tfList.put(new Time(2011, 11, 8, 13, 45), new Time(2011, 11, 8, 13, 45));
        tfList.put(new Time(2011, 11, 8, 13, 46), new Time(2011, 11, 8, 13, 47));

        
        for (Entry<Time, Time> e : tfList.entrySet()) {
            instance.setTime(e.getKey(), 15);
            assertEquals(e.getValue(), instance.isAccepted());
        }
        instance.setCampus(campusSouth);
        tfList.clear();
        tfList.put(new Time(2011, 11, 8, 13, 30), new Time(2011, 11, 8, 13, 30));
        tfList.put(new Time(2011, 11, 8, 13, 45), new Time(2011, 11, 8, 13, 46));
        
        for (Entry<Time, Time> e : tfList.entrySet()) {
            instance.setTime(e.getKey(), 15);
            assertEquals(e.getValue(), instance.isAccepted());
        }
    }
}
