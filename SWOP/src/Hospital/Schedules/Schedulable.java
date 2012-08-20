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
     * This makes this object visit the givenSchedulableVisitor object.
     * @param visitor the object that must be visited
     */
    void visitConstraint(SchedulableVisitor visitor);

    /**
     * This returns the constraints this schedulable has for new appointments.
     * These include BackToBackConstraints and PreferenceConstraint.
     *
     * Warning: Always create new list, otherwise not thread safe.
     * @return a list of Constraints
     */
    List<TimeFrameConstraint> getConstraints();
}
