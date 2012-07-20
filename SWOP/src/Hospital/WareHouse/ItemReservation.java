package Hospital.WareHouse;

import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.List;

public class ItemReservation implements StockChangeEvent {

    private Stock stock;
    private int itemsReserved;
    private Time reservationFor;
    private List<Item> items;

    public ItemReservation(int itemsReserved, Time reservationFor, Stock stock) {
        this.itemsReserved = itemsReserved;
        this.reservationFor = reservationFor;
        this.stock = stock;
    }

    public Time getTime() {
        return reservationFor;
    }

    public int getItemsReserved() {
        return itemsReserved;
    }

    public Stock getStock() {
        return stock;
    }

    public int compareTo(HasTime o) {
        return reservationFor.compareTo(o.getTime());
    }

    public int stockChange(int old) {
        return old - itemsReserved;
    }

    public int orderChange(int old) {
        return old;
    }

    public void removeReservation() throws ItemNotReservedException {
        stock.removeReservation(this);
    }

    public List<Item> use() {
        return items;
    }

    protected void removeFromStock() throws ItemNotReservedException {
        items = getStock().getItems(this);
    }
}
