package Hospital.Schedules.Constraints.Implementation;

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
import Hospital.Schedules.TimeFrame;
import Hospital.Treatments.Medication;
import Hospital.Treatments.Treatment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, CannotDoException {
        Treatment t = new Medication("Medication1", Boolean.TRUE, new ItemInfo[0]);
        ScheduleGroup scheduleGroup = new SingleSchedulableGroup(p);
        AppointmentCommand appC = new AppointmentCommand(w, t, Arrays.asList(scheduleGroup), new HighLowPriority(true));
        System.out.println(appC.execute());

        scheduleGroup = new SingleSchedulableGroup(p);
        t = new Medication("Medication2", Boolean.TRUE, new ItemInfo[0]);
        appC = new AppointmentCommand(w, t, Arrays.asList(scheduleGroup), new HighLowPriority(true));
        System.out.println(appC.execute());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setValidNurse method, of class NurseAppointmentBackToBackConstraint.
     */
    @Test
    public void testSetValidNurse() throws ArgumentIsNullException, ArgumentConstraintException {
        NurseAppointmentBackToBackConstraint instance = new NurseAppointmentBackToBackConstraint();
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        instance.reset();
        instance.setNurse(nurse);
        instance.setTimeFrame(tf);
        assertTrue("Time starts at the hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 1), 20);
        instance.reset();
        instance.setNurse(nurse);
        instance.setTimeFrame(tf);
        assertFalse("Time doesn't start at the hour.", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 20), 20);
        instance.reset();
        instance.setNurse(nurse);
        instance.setTimeFrame(tf);
        assertTrue("Time is backtoback with appointment", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 21), 20);
        instance.reset();
        instance.setNurse(nurse);
        instance.setTimeFrame(tf);
        assertFalse("Time is not backtoback with appointment", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 40), 20);
        instance.reset();
        instance.setNurse(nurse);
        instance.setTimeFrame(tf);
        assertTrue("Time is backtoback with appointment", instance.isAccepted());

        tf = new TimeFrame(new Time(2011, 11, 8, 9, 41), 20);
        instance.reset();
        instance.setNurse(nurse);
        instance.setTimeFrame(tf);
        assertFalse("Time is not backtoback with appointment", instance.isAccepted());
    }
}
