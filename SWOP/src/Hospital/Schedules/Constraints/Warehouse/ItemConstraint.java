package Hospital.Schedules.Constraints.Warehouse;

import Hospital.Exception.Warehouse.StockException;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Treatments.Treatment;
import Hospital.WareHouse.ItemInfo;
import Hospital.World.Campus;
import Hospital.World.Time;

/**
 * Creates a constraint on the warehouse of the tested campus so that all items are available on the appointment's starttime
 */
public class ItemConstraint extends TimeFrameConstraint {

    private Time tf;
    private Campus campus;
    private final Treatment app;

    /**
     * Creates a new ItemCronstraint for the given treatment.
     * @param app
     */
    public ItemConstraint(Treatment app) {
        this.app = app;
    }

    @Override
    public Time isAccepted() {
        if (tf == null || campus == null) {
            return null;
        }
        ItemInfo[] infos = this.app.getNeededItems();
        for (ItemInfo i : infos) {
            try {
                if (!campus.getWarehouse().getStock(i.getName())
                        .testReservation(i.getCount(), tf.getTime())) {
                    return tf.getLaterTime(1);
                }
            } catch (StockException ex) {
                //The item is not in stock in that warehouse.
                return null;
                //throw new Error(ex);
            }
        }
        return tf;
    }

    @Override
    public void reset() {
        tf = null;
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.tf = tf;
    }
}
