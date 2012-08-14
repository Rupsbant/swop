package Hospital.MedicalTest;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.World.World;

public class MedicalTestCreator {

    public static final MedicalTestCreator SINGLETON = new MedicalTestCreator();

    private MedicalTestCreator() {
    }

    public String makeBloodAnalysis(World w, Doctor d, Priority p, String focus, int numberOfAnalyses) throws ArgumentConstraintException, NoOpenedPatientFileException, InvalidArgumentException {
        MedicalTest med = new BloodAnalysis(focus, numberOfAnalyses);
        return handleCorrectness(w, d, med, p);
    }

    public String makeUltraSoundScan(World w, Doctor d, Priority p, String focus, boolean recordVideo, boolean recordImages) throws ArgumentIsNullException, InvalidArgumentException, NoOpenedPatientFileException {
        MedicalTest med = new UltraSoundScan(focus, recordVideo, recordImages);
        return handleCorrectness(w, d, med, p);
    }

    public String makeXRayScan(World w, Doctor d, Priority p, String bodyPart, int zoom, int numberOfImages) throws ArgumentConstraintException, NoOpenedPatientFileException, InvalidArgumentException {
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
