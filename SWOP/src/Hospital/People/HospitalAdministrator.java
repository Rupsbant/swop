package Hospital.People;

import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.CampusController;
import Hospital.Controllers.LoginController;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.World.Campus;

/**
 * The hospitals administrator, responsible for adding machines and staff, and forwarding the time of the system
 *
 */
public class HospitalAdministrator extends Staff {

    /**
     * Constructor
     * @param name the name of the administrator
     * @throws ArgumentConstraintException the name was empty
     * @throws ArgumentIsNullException the name was null
     */
    public HospitalAdministrator(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        super(name);
    }

    /**
     * @see Hospital.People.Staff#login()
     */
    @Override
    public LoginController login(CampusController cc) {
        return new AdministratorController(this, cc);
    }

    /**
     * @return "HospitalAdministrator"
     * @see Hospital.People.Staff#getRole()
     */
    @Override
    public String getRole() {
        return "HospitalAdministrator";
    }

    public TimeFrameConstraintImplementation getConstraints() {
        throw new UnsupportedOperationException("HospitalAdministrator has no appointments");
    }
}
