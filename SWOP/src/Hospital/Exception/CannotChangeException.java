package Hospital.Exception;

/**
 * This exception is thrown when an argument is given a value a second time.
 */
public class CannotChangeException extends Exception{
    
	/**
	 * Constructor
	 */
    public CannotChangeException() {
        super();
    }

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
    public CannotChangeException(String string) {
        super(string);
    }

}
