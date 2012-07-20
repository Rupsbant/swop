package Hospital.Schedules.Constraints.Warehouse;

import Hospital.Exception.Warehouse.StockException;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;
import Hospital.Treatments.Treatment;
import Hospital.WareHouse.ItemInfo;

public class ItemConstraint extends TimeFrameConstraint {

    private TimeFrame tf;
    private GetCampusConstraint constraint;
    private Treatment app;

    public ItemConstraint(GetCampusConstraint constraint, Treatment app) {
        this.constraint = constraint;
        this.app = app;
    }

    public Boolean isAccepted() {
        if (tf == null || constraint.getCampus() == null) {
            return null;
        }
        ItemInfo[] infos = this.app.getNeededItems();
        for (ItemInfo i : infos) {
            try {
                if (!constraint.getCampus().getWarehouse().getStock(i.getName()).testReservation(i.getCount(), tf.getTime())) {
                    return false;
                }
            } catch (StockException ex) {
                throw new Error(ex);
            }
        }
        return true;
    }

    public void reset() {
        tf = null;
    }

    @Override
    public void setSchedulable(Schedulable s) {
    }

    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }
}
