package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.Schedulable;

public interface HasPreference extends Schedulable {
    public Preference getPreference();
    public HasPreference setPreference(Preference p);
}
