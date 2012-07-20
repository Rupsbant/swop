package Hospital.Schedules.Constraints.Priority;

public class HighLowPriority implements Priority<HighLowPriority> {

    private boolean high;

    public HighLowPriority(boolean high) {
        this.high = high;
    }

    public boolean isHigh() {
        return high;
    }

    public boolean canPreempt(HighLowPriority t) {
        return this.isHigh() && !t.isHigh();
    }

    public boolean canBePreemptedBy(HighLowPriority t) {
        return t.isHigh() && !this.isHigh();
    }

    @Override
    public String toString() {
        return (isHigh() ? "High priority" : "Low priority");
    }
}
