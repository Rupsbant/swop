package Hospital.Machine;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**
 * A AbstractFactory pattern 
 * @author Rupsbant
 */
public interface MachineAbstractFactory {

    /**
     * Makes a new Machine with the given arguments
     * @param w the world this machine must be added to
     * @param id A unique id for this machine
     * @param info The campus this machine is stationed at
     * @param location The location this machine is placed on the campus
     * @return a description of the new machine
     * @throws InvalidArgumentException If one of the arguments did not satisfy it's constraints, or was null
     * @throws SchedulableAlreadyExistsException If a machine with the same unique id exists in the world 
     */
    Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException, SchedulableAlreadyExistsException;
    
    /**
     * Returns the type this AbstractFactory makes
     * @return String a description of this MachineFactory
     */
    String getType();
	 
}
