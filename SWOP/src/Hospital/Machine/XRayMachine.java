package Hospital.Machine;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**
 * X-ray machine used in medical tests
 */
public class XRayMachine extends Machine implements MachineAbstractFactory {

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
     * @param args The arguments to make the machine with
     * @return The new XRayMachine
     * @throws WrongArgumentListException If the length of the list is wrong or one of the argumentTypes in the list.
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
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
