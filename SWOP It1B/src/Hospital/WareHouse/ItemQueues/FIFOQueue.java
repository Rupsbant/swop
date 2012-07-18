package Hospital.WareHouse.ItemQueues;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.WareHouse.Item;
import Hospital.World.Time;
import java.util.LinkedList;

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
            throw new RuntimeException("New Time should not be null!");
        }
        try {
            for (int i = items.size(); i > 0; i--) {
                if (items.get(i - 1).isExpired(newTime)) {
                    items.remove(items.get(i - 1));
                }
            }
        } catch (ArgumentIsNullException e) {
            throw new RuntimeException("Nothing can be wrong, newTime was not null.");
        }
    }
}
