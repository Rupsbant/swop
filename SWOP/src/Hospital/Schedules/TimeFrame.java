package Hospital.Schedules;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.HasTime;
import Hospital.World.Time;

/**
 * Represents a timeframe used ins scheduling, has a starting time and a length
 */
public final class TimeFrame implements HasTime, Cloneable {

    /**
     * the starting time
     */
    private final Time time;
    /**
     * the length in minutes
     */
    private final int length;

    /**
     * Constructor
     * @param time the starting time
     * @param length the length in minutes
     * @throws ArgumentIsNullException time was null
     * @throws ArgumentConstraintException length was negative
     */
    public TimeFrame(Time time, int length) throws ArgumentIsNullException, ArgumentConstraintException {
        if (time == null) {
            throw new ArgumentIsNullException("Starttime of appointment is null");
        }
        this.time = time;
        if (!isValidLength(length)) {
            throw new ArgumentConstraintException("Length of appointment is negative");
        }
        this.length = length;
    }

    /**
     * Returns true if the length is more than 0 minutes.
     * @param length The appointmentLength to test.
     * @return length >=0
     */
    public static boolean isValidLength(int length) {
        return length >= 0;
    }

    /**
     * Return the length of the TimeFrame.
     * @return length(minutes);
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the startTime of the TimeFrame.
     * @return time.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Returns the endTime of the TimeFrame.
     * @return endTime.
     */
    public Time getEndTime() {
        return getTime().getLaterTime(length);
    }

    /**
     * Returns true if the two TimeFrame falls during the same time.
     * @param tf Other TimeFrame to test.
     * @return true if TimeFrames collide.
     */
    public boolean collides(TimeFrame tf) {
        return (getTime().compareTo(tf.getTime()) <= 0 && tf.getTime().compareTo(getEndTime()) < 0)
                || (tf.getTime().compareTo(getTime()) <= 0 && getTime().compareTo(tf.getEndTime()) < 0);
    }

    /**
     * Returns an ordering of the HasTimes;
     * @param tf HasTime to compare to.
     * @return The ordering based on the startTime.
     */
    public int compareTo(HasTime tf) {
        return getTime().compareTo(tf.getTime());
    }

    /**
     * Clones the TimeFrame
     * @return new TimeFrame(startTime, length);
     */
    @Override
    protected TimeFrame clone() {
        try {
            return new TimeFrame(time, length);
        } catch (ArgumentIsNullException ex) {
            throw new Error("time was not null");
        } catch (ArgumentConstraintException ex) {
            throw new Error("length was not null");
        }
    }

    /**
     * Checks if the given object is a TimeFrame that falls on the same time, with the same length.
     * @param obj TimeFrame to check with.
     * @return true if startTime and endTime and length of both Frames are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof TimeFrame) {
            TimeFrame cast = (TimeFrame) obj;
            return time.equals(cast.time) && length == cast.length;
        }
        return super.equals(obj);
    }

    /**
     * Implements hashCode consistent with equals(Object obj)
     * @return hash based on startTime and length.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 41 * hash + this.length;
        return hash;
    }

    /**
     * Gives a string back of the format : Frame on: yyyy/mm/dd hh:mm, length: ll
     */
    @Override
    public String toString() {
        return "Frame on: " + this.time + ", length: " + length;
    }
}
