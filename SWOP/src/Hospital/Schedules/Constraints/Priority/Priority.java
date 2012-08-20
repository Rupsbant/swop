package Hospital.Schedules.Constraints.Priority;

/**
 * Defines an interface that all priority schemes must implement
 * @param <E> The type of the own priorityschemen, so that it can compare to itself
 */
public interface Priority<E extends Priority> {

    /**
     * Returns true if this priority can preempt the given priority
     * @param t the priority to test with
     * @return true if this can preempt to given priority
     */
    boolean thisCanPreempt(E t);

}
