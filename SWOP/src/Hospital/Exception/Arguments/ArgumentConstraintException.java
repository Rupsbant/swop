package Hospital.Exception.Arguments;

/**
 * Thrown when the given argument didn't meet given constraints
 */
public class ArgumentConstraintException extends InvalidArgumentException {

    /**
     * Constructor
     * @param message details about the exception
     */
    public ArgumentConstraintException(String message) {
        super(message);
    }
}
