package Hospital.WareHouse.Items;

import Hospital.Argument.Argument;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.Item;
import Hospital.World.Time;

/**
 * A meal for patients
 */
public class Meal extends ExpiringItem {

    /**
     * Constructor
     * @param expirationTime the time at which this meal expires
     */
    public Meal(Time expirationTime) {
        super(expirationTime);
    }

    /**
     * @return "Meal"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Meal";
    }

    /**
     * @return a Meal which expires at the same time as this one
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public Meal clone() {
        return new Meal(getExpirationTime());
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args.length == 0) {
            return clone();
        }
        Time t = (Time) Utils.getAnswer(TimeArgument.class, "Time", args[0]);
        return new Meal(t);
    }

    /**
     * @return "Meal"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "Meal";
    }
}
