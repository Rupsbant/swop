package Hospital.Machine;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**.
 * Ultrasound machine used in medical tests
 */
public class UltraSoundMachine extends Machine implements MachineAbstractFactory {

    /**
     * The description of this MachineFactory
     */
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
     * @return The new UltraSound
     */
    @Override
    public Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException, SchedulableAlreadyExistsException {
        try {
            final UltraSoundMachine ultraSoundMachine = new UltraSoundMachine(id, location);
            ultraSoundMachine.setCampus(w.getCampusFromInfo(info));
            w.addSchedulable(ultraSoundMachine);
            return ultraSoundMachine;
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
