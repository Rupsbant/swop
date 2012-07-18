package Hospital.World;

import static org.junit.Assert.*;

import org.junit.Test;


public class TimeTest {
	
	
	
	@Test
    public void getLaterTimeTest(){
		Time t= new Time(2011,11,11,11,5);
		Time t2=t.getLaterTime(10);
		assertEquals(t.getYear(),t2.getYear());
		assertEquals(t.getMonth(),t2.getMonth());
		assertEquals(t.getDay(),t2.getDay());
		assertEquals(t.getHour(),t2.getHour());
		assertEquals(15,t2.getMinute());
	}
	
	
    @Test
    public void compareTo(){
		Time t= new Time(2011,11,11,11,5);
		Time t2=new Time(2011,11,11,11,15);
		Time t3= new Time(2011,11,11,11,15);
		Time t4= new Time(2011,11,11,10,5);
		assertTrue("t valt niet voor t3", t.compareTo(t3)<0);
		assertTrue("t2 valt niet op hetzelfde moment als t3", t2.compareTo(t3) == 0);
		assertTrue("t is niet later dan t4", t.compareTo(t4)>0);
	}
	
	@Test
	public void addDayTest(){
		Time t= new Time(2011,11,11,11,5);
		Time t2=TimeUtils.addDay(t);
		assertEquals(t.getYear(),t2.getYear());
		assertEquals(t.getMonth(),t2.getMonth());
		assertEquals(12,t2.getDay());
		assertEquals(t.getHour(),t2.getHour());
		assertEquals(t.getMinute(),t2.getMinute());
	}

}


