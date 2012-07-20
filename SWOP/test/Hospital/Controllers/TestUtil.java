package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Machine.XRayMachine;
import Hospital.Patient.DiagnosisFactory;
import Hospital.Patient.DiagnosisSecondOpinionFactory;
import Hospital.Patient.Patient;
import Hospital.Patient.PatientFactory;
import Hospital.People.Doctor;
import Hospital.People.HospitalAdministrator;
import Hospital.People.Nurse;
import Hospital.People.PeopleFactories.DoctorFactory;
import Hospital.People.PeopleFactories.NurseFactory;
import Hospital.Treatments.MedicationFactory;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tools class for testing
 * @author SWOP-12
 */
public class TestUtil {

    /**
     * This method creates a worldController for testing purposes,
     * This includes some doctors and nurses to test with.
     * @return A worldController with a basic world to start with.
     */
    public static WorldController getWorldControllerForTesting() throws ArgumentIsNullException {
        WorldController out = BasicWorld.getBasicWorld();
        return out;
    }

    public static World getWorldForTesting() throws ArgumentIsNullException {
        World w = BasicWorld.getWorldForTesting();
        return w;
    }
}
