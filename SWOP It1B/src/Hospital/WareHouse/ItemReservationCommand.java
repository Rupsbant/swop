package Hospital.WareHouse;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Factory.Command;
import Hospital.Factory.NeedsItems;
import java.util.ArrayList;
import java.util.List;

public class ItemReservationCommand implements Command {

    private NeedsItems appointable;
    private ArrayList<ItemReservation> reservations = new ArrayList<ItemReservation>();
    private int state = 0;

    public ItemReservationCommand(NeedsItems appointable) {
        this.appointable = appointable;
    }

    public String execute() throws CannotDoException {
        String out = "";
        for (ItemInfo i : appointable.getNeededItems()) {
            try {
                System.out.println("Adding:\"" + i.getName() + "\"");
                Warehouse warehouse = appointable.getAppointment().getCampus().getWarehouse();
                reservations.add(warehouse.getStock(i.getName()).reserveItem(i.getCount(), appointable.getAppointment().getTimeFrame().getTime()));
                out += i.getCount() + " item(s) reserved of type: " + i.getName() + "\n";
            } catch (NotEnoughItemsAvailableException ex) {
                throw new CannotDoException("Scheduling was wrong, it couldn't reserve the items at the scheduled time.");
            } catch (StockException ex) {
                throw new CannotDoException("Item was not found in warehouse: " + ex.getMessage());
            }
        }
        state = 1;
        return out;
    }

    public boolean isDone() {
        return state != 0;
    }

    public String undo() throws NotDoneException, CannotDoException {
        if (!isDone()) {
            throw new NotDoneException();
        }
        if (state == 2) {
            throw new CannotDoException("Items were used");
        }
        String out = "";
        for (ItemReservation r : reservations) {
            try {
                r.removeReservation();
                out += "Undone reservation of " + r.getStock().getName() + ", number of reserved items released: " + r.getItemsReserved() + "\n";
            } catch (ItemNotReservedException ex) {
                throw new Error("This cannot happen, all items are reserved on execution.");
            }
        }
        reservations.clear();
        return out;
    }

    public void use() throws CannotDoException, NotDoneException {
        if (!isDone()) {
            throw new NotDoneException();
        }
        if (state == 2) {
            throw new CannotDoException("Items were used");
        }
        for (ItemReservation r : reservations) {
            List<Item> items = r.use();
            items.clear(); //delete items
        }
    }
}
