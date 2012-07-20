package Hospital.World;

/**
 * An interface for an observer that gets notified when the time changes
 */
public interface TimeObserver {

    /**
     * This gets called if the Time is updated
     * @param newTime the new time to which is updated
     */
    public void timeUpdate(Time newTime);
}
