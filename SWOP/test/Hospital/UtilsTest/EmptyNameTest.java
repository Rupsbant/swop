package Hospital.UtilsTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Hospital.Utils;

public class EmptyNameTest {

	@Test
	public void emptyNameTest(){
		assertTrue(Utils.testEmpty(""));
		assertTrue(Utils.testEmpty("    "));
		assertTrue(Utils.testEmpty("            "));
		assertTrue(Utils.testEmpty("\n  \n    "));
		assertFalse(Utils.testEmpty("\nn  \n    "));
		assertFalse(Utils.testEmpty("fdsq"));	
	}
	
}
