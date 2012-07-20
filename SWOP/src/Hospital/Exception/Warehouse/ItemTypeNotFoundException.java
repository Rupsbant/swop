package Hospital.Exception.Warehouse;

/**
 * Thrown when a requested item type was not found
 * @author s0219269
 *
 */
public class ItemTypeNotFoundException extends Exception {

	/**
	 * Constructor
	 * @param string details about the exception 
	 */
	public ItemTypeNotFoundException(String string) {
		super(string);
	}

}
