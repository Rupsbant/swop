package Hospital.WareHouse.ItemQueues;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.WareHouse.ExpirationEvent;
import Hospital.WareHouse.Item;
import Hospital.WareHouse.StockChangeEvent;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FIFOQueue<I extends Item> implements ItemQueue<I> {

    /**
     * the backing storage for the items stored in this object
     */
    LinkedList<I> items = new LinkedList<I>();

    public void add(I item) {
        items.addLast(item);
    }

    public I[] getAll() {
        return items.toArray((I[]) new Item[0]);
    }

    public I get() {
        if(items.isEmpty()){
            return null;
        }
        return items.removeFirst();
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void timeUpdate(Time newTime) {
        if (newTime == null) {
            throw new Error("New Time should not be null!");
        }
        try {
            int hasExpired = 0;
            for (int i = items.size(); i > 0; i--) {
                if (items.get(i - 1).isExpired(newTime)) {
                    items.remove(items.get(i - 1));
                    hasExpired++;
                }
            }
            if (hasExpired > 0) {
                System.err.println(hasExpired + " items have expired!");
            }
        } catch (ArgumentIsNullException e) {
            throw new Error("Nothing can be wrong, newTime was not null.");
        }
    }

    @Override
    public List<StockChangeEvent> getEventList() {
        List<StockChangeEvent> out = new ArrayList<StockChangeEvent>();
        for (Item item : items) {
            if (item.hasExpirationTime()) {
                out.add(new ExpirationEvent(item.getExpirationTime()));
            }
        }
        return out;
    }
}
