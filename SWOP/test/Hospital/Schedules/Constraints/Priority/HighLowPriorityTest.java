package Hospital.Schedules.Constraints.Priority;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HighLowPriorityTest {

    public HighLowPriorityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isHigh method, of class HighLowPriority.
     */
    @Test
    public void testIsHigh() {
        HighLowPriority instance1 = new HighLowPriority(true);
        HighLowPriority instance2 = new HighLowPriority(true);
        HighLowPriority instance3 = new HighLowPriority(false);
        HighLowPriority instance4 = new HighLowPriority(false);

        assertTrue(instance1.canPreempt(instance3));
        assertTrue(instance3.canBePreemptedBy(instance1));

        assertFalse(instance3.canPreempt(instance1));
        assertFalse(instance1.canBePreemptedBy(instance3));
        
        assertFalse(instance1.canPreempt(instance2));
        assertFalse(instance1.canBePreemptedBy(instance2));

        assertFalse(instance4.canPreempt(instance3));
        assertFalse(instance4.canBePreemptedBy(instance3));
    }
}