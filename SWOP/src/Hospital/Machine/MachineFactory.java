package Hospital.Machine;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.World.CampusInfo;
import Hospital.World.World;

public interface MachineFactory {

    /**
     * Makes a new UltraSoundMachine with the given arguments
     * @param args The arguments : id and location
     * @return The new UltraSound
     * @throws WrongArgumentListException If the length of the list was wrong or one of it's types.
     * @throws ArgumentNotAnsweredException If one of the arguments was not answered.
     * @throws ArgumentConstraintException If one of the arguments did not satisfy it's constraints.
     * @throws ArgumentIsNullException If one of the arguments was null.
     */
    Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException, SchedulableAlreadyExistsException;
    
    String getType();
	 
}
