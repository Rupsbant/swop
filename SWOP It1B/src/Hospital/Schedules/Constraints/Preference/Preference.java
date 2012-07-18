package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

/**
 * This allows acccess to preferences.
 * Each preference is a prototype for others.
 * @author SWOP-12
 */
public interface Preference {
    //Oplossing gekozen
    boolean canAddAppointment(TimeFrame tf, Campus campus);
    
    void makeThisAsPreference(HasPreference d);

    String getDescription();
}
