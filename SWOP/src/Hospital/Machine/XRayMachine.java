package Hospital.Machine;

import Hospital.Argument.Argument;
import Hospital.Argument.CampusInfoArgument;
import Hospital.Argument.StringArgument;
import Hospital.Argument.WorldArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.CannotChangeException;
import Hospital.Utils;
import Hospital.World.CampusInfo;
import Hospital.World.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * X-ray machine used in medical tests
 */
public class XRayMachine extends Machine implements MachineFactory {

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
    public Machine make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String id = Utils.getAnswer(StringArgument.class, "ID", args[0]);
        Location location = new Location(Utils.getAnswer(StringArgument.class, "Location", args[1]));
        CampusInfo info = Utils.getAnswer(CampusInfoArgument.class, "info", args[2]);
        World w = Utils.getAnswer(WorldArgument.class, "world", args[3]);
        try {
            return new XRayMachine(id, location).setCampus(w.getCampusFromInfo(info));
        } catch (CannotChangeException ex) {
            Logger.getLogger(XRayMachine.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Return the name of this factory
     * @return "New XRayMachine"
     */
    public String getName() {
        return X_RAY_MACHINE_FACTORY;
    }
}
