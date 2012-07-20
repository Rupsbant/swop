package Hospital.Schedules.Constraints.Warehouse;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Treatments.Treatment;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.World.Time;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.World.Campus;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.ConstraintSolver.GetC;
import Hospital.World.BasicWorld;
import Hospital.World.World;
import Hospital.Schedules.TimeFrame;
import Hospital.Treatments.Medication;
import Hospital.WareHouse.ItemInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemConstraintTest {
    public static final String MEDICATION_NAME = "Aspirin";
    World w = BasicWorld.getWorldForTesting();
    Patient ruben;
    Patient jeroen;
    Doctor d;
    private Campus campusNorth;

    public ItemConstraintTest()  throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
        this.ruben = w.getPersonByName(Patient.class, "Ruben");
        this.jeroen = w.getPersonByName(Patient.class, "Jeroen");
        this.d = w.getPersonByName(Doctor.class, "Gregory House");
        campusNorth = w.getCampusFromInfo(w.getCampuses().get(0));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws NotEnoughItemsAvailableException, StockException, ItemNotReservedException {
        campusNorth.getWarehouse().getStock(MEDICATION_NAME).reserveItem(6, w.getWorldTime().getTime());
        campusNorth.getWarehouse().getStock(MEDICATION_NAME).reserveItem(2, new Time(2011,11,9,12,0));
        campusNorth.getWarehouse().getStock(MEDICATION_NAME).reserveItem(4, new Time(2011,11,12,10,0));
        campusNorth.getWarehouse().getStock(MEDICATION_NAME).reserveItem(4, new Time(2011,11,15,10,0));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isAccepted method, of class ItemConstraint.
     */
    @Test
    public void testIsAccepted() throws ArgumentIsNullException, ArgumentConstraintException {
        System.out.println("isAccepted");
        ItemInfo toOrder = new ItemInfo(MEDICATION_NAME, 3);
        Treatment t = new Medication("MedicationTest", Boolean.TRUE, new ItemInfo[]{toOrder});
        ItemConstraint instance = new ItemConstraint(new GetC(campusNorth), t);
        instance.setTimeFrame(new TimeFrame(new Time(2011, 11, 9, 13, 0), 20));
        assertFalse(instance.isAccepted());

        toOrder.addCount(-1);
        instance.reset();
        instance.setTimeFrame(new TimeFrame(new Time(2011, 11, 9, 13, 0), 20));
        assertTrue(instance.isAccepted());
    }
}