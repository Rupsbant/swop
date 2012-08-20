package Hospital.Interfaces;

import Hospital.Schedules.Appointable;
import Hospital.WareHouse.ItemInfo;

/**
 * This extends a appointable to a appointable that needs items on the given appointment
 * classes that implement this interface need to add a ItemReservationConstraint
 * @author Rupsbant
 */
public interface NeedsItems extends Appointable {

    /**
     * Returns a list of all needed items for this treatment.
     * @return List of names of items
     */
    ItemInfo[] getNeededItems();
}
