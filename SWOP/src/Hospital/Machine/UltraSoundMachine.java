package Hospital.Machine;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.CannotChangeException;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**.
 * Ultrasound machine used in medical tests
 */
public class UltraSoundMachine extends Machine implements MachineFactory {

    public static final String ULTRA_SOUND_MACHINE_FACTORY = "New UltraSoundMachine";

	/**
     * Constructor
     * @param id a unique identifier
     * @param location the location of this Ultrasound machine
     */
    public UltraSoundMachine(String id, Location location) {
        super(id, location);
    }

    /**
     * Returns a representation of this machine
     * @return "UltraSoundMachine: $id at $location"
     */
    @Override
    public String toString() {
        return "UltraSoundMachine: " + getId() + " at " + getLocation() + " at campus: " + getCampus().getName();
    }

    /**
     * Makes a new UltraSoundMachine with the given arguments
     * @param args The arguments : id and location
     * @return The new UltraSound
     * @throws WrongArgumentListException If the length of the list was wrong or one of it's types.
     * @throws ArgumentNotAnsweredException If one of the arguments was not answered.
     * @throws ArgumentConstraintException If one of the arguments did not satisfy it's constraints.
     * @throws ArgumentIsNullException If one of the arguments was null.
     */
    @Override
    public Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException {
        try {
            return new UltraSoundMachine(id, location).setCampus(w.getCampusFromInfo(info));
        } catch (CannotChangeException ex) {
            throw new Error("Cannot happen");
        }
    }

    /**
     * Returns the name of this factory
     * @return "New UltraSoundMachine"
     */
    @Override
    public String getType() {
        return ULTRA_SOUND_MACHINE_FACTORY;
    }
}
