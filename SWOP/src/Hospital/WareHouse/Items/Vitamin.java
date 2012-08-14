package Hospital.WareHouse.Items;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.Item;
import Hospital.World.Time;

/**
 * Vitamin pills
 */
public class Vitamin extends MedicationItem {

    /**
     * Constructor
     * @param expirationTime the time at which this vitamin medication expires
     */
    public Vitamin(Time expirationTime) {
        super(expirationTime);
    }

    /**
     * @return "Vitamin"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Vitamin";
    }

    /**
     * @return Vitamin medication with the same expiration time as this one
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public Vitamin clone() {
        return new Vitamin(getExpirationTime());
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args.length == 0) {
            return clone();
        }
        Time t = (Time) Utils.getAnswer(TimeArgument.class, "Time", args[0]);
        return new Vitamin(t);
    }

    /**
     * @return "Vitamin"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "Vitamin";
    }
}
