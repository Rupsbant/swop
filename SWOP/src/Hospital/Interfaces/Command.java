package Hospital.Interfaces;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;

/**
 * An interface for the command pattern used in factories.
 */
public interface Command {

    /**
     * Executes the command
     * @return details resulting from the execution
     * @throws CannotDoException When used this exception might arise from executing this command twice without undoing. <br> If a command is executed that blocks this one.
     */
    String execute() throws CannotDoException;

    /**
     * Returns whether the command was done.
     * @return a boolean indicating wether the command was executed
     */
    boolean isDone();

    /**
     * Undos the execution of the command
     * @return a string detailing the undoing of the command
     * @throws NotDoneException the action was not undone
     * @throws CannotDoException the command was not yet executed
     */
    String undo() throws NotDoneException, CannotDoException;
}
