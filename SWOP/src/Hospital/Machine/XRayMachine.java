package Hospital.Machine;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**
 * X-ray machine used in medical tests
 * The XRaymachine is self-replicating, a new XRayMachine can always be made from it's own object.
 */
public class XRayMachine extends Machine implements MachineAbstractFactory {

    /**
     * The description of this MachineFactory
     */
    public static final String X_RAY_MACHINE_FACTORY = "New XRayMachine";

    /**
     * Constructor
     * @param id a unique identifier
     * @param location the location of this X-ray machine
     */
    public XRayMachine(String id, Location location) {
        super(id, location);
    }

    /**
     * Returns a representation of this machine.
     * @return "XRayMachine: $id at $location"
     */
    @Override
    public String toString() {
        return "XRayMachine: " + getId() + " at " + getLocation() + " at campus: " + getCampus().getName();
    }

    /**
     * Makes a new factory with the given arguments: id and location
     * @param w The world this machine must be added to
     * @param info the Campus this machine is stationed at
     * @param location The location of this machine
     * @param id A unique id of this machine
     * @return The new XRayMachine
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     * @throws SchedulableAlreadyExistsException thrown if a machine with the same id already exists 
     */
    @Override
    public Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException, SchedulableAlreadyExistsException {
        try {
            XRayMachine xRayMachine = new XRayMachine(id, location);
            xRayMachine.setCampus(w.getCampusFromInfo(info));
            w.addSchedulable(xRayMachine);
            return xRayMachine;
        } catch (CannotChangeException ex) {
            throw new Error("This cannot happen");
        }
    }

    /**
     * Return the name of this factory
     * @return "New XRayMachine"
     */
    public String getType() {
        return X_RAY_MACHINE_FACTORY;
    }
}
