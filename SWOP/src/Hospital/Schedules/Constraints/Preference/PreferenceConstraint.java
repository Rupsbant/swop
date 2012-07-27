package Hospital.Schedules.Constraints.Preference;

import Hospital.People.Doctor;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class PreferenceConstraint extends TimeFrameConstraint {

    private Preference preference;
    private Campus campus;
    private TimeFrame tf;

    @Override
    public void setDoctor(Doctor d) {
        this.preference = d.getPreference();
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    public TimeFrame isAccepted() {
        if (preference == null || tf == null || campus == null) {
            return null;
        }
        if (preference.canAddAppointment(tf, campus)) {
            return tf;
        } else {
            return tf.next();
        }
    }

    public void reset() {
        campus = null;
        preference = null;
        tf = null;
    }
}
