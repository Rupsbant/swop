package Hospital.Exception;

/**
 * Thrown when an Info-object was invalid.
 */
public class IllegalInfo extends Exception {

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
    public IllegalInfo(String string) {
        super(string);
    }

    /**
	 * Constructor 
	 */
    public IllegalInfo() {
    }


}
