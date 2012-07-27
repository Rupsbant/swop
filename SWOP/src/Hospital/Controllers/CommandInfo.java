package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Factory.Command;

/**
 * Identifies a command in the system
 */
@SystemAPI
public class CommandInfo {

    /**
     * the command to represent
     */
    private Command original;

    /**
     * Constructor
     * @param original the command to represent
     */
    public CommandInfo(Command original) {
        this.original = original;
    }

    /**
     * @return the details of the represented command as string
     * @see java.lang.Object#toString()
     */
    @Override
    @SystemAPI
    public String toString() {
        return original.toString();
    }

    /**
     * Gets the command represented by this object
     * @return a Command-object
     */
    @SystemAPI
    Command getCommand() {
        return original;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            return true;
        }
        if(obj instanceof CommandInfo){
            return original.equals(((CommandInfo)obj).original);
        }
        return original.equals(obj);
    }
    
    
}
