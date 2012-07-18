package Hospital.WareHouse.OrderPlacers;

import Hospital.Exception.Arguments.ArgumentConstraintException;

/**
 * Interface for classes that calculates automatic orders for specific items
 */
public interface OrderPlacer {

    /**
     * This class calculates the amount of items needed for the given state of the stock
     * @param currentStock how much space is left in the stock
     * @param ordered how many items are already ordered
     * @param maxStock the max amount of items the stock can hold
     * @return The number of items that needs to be ordered.
     * @throws ArgumentConstraintException currentStock&lt;0
     * @throws ArgumentConstraintException maxStock&lt;=0
     * @throws ArgumentConstraintException ordered&lt;0
     * @throws ArgumentConstraintException currentStock>maxStock
     */
    public int checkStock(int currentStock, int ordered, int maxStock) throws ArgumentConstraintException;
}
