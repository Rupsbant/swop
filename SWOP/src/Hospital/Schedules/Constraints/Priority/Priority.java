package Hospital.Schedules.Constraints.Priority;

public interface Priority<E extends Priority> {

    boolean canPreempt(E t);

    boolean canBePreemptedBy(E t);

}
