package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class DoctorBackToBackConstraint extends TimeFrameConstraint {

    private Campus campus;
    private Schedule schedule;
    private TimeFrame tf;

    @Override
    public TimeFrame isAccepted() {
        if (schedule == null || tf == null || campus == null) {
            return null;
        }
        //Base output is on the next hour.
        if (tf.getTime().getMinute() == 0) {
            return tf;
        }

        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev != null) {
            int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
            int walkTime = prev.getCampus().getTravelTime(campus);
            if (timeDiff == walkTime) {
                // If the WalkTime is correct, say yes with the current proposed time.
                return tf;
            }
        }
        return tf.next();
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
