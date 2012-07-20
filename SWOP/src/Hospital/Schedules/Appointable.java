package Hospital.Schedules;

import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import java.util.List;

/**
 * An interface for objects that can have an associated appointment (eg. a treatment)
 */
public interface Appointable {

    /**
     * Returns the Appointment for this Appointable.
     * @return Appointment
     */
    public Appointment getAppointment();

    /**
     * Sets the Appointment of this Appointable.
     * @param appointment Appointment to set.
     */
    public void setAppointment(Appointment appointment);

    /**
     * Returns the constraints this Appointable must satisfy for the Patient.
     * @return Normally no constraints must be satisfied.
     */
    public List<TimeFrameConstraint> getConstraints();

    /**
     * Returns the resources of the hospital that are needed for this Appointable.
     * These include normally: a nurse and a machine.
     * @return A list of Groups to search from the world.
     */
    public List<MultiScheduleGroup> getScheduleGroups();

    /**
     * Returns the Decider of the Campus
     * @return A CampusDecider
     */
    public CampusDecider getCampusDecider();

    /**
     * Returns a TimeFrameDelay object that waits the needed time during scheduling based on the current time.
     * @return Returns a new TimeFrameDelay that makes the basic TimeFrame for scheduling
     */
    public TimeFrameDelay getTimeFrameDelay();
}
