package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Staff;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * Defines the constraint on the availability of nurses
 */
public class WorkingHoursTimeConstraint extends TimeFrameConstraint {

    private static final Time dayStart = new Time(0, 0, 0, 9, 0);
    private static final Time dayEnd = new Time(0, 0, 0, 17, 0);
    private Staff n;
    private TimeFrame tf;

    //TODO: Test this!!!!
    public Boolean isAccepted() {
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
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    public void setStaff(Staff n) {
        this.n = n;
    }

    public void reset() {
        n = null;
        tf = null;
    }
}
