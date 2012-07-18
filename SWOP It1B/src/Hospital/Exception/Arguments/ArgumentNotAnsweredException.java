package Hospital.Exception.Arguments;

/**
 * Thrown when a required Argument-object was not answered.
 */
public class ArgumentNotAnsweredException extends InvalidArgumentException {

    /**
     * Constructor
     * @param string details about the exception
     */
    public ArgumentNotAnsweredException(String string) {
        super(string);
    }

    /**
     * Constructor
     */
    public ArgumentNotAnsweredException() {
        super();
    }
}
