package Hospital.Schedules.Constraints;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;

/**
 * defines a constraint on the allowed times in a schedule
 * multiple constraints can be combined in a linked-list fashion
 */
public abstract class TimeFrameConstraint {

    /**
     * Check whether the Constraint is accepted.
     * @return true if valid.
     */
    public abstract Boolean isAccepted();

    /**
     * Resets the Constraint.
     */
    public abstract void reset();

    /**
     * Sets the TimeFrame of the appointment that is being made.
     * @param tf, The TimeFrame during which the appointment happens.
     */
    public abstract void setTimeFrame(TimeFrame tf);

    /**
     * (in)validates the constraint based on a given TimeFrame for a Schedulable
     * @param s the Schedulable-object
     */
    public void setSchedulable(Schedulable s) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Staffmember
     * @param s the Staffmember
     */
    public void setStaff(Staff s) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Patient
     * @param p the Patient-object
     */
    public void setPatient(Patient p) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Doctor
     * @param n the Doctor-object
     */
    public void setDoctor(Doctor d) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Nurse
     * @param n the Nurse-object
     */
    public void setNurse(Nurse n) {
    }
}
