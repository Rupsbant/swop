package Hospital.WareHouse.OrderPlacers;

import java.util.List;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Patient.Patient;
import Hospital.World.Campus;
import Hospital.World.World;

/**
 * Places automatic orders for meals
 */
public class MealOrderPlacer implements OrderPlacer {

    /**
     * The world in which this MealOrderPlacer resides
     */
    World w;
    /**
     * The campus that the number of patients need to be checked from.
     */
    Campus c;

    /**
     * Constructor
     * @param t the world from which we need to know the number of patients
     */
    public MealOrderPlacer(World t, Campus c) {
        w = t;
        this.c = c;
    }

    /**
     * @return the amount of meals that needs to be ordered
     * @see Hospital.WareHouse.OrderPlacers.OrderPlacer#checkStock(int, int, int)
     */
    @Override
    public int checkStock(int currentStockCount, int orderedItems, int maxStock) throws ArgumentConstraintException {
        return Math.max(0, Math.min(maxStock - currentStockCount - orderedItems, 15 + 3 * 2 * getNumberOfPatients() - (currentStockCount + orderedItems)));
    }

    /**
     * Gets the number of patients from the campus
     * @return the amount of patients checked into c
     */
    private int getNumberOfPatients() {
        List<Patient> temp = w.getResourceOfClass(Patient.class,c);
        int count = 0;
        for(Patient p : temp) {
        	if(!p.isDischarged()) count++;
        }
        return count;
    }
}
