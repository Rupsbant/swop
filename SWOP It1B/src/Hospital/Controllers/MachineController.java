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
import Hospital.Machine.Machine;
import Hospital.Machine.MachineFactory;

/**
 * Used to perform Machine-related actions.
 */
@SystemAPI
public class MachineController {

    /**
     * the world to perform these actions in
     */
    WorldController wc;
    /**
     * the administrator performing these actions
     */
    AdministratorController admin;

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
     * Gets the Arguments needed for the creation of a type of Machine.
     * @param machine the type of machine to get the arguments from
     * @return an array of PublicArguments which, when answered, can be used for the creation of a new machine of this type
     * @throws NotLoggedInException the administrator is not logged in
     * @throws NotAFactoryException the given type of machine was invalid
     */
    @SystemAPI
    public ArgumentList getMachineArguments(String machine) throws NotLoggedInException, NotAFactoryException {
        admin.checkLoggedIn();
        try {
            return wc.getFactoryArguments(MachineFactory.class, machine);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
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
    public String makeMachine(String machine, ArgumentList argv)
            throws NotAFactoryException,
            NotLoggedInException,
            WrongArgumentListException,
            SchedulableAlreadyExistsException,
            InvalidArgumentException, CannotChangeException {
        admin.checkLoggedIn();
        if (argv == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        MachineFactory factory;
        try {
            factory = wc.getWorld().getFactory(MachineFactory.class, machine);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
        Machine newTest = factory.make(argv.getAllArguments());
        wc.getWorld().addSchedulable(newTest);
        return newTest.toString();
    }

    /**
     * Gets the available types of machines that can be created.
     * @return an array of strings containing the names of machines
     */
    @SystemAPI
    public String[] getAvailableMachineFactories() {
        try {
            return wc.getAvailableFactories(MachineFactory.class).toArray(new String[0]);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
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
