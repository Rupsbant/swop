package Hospital.Exception;

/**
 * thrown when an order could not be found
 */
public class CannotFindException extends Exception {

    public CannotFindException() {
    }

    public CannotFindException(String string) {
        super(string);
    }
}
