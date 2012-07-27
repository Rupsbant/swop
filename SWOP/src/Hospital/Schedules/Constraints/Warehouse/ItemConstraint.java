package Hospital.Schedules.Constraints.Warehouse;

import Hospital.Exception.Warehouse.StockException;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.Treatments.Treatment;
import Hospital.WareHouse.ItemInfo;
import Hospital.World.Campus;

public class ItemConstraint extends TimeFrameConstraint {

    private TimeFrame tf;
    private Campus campus;
    private final Treatment app;

    public ItemConstraint(Treatment app) {
        this.app = app;
    }

    public TimeFrame isAccepted() {
        if (tf == null || campus == null) {
            return null;
        }
        ItemInfo[] infos = this.app.getNeededItems();
        for (ItemInfo i : infos) {
            try {
                if (!campus.getWarehouse().getStock(i.getName()).testReservation(i.getCount(), tf.getTime())) {
                    return tf.next();
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

    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }
}
