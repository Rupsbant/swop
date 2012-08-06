package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Scheduling.ScheduleConstraintException;
import Hospital.People.Unmovable;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import Hospital.World.Time;

public class UnmovableConstraint extends TimeFrameConstraint {

    private Unmovable unmovable;
    private Campus campus;
    private Time startTime;

    public UnmovableConstraint(Unmovable unmovable) {
        this.unmovable = unmovable;
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    public Time isAccepted() throws ScheduleConstraintException {
        if (campus == null) {
            return null;
        }
        if (!unmovable.getCampus().equals(campus)) {
            throw new ScheduleConstraintException();
        }
        return startTime;
    }

    public void reset() {
        campus = null;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.startTime = tf;
    }
}
