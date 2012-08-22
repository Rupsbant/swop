package Hospital.WareHouse.ItemQueues;

import Hospital.WareHouse.Item;
import Hospital.WareHouse.StockChangeEvent;
import Hospital.World.Time;
import java.util.List;

/**
 * An interface to define the storage-type of an item
 * @param <I> the item stored in this queue
 */
public interface ItemQueue<I extends Item> {

    /**
     * Add an item to this datastructure
     * @param item the item to add
     */
    public void add(I item);

    /**
     * Gets all the items in this datastructure
     * @return an array of all the items in this stack
     */
    public I[] getAll();

    /**
     * Gets the first accessible item from this datastructure
     * @return the first accessible item
     */
    public I get();

    /**
     * Gives the size of this datastructure
     * @return the amount of items
     */
    public int size();

    /**
     * Indicates whether this datastructure is empty
     * @return true if there are no items in this datastructure
     */
    public boolean isEmpty();

    /**
     * Updates this datastructure to a given time (removal of expired items)
     * @param newTime the new time to update to
     */
    public void timeUpdate(Time newTime);
    
    /**
     * Generates a list of StockChangeEvents, such as expiration dates
     * @return a list of StockChangeEvents
     */
    public List<StockChangeEvent> getEventList();
}
