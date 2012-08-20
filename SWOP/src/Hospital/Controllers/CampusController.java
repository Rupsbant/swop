package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.WareHouse.Warehouse;
import Hospital.WareHouse.FoodStock.FoodStock;
import Hospital.World.Campus;

/**
 * Provides controlled access to campuses and campus-related functionality
 * This enables the following usecases: 
 * check in : has enough food to register a new patient
 * login : states that a loginController is logged in at the given campus
 */
@SystemAPI
public class CampusController {

    /**
     * the campus to be controlled
     */
    private Campus campus;

    /**
     * This creates the CampusController for the given Campus
     * @param c the campus to be controlled
     * @throws ArgumentIsNullException c was null
     */
    @SystemAPI
    public CampusController(Campus c) throws ArgumentIsNullException {
        if (c == null) {
            throw new ArgumentIsNullException("The given Campus was null");
        }
        campus = c;
    }

    Campus getCampus() {
        return campus;
    }

    /**
     * 
     * @return The amount of patients that can check in and have food. amount of food/6 - amount of patients.
     */
    int getFoodCapacityForPatients() {
        Warehouse w = campus.getWarehouse();
        try {
            return ((FoodStock) w.getStock("Meal")).getPatientCapacity();
        } catch (StockException e) {
            return 0;
        }
    }
}
