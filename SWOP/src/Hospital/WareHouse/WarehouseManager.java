package Hospital.WareHouse;

import Hospital.Controllers.CampusController;
import Hospital.Controllers.LoginController;
import Hospital.Controllers.WareHouseController;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.People.Staff;
import Hospital.People.StaffRole;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import java.util.List;

/**
 * The staff member who manages the warehouse
 */
public class WarehouseManager extends Staff {

    private Campus campus;

    /**
     * Constructor
     * @param name the name of this warehouse manager
     * @throws ArgumentConstraintException the name was invalid (eg empty)
     * @throws ArgumentIsNullException the name was null
     */
    public WarehouseManager(String name) throws ArgumentConstraintException,
            ArgumentIsNullException {
        super(name);
    }

    /**
     * @see Hospital.People.Staff#login()
     */
    @Override
    public LoginController login(CampusController cc) {
        return new WareHouseController(this, cc);
    }

    /**
     * @return "WareHouseManager"
     * @see Hospital.People.Staff#getRole()
     */
    @Override
    public StaffRole getRole() {
        return StaffRole.WarehouseManager;
    }

    /**
     * Returns the permanent campus of this nurse.
     * @return campus.
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets the campus one time.
     */
    public WarehouseManager setCampus(Campus campus) throws CannotChangeException {
        if (this.campus != null) {
            throw new CannotChangeException("Can't set a campus of a nurse a second time");
        }
        this.campus = campus;
        return this;
    }

    public List<TimeFrameConstraint> getConstraints() {
        throw new UnsupportedOperationException("Warehousemanager has no appointments");
    }
}
