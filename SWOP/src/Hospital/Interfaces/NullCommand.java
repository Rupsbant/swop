package Hospital.Interfaces;

/**
 * This command does nothing.
 * Singleton pattern and stateless
 * @author SWOP-12
 */
public class NullCommand implements Command {

    public static NullCommand singleton = new NullCommand();

    /**
     * Does nothing
     * @return the empty String ""
     */
    public String execute() {
        return "";
    }

    /**
     * Is always done and finished
     * @return true
     */
    public boolean isDone() {
        return true;
    }

    /**
     * Does nothing
     * @return the empty String ""
     */
    public String undo() {
        return "";
    }

}
