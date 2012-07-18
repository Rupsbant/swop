package Hospital.Exception;


/**
 * Thrown when it is tried to add a Treatment that is already added.
 */
public class TreatmentAlreadyAddedException extends Exception {

	/**
	 * Constructor
	 */
    public TreatmentAlreadyAddedException() {
    }

    /**
	 * Constructor
	 * @param string details about the exception 
	 */
    public TreatmentAlreadyAddedException(String string){
        super(string);
    }



}
