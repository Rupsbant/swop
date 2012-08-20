package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.InvalidTimeException;
import java.util.ArrayList;

/**
 * A composite of world. Handles time and it's observers
 */
public class WorldTime implements TimeSubject {

    /**
     * the current time of the world
     */
    private Time time;
    /**
     * a list of all TimeObservers that should be notified by the world when time changes
     */
    private ArrayList<TimeObserver> timeObservers;

    /**
     * Creates a new WorldTime with the default time
     */
    public WorldTime() {
        timeObservers = new ArrayList<TimeObserver>();
        time = new Time();
        notifyTimeObservers();
    }

    /**
     * @see Hospital.World.TimeSubject#getTime()
     */
    @Override
    public Time getTime() {
        return time.clone();
    }

    /**
     * Sets the Time forward to the given time.
     * @param time Time to change to.
     * @throws InvalidTimeException If the time doesn't go forward.
     */
    public void setTime(Time time) throws InvalidTimeException {
        if (this.time.compareTo(time) >= 0) {
            throw new InvalidTimeException();
        }
        this.time = time;
        notifyTimeObservers();
    }

    /**
     * @see Hospital.World.TimeSubject#attachTimeObserver(Hospital.World.TimeObserver)
     */
    @Override
    public void attachTimeObserver(TimeObserver o) throws ArgumentIsNullException {
        if (o == null) {
            throw new ArgumentIsNullException("The observer should not be null");
        }
        timeObservers.add(o);
    }

    /**
     * @see Hospital.World.TimeSubject#detachTimeObserver(Hospital.World.TimeObserver)
     */
    @Override
    public void detachTimeObserver(TimeObserver o) {
        timeObservers.remove(o);
    }

    /**
     * Notifies all attached observers that the time has changed
     */
    private void notifyTimeObservers() {
        for (TimeObserver o : timeObservers) {
            o.timeUpdate(time);
        }
    }
}
