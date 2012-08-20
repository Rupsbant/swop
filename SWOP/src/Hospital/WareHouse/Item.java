package Hospital.WareHouse;

import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Warehouse.NeedsNoExpirationTimeException;
import Hospital.World.Time;

/**
 * An item in our hospital (eg medication)
 */
public abstract class Item implements Cloneable, ItemFactory {

    /**
     * Constructor
     */
    public Item() {
    }

    /**
     * Indicates whether this item has an expiration date
     * @return true if this item can expire
     */
    public boolean hasExpirationTime() {
        return false;
    }

    /**
     * The date at which this item expires
     * @return a Time-object with the expiration-date
     */
    public Time getExpirationTime() {
        return null;
    }

    /**
     * Indicates whether this item has expired at the given time
     * @param currentTime the time at which to check
     * @return true if the item is expired at the given time
     * @throws ArgumentIsNullException the given time argument was null
     */
    public boolean isExpired(Time currentTime) throws ArgumentIsNullException {
        return false;
    }

    /**
     * Sets the expiration date for this item
     * @param expirationTime the time at which this item expires
     * @throws NeedsNoExpirationTimeException this function was called even though the item needs no expiration date
     * @throws ArgumentIsNullException the given time object was null
     * @throws CannotChangeException the expiration date was already set
     */
    protected void setExpirationTime(Time expirationTime) throws NeedsNoExpirationTimeException, ArgumentIsNullException, CannotChangeException {
        throw new NeedsNoExpirationTimeException();
    }

    /**
     * Creates a clone of this object
     * @see java.lang.Object#clone()
     */
    @Override
    public abstract Item clone();

    @Override
    public PublicArgument[] getArguments() {
        return new PublicArgument[0];
    }

    /**
     * Returns the name of the Item.
     * @return the name
     */
    public abstract String getName();
}
