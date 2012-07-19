package Hospital.Schedules;

import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Schedules.Constraints.Implementation.DoctorBackToBackConstraint;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import java.util.Collections;
import java.util.List;

/**
 * An appointable object for an appointment between a specified doctor and patient
 */
public class DoctorPatientAppointment implements Appointable {

    public static final Priority PRIORITY = new HighLowPriority(false);
    /**
     * the associated appointment-object
     */
    private Appointment appointment;

    /**
     * @see Hospital.Schedules.Appointable#getAppointment()
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * @see Hospital.Schedules.Appointable#setAppointment(Hospital.Schedules.Appointment)
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @see Hospital.Schedules.Appointable#getConstraints()
     */
    public TimeFrameConstraint getConstraints() {
        return new DoctorBackToBackConstraint();
    }

    /**
     * @return no resources are needed for a DoctorPatientAppointment
     * @see Hospital.Schedules.Appointable#getScheduleGroups()
     */
    public List<MultiScheduleGroup> getScheduleGroups() {
        return Collections.emptyList();
    }

    /**
     * @return a TimeFrameDelay object with a delay of 60 minutes and a length of 30
     * @see Hospital.Schedules.Appointable#getTimeFrameDelay()
     */
    public TimeFrameDelay getTimeFrameDelay() {
        try {
            return new TimeFrameDelay(60, 30);
        } catch (ArgumentConstraintException ex) {
            throw new Error("30 was illegal appointmentLength, check code");
        }
    }

    /**
     * Since this is just a doctor-patient appointment, no items are needed
     * @return an empty array
     * @see Hospital.Schedules.Appointable#getNeededItems()
     */
    public String[] getNeededItems() {
        return new String[0];
    }
}
