package Hospital.Schedules.Constraints.Preference;

import Hospital.World.Campus;
import Hospital.World.Time;

/**
 * This allows acccess to preferences.
 * Each preference is a prototype for others.
 * @author SWOP-12
 */
public interface Preference {
    
    /**
     * checks if the appointment can be made according to the preference
     * @param timeToTest the time at which the tested appointment starts
     * @param length the length of the tested appointment
     * @param campus the campus where the appointment takes place
     * @return true if the appointment can be made
     */
    boolean canAddAppointment(Time timeToTest, int length, Campus campus);
    
    /**
     * sets the preference of d to this class and sets the schedulable to check the preference with to d
     * @param d the object that has a preference
     */
    void makeThisAsPreference(HasPreference d);

    /**
     * returns a description of this preference
     * @return a human readable description
     */
    String getDescription();
}
