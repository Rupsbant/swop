package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.WareHouse.Warehouse;
import Hospital.WareHouse.WarehouseMaker;

/**
 * A campus is a spatially grouped collection of buildings and places belonging to the hospital.
 */
public class Campus {

    /**
     * the name this campus goes by
     */
    private String name;
    /**
     * the warehouse this campus uses
     */
    private Warehouse warehouse;

    public Campus(String name, World w) {
        this.name = name;
        this.warehouse = WarehouseMaker.getWarehouse(w, this);
    }

    /**
     * returns the time it takes to travel to this campus
     * @param campusThisAppointment the campus from where we leave
     * @return the time it takes to travel in minutes, 0 if this campus was given
     */
    public int getTravelTime(Campus campusThisAppointment) {
        if (this.equals(campusThisAppointment)) {
            return 0;
        } else {
            return 15;
        }
    }

    /**
     * @return the name of the campus
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Campus) {
            Campus c = (Campus) o;
            return this.getName().equals(c.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * creates a CampusInfo-object identifying this campus
     * @return CampusInfo-object
     */
    public CampusInfo getCampusInfo() {
        try {
            return new CampusInfo(this.getName());
        } catch (ArgumentIsNullException e) {
            throw new Error("Java is broken.");
        }
    }

    /**
     * Gets the warehouse that stores the items in this campus
     * @return the warehouse
     */
    public Warehouse getWarehouse() {
        return this.warehouse;
    }
}
