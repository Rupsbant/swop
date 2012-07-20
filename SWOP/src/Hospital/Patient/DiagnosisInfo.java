package Hospital.Patient;

import Hospital.SystemAPI;

/**
 * A reference to a diagnosis.
 */
@SystemAPI
public final class DiagnosisInfo {

    /**
     * the diagnosis to represent
     */
    private Diagnosis original;

    /**
     * Constructor
     * @param original the diagnosis to represent
     */
    public DiagnosisInfo(Diagnosis original) {
        this.original = original;
    }

    /**
     * Returns the Wrapped Diagnosis
     * @return diagnosis
     */
    protected Diagnosis get() {
        return original;
    }

    /**
     * Returns the description of the wrapped Diagnosis
     * @return a detailed string describing the wrapped diagnosis
     */
    @Override
    @SystemAPI
    public String toString() {
        return original.toString();
    }

    /**
     * Checks whether the represented Diagnosis has an associated treatment
     * @return true if the represented Diagnosis was given a treatment.
     */
    @SystemAPI
    public boolean hasTreatment() {
        return original.hasTreatment();
    }
    
    /**
     * Gives back information on the associated treatment.
     * @return information on the associated treatment.
     * @throws NullPointerException if the diagnosis has no treatment associated
     */
    @SystemAPI
    public String treatmentString() throws NullPointerException {
        return original.getTreatment().toString();
    }
}
