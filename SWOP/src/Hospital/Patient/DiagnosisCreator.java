package Hospital.Patient;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.People.Doctor;
import Hospital.World.World;

/**
 * This class creates Diagnoses and introduces them in a safe way to the world
 * @author Rupsbant
 */
public class DiagnosisCreator {

    /**
     * The singleton
     */
    public final static DiagnosisCreator SINGLETON = new DiagnosisCreator();

    private DiagnosisCreator() {
    }

    /**
     * Creates a normal diagnosis
     * @param content the content of the diagnosis
     * @param doctor the doctor that enters the diagnosis
     * @return a description of the diagnosis
     * @throws NoOpenedPatientFileException
     * @throws PatientIsDischargedException
     * @throws InvalidArgumentException if doctor was null
     */
    public String makeNormalDiagnosis(String content, Doctor doctor) 
            throws NoOpenedPatientFileException, PatientIsDischargedException, InvalidArgumentException {
        Diagnosis newDiagnosis = new Diagnosis(content, doctor);
        DiagnosisCommand diaC = new DiagnosisCommand(doctor, newDiagnosis);
        try {
            return doctor.getHistory().addCommand(diaC);
        } catch (CannotDoException ex) {
            throw new Error("Should always be possible");
        }
    }

    /**
     * Creates a diagnosis that needs approval.
     * @param content
     * @param original The original doctor
     * @param approvalOf The doctor that needs to give approval
     * @return a description of the created diagnosis
     * @throws NoOpenedPatientFileException
     * @throws PatientIsDischargedException
     * @throws InvalidArgumentException if doctor or approvalOf were null.
     */
    public String makeSecondOpinionDiagnosis(String content, Doctor original, Doctor approvalOf) 
            throws NoOpenedPatientFileException, PatientIsDischargedException, InvalidArgumentException {
        DiagnosisSecondOpinion newDiagnosis = new DiagnosisSecondOpinion(content, original, approvalOf);
        DiagnosisCommand diaC = new DiagnosisCommand(original, newDiagnosis, approvalOf);
        try {
            return original.getHistory().addCommand(diaC);
        } catch (CannotDoException ex) {
            throw new Error("Should always be possible");
        }
    }
}
