package Hospital.WareHouse.OrderPlacers;

import Hospital.Exception.Arguments.ArgumentConstraintException;

/**
 * Used to completely fill up a stock
 */
public class FullyStockOrderPlacer implements OrderPlacer {

    /**
     * @return the number of items needed to completely fill up this stock
     * @see Hospital.WareHouse.OrderPlacers.OrderPlacer#checkStock(int, int, int)
     */
    public int checkStock(int currentStock, int ordered, int maxStock) throws ArgumentConstraintException {
        return Math.max(0, maxStock - currentStock - ordered);
    }
}
