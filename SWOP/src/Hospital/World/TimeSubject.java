package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;

/**
 * An interface for subject-objects that can be observed by a TimeObserver
 */
public interface TimeSubject {

    /**
     * Gets the time stored by this subject
     * @return a Time-object
     */
    public Time getTime();

    /**
     * Attaches a new TimeObserver to this subject
     * @param o the observer to attach
     * @throws ArgumentIsNullException the given TimeObserver was null
     */
    public void attachTimeObserver(TimeObserver o)
            throws ArgumentIsNullException;

    /**
     * Detaches a TimeObserver that was attached to this subject
     * @param o the TimeObserver to detach
     */
    public void detachTimeObserver(TimeObserver o);
}
