package Hospital.Schedules.Constraints.Implementation;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class DoctorBackToBackConstraint extends TimeFrameConstraint implements GetCampusConstraint {

    private Campus campusThisAppointment;
    private Schedule schedule;
    private TimeFrame tf;

    public Boolean isAccepted() {
        if (schedule == null || tf == null || campusThisAppointment == null) {
            return null;
        }
        if (tf.getTime().getMinute() == 0) {
            return true;
        }
        Appointment next = schedule.getAppointmentAfter(tf.getEndTime());
        if (next != null) {
            int timeDiff = next.getTimeFrame().getTime().getMinutesDiff(tf.getEndTime());
            int walkTime = next.getCampus().getTravelTime(campusThisAppointment);
            if (timeDiff < walkTime) {
                return false;
            }
        }

        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev == null) {
            return false;
        }
        int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
        int walkTime = prev.getCampus().getTravelTime(campusThisAppointment);
        return timeDiff == walkTime;
    }

    public void reset() {
        tf = null;
        campusThisAppointment = null;
        schedule = null;
    }

    @Override
    public void setDoctor(Doctor d) {
        this.schedule = d.getSchedule();
    }

    @Override
    public void setPatient(Patient p) {
        this.campusThisAppointment = p.getCampus();
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    public Campus getCampus() {
        return this.campusThisAppointment;
    }
}
