package Hospital.Patient;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.People.Doctor;
import Hospital.World.World;

public class DiagnosisCreator {

    public final static DiagnosisCreator SINGLETON = new DiagnosisCreator();

    private DiagnosisCreator() {
    }

    public String makeNormalDiagnosis(World world, String content, Doctor doctor) 
            throws NoOpenedPatientFileException, PatientIsDischargedException, InvalidArgumentException {
        Diagnosis newDiagnosis = new Diagnosis(content, doctor);
        DiagnosisCommand diaC = new DiagnosisCommand(world, doctor, newDiagnosis);
        try {
            return doctor.getHistory().addCommand(diaC);
        } catch (CannotDoException ex) {
            throw new Error("Should always be possible");
        }
    }

    public String makeSecondOpinionDiagnosis(World world, String content, Doctor original, Doctor approvalOf) 
            throws NoOpenedPatientFileException, PatientIsDischargedException, InvalidArgumentException {
        DiagnosisSecondOpinion newDiagnosis = new DiagnosisSecondOpinion(content, original, approvalOf);
        DiagnosisCommand diaC = new DiagnosisCommand(world, original, newDiagnosis, approvalOf);
        try {
            return original.getHistory().addCommand(diaC);
        } catch (CannotDoException ex) {
            throw new Error("Should always be possible");
        }
    }
}
