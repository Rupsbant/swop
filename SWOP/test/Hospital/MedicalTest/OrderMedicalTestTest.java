package Hospital.MedicalTest;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.MedicalTestController;
import Hospital.Controllers.TestUtil;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Controllers.WorldController;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.People.LoginInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class OrderMedicalTestTest {

    WorldController wc;
    DoctorController dc;
    MedicalTestController mc;
    
    public OrderMedicalTestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws ArgumentIsNullException, NoPersonWithNameAndRoleException, ArgumentConstraintException {
        wc = TestUtil.getWorldControllerForTesting();
        dc = (DoctorController) wc.login(wc.getCampuses().get(0),new LoginInfo("Gregory House", "Doctor")); //don't search for doctor
        mc = new MedicalTestController(wc, dc);
    }

    @After
    public void tearDown() {
    }

    /**
     * This tests if the factories are added correctly:
     * Each factory should have an String and MedicalTestFactory in the map registeredTests
     *
     * We can only test the ones that are added and not the complete count.
     */
    @Test
    public void testFactories() throws ArgumentIsNullException {
    }

    @Test
    public void getArguments() throws NotLoggedInException, NotAFactoryException{
        PublicArgument[] args = mc.getMedicalTestArguments("New XRayScan").getPublicArguments();
        assertEquals("Wrong length", 4, args.length);
        assertEquals("Wrong type, first", StringArgument.class, args[0].getClass());
        assertEquals("Wrong type, second", IntegerArgument.class, args[1].getClass());
        assertEquals("Wrong type, second", IntegerArgument.class, args[2].getClass());
        assertEquals("Wrong question, first", "Enter the body part: ", args[0].getQuestion());
        assertEquals("Wrong question, second", "Enter number of images: ", args[1].getQuestion());
        assertEquals("Wrong question, second", "Enter zoom: ", args[2].getQuestion());
    }


    @Test
    public void breakFactoryName() throws NotLoggedInException{
        try {
            mc.getMedicalTestArguments(null);
            fail("throw exception");
        } catch (NotAFactoryException ex) {}
        try {
            mc.getMedicalTestArguments("qdsfhjkldfhsjkdfhqsdkl"); //I hope they never create this mdeicalTest...
            fail("throw exception");
        } catch (NotAFactoryException ex) {}
    }


}