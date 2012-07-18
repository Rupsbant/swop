package Hospital.WareHouse.Items;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.TimeArgument;
import Hospital.WareHouse.Item;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.World.Time;

/**
 * An item that expires over time
 */
public abstract class ExpiringItem extends Item {

    /**
     * the time at which this item expires
     */
    private Time expirationTime;

    /**
     * Constructor
     * @param expirationTime the time at which this item expires
     */
    ExpiringItem(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * @see Hospital.WareHouse.Item#getExpirationTime()
     */
    @Override
    public Time getExpirationTime() {
        return expirationTime.clone();
    }

    /**
     * @return true
     * @see Hospital.WareHouse.Item#hasExpirationTime()
     */
    @Override
    public boolean hasExpirationTime() {
        return true;
    }

    /**
     * @see Hospital.WareHouse.Item#isExpired(Hospital.World.Time)
     */
    @Override
    public boolean isExpired(Time currentTime) throws ArgumentIsNullException {
        if (currentTime == null) {
            throw new ArgumentIsNullException("CurrentTime is null.");
        }
        return expirationTime.compareTo(currentTime) < 0;
    }

    /**
     * @see Hospital.WareHouse.Item#setExpirationTime(Hospital.World.Time)
     */
    @Override
    protected void setExpirationTime(Time expirationTime) throws ArgumentIsNullException, CannotChangeException {
        if (expirationTime == null) {
            throw new ArgumentIsNullException();
        }
        if (this.expirationTime != null) {
            throw new CannotChangeException();
        }
        this.expirationTime = expirationTime;
    }

    /**
     * @see Hospital.WareHouse.Item#getArguments()
     */
    @Override
    public PublicArgument[] getArguments() {
        return new PublicArgument[]{new TimeArgument("Enter the expirationdate")};
    }
}
