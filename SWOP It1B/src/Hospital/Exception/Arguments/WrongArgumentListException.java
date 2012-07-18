package Hospital.Exception.Arguments;

/**
 * Thrown when the given argument-list doesn't match the expected one.
 */
public class WrongArgumentListException extends InvalidArgumentException {

    /**
     * Constructor
     * @param string details about the exception
     */
    public WrongArgumentListException(String string) {
        super(string);
    }

    /**
     * Constructor
     */
    public WrongArgumentListException() {
        super();
    }
}
