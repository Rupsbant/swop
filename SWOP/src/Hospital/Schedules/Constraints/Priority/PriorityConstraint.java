package Hospital.Schedules.Constraints.Priority;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PriorityConstraint extends TimeFrameConstraintImplementation {

    private Priority priority;
    private TimeFrame tf;
    private Set<Schedule> attendeeSchedules = new HashSet<Schedule>();

    public PriorityConstraint(Priority priority) {
        this.priority = priority;
    }

    @Override
    protected void setValidSchedulable(Schedulable s) {
        attendeeSchedules.add(s.getSchedule());
    }

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    protected Boolean isAccepted() {
        if(tf == null){
            return null;
        }
        for(Schedule schedule : attendeeSchedules){
            List<Appointment> collidingSchedules = schedule.getCollidingAppointments(tf);
            for (Appointment p : collidingSchedules) {
                if (!priority.canPreempt(p.getPriority())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void reset() {
        this.tf = null;
        attendeeSchedules.clear();
    }
}
