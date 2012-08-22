package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import java.util.GregorianCalendar;

/**
 * This object holds a date and time and defines some
 * useful functions on them.
 */
public class Time implements HasTime, Cloneable {

    /**
     * the stores the time and date
     */
    private GregorianCalendar time;

    /**
     * Creates a new time object at 8/11/2011 8:00
     */
    public Time() {
        this(2011,11,8,8,00);
    }

    /**
     * Constructor for a given time and date.
     * @param year the year to set
     * @param month the month to set
     * @param day the day of the month to set
     * @param hour the hour to set
     * @param minute the minutes into the hour to set
     */
    public Time(int year, int month, int day, int hour, int minute) {
        time = new GregorianCalendar(year, month-1, day, hour, minute);
    }

    /**
     * Constructor for a given time and date.
     * @param numbers The times to set, positions 0 to 4 are respectively year, month, day, hour, minute;
     * @throws ArgumentIsNullException the given array of numbers was null
     * @throws WrongArgumentListException the right amount of numbers was not given
     */
    public Time(int[] numbers) throws ArgumentIsNullException, WrongArgumentListException {
        if (numbers == null) {
            throw new ArgumentIsNullException("Numberlist was null");
        }
        if (numbers.length != 5) {
            throw new WrongArgumentListException("Length wrong");
        }
        time = new GregorianCalendar(numbers[0], numbers[1]-1, numbers[2], numbers[3], numbers[4]);
    }

    /**
     * Creates a Time object from an existing GregorianCalendar object
     * @param t the object to create a Time object from
     */
    private Time(GregorianCalendar t) {
        time = t;
    }

    /**
     * Gets the represented time as a GregorianCalendar object
     * @return a clone of the internal GregorianCalendar object
     */
    private GregorianCalendar getGregorianTime() {
        return (GregorianCalendar) time.clone();
    }

    /**
     * Returns a clone of this Time.
     * @return new Time(getTime());
     */
    @Override
    public Time clone() {
        return new Time(getGregorianTime());
    }

    @Override
    public Time getTime() {
        return this;
    }

    /**
     * Compares this object to another Time object
     * @param t the other object to compare to
     * @return  0 if t represents the same time as this object.
     * 			less than 0 if this object comes before t in time
     * 			more than 0 if this object comes after t in time
     */
    @Override
    public int compareTo(HasTime t) {
        return getGregorianTime().compareTo(t.getTime().getGregorianTime());
    }

    /**
     * Returns a new Time-object representing a time <i>minutes</i> later than this one.
     * @param minutes the amount of minutes to add
     * @return a new Time-object representing a time <i>minutes</i> later than this object.
     */
    public Time getLaterTime(int minutes) {
        GregorianCalendar newtime = (GregorianCalendar) time.clone();
        newtime.add(GregorianCalendar.MINUTE, minutes);
        return new Time(newtime);
    }

    /**
     * Gives back a moment later in time.
     * @param year amount of years to be added
     * @param month amount of months to be added
     * @param day amount of days to be added
     * @param hour amount of hours to be added
     * @param minute amount of minutes to be added
     * @return a new Time-object representing a time later than the time in this object, as specified by the parameters
     */
    public Time getDiffTime(int year, int month, int day, int hour, int minute) {
        GregorianCalendar newtime = (GregorianCalendar) time.clone();
        newtime.add(GregorianCalendar.YEAR, year);
        newtime.add(GregorianCalendar.MONTH, month);
        newtime.add(GregorianCalendar.DAY_OF_MONTH, day);
        newtime.add(GregorianCalendar.HOUR_OF_DAY, hour);
        newtime.add(GregorianCalendar.MINUTE, minute);
        return new Time(newtime);
    }

    /**
     * Getter for the year.
     * @return the year of the time represented by this object
     */
    public int getYear() {
        return time.get(GregorianCalendar.YEAR);
    }

    /**
     * Getter for the month.
     * @return the month of the time represented by this object
     */
    public int getMonth() {
        return time.get(GregorianCalendar.MONTH)+1;
    }

    /**
     * Getter for the date.
     * @return the date of the time represented by this object
     */
    public int getDay() {
        return time.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Getter for the hour.
     * @return the hour of the time represented by this object
     */
    public int getHour() {
        return time.get(GregorianCalendar.HOUR_OF_DAY);
    }

    /**
     * Getter for the minutes.
     * @return the minutes of the time represented by this object
     */
    public int getMinute() {
        return time.get(GregorianCalendar.MINUTE);
    }

    /**
     * Gets the absulute timedifference in minutes.
     * @param t The time to compare to.
     * @return the difference in minutes compared to t
     */
    public int getMinutesDiff(Time t) {
        return Math.abs((int) ((this.time.getTimeInMillis() - t.time.getTimeInMillis()) / 60000));
    }

    /**
     * Creates a readable Time-string.
     * @return The time in the following format: "dd/mm/yyyy HH:MM"
     */
    @Override
    public String toString() {
        return this.getYear() + "/" + this.getMonth() + "/" + this.getDay() + " " + this.getHour() + ":" + (this.getMinute()+100+"").substring(1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Time) {
            Time cast = (Time) obj;
            return time.equals(cast.time);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.time != null ? this.time.hashCode() : 0);
        return hash;
    }
}
