package Hospital.Machine;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.World.CampusInfo;
import Hospital.World.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Surgical equipment used for treatments
 */
public class SurgicalEquipment extends Machine implements MachineFactory {

    public static final String SURGICAL_EQUIPMENT_FACTORY = "New SurgicalEquipment";

    /**
     * Constructor
     * @param id a unique identifier
     * @param location the location of this surgical equipment
     */
    public SurgicalEquipment(String id, Location location) {
        super(id, location);
    }

    /**
     * Makes a representation of this object.
     * @return "SurgicalEquipment: $id at $location"
     */
    @Override
    public String toString() {
        return "SurgicalEquipment: " + getId() + " at " + getLocation() + " at campus: " + getCampus().getName();
    }

    /**
     * Makes a new SurgicalEquipment with the given arguments.
     * @param args The arguments to make this machine with: id and location.
     * @return A new SurgicalEquipment
     * @throws WrongArgumentListException If the list is of the wrong length or contains the wrong types.
     * @throws ArgumentNotAnsweredException If an Argument was not answered.
     * @throws ArgumentConstraintException If an Argument did not satisfy it's constraints.
     * @throws ArgumentIsNullException If an Argument was null.
     */
    @Override
    public Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException, SchedulableAlreadyExistsException {
        try {
            SurgicalEquipment surgicalEquipment = new SurgicalEquipment(id, location);
            surgicalEquipment.setCampus(w.getCampusFromInfo(info));
            w.addSchedulable(surgicalEquipment);
            return surgicalEquipment;
        } catch (CannotChangeException ex) {
            Logger.getLogger(SurgicalEquipment.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Returns the name of this factory
     * @return "New SurgicalEquipment"
     */
    @Override
    public String getType() {
        return SURGICAL_EQUIPMENT_FACTORY;
    }
}
