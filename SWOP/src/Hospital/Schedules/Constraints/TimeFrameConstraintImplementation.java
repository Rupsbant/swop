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

    public final TimeFrameConstraint setTimeFrame(TimeFrame tf) {
        setValidTimeFrame(tf);
        if (next != null) {
            next.setTimeFrame(tf);
        }
        return this;
    }

    public final TimeFrameConstraintImplementation setPatient(Patient p) {
        setValidPatient(p);
        if (next != null) {
            next.setPatient(p);
        }
        return this;
    }

    public final TimeFrameConstraintImplementation setStaff(Staff n) {
        setValidStaff(n);
        if (next != null) {
            next.setStaff(n);
        }
        return this;
    }

    public final TimeFrameConstraintImplementation setDoctor(Doctor d) {
        setValidDoctor(d);
        if (next != null) {
            next.setDoctor(d);
        }
        return this;
    }

    public final TimeFrameConstraintImplementation setNurse(Nurse n) {
        setValidNurse(n);
        if (next != null) {
            next.setNurse(n);
        }
        return this;
    }

    public final TimeFrameConstraintImplementation setSchedulable(Schedulable p) {
        setValidSchedulable(p);
        if (next != null) {
            next.setSchedulable(p);
        }
        return this;
    }

    /**
     * Check whether all Constraints in the list are accepted.
     * @return true if valid.
     */
    public final Boolean acceptAll() {
        if (isAccepted() == null) {
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
        reset();
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
    protected abstract void reset();

    /**
     * Sets the TimeFrame during which the appointment happens.
     * Always needs to be implemented.
     * @param tf the TimeFrame of the Appointment
     */
    protected abstract void setValidTimeFrame(TimeFrame tf);

    /**
     * (in)validates the constraint based on a given TimeFrame for a Schedulable
     * @param s the Schedulable-object
     */
    protected void setValidSchedulable(Schedulable s) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Staffmember
     * @param s the Staffmember
     */
    protected void setValidStaff(Staff s) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Patient
     * @param p the Patient-object
     */
    protected void setValidPatient(Patient p) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Doctor
     * @param n the Doctor-object
     */
    protected void setValidDoctor(Doctor d) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Nurse
     * @param n the Nurse-object
     */
    protected void setValidNurse(Nurse n) {
    }
}
