package Hospital.WareHouse.Items;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.Item;
import Hospital.World.Time;

/**
 * Other classes of medication that are not in the system are misc
 */
public class Misc extends MedicationItem {

    /**
     * Constructor
     * @param expirationTime the time at which this medication expires
     */
    public Misc(Time expirationTime) {
        super(expirationTime);
    }

    /**
     * @return "Misc"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * A misc item with the same expiration time
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public Misc clone() {
        return new Misc(getExpirationTime());
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args.length == 0) {
            return clone();
        }
        Time t = (Time) Utils.getAnswer(TimeArgument.class, "Time", args[0]);
        return new Misc(t);
    }

    /**
     * @return "Misc"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "Misc";
    }
}
