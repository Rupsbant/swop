package Hospital.Schedules.Constraints.Warehouse;

import Hospital.Exception.Warehouse.StockException;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;
import Hospital.Treatments.Treatment;
import Hospital.WareHouse.ItemInfo;

public class ItemConstraint extends TimeFrameConstraintImplementation {

    private TimeFrame tf;
    private GetCampusConstraint constraint;
    private Treatment app;

    public ItemConstraint(GetCampusConstraint constraint, Treatment app) {
        this.constraint = constraint;
        this.app = app;
    }

    @Override
    protected Boolean isAccepted() {
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

    @Override
    protected void reset() {
        tf = null;
    }

    @Override
    protected void setValidSchedulable(Schedulable s) {
    }

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }
}
