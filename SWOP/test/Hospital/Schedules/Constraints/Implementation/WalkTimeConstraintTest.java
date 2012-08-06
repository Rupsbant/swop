package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.ScheduleTestUtil;
import Hospital.World.BasicWorld;
import Hospital.World.Campus;
import Hospital.World.World;
import java.util.Arrays;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.Time;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import Hospital.Schedules.TimeFrame;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WalkTimeConstraintTest {

    private World w = BasicWorld.getWorldForTesting();
    private Patient ruben;
    private Doctor d;
    private Campus campusNorth;
    private Campus campusSouth;

    public WalkTimeConstraintTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
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
     * Test of isAccepted method, of class WalkTimeConstraint.
     */
    @Test
    public void testIsAcceptedCampusNorth() throws ArgumentIsNullException, ArgumentConstraintException {
        WalkTimeConstraint instance = new WalkTimeConstraint();
        Map<TimeFrame, TimeFrame> tfList = new TreeMap<TimeFrame, TimeFrame>();
        d.visitConstraint(instance);
        instance.setCampus(campusNorth);
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 0), 15), new TimeFrame(new Time(2011, 11, 8, 9, 0), 15));   //WalkTime == 0, always possible, 
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 15), 15), new TimeFrame(new Time(2011, 11, 8, 9, 15), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 16), 15), new TimeFrame(new Time(2011, 11, 8, 9, 16), 15)); //if at same time, it will be preempted so can't be found
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 50), 15), new TimeFrame(new Time(2011, 11, 8, 9, 50), 15));
        
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 12, 40), 15), new TimeFrame(new Time(2011, 11, 8, 12, 40), 15)); //WalkTime == 15
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 12, 41), 15), new TimeFrame(new Time(2011, 11, 8, 12, 56), 15)); //If not enough WalkTime between go to first collision time
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 12, 55), 15), new TimeFrame(new Time(2011, 11, 8, 12, 56), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 12, 56), 15), new TimeFrame(new Time(2011, 11, 8, 12, 56), 15)); //If collision, don't care
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 30), 15), new TimeFrame(new Time(2011, 11, 8, 13, 45), 15)); //If not enough WalkTime before: fast-forward until enough WalkTime


        for (Entry<TimeFrame, TimeFrame> e : tfList.entrySet()) {
            instance.setTimeFrame(e.getKey());
            assertEquals(e.getValue(), instance.isAccepted());
        }
    }

    @Test
    public void testIsAcceptedOtherCampus() throws ArgumentIsNullException, ArgumentConstraintException {
        WalkTimeConstraint instance = new WalkTimeConstraint();
        Map<TimeFrame, TimeFrame> tfList = new TreeMap<TimeFrame, TimeFrame>();
        d.visitConstraint(instance);

        instance.setCampus(campusSouth);
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 0), 15), new TimeFrame(new Time(2011, 11, 8, 9, 0), 15)); //Not colliding, enough time
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 1), 15), new TimeFrame(new Time(2011, 11, 8, 9, 16), 15));//WalkTime colliding, go to colliding time
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 16), 15), new TimeFrame(new Time(2011, 11, 8, 9, 16), 15)); //Colliding, don't care, possible preemption
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 9, 50), 15), new TimeFrame(new Time(2011, 11, 8, 10, 5), 15)); //Not enough time, can't preempt (to late): go to first right time
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 12, 55), 15), new TimeFrame(new Time(2011, 11, 8, 12, 55), 15)); //Same campus, never walking problem, otherwise colliding (-1) minute walking
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 15), 15), new TimeFrame(new Time(2011, 11, 8, 13, 15), 15));
        tfList.put(new TimeFrame(new Time(2011, 11, 8, 13, 30), 15), new TimeFrame(new Time(2011, 11, 8, 13, 30), 15));


        for (Entry<TimeFrame, TimeFrame> e : tfList.entrySet()) {
            instance.setTimeFrame(e.getKey());
            assertEquals(e.getValue(), instance.isAccepted());
        }
    }

    @Test
    public void testReset() throws ArgumentConstraintException, ArgumentIsNullException {
        WalkTimeConstraint instance = new WalkTimeConstraint();
        assertEquals(null, instance.isAccepted());
        d.visitConstraint(instance);
        assertEquals(null, instance.isAccepted());
        instance.setCampus(campusNorth);
        assertEquals(null, instance.isAccepted());
        instance.setTimeFrame(new TimeFrame(new Time(), 15));
        assertNotSame(null, instance.isAccepted());
        instance.reset();
        assertEquals(null, instance.isAccepted());
    }
}
