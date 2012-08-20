package Hospital.Interfaces;

import Hospital.Schedules.Appointable;
import Hospital.WareHouse.ItemInfo;

public interface NeedsItems extends Appointable {

    /**
     * Returns a list of all needed items for this treatment.
     * @return List of names of items
     */
    ItemInfo[] getNeededItems();
}
