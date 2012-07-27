package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorBackToBackConstraint extends TimeFrameConstraint {

    private Campus campus;
    private Schedule schedule;
    private TimeFrame tf;

    @Override
    public TimeFrame isAccepted() {
        if (schedule == null || tf == null || campus == null) {
            return null;
        }
        if (tf.getTime().getMinute() == 0) {
            return tf;
        }
        Appointment next = schedule.getAppointmentAfter(tf.getEndTime());
        if (next != null) {
            int timeDiff = next.getTimeFrame().getTime().getMinutesDiff(tf.getEndTime());
            int walkTime = next.getCampus().getTravelTime(campus);
            if (timeDiff < walkTime) {
                Time startTime = next.getTimeFrame().getEndTime().getLaterTime(walkTime);
                TimeFrame out;
                try {
                    out = new TimeFrame(startTime, tf.getLength());
                } catch (Exception ex) {
                    throw new Error(ex);
                }
                return out;
            }
        }

        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev == null) {
            return tf.next();
        }
        int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
        int walkTime = prev.getCampus().getTravelTime(campus);
        if(timeDiff == walkTime){
            return tf;
        } else {
            return tf.next();
        }
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
