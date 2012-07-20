package Hospital.Exception.Scheduling;

/**
 * Thrown when a an error occurs during scheduling.
 * A message may be specified
 */
public class SchedulingException extends Exception {

    /**
     * Constructor
     */
    public SchedulingException() {
        super();
    }

    /**
     * Constructor
     * @param string details about the exception
     */
    public SchedulingException(String string) {
        super(string);
    }
}
