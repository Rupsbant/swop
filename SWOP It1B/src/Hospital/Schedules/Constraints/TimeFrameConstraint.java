package Hospital.Schedules.Constraints;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;

public interface TimeFrameConstraint {

    /**
     * Check whether all Constraints in the list are accepted.
     * @return true if valid.
     */
    Boolean acceptAll();

    /**
     * Unsets all Constraints.
     */
    void resetAll();

    /**
     * Adds a constraintList to the end of this list.
     * @param toAdd constraintlist to add.
     * @return this, for chaining.
     */
    TimeFrameConstraint addConstraintList(TimeFrameConstraint toAdd);

    /**
     * Tries to accept all Constraints with a TimeFrame and a Doctor.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param d Doctor to check with.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameDoctor(TimeFrame tf, Doctor d);

    /**
     * Tries to accept all Constraints with a TimeFrame and a Doctor.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param n Doctor to check with.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameNurse(TimeFrame tf, Nurse n);

    /**
     * Tries to accept all Constraints with a TimeFrame and a Patient.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param p Patient to check with.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFramePatient(TimeFrame tf, Patient p);

    /**
     * Tries to accept all Constraints with a TimeFrame and a Schedulable.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param p Schedulable to check with.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameSchedulable(TimeFrame tf, Schedulable p);

    /**
     * Tries to accept all Constraints with a TimeFrame and a Staffmember.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param n Staffmember to check with.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameStaff(TimeFrame tf, Staff n);

}
