package Hospital.WareHouse.Items;

import Hospital.Argument.Argument;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.Item;
import Hospital.World.Time;

/**
 * Aspirin, a medicine
 */
public class Aspirin extends MedicationItem {

    /**
     * Constructor
     * @param expirationTime the time at which this aspirin expires
     */
    public Aspirin(Time expirationTime) {
        super(expirationTime);
    }

    /**
     * @return "Aspirin"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Aspirin";
    }

    /**
     * @return an Aspirin with the same expiry date
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public Aspirin clone() {
        return new Aspirin(getExpirationTime());
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args.length == 0) {
            return clone();
        }
        Time t = (Time) Utils.getAnswer(TimeArgument.class, "Time", args[0]);
        return new Aspirin(t);
    }

    /**
     * @return "Aspirin"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "Aspirin";
    }
}
