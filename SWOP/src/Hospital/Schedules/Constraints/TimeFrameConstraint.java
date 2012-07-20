package Hospital.Schedules.Constraints;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.SchedulableVisitor;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

/**
 * defines a constraint on the allowed times in a schedule
 * multiple constraints can be combined in a linked-list fashion
 */
public abstract class TimeFrameConstraint extends SchedulableVisitor {

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
     * Sets the Campus the appointment is taking place at.
     * @param c, The Campus where the appointment is.
     */
    public void setCampus(Campus c){}

    /**
     * Sets the TimeFrame of the appointment that is being made.
     * @param tf, The TimeFrame during which the appointment happens.
     */
    public abstract void setTimeFrame(TimeFrame tf);
}
