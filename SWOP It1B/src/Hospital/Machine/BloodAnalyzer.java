package Hospital.Machine;

import Hospital.Argument.Argument;
import Hospital.Argument.CampusInfoArgument;
import Hospital.Argument.StringArgument;
import Hospital.Argument.WorldArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.CannotChangeException;
import Hospital.Utils;
import Hospital.World.CampusInfo;
import Hospital.World.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The blood analyzer machine used for medical tests
 */
public class BloodAnalyzer extends Machine implements MachineFactory {

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
    public Machine make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String id = Utils.getAnswer(StringArgument.class, "ID", args[0]);
        Location location = new Location(Utils.getAnswer(StringArgument.class, "Location", args[1]));
        CampusInfo info = Utils.getAnswer(CampusInfoArgument.class, "info", args[2]);
        World w = Utils.getAnswer(WorldArgument.class, "world", args[3]);
        try {
            return new BloodAnalyzer(id, location).setCampus(w.getCampusFromInfo(info));
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
    public String getName() {
        return BLOOD_ANALYZER_FACTORY;
    }
}
