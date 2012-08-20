package Hospital.Exception.Arguments;

/**
 * This exception is thrown when some arguments or parameters were not as expected
 */
public abstract class InvalidArgumentException extends Exception {

    /**
     * Constructor
     */
    public InvalidArgumentException() {
    }

    /**
     * The message for this exception
     * @param str a message
     */
    public InvalidArgumentException(String str) {
        super(str);
    }
}
