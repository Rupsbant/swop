package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.Schedulable;

/**
 * This interface enables a schedulable to have a preference to choose how to move between campusses
 * @author Rupsbant
 */
public interface HasPreference extends Schedulable {
    /**
     * Returns the preference of this HasPreference object
     * @return the preference
     */
    public Preference getPreference();
    /**
     * Sets the preference of this object
     * @param p the preferene to set
     */
    public void setPreference(Preference p);
}
