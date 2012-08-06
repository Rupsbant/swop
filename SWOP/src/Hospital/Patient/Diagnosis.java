package Hospital.Patient;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.WrongTreatmentException;
import Hospital.People.Doctor;
import Hospital.Treatments.Treatment;

/**
 * Holds the diagnosis for a patient
 */
public class Diagnosis {

    /**
     * The details of the diagnosis
     */
    private String diagnoseDetails;
    /**
     * The doctor that made the diagnosis
     */
    private Doctor originalDoctor;
    /**
     * The treatment to cure the diagnosed problem
     */
    private Treatment treatment;

    /**
     * Constructor
     * @param diagnoseDetails a string detailing the diagnosis
     * @param originalDoctor the doctor that made the diagnosis
     * @throws ArgumentIsNullException the given doctor was null
     */
    public Diagnosis(String diagnoseDetails, Doctor originalDoctor) throws ArgumentIsNullException {
        this.setOriginalDoctor(originalDoctor);
        this.setDiagnoseDetails(diagnoseDetails);
    }

    /**
     * Returns the original doctor of this diagnosis.
     * @return the original doctor
     */
    public Doctor getOriginalDoctor() {
        return originalDoctor;
    }

    /**
     * sets the doctor that made the diagnosis
     * @param originalDoctor the doctor
     * @throws ArgumentIsNullException the given doctor was null
     */
    private void setOriginalDoctor(Doctor originalDoctor) throws ArgumentIsNullException {
        if (originalDoctor == null) {
            throw new ArgumentIsNullException("Doctor was null");
        }
        this.originalDoctor = originalDoctor;
    }

    /**
     * Sets the details of the diagnosis
     * @param diagnoseDetails a string detailing the diagnosis
     */
    private void setDiagnoseDetails(String diagnoseDetails) {
        this.diagnoseDetails = diagnoseDetails;
    }

    /**
     * Returns the details of the Diagnosis
     * @return The diagnosis details.
     */
    public String getDiagnoseDetails() {
        return diagnoseDetails;
    }

    /**
     * Returns a description of this diagnosis.
     * @return "Diagnose details: $diagnoseDetails <br> Doctor: $originalDoctor"
     */
    @Override
    public String toString() {
        return "Diagnose details: " + this.getDiagnoseDetails() + "\n"
                + "Doctor: " + this.getOriginalDoctor();
    }

    /**
     * Returns true if the diagnosis is fully approved.
     * @return true if the diagnosis is fully approved. 
     */
    public Boolean canStartTreatment() {
        return treatment != null;
    }

    /**
     * Sets the treatment for this diagnosis.
     * @param treatment The treatment to set.
     * @throws ArgumentIsNullException If the treatment was null.
     * @throws CannotChangeException If the treatment was already set.
     * @throws CannotDoException the associated treatment was already scheduled
     */
    public String setTreatment(Treatment treatment)
            throws ArgumentIsNullException,
            CannotChangeException,
            CannotDoException {
        if (treatment == null) {
            throw new ArgumentIsNullException("Can't set null treatment, use removeTreatment.");
        }
        if (this.treatment != null) {
            throw new CannotChangeException("A treatment was already set, remove that one first.");
        }
        this.treatment = treatment;
        return updateState();
    }

    /**
     * (Un)Schedules the treatment depending on whether the treatment can be started
     * @throws CannotDoException the treatment could not be (un)scheduled
     */
    protected String updateState() throws CannotDoException {
        if (canStartTreatment()) {
            return this.treatment.schedule();
        } else if (this.treatment != null) {
            return this.treatment.unSchedule();
        } else {
            return "";
        }
    }

    /**
     * Removes the treatment. The treatment to remove must be known.
     * @param t The treatment to try to remove.
     * @throws WrongTreatmentException If the Treatment that tried to be removed was not set in this diagnosis.
     * @throws CannotDoException the treatment was not yet scheduled
     */
    public String removeTreatment(Treatment t)
            throws WrongTreatmentException, CannotDoException {
        if (this.treatment == null) {
            throw new CannotDoException("No treatment was associated, cannot remove");
        }
        if (!this.treatment.equals(t)) {
            throw new WrongTreatmentException("Can't remove another treatement!");
        }
        this.treatment.unSchedule();
        this.treatment = null;
        return updateState();
    }

    /**
     * Return true if this Diagnosis has an associated Treatment.
     * @return true if this Diagnosis has an associated Treatment.
     */
    public boolean hasTreatment() {
        return treatment != null;
    }

    /**
     * Returns a public wrapperObject of this diagnosis.
     * @return A new DiagnosisInfo that wraps this diagnosis.
     */
    DiagnosisInfo getDiagnosisInfo() {
        return new DiagnosisInfo(this);
    }

    /**
     * Returns a description with all information of this diagnosis.
     * @return The standard description with the description of the treatment.
     */
    public String advancedString() {
        return toString() + "\n" + (hasTreatment() ? getTreatment().toString() : "Untreated.");
    }

    /**
     * Returns the associated Treatment.
     * @return Treatment set by setTreatment();
     */
    public Treatment getTreatment() {
        return treatment;
    }
}
