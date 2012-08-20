package Hospital.Argument;

import Hospital.World.CampusInfo;
import Hospital.World.World;

/**
 * Campus version of an Argument, selectes a Campus from a list.
 * @author SWOP-12
 */
public final class CampusInfoArgument extends ListArgument<CampusInfo> implements PublicArgument<CampusInfo>, WorldPopulatedArgument {

    /**
     * Constructor, Creates a CampusInfoArgument with a given question
     * @param question the question to ask a user.
     */
    public CampusInfoArgument(String question) {
        super(question);
    }

    /**
     * Retrieves all CampusInfo objects from the world to select the Campus from
     * @param w The world that has the Campuses
     */
    @Override
    public void setWorld(World w) {
        setPossible(w.getCampuses());
    }
}
