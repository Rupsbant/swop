package Hospital.WareHouse.Items;

import Hospital.World.Time;

/**
 * A medication item, this class is used to restrict the types of items used at certain places
 */
public abstract class MedicationItem extends ExpiringItem {

    /**
     * Constructor
     * @param expirationTime the time at which this medication expires
     */
    MedicationItem(Time expirationTime) {
        super(expirationTime);
    }
}
