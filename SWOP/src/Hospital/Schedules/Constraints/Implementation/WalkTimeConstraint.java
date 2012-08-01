package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalkTimeConstraint extends TimeFrameConstraint {

    private Campus campus;
    private List<Schedule> schedules = new ArrayList<Schedule>();
    private TimeFrame tf;

    @Override
    public TimeFrame isAccepted() {
        if (tf == null || campus == null) {
            return null;
        }
        Time out = tf.getTime();
        for (Schedule schedule : schedules) {
            Time out2 = handleNext(schedule);
            out = (out.compareTo(out2) > 0 ? out : out2);
            out2 = handlePrevious(schedule);
            out = (out.compareTo(out2) > 0 ? out : out2);
        }
        try {
            return new TimeFrame(out, tf.getLength());
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private Time handlePrevious(Schedule schedule) {
        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev != null) {
            int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
            int walkTime = prev.getCampus().getTravelTime(campus);
            if (timeDiff < walkTime) {
                //if not enough time, add more
                return prev.getTimeFrame().getEndTime().getLaterTime(walkTime);
            }
        }
        return tf.getTime();
    }

    private Time handleNext(Schedule schedule) {
        Appointment next = schedule.getAppointmentAfter(tf.getTime());
        if (next != null) {
            int timeDiff = next.getTimeFrame().getTime().getMinutesDiff(tf.getEndTime());
            int walkTime = next.getCampus().getTravelTime(campus);
            if (timeDiff < walkTime) {
                // if not enough, start after the next appointment
                return next.getTimeFrame().getEndTime().getLaterTime(walkTime);
            }
        }
        return tf.getTime();
    }

    @Override
    public void setSchedulable(Schedulable s) {
        schedules.add(s.getSchedule());
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    public void reset() {
        this.tf = null;
        this.campus = null;
        this.schedules.clear();
    }
}
