package Hospital.Argument;

import Hospital.World.World;

/**
 * Interface to populate information in Arguments that need it.
 * @author SWOP-12
 */
public interface WorldPopulatedArgument {

    /**
     * Populate this object with all the information from the world w
     * @param w the world to populate with
     * @return this for chaining
     */
    WorldPopulatedArgument setWorld(World w);
}
