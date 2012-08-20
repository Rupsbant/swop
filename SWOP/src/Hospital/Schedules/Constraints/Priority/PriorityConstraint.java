package Hospital.Schedules.Constraints.Priority;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.World.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This constraint checks that all colliding appointments of all attendees have a lower priority and can be preempted
 */
public class PriorityConstraint extends TimeFrameConstraint {

    private Priority priority;
    private Time tf;
    private int length;
    private Set<Schedule> attendeeSchedules = new HashSet<Schedule>();

    /**
     * Creates a new PriorityConstraint with the given priority
     * @param priority the priority this appointment will have
     */
    public PriorityConstraint(Priority priority) {
        this.priority = priority;
    }

    @Override
    public Time isAccepted() {
        if (tf == null) {
            return null;
        }
        for (Schedule schedule : attendeeSchedules) {
            List<Appointment> collidingSchedules = 
                    schedule.getCollidingAppointments(tf, length);
            for (Appointment p : collidingSchedules) {
                if (!priority.thisCanPreempt(p.getPriority())) {
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

    @Override
    public void reset() {
        this.tf = null;
        attendeeSchedules.clear();
    }
}
