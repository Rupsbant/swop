package Hospital.Exception.Warehouse;

/**
 * Thrown when an requested stock type does not exist
 */
public class IllegalStockTypeException extends Exception {

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
	public IllegalStockTypeException(String string) {
		super(string);
	}

}
