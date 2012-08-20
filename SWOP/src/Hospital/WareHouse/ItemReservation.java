package Hospital.WareHouse;

import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.List;

/**
 * A ItemReservation: a number of items reserved from a stock
 */
public class ItemReservation implements StockChangeEvent {

    private Stock stock;
    private int itemsReserved;
    private Time reservationFor;
    private List<Item> items;

    /**
     * Creates an itemreservation of the given number items
     * at the given time from the given stock
     * @param itemsReserved
     * @param reservationFor
     * @param stock
     */
    public ItemReservation(int itemsReserved, Time reservationFor, Stock stock) {
        this.itemsReserved = itemsReserved;
        this.reservationFor = reservationFor;
        this.stock = stock;
    }

    @Override
    public Time getTime() {
        return reservationFor;
    }

    /**
     * Returns the number of items reserved
     * @return number of items reserved
     */
    public int getItemsReserved() {
        return itemsReserved;
    }

    /**
     * Returns the stock
     * @return the stock
     */
    public Stock getStock() {
        return stock;
    }

    @Override
    public int compareTo(HasTime o) {
        return reservationFor.compareTo(o.getTime());
    }

    @Override
    public int stockChange(int old) {
        return old - itemsReserved;
    }

    @Override
    public int orderChange(int old) {
        return old;
    }

    /**
     * Removes the reservation from the stock
     * @throws ItemNotReservedException
     */
    public void removeReservation() throws ItemNotReservedException {
        stock.removeReservation(this);
    }

    /**
     * Returns the items from the stock
     * @return List of items
     */
    public List<Item> use() {
        return items;
    }

    /**
     * Removes the reserved items from the stock into this reservation
     * This should only be used at the right time.
     * @throws ItemNotReservedException
     */
    protected void removeFromStock() throws ItemNotReservedException {
        items = getStock().getItems(this);
    }
}
