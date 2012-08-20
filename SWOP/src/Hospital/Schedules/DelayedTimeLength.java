package Hospital.Schedules;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.World.Time;
import Hospital.World.WorldTime;

/**
 * A moveable timeframe with a minimum delay
 */
public class DelayedTimeLength {

    /**
     * the minimum delay for this timeframe in minutes
     */
    private final int delay;
    /**
     * The length of the appointment
     */
    private final int length;
    /**
     * the world from which to get the current time
     */
    private WorldTime worldTime;

    /**
     * Constructor
     * @param delay the minimum delay for this timeframe in minutes
     * @param length the length in minutes
     * @throws ArgumentConstraintException length or delay was negative
     */
    public DelayedTimeLength(int delay, int length) throws ArgumentConstraintException {
        if (delay < 0) {
            throw new ArgumentConstraintException("Delay can't be less than zero");
        }
        this.delay = delay;
        if (length < 0) {
            throw new ArgumentConstraintException("Length can't be less than 0");
        }
        this.length = length;
    }

    /**
     * Sets the world from which to get the current time
     * @param w the world to set
     * @return this object
     */
    DelayedTimeLength setWorldTime(WorldTime wt) {
        worldTime = wt;
        return this;
    }

    /**
     * Returns the first TimeFrame that can be used for planning.
     * @return TimeFrame
     */
    public Time getDelayedTime() {
        return worldTime.getTime().getLaterTime(delay);
    }
    
    /**
     * returns the length of the appointment to be made
     * @return the length of the appointment to be made
     */
    public int getLength(){
        return this.length;
    }
}
