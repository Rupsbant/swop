package Hospital.WareHouse;

import Hospital.World.HasTime;

/**
 * A simulated stockChangeEvent used to check the stockconsistency
 */
public interface StockChangeEvent extends HasTime {

    /**
     * change the stock from the old stock to the new
     * @param old the old number of items
     * @return the new number of items
     */
    int stockChange(int old);

    /**
     * change the number of items ordered
     * @param old the old number of ordered items
     * @return the new number of ordered items
     */
    int orderChange(int old);
}
