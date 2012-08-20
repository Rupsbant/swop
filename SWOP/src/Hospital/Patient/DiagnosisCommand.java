package Hospital.Patient;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Interfaces.Command;
import Hospital.People.Doctor;

/**
 * Creates a diagnosis (with or without second opinion), and undoes that later if necessary
 */
public class DiagnosisCommand implements Command {

    /**
     * the created diagnosis
     */
    private Diagnosis diagnosis;
    /**
     * the patient to which the diagnosis applies
     */
    private Patient toAdd;
    /**
     * the doctor to ask a second opinion from
     */
    private Doctor secondOpinion;
    /**
     * indicates whether this command is already executed
     */
    private boolean done = false;

    /**
     * Constructor: Create a diagnosisCommand with a possible second opinion
     * @param doctor the doctor making the diagnosis
     * @param diagnosis the diagnosis that must be added to the patient
     * @param approvalOf the doctor that must approve the diagnosis, null if no approval is needed
     * @throws NoOpenedPatientFileException the creating doctor has no patient file opened
     * @throws PatientIsDischargedException the patient this diagnosis applies to is not checked in
     * @throws InvalidArgumentException if the given doctor was null
     */
    public DiagnosisCommand(Doctor doctor, Diagnosis diagnosis, Doctor approvalOf)
            throws NoOpenedPatientFileException,
            PatientIsDischargedException,
            InvalidArgumentException {
        secondOpinion = approvalOf;
        if (doctor == null) {
            throw new ArgumentIsNullException("Doctor was null");
        }
        doctor.checkOpenedPatient();
        if (doctor.getOpenedPatient().isDischarged()) {
            throw new PatientIsDischargedException();
        }
        this.diagnosis = diagnosis;
        toAdd = doctor.getOpenedPatient();
    }

    /**
     *  Create a DiagnosisCommand that doesn't need a second opinion
     * @param doctor The doctor that makes the diagnosis
     * @param diagnosis The diagnosis that must be introduced
     * @throws NoOpenedPatientFileException if no patient was opened
     * @throws PatientIsDischargedException if the patient that gets the diagnosis is already discharged
     * @throws InvalidArgumentException if the given doctor was null
     */
    public DiagnosisCommand(Doctor doctor, Diagnosis diagnosis)
            throws NoOpenedPatientFileException, PatientIsDischargedException, InvalidArgumentException {
        this(doctor, diagnosis, null);
    }

    /**
     * Adds the diagnosis to the patient
     * Adds the diagnosis to the list of second opinions of the doctor that needs to approve the diagnosis
     * @return a description of the executed command
     * @throws CannotDoException if something went wrong
     */
    @Override
    public String execute() throws CannotDoException {
        if (done) {
            throw new CannotDoException("Already done!");
        }
        toAdd.addDiagnosis(diagnosis);
        if (secondOpinion != null) {
            secondOpinion.addSecondOpinions((DiagnosisSecondOpinion) diagnosis);
        }
        done = true;
        return diagnosis.toString();
    }

    /**
     * Removes the diagnosis from the patient
     * This cannot be done when a treatment was added.
     * @return a description of the undone command
     * @throws CannotDoException if something went wrong
     */
    @Override
    public String undo() throws CannotDoException {
        if (!done) {
            throw new CannotDoException("Not yet done!");
        }
        if (diagnosis.hasTreatment()) {
            throw new CannotDoException("Diagnosis has treatment, undo this one first");
        }
        if (!toAdd.removeDiagnosis(diagnosis)) {
            throw new CannotDoException("Diagnosis not found!");
        }
        done = false;
        return "Undone:\n" + this.toString();
    }

    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * @return the details of this diagnosis as string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (secondOpinion == null) {
            return diagnosis.toString() + "\nPatient: " + toAdd.toString();
        }
        return diagnosis.toString() + "\nPatient: " + toAdd.toString() + secondOpinion.toString();
    }
}
