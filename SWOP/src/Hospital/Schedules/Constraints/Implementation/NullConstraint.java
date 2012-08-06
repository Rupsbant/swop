package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Time;

/**
 * This creates a fake Constraint that is always accepted.
 */
public class NullConstraint extends TimeFrameConstraint {
    private Time cached;

    /**
     * Returns if the Constraint is accepted by the last setValid___.
     * @return true, always accept
     */
    public Time isAccepted() {
        return cached;
    }

    public void reset() {
    }

    public void setTime(Time tf, int length) {
        this.cached = tf;
    }
}
