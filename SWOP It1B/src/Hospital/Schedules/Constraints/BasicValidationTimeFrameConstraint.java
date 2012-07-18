package Hospital.Schedules.Constraints;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;

public abstract class BasicValidationTimeFrameConstraint extends TimeFrameConstraintImplementation {

    public BasicValidationTimeFrameConstraint(TimeFrameConstraint next) {
        super(next);
    }

    public BasicValidationTimeFrameConstraint() {
        super();
    }

    protected void setValidDoctor(TimeFrame tf, Doctor d) {
    }

    protected void setValidNurse(TimeFrame tf, Nurse n) {
    }

    protected void setValidPatient(TimeFrame tf, Patient p) {
    }

    protected void setValidSchedulable(TimeFrame tf, Schedulable s) {
    }

    protected void setValidStaff(TimeFrame tf, Staff n) {
    }
}
