package Hospital.Controllers;

import Hospital.Exception.Patient.CannotDischargeException;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;

public class WorldControllerTest {
	WorldController wc;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException {
        wc = TestUtil.getWorldControllerForTesting();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void dischargedPatientsTest() throws ArgumentConstraintException, ArgumentIsNullException, NoPersonWithNameAndRoleException, CannotDischargeException {
    	assertEquals("wrong number of not-discharged patients!",wc.getNotDischargedPatients().size(),2);    
    	assertTrue(wc.getNotDischargedPatients().contains("Jeroen"));
    	assertTrue(wc.getNotDischargedPatients().contains("Ruben"));
    	assertFalse(wc.getNotDischargedPatients().contains("Tom"));
    	wc.getWorld().getPersonByName(Patient.class, "Jeroen").dischargePatient();
    	assertFalse(wc.getNotDischargedPatients().contains("Jeroen"));
    	assertEquals("wrong number of not-discharged patients!",wc.getNotDischargedPatients().size(),1);    
    	wc.getWorld().getPersonByName(Patient.class, "Ruben").dischargePatient();
    	assertFalse(wc.getNotDischargedPatients().contains("Ruben"));
    	assertEquals("wrong number of not-discharged patients!",wc.getNotDischargedPatients().size(),0);    
    }

}
