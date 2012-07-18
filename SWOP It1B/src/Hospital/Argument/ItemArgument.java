package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.WareHouse.Items.MedicationItem;
import Hospital.World.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class supports the adding of the name of a item to the argumentList
 */
@SystemAPI
public class ItemArgument extends StringArgument implements WorldPopulatedArgument {

    private String[] items;
    
    /**
     * Constructor
     * @param question The question to ask for the Argument
     *                  For information purposes only.
     */
    public ItemArgument(String question) {
        super(question);
    }

    /**
     * Populates the Argument with the names of all MedicationItems.
     * Since all warehouses are the same we can get these from just one.
     * @param w The world to get the MedicationItem names from
     * @return this
     */
    public WorldPopulatedArgument setWorld(World w) {
        try {
            items = w.getCampusFromInfo(w.getCampuses().get(0)).getWarehouse().getStockNamesByType(MedicationItem.class).toArray(new String[0]);
        } catch (ArgumentConstraintException ex) {
            Logger.getLogger(ItemArgument.class.getName()).log(Level.SEVERE, "The campus exists always", ex);
        }
        return this;
    }

    /**
     * Returns the names of the MedicationItems
     * @return names of Medication
     */
    @SystemAPI
    public String[] getPossibleAnswers() {
        return items;
    }
}
