package Hospital.Schedules.Constraints.Warehouse;

import Hospital.Exception.Warehouse.StockException;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Treatments.Treatment;
import Hospital.WareHouse.ItemInfo;
import Hospital.World.Campus;
import Hospital.World.Time;

public class ItemConstraint extends TimeFrameConstraint {

    private Time tf;
    private Campus campus;
    private final Treatment app;

    public ItemConstraint(Treatment app) {
        this.app = app;
    }

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

    public void reset() {
        tf = null;
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    public void setTime(Time tf, int length) {
        this.tf = tf;
    }
}
