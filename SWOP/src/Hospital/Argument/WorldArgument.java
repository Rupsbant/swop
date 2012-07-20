package Hospital.Argument;

import Hospital.World.World;

/**
 * This is a private argument to give the world to the factories etc.
 * @author SWOP-12
 */
public class WorldArgument implements Argument<World>, WorldPopulatedArgument {

    private World w;

    /**
     * Returns the world this argument was populated with
     * @return world
     */
    public World getAnswer() {
        return w;
    }

    /**
     * Returns the question with this argument
     * @return "The world as an argument to use"
     */
    public String getQuestion() {
        return "The world as an argument to use";
    }

    /**
     * Sets the world in this argument
     * @param w The answer this WorldArgument needs to give.
     * @return this
     */
    public WorldArgument setWorld(World w) {
        this.w = w;
        return this;
    }
}
