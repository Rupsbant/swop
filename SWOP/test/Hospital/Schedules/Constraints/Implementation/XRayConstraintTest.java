package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.Constraints.XRayConstraint.XRayConstraint;
import Hospital.World.Time;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.MedicalTest.XRayScan;
import Hospital.Patient.Patient;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrame;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

public class XRayConstraintTest {

    Patient p;

    @Before
    public void setUp() throws ArgumentConstraintException, ArgumentIsNullException {
        p = new Patient("Ruben");

        XRayScan xray = new XRayScan(3, 4, "Hoofd");
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 15);
        Appointment app = new Appointment(tf, null, null, null);
        xray.setAppointment(app);
        p.addMedicalTest(xray);

        xray = new XRayScan(3, 4, "Schouders");
        tf = new TimeFrame(new Time(2012, 5, 8, 9, 0), 15);
        app = new Appointment(tf, null, null, null);
        xray.setAppointment(app);
        p.addMedicalTest(xray);
    }

    /**
     * Test of setValidPatient method, of class XRayConstraint.
     */
    @Test
    public void testAddEnough() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("AddEnough");
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 9, 9, 0), 15);
        XRayConstraint instance = new XRayConstraint(1);
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can fit another XRay", instance.isAccepted());
    }

    /**
     * Test of setValidPatient method, of class XRayConstraint.
     */
    @Test
    public void testWaitYear() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("waitYear");
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 9, 9, 0), 15);
        XRayConstraint instance = new XRayConstraint(3);
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new TimeFrame(new Time(2012, 11, 8, 9, 0), 15);
        instance.reset();
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new TimeFrame(new Time(2012, 11, 9, 9, 0), 15);
        instance.reset();
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can fit another XRay", instance.isAccepted());
    }

    @Test
    public void testWaitYearHalf() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("waitYearHalf");
        TimeFrame tf = new TimeFrame(new Time(2012, 11, 8, 9, 0), 15);
        XRayConstraint instance = new XRayConstraint(7);
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new TimeFrame(new Time(2012, 11, 9, 9, 0), 15);
        instance.reset();
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new TimeFrame(new Time(2013, 5, 8, 9, 0), 15);
        instance.reset();
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertNotSame(tf, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new TimeFrame(new Time(2013, 5, 9, 9, 0), 15);
        instance.reset();
        instance.setPatient(p);
        instance.setTimeFrame(tf);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can fit another XRay", instance.isAccepted());
    }
}
