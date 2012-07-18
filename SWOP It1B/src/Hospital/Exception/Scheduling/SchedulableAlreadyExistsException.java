package Hospital.Exception.Scheduling;

/**
 * Thrown when a schedulable object already exists in the world.
 */

public class SchedulableAlreadyExistsException extends Exception {

	/**
	 * Constructor
	 */
    public SchedulableAlreadyExistsException() {
        super();
    }

    /**
	 * Constructor
	 * @param string details about the exception 
	 */
    public SchedulableAlreadyExistsException(String string) {
        super(string);
    }

}
