package Hospital.Schedules.Constraints.Preference;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class PreferenceConstraint extends TimeFrameConstraintImplementation {

    public PreferenceConstraint(TimeFrameConstraint tfc) {
        super(tfc);
    }

    public PreferenceConstraint() {
    }
    private Preference preference;
    private Campus campus;
    private TimeFrame tf;

    @Override
    protected void setValidDoctor(Doctor d) {
        this.preference = d.getPreference();
    }

    @Override
    protected void setValidPatient(Patient p) {
        this.campus = p.getCampus();
    }

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    protected Boolean isAccepted() {
        if (preference == null || tf == null || campus == null) {
            return null;
        }
        return preference.canAddAppointment(tf, campus);
    }

    @Override
    protected void resetValid() {
        campus = null;
        preference = null;
        tf = null;
    }
}
