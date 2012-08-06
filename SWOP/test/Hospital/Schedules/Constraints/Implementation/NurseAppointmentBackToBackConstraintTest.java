package Hospital.Schedules.Constraints.Implementation;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.WareHouse.ItemInfo;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.World.Time;
import java.util.Arrays;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Patient.Patient;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.People.Nurse;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import Hospital.Treatments.Medication;
import Hospital.Treatments.Treatment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NurseAppointmentBackToBackConstraintTest {

    World w = BasicWorld.getWorldForTesting();
    Patient p;
    Nurse nurse;

    public NurseAppointmentBackToBackConstraintTest() throws ArgumentConstraintException, ArgumentIsNullException, NoPersonWithNameAndRoleException {
        this.p = w.getPersonByName(Patient.class, "Ruben");
        this.nurse = w.getPersonByName(Nurse.class, "Nurse Joy");
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException {
        Treatment t = new Medication("Medication1", Boolean.TRUE, new ItemInfo[0]);
        ScheduleGroup scheduleGroup = new SingleSchedulableGroup(p);
        AppointmentCommand appC = new AppointmentCommand(w, t, Arrays.asList(scheduleGroup), new HighLowPriority(true));
        //Creates appointment with Nurse Joy, a bit magic... Do not change the order of the nurses ;)
        //9:00->9:20
        appC.execute();

        scheduleGroup = new SingleSchedulableGroup(p);
        t = new Medication("Medication2", Boolean.TRUE, new ItemInfo[0]);
        appC = new AppointmentCommand(w, t, Arrays.asList(scheduleGroup), new HighLowPriority(true));
        //Creates appointment with Nurse Joy, a bit magic... Do not change the order of the nurses ;)
        //9:20->9:40
        appC.execute();
    }

    /**
     * Test of setValidNurse method, of class NurseAppointmentBackToBackConstraint.
     */
    @Test
    public void testSetValidNurse() throws ArgumentIsNullException, ArgumentConstraintException {
        NurseAppointmentBackToBackConstraint instance = new NurseAppointmentBackToBackConstraint();
        instance.reset();
        instance.setNurse(nurse);
        Map<Time, Time> tfList = new TreeMap<Time, Time>();
        tfList.put(new Time(2011, 11, 8, 9, 0), new Time(2011, 11, 8, 9, 0));
        tfList.put(new Time(2011, 11, 8, 9, 1), new Time(2011, 11, 8, 9, 2));
        tfList.put(new Time(2011, 11, 8, 9, 20), new Time(2011, 11, 8, 9, 20));
        tfList.put(new Time(2011, 11, 8, 9, 21), new Time(2011, 11, 8, 9, 22));
        tfList.put(new Time(2011, 11, 8, 9, 40), new Time(2011, 11, 8, 9, 40));
        tfList.put(new Time(2011, 11, 8, 9, 41), new Time(2011, 11, 8, 9, 42));
        tfList.put(new Time(2011, 11, 8, 13, 45), new Time(2011, 11, 8, 13, 46));
        tfList.put(new Time(2011, 11, 8, 13, 46), new Time(2011, 11, 8, 13, 47));

        
        for (Entry<Time, Time> e : tfList.entrySet()) {
            instance.setTime(e.getKey(), 15);
            assertEquals(e.getValue(), instance.isAccepted());
        }
    }
}
