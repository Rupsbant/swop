package Hospital.Schedules;

import java.util.List;

/**
 * An interface for objects that can be scheduled, in other words, that can attend an appointment
 */
public interface Schedulable {

    /**
     * Returns this Schedulable's schedule
     * @return schedule 
     */
    Schedule getSchedule();

    /**
     * This makes this object visit the TimeFrameConstraints during scheduling.
     * @param tfContstraints The constraints that must be satisfied.
     * @return The constraints for simpler code : schedulable.setValidTimeFrame(tf, tfc).isAccepted();.
     */
    void visitConstraint(SchedulableVisitor tfContstraints);

    /**
     * This returns the constraints this schedulable has for new appointments.
     * These include BackToBackConstraints and PreferenceConstraint.
     *
     * Warning: Always create new list, otherwise not thread safe.
     */
    List<TimeFrameConstraint> getConstraints();
}
