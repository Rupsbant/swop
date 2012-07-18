package Hospital.WareHouse;

/**
 * An interface for observers with a stock as subject
 */
public interface StockObserver {

    /**
     * This function gets called when the subject-stock is updated
     */
    public void stockUpdate();
}
