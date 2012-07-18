package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.MedicalTest.MedicalTestFactory;
import Hospital.Treatments.TreatmentFactory;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.WorldController;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupsbant
 */
public class WorldTest {
    WorldController wc;

    public WorldTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException {
        wc = TestUtil.getWorldControllerForTesting();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTime() throws ArgumentIsNullException{
    	wc.getTime().equals(new Time());
    }
}
