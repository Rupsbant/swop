package Hospital.Patient;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.People.Doctor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A diagnosis which requires a second opinion
 */
public class DiagnosisSecondOpinion extends Diagnosis {

    /**
     * the doctor from which to ask the second opinion
     */
    private Doctor secondOpinion;
    /**
     * indicates whether the diagnosis has been approved
     */
    private boolean approved;

    /**
     * Constructor
     * @param diagnoseDetails a string detailing the diagnosis 
     * @param originalDoctor the doctor that made the diagnosis
     * @param secondDoctor the doctor that needs to approve the diagnosis
     * @throws ArgumentIsNullException one of the given doctors was null
     */
    public DiagnosisSecondOpinion(String diagnoseDetails, Doctor originalDoctor, Doctor secondDoctor) throws ArgumentIsNullException {
        super(diagnoseDetails, originalDoctor);
        this.setSecondOpinion(secondDoctor);
        this.setApproved(false);
    }

    /**
     * Sets the doctor that needs to approve the second-opinion
     * @param secondOpinion The doctor that needs to approve the second-opinion.
     * @throws ArgumentIsNullException If the given doctor was null.
     */
    public final void setSecondOpinion(Doctor secondOpinion) throws ArgumentIsNullException {
        if (secondOpinion == null) {
            throw new ArgumentIsNullException("Second-opinionDoctor can't be null");
        }
        this.secondOpinion = secondOpinion;
    }

    /**
     * Returns the secondOpinion Doctor.
     * @return secondOpinion The doctor that needs to approve the second-opinion.
     */
    public final Doctor getSecondOpinion() {
        return secondOpinion;
    }

    /**
     * Returns a description of the Diagnosis
     * @return Description of second-opinion diagnosis
     */
    @Override
    public String toString() {
        return "Diagnose details: " + this.getDiagnoseDetails() + "\n"
                + "Original Doctor: " + this.getOriginalDoctor() + "\n"
                + "Doctor Second Opinion: " + this.getSecondOpinion();
    }

    /**
     * Sets whether this Diagnosis is valid or not.
     * @param valid whether The diagnosis is approved or not.
     * @throws CannotDoException the associated treatment could not be (un)scheduled
     */
    public final String setApproved(Boolean valid) {
        this.approved = valid;
        try {
            return updateState();
        } catch (CannotDoException ex) {
            return "An error occured";
        }
    }

    /**
     * If this diagnosis is approved(true), disapproved(false), or not yet (null).
     * @return The approval rate of this Diagnosis.
     */
    public Boolean isApproved() {
        return approved;
    }

    /**
     * Returns if the treatment can be started: it needs full approval first.
     * @return If the treatment can start.
     */
    @Override
    public Boolean canStartTreatment() {
        return isApproved();
    }
}
