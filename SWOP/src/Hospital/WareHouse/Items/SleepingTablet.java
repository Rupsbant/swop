package Hospital.WareHouse.Items;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.Item;
import Hospital.World.Time;

/**
 * Sleeping medication in tablet form
 */
public class SleepingTablet extends MedicationItem {

    /**
     * Constructor
     * @param expirationTime the time at which this sleeping tablet expires
     */
    public SleepingTablet(Time expirationTime) {
        super(expirationTime);
    }

    /**
     * @return "SleepingTablet"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SleepingTablet";
    }

    /**
     * @return a sleeping tablet with the same expiration time as this one
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public SleepingTablet clone() {
        return new SleepingTablet(getExpirationTime());
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(PublicArgument[] args) throws InvalidArgumentException, WrongArgumentListException {
        if(args.length == 0){
            return clone();
        }
        Time t = (Time) Utils.getAnswer(TimeArgument.class, "Time", args[0]);
        return new SleepingTablet(t);
    }

    /**
     * @return "SleepingPill"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "SleepingPill";
    }
}
