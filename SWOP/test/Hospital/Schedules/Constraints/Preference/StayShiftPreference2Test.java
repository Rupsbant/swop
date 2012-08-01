package Hospital.Schedules.Constraints.Preference;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.Time;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import org.junit.Test;
import static org.junit.Assert.*;

public class StayShiftPreference2Test {

    private World w = BasicWorld.getWorldForTesting();
    private Patient ruben;
    private Patient jeroen;
    private Doctor d;
    private Campus campusNorth;

    public StayShiftPreference2Test() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
    }

    /**
     * Test of canAddAppointment method, of class StayShiftPreference.
     */
    @Test
    public void testCanAddAppointment() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("canAddAppointment");
        TimeFrame tf = new TimeFrame(new Time(2011, 11, 8, 9, 0), 20);
        StayShiftPreference instance = new StayShiftPreference(d);
        boolean result = instance.canAddAppointment(tf, campusNorth);
        assertTrue(result);
    }
}
