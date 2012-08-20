package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Time;

/**
 * This creates a fake Constraint that is always accepted.
 */
public class NullConstraint extends TimeFrameConstraint {
    private Time cached;

    /**
     * Returns the last set time
     * @return the time as set by setTime(...)
     */
    @Override
    public Time isAccepted() {
        return cached;
    }

    @Override
    public void reset() {
        cached = null;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.cached = tf;
    }
}
