package Hospital.WareHouse.Items;

import Hospital.Argument.Argument;
import Hospital.WareHouse.Item;

/**
 * Plaster for cast-treatments
 */
public class Plaster extends Item {

    /**
     * @return "Plaster"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Plaster";
    }

    /**
     * @return another Plaster item
     * @see Hospital.WareHouse.Item#clone()
     */
    @Override
    public Plaster clone() {
        return new Plaster();
    }

    /**
     * @see Hospital.WareHouse.ItemFactory#clone(Hospital.Argument.Argument[])
     */
    public Item clone(Argument[] args) {
        return clone();
    }

    /**
     * @return "Plaster"
     * @see Hospital.WareHouse.Item#getName()
     */
    @Override
    public String getName() {
        return "Plaster";
    }
}
