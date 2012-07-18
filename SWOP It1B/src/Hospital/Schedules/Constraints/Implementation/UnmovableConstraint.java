package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Unmovable;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.BasicTimeFrameConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;

public class UnmovableConstraint extends BasicTimeFrameConstraint {

    private Unmovable unmovable;

    public UnmovableConstraint(TimeFrameConstraint next, Unmovable unmovable) {
        super(next);
        this.unmovable = unmovable;
    }

    public UnmovableConstraint(Unmovable unmovable) {
        this(null, unmovable);
    }

    @Override
    protected void setValidSchedulable(TimeFrame tf, Schedulable s) {
        Schedule schedule = s.getSchedule();

        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev != null) {
            int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
            int walkTime = prev.getCampus().getTravelTime(unmovable.getCampus());
            if (timeDiff < walkTime) {
                setValid(false);
            }
        }
        Appointment next = schedule.getAppointmentAfter(tf.getEndTime());
        if (next != null) {
            int timeDiff = next.getTimeFrame().getTime().getMinutesDiff(tf.getEndTime());
            int walkTime = next.getCampus().getTravelTime(unmovable.getCampus());
            if (timeDiff < walkTime) {
                setValid(false);
            }
        }
    }

    @Override
    protected void resetValid() {
        super.resetValid();
        super.setValid(true);
    }
}
