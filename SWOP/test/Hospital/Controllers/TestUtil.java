package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.World.BasicWorld;
import Hospital.World.World;

/**
 * Tools class for testing
 * @author SWOP-12
 */
public class TestUtil {

    /**
     * This method creates a worldController for testing purposes,
     * This includes some doctors and nurses to test with.
     * @return A worldController with a basic world to start with.
     */
    public static WorldController getWorldControllerForTesting() throws ArgumentIsNullException {
        WorldController out = BasicWorld.getBasicWorld();
        return out;
    }

    public static World getWorldForTesting() throws ArgumentIsNullException {
        World w = BasicWorld.getWorldForTesting();
        return w;
    }
}
