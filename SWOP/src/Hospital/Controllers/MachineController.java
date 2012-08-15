//TODO: catch "somehow"-exceptions
package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Machine.Location;
import Hospital.Machine.Machine;
import Hospital.Machine.MachineAbstractFactory;
import Hospital.World.CampusInfo;

/**
 * Used to perform Machine-related actions.
 */
@SystemAPI
public class MachineController {

    /**
     * the world to perform these actions in
     */
    private WorldController wc;
    /**
     * the administrator performing these actions
     */
    private AdministratorController admin;

    /**
     * Constructor
     * @param wc the world to perform these actions in
     * @param admin the administrator performing these actions
     * @throws ArgumentIsNullException a given argument was null
     */
    @SystemAPI
    public MachineController(WorldController wc, AdministratorController admin) throws ArgumentIsNullException {
        setAdministratorController(admin);
        setWorldController(wc);
    }

    /**
     * Makes a machine of the given type.
     * @param machine the type of machine to create
     * @param argv the arguments for the creation of this machine
     * @return the details of the newly created machine
     * @throws NotAFactoryException an invalid type of machine was given
     * @throws NotLoggedInException the administrator is not logged in
     * @throws WrongArgumentListException the given arguments do not match the type of machine
     * @throws SchedulableAlreadyExistsException this machine already exists in the world
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     * @throws CannotChangeException 
     */
    @SystemAPI
    public String makeMachine(String machineType, String id, Location location, CampusInfo info)
            throws NotAFactoryException,
            NotLoggedInException,
            WrongArgumentListException,
            SchedulableAlreadyExistsException,
            InvalidArgumentException, CannotChangeException {
        admin.checkLoggedIn();
        MachineAbstractFactory factory = wc.getWorld().getMachineFactory(machineType);
        Machine newTest = factory.make(wc.getWorld(), info, location, id);
        return newTest.toString();
    }

    /**
     * Gets the available types of machines that can be created.
     * @return an array of strings containing the names of machines
     */
    @SystemAPI
    public String[] getAvailableMachineFactories() {
        return wc.getWorld().getMachineFactories();
    }

    /**
     * Sets the world to which this controller applies
     * @param wc the WorldController of the world to set
     * @throws ArgumentIsNullException the given WorldController was null
     */
    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if (wc == null) {
            throw new ArgumentIsNullException("wc should not be null.");
        }
        this.wc = wc;
    }

    /**
     * Sets the administrator which uses this controller
     * @param admin the AdministratorController which will use this controller
     * @throws ArgumentIsNullException the given AdministratorController was null
     */
    private void setAdministratorController(AdministratorController admin) throws ArgumentIsNullException {
        if (admin == null) {
            throw new ArgumentIsNullException("ac should not be null.");
        }
        this.admin = admin;
    }
}
