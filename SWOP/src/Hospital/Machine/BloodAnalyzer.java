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
 * The blood analyzer machine used for medical tests
 */
public class BloodAnalyzer extends Machine implements MachineAbstractFactory {

    public static final String BLOOD_ANALYZER_FACTORY = "New BloodAnalyzer";

    /**
     * Constructor
     * @param id the identifier of this machine
     * @param location this machine's location
     */
    public BloodAnalyzer(String id, Location location) {
        super(id, location);
    }

    /**
     * Returns a representation of this BloodAnalyzer
     * @return "BloodAnalyzer: $id at $location"
     */
    @Override
    public String toString() {
        return "BloodAnalyzer: " + getId() + " at " + getLocation() + " at campus: " + getCampus().getName();
    }

    /**
     * Makes a new BloodAnalyzer with the given Arguments.
     * @param args The ID and Location of this BloodAnalyzer
     * @return The new Machine
     * @throws WrongArgumentListException If the length is wrong or an Argument is of the wrong type.
     * @throws ArgumentNotAnsweredException If an Argument was not answered fully.
     * @throws ArgumentConstraintException If an Argument did not satisfy some constraints.
     * @throws ArgumentIsNullException If the argumentList of an Argument in the list was null.
     */
    @Override
    public Machine make(World w, CampusInfo info, Location location, String id) throws InvalidArgumentException, SchedulableAlreadyExistsException {
        try {
            Machine blood = new BloodAnalyzer(id, location);
            blood.setCampus(w.getCampusFromInfo(info));
            w.addSchedulable(blood);
            return blood;
        } catch (CannotChangeException ex) {
            Logger.getLogger(BloodAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Return the name of this factory.
     * @return This factoryname.
     */
    @Override
    public String getType() {
        return BLOOD_ANALYZER_FACTORY;
    }
}
