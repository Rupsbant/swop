package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Scheduling.ScheduleConstraintException;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.World.Time;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import java.util.Arrays;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.People.Nurse;
import Hospital.Treatments.Medication;
import Hospital.Treatments.Treatment;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Schedules.TimeFrame;
import Hospital.WareHouse.ItemInfo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupsbant
 */
public class UnmovableConstraintTest {

    private World w = BasicWorld.getWorldForTesting();
    private Patient ruben;
    private Nurse nurse;
    private Nurse otherNurse;

    public UnmovableConstraintTest() throws ArgumentConstraintException, ArgumentIsNullException, NoPersonWithNameAndRoleException {
        this.ruben = new Patient("Ruben");
        this.nurse = w.getPersonByName(Nurse.class, "Nurse Joy");
        this.otherNurse = w.getPersonByName(Nurse.class, "Verpleegster");
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException {
        Treatment t = new Medication("Medication1", Boolean.TRUE, new ItemInfo[0]);
        ScheduleGroup scheduleGroup = new SingleSchedulableGroup(ruben);
        AppointmentCommand appC = new AppointmentCommand(w, t, Arrays.asList(scheduleGroup), new HighLowPriority(true));
        appC.execute();

        scheduleGroup = new SingleSchedulableGroup(ruben);
        t = new Medication("Medication2", Boolean.TRUE, new ItemInfo[0]);
        appC = new AppointmentCommand(w, t, Arrays.asList(scheduleGroup), new HighLowPriority(true));
        appC.execute();
    }

    /**
     * Test of setValidSchedulable method, of class UnmovableConstraint.
     */
    @Test
    public void testSetValidSchedulableNurse() throws ArgumentIsNullException, ArgumentConstraintException, ScheduleConstraintException {
        UnmovableConstraint instance = new UnmovableConstraint(nurse);

        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        instance.reset();
        instance.setCampus(nurse.getCampus());
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can always move instantly on campus", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 1), 20);
        instance.reset();
        instance.setCampus(nurse.getCampus());
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can always move instantly on campus", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 19), 20);
        instance.reset();
        instance.setCampus(nurse.getCampus());
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can always move instantly on campus", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 20), 20);
        instance.reset();
        instance.setCampus(nurse.getCampus());
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can always move instantly on campus", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 21), 20);
        instance.reset();
        instance.setCampus(nurse.getCampus());
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can always move instantly on campus", instance.isAccepted());
    }

    /**
     * Test of resetValid method, of class UnmovableConstraint.
     */
    @Test
    public void testResetValid() throws ArgumentIsNullException, ArgumentConstraintException, ScheduleConstraintException {
        UnmovableConstraint instance = new UnmovableConstraint(nurse);
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 21), 20);
        instance.setCampus(nurse.getCampus());
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue(instance.isAccepted());
        instance.reset();
        assertEquals(null, instance.isAccepted());
    }

    @Test (expected=ScheduleConstraintException.class)
    public void testWrongCampus() throws ArgumentIsNullException, ArgumentConstraintException, ScheduleConstraintException {
        UnmovableConstraint instance = new UnmovableConstraint(nurse);
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 21), 20);
        instance.setCampus(otherNurse.getCampus());
        instance.setTimeFrame(tf);
        instance.isAccepted();
    }
}
