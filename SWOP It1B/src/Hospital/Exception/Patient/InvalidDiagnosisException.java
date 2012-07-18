package Hospital.Exception.Patient;

/**
 * Thrown when a used Diagnosis did not exist or was otherwise invalid.
 */
public class InvalidDiagnosisException extends Exception{

	/**
	 * Constructor
	 */
    public InvalidDiagnosisException() {
        super();
    }

    /**
	 * Constructor
	 * @param string details about the exception 
	 */
    public InvalidDiagnosisException(String string) {
        super(string);
    }

}
