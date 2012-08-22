package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NotLoggedInException;
import Hospital.People.PeopleCreator;
import Hospital.People.StaffRole;
import Hospital.World.CampusInfo;

/**
 * This controller enables the following usecases:
 * Create new staffmembers, a single API call is enough.
 */
@SystemAPI
public class StaffController {

    /**
     * the admin which performs these actions
     */
    private AdministratorController ac;
    /**
     * the world in which these actions are performed
     */
    private WorldController wc;

    /**
     * Constructor
     * @param wc the world in which these actions are performed
     * @param ac the admin which performs these actions
     * @throws ArgumentIsNullException wc or ac was null
     */
    @SystemAPI
    public StaffController(WorldController wc, AdministratorController ac) throws ArgumentIsNullException {
        setWorldController(wc);
        setAdministratorController(ac);
    }

    /**
     * Creates a new staff-member
     * @param role the type of the staff-member to create
     * @param name the name of the new staffmember
     * @param info the campus the staffmemebr is stationed at, not null for nurse and warehousemanager
     * @return the details of the created staffmember
     * @throws InvalidArgumentException thrown if soma parameters were null and expected
     * @throws NotLoggedInException thrown if the hospitaladmin was logged out or something
     * @throws SchedulableAlreadyExistsException  thrown if the name of the new staffmember already exists
     */
    @SystemAPI
    public String makeStaffMember(StaffRole role, String name, CampusInfo info) throws InvalidArgumentException, NotLoggedInException, SchedulableAlreadyExistsException {
        ac.checkLoggedIn();
        return PeopleCreator.SINGLETON.makeStaff(role, wc.getWorld(), name, info);
    }

    /**
     * Sets the world to which this controller applies
     * @param wc a WorldController
     * @throws ArgumentIsNullException wc was null
     */
    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if (wc == null) {
            throw new ArgumentIsNullException("wc should not be null.");
        }
        this.wc = wc;
    }

    /**
     * Sets the admin which uses this controller
     * @param ac an AdministratorController
     * @throws ArgumentIsNullException ac was null
     */
    private void setAdministratorController(AdministratorController ac) throws ArgumentIsNullException {
        if (ac == null) {
            throw new ArgumentIsNullException("ac should not be null.");
        }
        this.ac = ac;
    }
}
