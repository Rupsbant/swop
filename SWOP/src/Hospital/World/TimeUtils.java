package Hospital.World;

import Hospital.SystemAPI;

/**
 * This class defines some utility functions on Time objects
 */
public class TimeUtils {

    /**
     * Gives back a new Time-object representing a time one day later.
     * @param t The time to start with.
     * @return a new Time-object representing a time one day later than this one.
     */
	@SystemAPI
    public static Time addDay(Time t) {
        return t.getDiffTime(0, 0, 1, 0, 0);
    }

    /**
     * Returns a new Time-object of the start of the day of the given Time.
     * @param t Time to start with.
     * @return A Time with the same day, month, year on 00:00.
     */
    @SystemAPI
    public static Time getStartOfDay(Time t) {
        int year = t.getYear();
        int month = t.getMonth();
        int day = t.getDay();
        return new Time(year, month, day, 0, 0);
    }

    /**
     * Returns the time one year before.
     * @param t Time to start with.
     * @return Time minus one year.
     */
    @SystemAPI
    public static Time getLastYear(Time t) {
        return t.getDiffTime(-1, 0, 0, 0, 0);
    }

    /**
     * Returns the time one year before.
     * @param t Time to start with.
     * @return Time minus one year.
     */
    @SystemAPI
    public static Time getNextYear(Time t) {
        return t.getDiffTime(1, 0, 0, 0, 0);
    }

    /**
     * Copies the day from one time and the hour from another.
     * @param day
     * @param hours
     * @return A new Time object.
     */
    public static Time copyDay(Time day, Time hours) {
        return new Time(day.getYear(), day.getMonth(), day.getDay(), hours.getHour(), hours.getMinute());
    }
}
