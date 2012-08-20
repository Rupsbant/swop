package Hospital.People;

import Hospital.Controllers.CommandInfo;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;
import Hospital.Interfaces.Command;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandHistory {

    /**
     * commandinfos from recent done commands
     */
    private ArrayList<CommandInfo> recent = new ArrayList<CommandInfo>();
    /**
     * commandinfos from recent undone commands
     */
    private ArrayList<CommandInfo> undone = new ArrayList<CommandInfo>();

    /**
     * get recent commands
     * @return latest 20 recent commands
     */
    public CommandInfo[] getRecentCommands() {
        return Arrays.copyOf(recent.toArray(new CommandInfo[0]), Math.min(recent.size(), 20));
    }

    /**
     * getundonecommands
     * @return latest 5 undone commands
     */
    public CommandInfo[] getUndoneCommands() {
        return Arrays.copyOf(undone.toArray(new CommandInfo[0]), Math.min(undone.size(), 5));
    }

    /**
     * adds a command to recent commands
     * @param comm command to be added
     */
    public String addCommand(Command comm) throws CannotDoException {
        recent.add(new CommandInfo(comm));
        return comm.execute();
    }

    /**
     * undo's a command
     * @param commInfo commandinfo to undo
     * @param command command to undo
     * @throws CannotDoException when command cannot be undone
     * @return the details of undoing the command
     */
    public String undoCommand(CommandInfo commInfo, Command command) throws CannotDoException {
        if(!commInfo.equals(command)){
            throw new Error("the Command and CommandInfo should always be linked");
        }
        if (!recent.contains(commInfo)) {
            throw new IllegalArgumentException("CommandInfo not found");
        }
        try {
            String string = command.undo();
            recent.remove(commInfo);
            undone.add(commInfo);
            return string;
        } catch (NotDoneException ex) {
            throw new Error("Command was done");
        }
    }

    /**
     * redo's a command
     * @param commInfo commandinfo to redo
     * @param command command to redo
     * @throws CannotDoException when command cannot be redone
     * @return the details of redoing the command
     */
    public String redoCommand(CommandInfo commInfo, Command command) throws CannotDoException {
        if(!commInfo.equals(command)){
            throw new Error("the Command and CommandInfo should always be linked");
        }
        if (!undone.contains(commInfo)) {
            throw new IllegalArgumentException("CommandInfo not found");
        }
        String string = command.execute();
        undone.remove(commInfo);
        recent.add(commInfo);
        return string;
    }
}
