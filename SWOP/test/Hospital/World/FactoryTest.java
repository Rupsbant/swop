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
import org.junit.Before;
import static org.junit.Assert.*;

public class FactoryTest {
    private WorldController wc;
    private DoctorController dc;
    private TreatmentController tc;
    private MedicalTestController mc;

    @Before
    public void setUp() throws NoPersonWithNameAndRoleException, ArgumentIsNullException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),wc.getLogins().get(4)); //Doctor house
        tc = new TreatmentController(wc, dc);
        mc = new MedicalTestController(wc, dc);
    }
    
    @Test
    public void testAvailability(){
        List<String> tests = Arrays.asList(tc.getAvailableTreatments());
        assertTrue("There should at least be the MedicationFactory", 0 < tests.size());
        assertTrue("MedicationFactory was not found", tests.contains("Medication"));
    }
}