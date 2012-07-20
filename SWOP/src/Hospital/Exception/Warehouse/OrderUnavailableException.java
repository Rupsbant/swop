package Hospital.Exception.Warehouse;

/**
 * Thrown when an order is unavailable
 */
public class OrderUnavailableException extends Exception {

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
	public OrderUnavailableException(String string) {
		super(string);
	}

}
