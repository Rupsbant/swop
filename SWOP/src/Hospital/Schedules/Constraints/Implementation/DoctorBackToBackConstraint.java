package Hospital.Schedules.Constraints.Implementation;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class DoctorBackToBackConstraint extends TimeFrameConstraint {

    private Campus campus;
    private Schedule schedule;
    private TimeFrame tf;

    public Boolean isAccepted() {
        if (schedule == null || tf == null || campus == null) {
            return null;
        }
        if (tf.getTime().getMinute() == 0) {
            return true;
        }
        Appointment next = schedule.getAppointmentAfter(tf.getEndTime());
        if (next != null) {
            int timeDiff = next.getTimeFrame().getTime().getMinutesDiff(tf.getEndTime());
            int walkTime = next.getCampus().getTravelTime(campus);
            if (timeDiff < walkTime) {
                return false;
            }
        }

        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev == null) {
            return false;
        }
        int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
        int walkTime = prev.getCampus().getTravelTime(campus);
        return timeDiff == walkTime;
    }

    public void reset() {
        tf = null;
        campus = null;
        schedule = null;
    }

    @Override
    public void setDoctor(Doctor d) {
        this.schedule = d.getSchedule();
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }
}
