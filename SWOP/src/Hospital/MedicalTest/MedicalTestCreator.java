package Hospital.MedicalTest;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.World.World;

/**
 * This objects creates medicalTests and introduces them in a safe way to the world
 * @author Rupsbant
 */
public class MedicalTestCreator {

    /**
     * The singleton object.
     */
    public static final MedicalTestCreator SINGLETON = new MedicalTestCreator();

    private MedicalTestCreator() {
    }

    /**
     * Creates a new BloodAnalysis
     * @param w The world this MedicalTest is created in
     * @param d The doctor that orders this medicalTest
     * @param p The priority to schedule this medicalTest with
     * @param focus the focus of this bloodanalysis
     * @param numberOfAnalyses the number of analyses that must be made
     * @return a description of the created medicalTest
     * @throws ArgumentConstraintException If the number of analyses was negative
     * @throws NoOpenedPatientFileException If no patientFile was opened 
     * @throws InvalidArgumentException If some argument was null or some constraint was not satisfied
     */
    public String makeBloodAnalysis(World w, Doctor d, Priority p, String focus, int numberOfAnalyses) throws ArgumentConstraintException, NoOpenedPatientFileException, InvalidArgumentException {
        MedicalTest med = new BloodAnalysis(focus, numberOfAnalyses);
        return handleCorrectness(w, d, med, p);
    }


    /**
     * Creates a new BloodAnalysis
     * @param w The world this MedicalTest is created in
     * @param d The doctor that orders this medicalTest
     * @param p The priority to schedule this medicalTest with
     * @param focus the focus of this ultrasoundscan
     * @param recordVideo if video must be recorded
     * @param recordImages if images must be taken
     * @return a description of the created medicalTest
     * @throws NoOpenedPatientFileException If no patientFile was opened 
     * @throws InvalidArgumentException If some argument was null or some constraint was not satisfied
     */
    public String makeUltraSoundScan(World w, Doctor d, Priority p, String focus, boolean recordVideo, boolean recordImages) throws InvalidArgumentException, NoOpenedPatientFileException {
        MedicalTest med = new UltraSoundScan(focus, recordVideo, recordImages);
        return handleCorrectness(w, d, med, p);
    }


    /**
     * Creates a new BloodAnalysis
     * @param w The world this MedicalTest is created in
     * @param d The doctor that orders this medicalTest
     * @param p The priority to schedule this medicalTest with
     * @param bodyPart The bodypart that must be XRayed
     * @param zoom The zoom at which the XRay must be made
     * @param numberOfImages  The number of images that must be taken
     * @return a description of the created medicalTest
     * @throws NoOpenedPatientFileException If no patientFile was opened 
     * @throws InvalidArgumentException If some argument was null or some constraint was not satisfied
     */
    public String makeXRayScan(World w, Doctor d, Priority p, String bodyPart, int zoom, int numberOfImages) throws NoOpenedPatientFileException, InvalidArgumentException {
        MedicalTest med = new XRayScan(zoom, numberOfImages, bodyPart);
        return handleCorrectness(w, d, med, p);
    }

    private String handleCorrectness(World world, Doctor doctor, MedicalTest med, Priority p) throws NoOpenedPatientFileException, InvalidArgumentException {
        Patient openedPatient = doctor.getOpenedPatient();
        MedicalTestCommand medCom = new MedicalTestCommand(world, openedPatient, med, p);
        try {
            return doctor.getHistory().addCommand(medCom);
        } catch (CannotDoException ex) {
            throw new Error("New command was not done: " + ex);
        }
    }
}
