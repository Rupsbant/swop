package Hospital.Exception.Warehouse;

/**
 * Thrown when an item was not reserved for use.
 */
public class ItemNotReservedException extends Exception {

    /**
     * Constructor
     */
    public ItemNotReservedException() {
        super();
    }

    /**
     * Constructor
     * @param string error message to give with this exception
     */
    public ItemNotReservedException(String string) {
        super(string);
    }

}
