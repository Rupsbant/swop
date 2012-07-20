package Hospital.Treatments;

import Hospital.Factory.Factory;

/**
 * A dummy inteface for a factory for creating treatments
 * This interface is used in filtering (eg. finding all possible types of treatment in a world)
 */
public interface TreatmentFactory extends Factory<Treatment> {

    /**
     * Returns true if the Treatment needs Medication. I.e. medication, perhaps aftercare of surgery, etc.
     * @return true if the Treatment needs Medication.
     */
    public boolean needsMedication();
}
