package Hospital.WareHouse.OrderPlacers;

import Hospital.Utils;
import Hospital.Exception.Arguments.ArgumentConstraintException;

/**
 * Default orderplacer to calculate how much items are needed
 */
public class NormalOrderPlacer implements OrderPlacer {

    /**
     * Constructor
     */
    public NormalOrderPlacer() {
    }

    /**
     * @see Hospital.WareHouse.OrderPlacers.OrderPlacer#checkStock(int, int, int)
     */
    public int checkStock(int currentStock, int ordered, int maxStock) throws ArgumentConstraintException {
        if(!(currentStock>=0)) throw new ArgumentConstraintException("A stock can not hold a negative amount of items.");
        if(!(ordered>=0)) throw new ArgumentConstraintException("A negative amount of items can not be ordered.");
        if(!(maxStock>0)) throw new ArgumentConstraintException("The stock needs a capacity larger than 0.");
        int currentSpaceAvailable = maxStock - currentStock;
        if(!(currentSpaceAvailable>=0)) throw new ArgumentConstraintException("The stock can not be filled over capacity.");
        if (currentStock <= maxStock / 2) {
            if (currentSpaceAvailable - ordered > 0) {
                return currentSpaceAvailable - ordered;
            }
        }
        return 0;
    }
}
