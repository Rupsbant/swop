package Hospital.Exception.Command;

/**
 * Thrown when an action could not be performed.
 */
public class CannotDoException extends Exception{

	/**
	 * Constructor
	 */
    public CannotDoException() {
        super();
    }

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
    public CannotDoException(String string) {
        super(string);
    }

}
