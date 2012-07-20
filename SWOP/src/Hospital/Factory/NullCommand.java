package Hospital.Factory;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;

/**
 * This command does nothing.
 * Singleton pattern and stateless
 * @author SWOP-12
 */
public class NullCommand implements Command {

    public static NullCommand singleton = new NullCommand();

    public String execute() throws CannotDoException {
        //return "NullCommand: Nothing to do here\n";
        return "";
    }

    public boolean isDone() {
        return true;
    }

    public String undo() throws NotDoneException, CannotDoException {
        //return "NullCommand: Nothing to undo\n";
        return "";
    }

}
