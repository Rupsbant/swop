package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.PeopleCreator;
import Hospital.People.StaffRole;
import Hospital.Schedules.Schedulable;
import Hospital.World.CampusInfo;

/**
 * Used for staff-related actions.
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
     * Creates a new staff member
     * @param staff the type of staff to be created
     * @param argv the arguments to the creation of the new staff member
     * @return the details of newly created staff member
     * @throws NotAFactoryException the given type does not exist in this world
     * @throws NotLoggedInException the admin is not logged in
     * @throws WrongArgumentListException the given arguments did not match the type of staff
     * @throws SchedulableAlreadyExistsException this staff member already exists in this world
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     * @throws CannotChangeException 
     */
    @SystemAPI
    public String makeStaffMember(StaffRole role, String name, CampusInfo info) throws InvalidArgumentException, NotLoggedInException, SchedulableAlreadyExistsException, CannotChangeException {
        ac.checkLoggedIn();
        switch (role) {
            case Doctor:
                return PeopleCreator.SINGLETON.makeDoctor(wc.getWorld(), name);
            case Nurse:
                return PeopleCreator.SINGLETON.makeNurse(wc.getWorld(), name, info);
            case WarehouseManager:
                return PeopleCreator.SINGLETON.makeWarehouseManager(wc.getWorld(), name, info);
            default:
                throw new ArgumentConstraintException("This Role cannot be created");
        }
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
