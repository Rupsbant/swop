package Hospital.Schedules.Constraints.Preference;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.BasicValidationTimeFrameConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class PreferenceConstraint extends BasicValidationTimeFrameConstraint {
    
    public PreferenceConstraint(TimeFrameConstraint tfc) {
        super(tfc);
    }

    public PreferenceConstraint() {
    }

    private Preference preference;
    private Campus campus;
    private TimeFrame tf;
    
    /**
     * (in)validates the constraint based on a given TimeFrame for a Doctor
     * @param tf the TimeFrame-object
     * @param n the Doctor-object
     */
    @Override
    protected void setValidDoctor(TimeFrame tf, Doctor d) {
        preference = d.getPreference();
        this.tf = tf;
    }

    @Override
    protected void setValidPatient(TimeFrame tf, Patient p) {
        campus = p.getCampus();
    }

    @Override
    protected Boolean isAccepted() {
        if(preference == null || tf == null || campus == null){
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
