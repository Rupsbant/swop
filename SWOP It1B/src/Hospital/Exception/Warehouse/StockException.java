package Hospital.Exception.Warehouse;

/**
 * Thrown when the stock is not available
 */
public class StockException extends Exception {

    public StockException() {
    }

    public StockException(String string) {
        super(string);
    }
}
