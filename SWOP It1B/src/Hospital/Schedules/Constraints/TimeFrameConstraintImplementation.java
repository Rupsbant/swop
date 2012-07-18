package Hospital.Schedules.Constraints;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;

/**
 * defines a constraint on the allowed times in a schedule
 * multiple constraints can be combined in a linked-list fashion
 */
public abstract class TimeFrameConstraintImplementation implements TimeFrameConstraint {

    /**
     * link to the next constraint in a combined constraint
     */
    private TimeFrameConstraint next;

    /**
     * Constructor
     * @param next link to the next constraint for combination
     */
    public TimeFrameConstraintImplementation(TimeFrameConstraint next) {
        this.next = next;
    }

    /**
     * Constructor
     */
    public TimeFrameConstraintImplementation() {
        this(null);
    }

    /**
     * Adds a constraintList to the end of this list.
     * @param toAdd constraintlist to add.
     * @return this, for chaining.
     */
    public final TimeFrameConstraint addConstraintList(TimeFrameConstraint toAdd) {
        if (next == null) {
            this.next = toAdd;
        } else {
            this.next.addConstraintList(toAdd);
        }
        return this;
    }

    /**
     * Tries to accept all Constraints with a TimeFrame and a Patient.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param p Patient to check with.
     * @return this, for chaining.
     */
    public final TimeFrameConstraintImplementation setValidTimeFramePatient(TimeFrame tf, Patient p) {
        setValidPatient(tf, p);
        if (next != null) {
            next.setValidTimeFramePatient(tf, p);
        }
        return this;
    }

    /**
     * Tries to accept all Constraints with a TimeFrame and a Staffmember.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param n Staffmember to check with.
     * @return this, for chaining.
     */
    public final TimeFrameConstraintImplementation setValidTimeFrameStaff(TimeFrame tf, Staff n) {
        setValidStaff(tf, n);
        if (next != null) {
            next.setValidTimeFrameStaff(tf, n);
        }
        return this;
    }

    /**
     * Tries to accept all Constraints with a TimeFrame and a Doctor.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param d Doctor to check with.
     * @return this, for chaining.
     */
    public final TimeFrameConstraintImplementation setValidTimeFrameDoctor(TimeFrame tf, Doctor d) {
        setValidDoctor(tf, d);
        if (next != null) {
            next.setValidTimeFrameDoctor(tf, d);
        }
        return this;
    }

    /**
     * Tries to accept all Constraints with a TimeFrame and a Doctor.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param n Doctor to check with.
     * @return this, for chaining.
     */
    public final TimeFrameConstraintImplementation setValidTimeFrameNurse(TimeFrame tf, Nurse n) {
        setValidNurse(tf, n);
        if (next != null) {
            next.setValidTimeFrameNurse(tf, n);
        }
        return this;
    }

    /**
     * Tries to accept all Constraints with a TimeFrame and a Schedulable.
     * @param tf TimeFrame during which the constraint must be valid.
     * @param p Schedulable to check with.
     * @return this, for chaining.
     */
    public final TimeFrameConstraintImplementation setValidTimeFrameSchedulable(TimeFrame tf, Schedulable p) {
        setValidSchedulable(tf, p);
        if (next != null) {
            next.setValidTimeFrameSchedulable(tf, p);
        }
        return this;
    }

    /**
     * Check whether all Constraints in the list are accepted.
     * @return true if valid.
     */
    public final Boolean acceptAll() {
        if (isAccepted() == null) {
            //System.out.println("Unknown:" + this);
            return null;
        }
        if (!isAccepted()) {
            return false;
        }
        if (next == null) {
            return true;
        }
        return next.acceptAll();
    }

    /**
     * Unsets all Constraints.
     */
    public final void resetAll() {
        resetValid();
        if (next != null) {
            next.resetAll();
        }
    }

    ////////////////////
    //Basic Acceptance//
    ////////////////////
    /**
     * Returns true if the Constraint is accepted by the last setValidTimeFrame.
     * @return true if constraint accepted.
     */
    protected abstract Boolean isAccepted();

    /**
     * Resets this constraint to the default value.
     */
    protected abstract void resetValid();

    /**
     * (in)validates the constraint based on a given TimeFrame for a Schedulable
     * @param tf the TimeFrame-object
     * @param s the Schedulable-object
     */
    protected abstract void setValidSchedulable(TimeFrame tf, Schedulable s);

    /**
     * (in)validates the constraint based on a given TimeFrame for a Staffmember
     * @param tf the TimeFrame-object
     * @param n the Staffmember
     */
    protected abstract void setValidStaff(TimeFrame tf, Staff n);

    /**
     * (in)validates the constraint based on a given TimeFrame for a Patient
     * @param tf the TimeFrame-object
     * @param p the Patient-object
     */
    protected abstract void setValidPatient(TimeFrame tf, Patient p);

    /**
     * (in)validates the constraint based on a given TimeFrame for a Doctor
     * @param tf the TimeFrame-object
     * @param n the Doctor-object
     */
    protected abstract void setValidDoctor(TimeFrame tf, Doctor d);

    /**
     * (in)validates the constraint based on a given TimeFrame for a Nurse
     * @param tf the TimeFrame-object
     * @param n the Nurse-object
     */
    protected abstract void setValidNurse(TimeFrame tf, Nurse n);
}
