package Hospital.Schedules;

import Hospital.Exception.Scheduling.ScheduleConstraintException;
import Hospital.World.Campus;
import Hospital.World.Time;

/**
 * defines a constraint on the allowed times in a schedule
 * multiple constraints can be combined in a linked-list fashion
 */
public abstract class TimeFrameConstraint extends SchedulableVisitor {

    /**
     * Check whether the Constraint is accepted. If the TimeFrame is not valid, 
     * the first known valid TimeFrame must be returned, if no TimeFrame can be 
     * predicted, the next minute is returned. 
     * 
     * A ScheduleConstraintException is thrown if there will never be a valid 
     * TimeFrame. Note, if this can't be predicted you may have an infinite 
     * loop.
     * @return the last timeFrame itself, if valid
     *         the first open TimeFrame if invalid and known
     *         the next minute, if invalid and unknown
     *         null if information was missing
     * @throws ScheduleConstraintException when there is never a next valid Constraint
     */
    public abstract Time isAccepted() throws ScheduleConstraintException;

    /**
     * Resets the Constraint.
     */
    public abstract void reset();

    /**
     * Sets the Campus the appointment is taking place at. 
     * @param c The Campus where the appointment is.
     */
    public void setCampus(Campus c){}

    /**
     * Sets the TimeFrame of the appointment that is being made.
     * @param time the time at which the appointment is tried to be scheduled
     * @param length the length of the tested appointment
     */
    public abstract void setTime(Time time, int length);
}
