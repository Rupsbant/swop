package Hospital.Schedules.Constraints.Priority;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.BasicTimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import java.util.List;

public class PriorityConstraint extends BasicTimeFrameConstraint {

    Priority priority;

    public PriorityConstraint(Priority priority) {
        this.priority = priority;
        super.setValid(true);
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Schedulable
     * @param tf the TimeFrame-object
     * @param s the Schedulable-object
     */
    @Override
    protected void setValidSchedulable(TimeFrame tf, Schedulable s) {
        if (isAccepted() != null && !isAccepted()) {
            return;
        }
        super.setValid(true);
        Schedule sched = s.getSchedule();
        List<Appointment> collidingSchedules = sched.getCollidingAppointments(tf);
        for (Appointment p : collidingSchedules) {
            //super.setValid(false);
            if (!priority.canPreempt(p.getPriority())) {
                super.setValid(false);
                break;
            }
        }
    }
}
