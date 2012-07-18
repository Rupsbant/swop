package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.Staff;
import Hospital.People.PeopleFactories.StaffFactory;

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
     * Gets the arguments needed for the creation of new staff
     * @param staff the type of staff to be created
     * @return an array of PublicArguments which, when answered, can be used for the creation of a new staffmember of the given type
     * @throws NotLoggedInException the admin is not logged in
     * @throws NotAFactoryException the given type does not exist in this world
     */
    @SystemAPI
    public ArgumentList getStaffArguments(String staff) throws NotLoggedInException, NotAFactoryException {
        ac.checkLoggedIn();
        try {
            return wc.getFactoryArguments(StaffFactory.class, staff);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
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
    public String makeStaffMember(String staff, ArgumentList argv) throws NotAFactoryException, NotLoggedInException, InvalidArgumentException, WrongArgumentListException, SchedulableAlreadyExistsException, CannotChangeException {
        ac.checkLoggedIn();
        if (argv == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        StaffFactory factory = null;
        try {
            factory = wc.getWorld().getFactory(StaffFactory.class, staff);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
        Staff newTest = (Staff) factory.make(argv.getAllArguments());
        wc.getWorld().addSchedulable(newTest);
        return newTest.toString();
    }

    /**
     * Gets the available types of staff that can be created
     * @return an array of strings containing names of the types of staff
     */
    @SystemAPI
    public String[] getAvailableStaffFactories() {
        try {
            return wc.getAvailableFactories(StaffFactory.class).toArray(new String[0]);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
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
