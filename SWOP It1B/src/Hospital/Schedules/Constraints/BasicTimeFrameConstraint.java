package Hospital.Schedules.Constraints;

public abstract class BasicTimeFrameConstraint extends TimeFrameConstraintImplementation {


    public BasicTimeFrameConstraint(TimeFrameConstraint tfc) {
        super(tfc);
        resetValid();
    }

    public BasicTimeFrameConstraint() {
        this(null);
    }

    ////////////////////
    //Basic Acceptance//
    ////////////////////
    /**
     * Used in determining whether the Constraint is accepted by the last setValidTimeFrame
     */
    private Boolean accepted;

    /**
     * Returns true if the Constraint is accepted by the last setValidTimeFrame.
     * @return true if constraint accepted.
     */
    protected Boolean isAccepted() {
        return accepted;
    }

    /**
     * Marks the constraint as valid
     */
    protected void setValid(boolean b) {
        accepted = b;
    }

    /**
     * Resets this constraint to invalidated
     */
    protected void resetValid() {
        accepted = null;
    }
}
