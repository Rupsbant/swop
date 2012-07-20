package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.TimeFrame;

/**
 * This creates a fake Constraint that is always accepted.
 */
public class NullConstraint extends TimeFrameConstraintImplementation {

    /**
     * Returns if the Constraint is accepted by the last setValid___.
     * @return true, always accept
     */
    @Override
    protected Boolean isAccepted() {
        return true;
    }

    @Override
    protected void reset() {
    }

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
    }
}
