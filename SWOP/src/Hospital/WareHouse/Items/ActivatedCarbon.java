package Hospital.WareHouse.Items;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.Item;
import Hospital.World.Time;

/**
 * Activated carbon as medicine
 */
public class ActivatedCarbon extends MedicationItem {

    /**
     * Construction
     * @param expirationTime the time when this medication expires
     */
    public ActivatedCarbon(Time expirationTime) {
        super(expirationTime);
    }

    /**
     * @return "ActivatedCarbon"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ActivatedCarbon";
    }

    /**
     * @return a new ActivatedCarbon item with the same expiration date
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public ActivatedCarbon clone() {
        return new ActivatedCarbon(getExpirationTime());
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args.length == 0) {
            return clone();
        }
        Time t = (Time) Utils.getAnswer(TimeArgument.class, "Time", args[0]);
        return new ActivatedCarbon(t);
    }

    /**
     * @return "ActivatedCarbon"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "ActivatedCarbon";
    }
}
