package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;

/**
 * This creates a fake Constraint that is always accepted.
 */
public class NullConstraint extends TimeFrameConstraint {

    /**
     * Returns if the Constraint is accepted by the last setValid___.
     * @return true, always accept
     */
    public Boolean isAccepted() {
        return true;
    }

    public void reset() {
    }

    public void setTimeFrame(TimeFrame tf) {
    }
}
