package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Staff;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * Defines the constraint on the availability of nurses
 */
public class WorkingHoursTimeConstraint extends TimeFrameConstraintImplementation {

    private static final Time dayStart = new Time(0, 0, 0, 9, 0);
    private static final Time dayEnd = new Time(0, 0, 0, 17, 0);
    private Staff n;
    private TimeFrame tf;

    /**
     * Constructor
     */
    public WorkingHoursTimeConstraint() {
    }

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    /**
     * @see Hospital.Schedules.Constraints.TimeFrameConstraint#setValidNurse(Hospital.Schedules.TimeFrame, Hospital.People.Nurse)
     */
    @Override
    protected void setValidStaff(Staff n) {
        this.n = n;
    }

    //TODO: Test this!!!!
    @Override
    protected Boolean isAccepted() {
        if (tf == null || n == null) {
            return null;
        }
        Time start = TimeUtils.copyDay(tf.getTime(), dayStart);
        Time end = TimeUtils.copyDay(tf.getTime(), dayEnd);
        if (tf.compareTo(start) < 0) {
            return false;
        } else if (end.compareTo(tf.getEndTime()) < 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void reset() {
        n = null;
        tf = null;
    }
}
