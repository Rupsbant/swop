package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.MedicalTest.XRayScan;
import Hospital.Patient.Patient;
import Hospital.Schedules.Appointment;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

public class XRayConstraintTest {

    private Patient p;

    @Before
    public void setUp() throws ArgumentConstraintException, ArgumentIsNullException {
        p = new Patient("Ruben");
        final int numberOfXRays = 4;

        XRayScan xray = new XRayScan(3, numberOfXRays, "Hoofd");
        Time tf = new Time(2011, 11, 8, 9, 0);
        Appointment app = new Appointment(tf, 20, null, null, null);
        xray.setAppointment(app);
        p.addMedicalTest(xray);

        xray = new XRayScan(3, numberOfXRays, "Schouders");
        tf = new Time(2012, 5, 8, 9, 0);
        app = new Appointment(tf, 20, null, null, null);
        xray.setAppointment(app);
        p.addMedicalTest(xray);
    }

    /**
     * Test of setValidPatient method, of class XRayConstraint.
     */
    @Test
    public void testAddEnough() throws ArgumentIsNullException, ArgumentConstraintException {
        Time tf = new Time(2011, 11, 9, 9, 0);
        XRayConstraint instance = new XRayConstraint(1);
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can fit another XRay", instance.isAccepted());
    }

    /**
     * Test of setValidPatient method, of class XRayConstraint.
     */
    @Test
    public void testWaitYear() throws ArgumentIsNullException, ArgumentConstraintException {
        Time tf = new Time(2011, 11, 9, 9, 0);
        Time firstGood = new Time(2012, 11, 9, 0, 0);
        XRayConstraint instance = new XRayConstraint(3);
        instance.setPatient(p);
        instance.setTime(tf, 15);
        Time accepted = instance.isAccepted();
        assertNotSame(tf, accepted);
        assertEquals(firstGood, accepted);
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new Time(2012, 11, 9, 8, 59);
        instance.reset();
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(firstGood, accepted);
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new Time(2012, 11, 9, 9, 0);
        instance.reset();
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can fit another XRay", instance.isAccepted());
    }

    @Test
    public void testWaitYearHalf() throws ArgumentIsNullException, ArgumentConstraintException {
        Time tf = new Time(2012, 11, 8, 9, 0);
        Time firstGood = new Time(2013, 5, 9, 0, 0);
        XRayConstraint instance = new XRayConstraint(7);
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(firstGood, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new Time(2012, 11, 9, 9, 0);
        instance.reset();
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(firstGood, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new Time(2013, 5, 8, 9, 0);
        instance.reset();
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertNotSame(tf, instance.isAccepted());
        assertEquals(firstGood, instance.isAccepted());
        //assertFalse("Can't fit another XRay", instance.isAccepted());

        tf = new Time(2013, 5, 9, 9, 0);
        instance.reset();
        instance.setPatient(p);
        instance.setTime(tf, 15);
        assertEquals(tf, instance.isAccepted());
        //assertTrue("Can fit another XRay", instance.isAccepted());
    }
}
