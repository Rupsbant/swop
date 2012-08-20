package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * Defines the constraint that appointments can only happen between 9:00 and 17:00
 */
public class WorkingHoursTimeConstraint extends TimeFrameConstraint {

    private static final Time dayStart = new Time(0, 0, 0, 9, 0);
    private static final Time dayEnd = new Time(0, 0, 0, 17, 0);
    private Time tf;
    private int length;

    @Override
    public Time isAccepted() {
        if (tf == null) {
            return null;
        }
        Time start = TimeUtils.copyDay(tf.getTime(), dayStart);
        Time end = TimeUtils.copyDay(tf.getTime(), dayEnd);
        if (tf.compareTo(start) < 0) {
            try {
                return start;
            } catch (Exception ex) {
                throw new Error(ex);
            }
        } else if (end.compareTo(tf.getLaterTime(length)) < 0) {
            Time startNextDay = TimeUtils.addDay(start);
            try {
                return startNextDay;
            } catch (Exception ex) {
                throw new Error(ex);
            }
        } else {
            return tf;
        }
    }

    @Override
    public void setTime(Time tf, int length) {
        this.tf = tf;
        this.length = length;
    }

    @Override
    public void reset() {
        tf = null;
    }
}
