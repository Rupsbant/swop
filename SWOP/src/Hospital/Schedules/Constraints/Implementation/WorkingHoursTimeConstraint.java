package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * Defines the constraint on the availability of nurses
 */
public class WorkingHoursTimeConstraint extends TimeFrameConstraint {

    private static final Time dayStart = new Time(0, 0, 0, 9, 0);
    private static final Time dayEnd = new Time(0, 0, 0, 17, 0);
    private TimeFrame tf;

    public TimeFrame isAccepted() {
        if (tf == null) {
            return null;
        }
        Time start = TimeUtils.copyDay(tf.getTime(), dayStart);
        Time end = TimeUtils.copyDay(tf.getTime(), dayEnd);
        if (tf.compareTo(start) < 0) {
            try {
                return new TimeFrame(start, tf.getLength());
            } catch (Exception ex) {
                throw new Error(ex);
            }
        } else if (end.compareTo(tf.getEndTime()) < 0) {
            return tf.next();
        } else {
            return tf;
        }
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    public void reset() {
        tf = null;
    }
}
