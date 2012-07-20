package Hospital.Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.People.LoginInfo;

public class AvailableFactoriesTest {
	WorldController wc;
	StaffController sc;
	AdministratorController ac;
	MachineController mc;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
    	wc = TestUtil.getWorldControllerForTesting();
         List<LoginInfo> logins = wc.getLogins();
         for (int i = 0; i < logins.size(); i++) {
             if (logins.get(i).getRole().equals("HospitalAdministrator")) {
                 ac = (AdministratorController) wc.login(wc.getCampuses().get(0),logins.get(i));
             }
         }
        sc = new StaffController(wc, ac);
        mc = new MachineController(wc, ac);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void availableStaffFactoryTest() {
    	String[] fac = sc.getAvailableStaffFactories();
    	ArrayList<String> facs = new ArrayList<String>();
    	for(int i=0;i<fac.length;i++)
    		facs.add(fac[i]);
    	assertEquals("wrong number of staff factories",facs.size(),3);
    	assertTrue(facs.contains("New Nurse"));
    	assertTrue(facs.contains("New Doctor"));
    	assertFalse(facs.contains("New HospitalAdministrator"));
    	assertTrue(facs.contains("New WarehouseManager"));
    }
    
    @Test
    public void availableMachineFactoryTest() {
    	String[] fac = mc.getAvailableMachineFactories();
    	ArrayList<String> facs = new ArrayList<String>();
    	for(int i=0;i<fac.length;i++)
    		facs.add(fac[i]);
    	assertEquals("wrong number of staff factories",facs.size(),4);
    	assertTrue(facs.contains("New BloodAnalyzer"));
    	assertTrue(facs.contains("New XRayMachine"));
    	assertTrue(facs.contains("New SurgicalEquipment"));
    	assertTrue(facs.contains("New UltraSoundMachine"));
    	assertFalse(facs.contains("New HospitalAdministrator"));
    	assertFalse(facs.contains("New WarehouseManager"));
    	assertFalse(facs.contains("New XRayMachine "));
    }
    
}
