package Hospital.WareHouse.ItemQueues;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.WareHouse.ExpirationEvent;
import Hospital.WareHouse.Item;
import Hospital.WareHouse.StockChangeEvent;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A last-in-first-out structure for storing items, better known as a stack
 * @param <I> the type of item to store
 */
public class LIFOQueue<I extends Item> implements ItemQueue<I> {

    /**
     * the backing storage for the items stored in this object
     */
    LinkedList<I> items = new LinkedList<I>();

    /**
     * @see Hospital.WareHouse.ItemQueues.ItemQueue#add(Hospital.WareHouse.Item)
     */
    public void add(I item) {
        items.addFirst(item);
    }

    /**
     * @return the last added item to this stack
     * @see Hospital.WareHouse.ItemQueues.ItemQueue#get()
     */
    public I get() {
        return items.removeFirst();
    }

    /**
     * @see Hospital.WareHouse.ItemQueues.ItemQueue#size()
     */
    public int size() {
        return items.size();
    }

    /**
     * @see Hospital.WareHouse.ItemQueues.ItemQueue#getAll()
     */
    public I[] getAll() {
        return items.toArray((I[]) new Item[0]);
    }

    /**
     * @see Hospital.WareHouse.ItemQueues.ItemQueue#isEmpty()
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @see Hospital.WareHouse.ItemQueues.ItemQueue#timeUpdate(Hospital.World.Time)
     */
    @Override
    public void timeUpdate(Time newTime) {
        if (newTime == null) {
            throw new Error("New Time should not be null!");
        }
        try {
            for (int i = items.size(); i > 0; i--) {
                if (items.get(i - 1).isExpired(newTime)) {
                    items.remove(items.get(i - 1));
                }
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
