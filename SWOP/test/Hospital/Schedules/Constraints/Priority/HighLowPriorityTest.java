package Hospital.Schedules.Constraints.Priority;

import org.junit.Test;
import static org.junit.Assert.*;

public class HighLowPriorityTest {

    /**
     * Test of isHigh method, of class HighLowPriority.
     */
    @Test
    public void testIsHigh() {
        HighLowPriority instance1 = new HighLowPriority(true);
        HighLowPriority instance2 = new HighLowPriority(true);
        HighLowPriority instance3 = new HighLowPriority(false);
        HighLowPriority instance4 = new HighLowPriority(false);

        assertTrue(instance1.thisCanPreempt(instance3));

        assertFalse(instance3.thisCanPreempt(instance1));
        
        assertFalse(instance1.thisCanPreempt(instance2));

        assertFalse(instance4.thisCanPreempt(instance3));
    }
}