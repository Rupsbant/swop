package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.WareHouse.Items.MedicationItem;
import Hospital.World.World;

/**
 * This class is used to enter one or multiple comma seperated itemnames to a answer
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
     */
    @Override
    public void setWorld(World w) {
        try {
            items = w.getCampusFromInfo(w.getCampuses().get(0)).getWarehouse().getStockNamesByType(MedicationItem.class).toArray(new String[0]);
        } catch (ArgumentConstraintException ex) {
            throw new Error("The campus always exists");
        }
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
