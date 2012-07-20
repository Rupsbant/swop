package Hospital.Exception.Arguments;

import Hospital.SystemAPI;

/**
 * Thrown when a given argument was null.
 */
public class ArgumentIsNullException extends InvalidArgumentException {

    /**
     * Constructor
     */
    public ArgumentIsNullException() {
    }

    /**
     * Constructor
     * @param message details about the exception
     */
    @SystemAPI
    public ArgumentIsNullException(String message) {
        super(message);
    }
}
