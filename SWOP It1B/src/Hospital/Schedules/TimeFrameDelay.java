package Hospital.Schedules;

import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.WorldTime;

/**
 * A moveable timeframe with a minimum delay
 */
public class TimeFrameDelay {

    /**
     * the minimum delay for this timeframe in minutes
     */
    private final int delay;
    /**
     * the length in minutes
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
    public TimeFrameDelay(int delay, int length) throws ArgumentConstraintException {
        if (delay < 0) {
            throw new ArgumentConstraintException("Delay can't be less than zero");
        }
        this.delay = delay;
        if (!TimeFrame.isValidLength(length)) {
            throw new ArgumentConstraintException("Length can't be less than zero");
        }
        this.length = length;
    }

    /**
     * Sets the world from which to get the current time
     * @param w the world to set
     * @return this object
     * @throws CannotChangeException the world was already set
     */
    TimeFrameDelay setWorldTime(WorldTime wt) throws CannotChangeException {
        if (worldTime != null) {
            throw new CannotChangeException("World can't be set a second time");
        }
        worldTime = wt;
        return this;
    }

    /**
     * Returns the first TimeFrame that can be used for planning.
     * @return TimeFrame
     */
    public TimeFrame getDelayedTimeFrame() {
        try {
            return new TimeFrame(worldTime.getTime().getLaterTime(delay), length);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("getLaterTime is not null");
        } catch (ArgumentConstraintException ex) {
            throw new RuntimeException("length is greater than zero");
        }
    }
}
