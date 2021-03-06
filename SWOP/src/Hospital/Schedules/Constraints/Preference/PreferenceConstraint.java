package Hospital.Schedules.Constraints.Preference;

import Hospital.People.Doctor;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import Hospital.World.Time;

/**
 * Checks with the preference of the doctors if the appointment can be scheduled at the given campus
 */
public class PreferenceConstraint extends TimeFrameConstraint {

    private Preference preference;
    private Campus campus;
    private Time tf;
    private int length;

    @Override
    public void setDoctor(Doctor d) {
        this.preference = d.getPreference();
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.tf = tf;
        this.length = length;
    }

    @Override
    public Time isAccepted() {
        if (preference == null || tf == null || campus == null) {
            return null;
        }
        if (preference.canAddAppointment(tf, length, campus)) {
            return tf;
        } else {
            return tf.getLaterTime(1);
        }
    }

    @Override
    public void reset() {
        campus = null;
        preference = null;
        tf = null;
    }
}
