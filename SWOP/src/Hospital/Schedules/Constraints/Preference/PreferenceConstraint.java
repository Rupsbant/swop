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

    public Boolean isAccepted() {
        if (preference == null || tf == null || campus == null) {
            System.out.println(preference+""+tf+campus);
            return null;
        }
        return preference.canAddAppointment(tf, campus);
    }

    public void reset() {
        campus = null;
        preference = null;
        tf = null;
    }
}
