package Hospital.Schedules;

import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Schedules.GetCampus.PatientDecides;
import Hospital.Schedules.Constraints.Implementation.DoctorBackToBackConstraint;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An appointable object for an appointment between a specified doctor and patient
 */
public class DoctorPatientAppointment implements Appointable {

    /**
     * The priority of DoctorPatientAppointments is low
     */
    public static final Priority PRIORITY = new HighLowPriority(false);
    /**
     * The time to wait from now to schedule the appointment is 60 minutes
     */
    public static final int TIME_TO_WAIT = 60;
    /**
     * A DoctorPatientAppointment is 30 minutes long.
     */
    public static final int APPOINTMEN_LENGTH = 30;
    /**
     * the associated appointment-object
     */
    private Appointment appointment;

    @Override
    public Appointment getAppointment() {
        return appointment;
    }

    @Override
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public List<TimeFrameConstraint> getConstraints() {
        DoctorBackToBackConstraint doctorConstraint = new DoctorBackToBackConstraint();
        List<TimeFrameConstraint> constraints = new ArrayList<TimeFrameConstraint>();
        constraints.add(doctorConstraint);
        return constraints;
    }

    /**
     * The Patient decides where the appointment is, the doctor moves to the patient
     * @return new PatientDecides();
     */
    @Override
    public CampusDecider getCampusDecider() {
        return new PatientDecides();
    }

    /**
     * @return all resources are chosen by the user
     * @see Hospital.Schedules.Appointable#getScheduleGroups()
     */
    @Override
    public List<MultiScheduleGroup> getScheduleGroups() {
        return Collections.emptyList();
    }

    /**
     * @return a TimeFrameDelay object with a delay of 60 minutes and a length of 30
     * @see Hospital.Schedules.Appointable#getTimeFrameDelay()
     */
    @Override
    public DelayedTimeLength getDelayedTimeLength() {
        try {
            return new DelayedTimeLength(TIME_TO_WAIT, APPOINTMEN_LENGTH);
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
