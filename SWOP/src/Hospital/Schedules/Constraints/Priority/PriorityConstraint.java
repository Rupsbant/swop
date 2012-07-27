package Hospital.Schedules.Constraints.Priority;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PriorityConstraint extends TimeFrameConstraint {

    private Priority priority;
    private TimeFrame tf;
    private Set<Schedule> attendeeSchedules = new HashSet<Schedule>();

    public PriorityConstraint(Priority priority) {
        this.priority = priority;
    }

    public TimeFrame isAccepted() {
        if (tf == null) {
            return null;
        }
        for (Schedule schedule : attendeeSchedules) {
            List<Appointment> collidingSchedules = schedule.getCollidingAppointments(tf);
            for (Appointment p : collidingSchedules) {
                if (!priority.canPreempt(p.getPriority())) {
                    Time start = p.getTimeFrame().getEndTime();
                    TimeFrame out;
                    try {
                        out = new TimeFrame(start, tf.getLength());
                    } catch (Exception ex) {
                        throw new Error("This cannot happen");
                    }
                    return out;
                }
            }
        }
        return tf;
    }

    @Override
    public void setSchedulable(Schedulable s) {
        attendeeSchedules.add(s.getSchedule());
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    public void reset() {
        this.tf = null;
        attendeeSchedules.clear();
    }
}
