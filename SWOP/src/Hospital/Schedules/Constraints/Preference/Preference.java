package Hospital.Schedules.Constraints.Preference;

import Hospital.World.Campus;
import Hospital.World.Time;

/**
 * This allows acccess to preferences.
 * Each preference is a prototype for others.
 * @author SWOP-12
 */
public interface Preference {
    
    boolean canAddAppointment(Time timeToTest, int length, Campus campus);
    
    void makeThisAsPreference(HasPreference d);

    String getDescription();
}
