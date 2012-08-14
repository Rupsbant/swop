package Hospital.People.PeopleFactories;

import Hospital.Utils;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.CampusInfoArgument;
import Hospital.Argument.StringArgument;
import Hospital.Argument.WorldArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.WareHouse.WarehouseManager;

/**
 * A factory for creating warehousemanagers to add to the world
 * used by HospitalAdministratorController
 */
public class WarehouseFactory implements  StaffFactory {

    public static final String WAREHOUSE_MANAGER_FACTORY = "New WarehouseManager";

    /**
     * Creates a WarehouseManager object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public WarehouseManager make(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String name = (String) Utils.getAnswer(StringArgument.class, "name", args[0]);
        return new WarehouseManager(name);
    }

    /**
     * @see Hospital.Factory.Factory#getArguments()
     */
    public PublicArgument[] getEmptyArgumentList() {
        PublicArgument[] args = new PublicArgument[3];
        args[0] = new StringArgument("Enter the name of the warehousemanager: ");
        args[1] = new CampusInfoArgument("Enter the number of the campus");
        args[2] = new WorldArgument();
        return args;
    }

    /**
     * @return "New WarehouseManager" 
     * @see Hospital.Factory.Factory#getName()
     */
    public String getName() {
        return WAREHOUSE_MANAGER_FACTORY;
    }

    public boolean validate(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("Argumentlist is null");
        }
        if (args.length != 3) {
            throw new WrongArgumentListException("Argumentlist of the wrong length");
        }
        Utils.getAnswer(StringArgument.class, "name", args[0]);
        Utils.getAnswer(CampusInfoArgument.class, "the campus", args[1]);
        Utils.getAnswer(WorldArgument.class, "world", args[2]);
        return true;
    }
}
