package Hospital.Exception;

/**
 * Thrown when the wrong Treatment type was used
 */
public class WrongTreatmentException extends Exception{

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
    public WrongTreatmentException(String string) {
        super(string);
    }

    /**
	 * Constructor
	 */
    public WrongTreatmentException() {
        super();
    }
}
