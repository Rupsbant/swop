package Hospital.Schedules.Constraints.Priority;

/**
 * 
 * Defines a priorityscheme with to prioritylevels: high and low
 * Only high can preempt low.
 */
public class HighLowPriority implements Priority<HighLowPriority> {

    private boolean isHigh;

    /**
     * Creates a priority that is high or low
     * @param isHigh true if high, otherwise low
     */
    public HighLowPriority(boolean isHigh) {
        this.isHigh = isHigh;
    }

    /**
     * returns if this priority is high or low
     * @return true if high
     */
    public boolean isHigh() {
        return isHigh;
    }

    @Override
    public boolean thisCanPreempt(HighLowPriority t) {
        return this.isHigh() && !t.isHigh();
    }

    @Override
    public String toString() {
        return (isHigh() ? "High priority" : "Low priority");
    }
}
