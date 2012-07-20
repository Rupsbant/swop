package Hospital.World;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.MedicalTestController;
import Hospital.Controllers.TestUtil;
import Hospital.Controllers.TreatmentController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class FactoryTest {
    WorldController wc;
    DoctorController dc;
    TreatmentController tc;
    MedicalTestController mc;

    public FactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws NoPersonWithNameAndRoleException, ArgumentIsNullException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),wc.getLogins().get(4)); //Doctor house
        tc = new TreatmentController(wc, dc);
        mc = new MedicalTestController(wc, dc);
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testAvailability(){
        List<String> tests = Arrays.asList(tc.getAvailableTreatments());
        assertTrue("There should at least be the MedicationFactory", 0 < tests.size());
        assertTrue("MedicationFactory was not found", tests.contains("Medication"));

        tests = Arrays.asList(mc.getAvailableMedicalTests());
        assertTrue("There should at least be the XRayFactory", 0 < tests.size());
        assertTrue("XRayFactory was not found", tests.contains("New XRayScan"));
    }
}