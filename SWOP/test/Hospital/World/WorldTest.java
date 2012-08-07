package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WorldTest {
    private WorldController wc;

    @Before
    public void setUp() throws ArgumentIsNullException {
        wc = TestUtil.getWorldControllerForTesting();
    }

    @Test
    public void testTime() throws ArgumentIsNullException{
    	wc.getTime().equals(new Time());
    }
}
