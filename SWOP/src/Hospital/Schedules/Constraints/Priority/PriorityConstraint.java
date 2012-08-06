package Hospital.Schedules.Constraints.Priority;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.World.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PriorityConstraint extends TimeFrameConstraint {

    private Priority priority;
    private Time tf;
    private int length;
    private Set<Schedule> attendeeSchedules = new HashSet<Schedule>();

    public PriorityConstraint(Priority priority) {
        this.priority = priority;
    }

    public Time isAccepted() {
        if (tf == null) {
            return null;
        }
        for (Schedule schedule : attendeeSchedules) {
            List<Appointment> collidingSchedules = 
                    schedule.getCollidingAppointments(tf, length);
            for (Appointment p : collidingSchedules) {
                if (!priority.canPreempt(p.getPriority())) {
                    Time start = p.getEndTime();
                    return start;
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
    public void setTime(Time tf, int length) {
        this.tf = tf;
        this.length = length;
    }

    public void reset() {
        this.tf = null;
        attendeeSchedules.clear();
    }
}
