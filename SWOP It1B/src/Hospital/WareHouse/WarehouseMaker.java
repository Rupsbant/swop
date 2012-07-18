package Hospital.WareHouse;

import Hospital.WareHouse.FoodStock.FoodStock;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.WareHouse.Items.ActivatedCarbon;
import Hospital.WareHouse.Items.Aspirin;
import Hospital.WareHouse.Items.Meal;
import Hospital.WareHouse.Items.MedicationItem;
import Hospital.WareHouse.Items.Misc;
import Hospital.WareHouse.Items.Plaster;
import Hospital.WareHouse.Items.SleepingTablet;
import Hospital.WareHouse.Items.Vitamin;
import Hospital.WareHouse.OrderPlacers.FullyStockOrderPlacer;
import Hospital.WareHouse.OrderPlacers.MealOrderPlacer;
import Hospital.WareHouse.OrderPlacers.NormalOrderPlacer;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.World;
import Hospital.World.WorldTime;

/**
 * A static factory for creating warehouses
 */
public class WarehouseMaker {
    
	/**
	 * Creates a new Warehouse
	 * @param w the world in which this warehouse exists
	 * @param c the campus which this warehouse will supply
	 * @return a Warehouse-object
	 */
    public static Warehouse getWarehouse(World w, Campus c){
        Warehouse warehouse = new Warehouse();
        try {
            // we zetten de expiringtime van voedsel op 7 dagen en van medicatie op 3 jaar
            // dit is enkel voor de beginstock en is random gekozen (stond nergens in opgave)
            WorldTime wt = w.getWorldTime();
            Time time = wt.getTime();
            Time expiringTime1 = time.getLaterTime(7 * 24 * 60);
            Time expiringTime2 = time.getLaterTime(3 * 365 * 24 * 60);

            Stock plasterStock = new Stock(8, new FullyStockOrderPlacer(), new Plaster(), wt);
            warehouse.addStock(plasterStock);
            
            Stock mealStock = new FoodStock(120, new MealOrderPlacer(w,c), new Meal(expiringTime1), wt, w, c);
            warehouse.addStock(mealStock);
            
            MedicationItem[] meds = new MedicationItem[]{new ActivatedCarbon(expiringTime2), new Misc(expiringTime2),
                new Aspirin(expiringTime2), new SleepingTablet(expiringTime2), new Vitamin(expiringTime2)};
            for (MedicationItem med : meds) {
                Stock temp = new Stock(10, new NormalOrderPlacer(), med, wt);
                warehouse.addStock(temp);
            }

        } catch (ArgumentConstraintException e) {
            throw new RuntimeException("argumentcontraint exception shouldnt be thrown here!");
        } catch (ArgumentIsNullException e) {
            throw new RuntimeException("argumentisnull exception shouldnt be thrown here!");
        }
        return warehouse;
    }

}
