package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;

/**
 * This creates a fake Constraint that is always accepted.
 */
public class NullConstraint extends TimeFrameConstraint {
    private TimeFrame cached;

    /**
     * Returns if the Constraint is accepted by the last setValid___.
     * @return true, always accept
     */
    public TimeFrame isAccepted() {
        return cached;
    }

    public void reset() {
    }

    public void setTimeFrame(TimeFrame tf) {
        this.cached = tf;
    }
}
