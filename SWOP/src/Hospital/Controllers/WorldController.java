package Hospital.Controllers;

import Hospital.Argument.ListArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Factory.Factory;
import Hospital.Patient.Patient;
import Hospital.People.LoginInfo;
import Hospital.Schedules.Constraints.Preference.Preference;
import Hospital.SystemAPI;
import Hospital.WareHouse.FoodStock.FoodStock;
import Hospital.WareHouse.Items.Meal;
import Hospital.World.BasicWorld;
import Hospital.World.Campus;
import Hospital.World.CampusInfo;
import Hospital.World.Time;
import Hospital.World.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This controller gives controlled access to the world it represents.
 */
@SystemAPI
public class WorldController {

    /**
     * the world represented by this controller
     */
    private World world;

    /**
     * This creates a WorldController for the given World
     * @param w The world to control
     * @throws ArgumentIsNullException if w was null
     */
    @SystemAPI
    public WorldController(World w) throws ArgumentIsNullException {
        setWorld(w);
    }

    /**
     * Sets he world represented by this controller
     * @param w a WorldController
     * @throws ArgumentIsNullException wc was null
     */
    private void setWorld(World w) throws ArgumentIsNullException {
        if (w == null) {
            throw new ArgumentIsNullException("World can't be null");
        }
        this.world = w;
    }

    /**
     * This method returns the arguments of a factory
     * @param clazz The factoryClass to filter on
     * @param factoryName The name of the factory
     * @return An array of arguments to be answered
     * @throws NotLoggedInException if this controller was logged out.
     * @throws ArgumentIsNullException if a null class was entered
     * @throws NotAFactoryException if the factory with the given name did not exist
     */
    ArgumentList getFactoryArguments(Class<? extends Factory> clazz, String factoryName)
            throws NotLoggedInException, ArgumentIsNullException, NotAFactoryException {
        return new ArgumentList(getWorld().getFactory(clazz, factoryName).getEmptyArgumentList()).setWorld(world);
    }

    /**
     * This method gives back a list of all staffmembers that can log in.
     * @return List of descriptions of staffmembers
     */
    @SystemAPI
    public List<LoginInfo> getLogins() {
        return world.getLogins();
    }

    /**
     * Gets a list of campuses in this world
     * @return List with a CampusInfo for every campus in this world
     */
    @SystemAPI
    public List<CampusInfo> getCampuses() {
        return world.getCampuses();
    }

    /**
     * Returns a filtered list with all names of the registered patients.
     * @return List of patients in the world this WorldController controls.
     */
    @SystemAPI
    public ArrayList<String> getPatients() {
        List<Patient> temp = world.getResourceOfClass(Patient.class);
        ArrayList<String> out = new ArrayList<String>();
        for (Patient p : temp) {
            out.add(p.toString());
        }
        return out;
    }

    /**
     * Returns the list of names of patients that are not discharged yet.
     * @return List of the names of undischarged Patients.
     */
    @SystemAPI
    public ArrayList<String> getNotDischargedPatients() {
        List<Patient> temp = world.getResourceOfClass(Patient.class);
        ArrayList<String> out = new ArrayList<String>();
        for (Patient p : temp) {
            if (!p.isDischarged()) {
                out.add(p.toString());
            }
        }
        return out;
    }

    /**
     * Logs the person represented by the given LoginInfo in.
     * @param campus The campus to login at.
     * @param person The LoginInfo to log a person in with
     * @return the LoginController that represents the person for this session.
     * @throws NoPersonWithNameAndRoleException if the person was not found/does not exist
     * @throws ArgumentIsNullException if the given person was null
     * @throws ArgumentConstraintException the campus was not found in the world
     */
    @SystemAPI
    public LoginController login(CampusInfo campus, LoginInfo person) throws NoPersonWithNameAndRoleException, ArgumentIsNullException, ArgumentConstraintException {
        Campus c = world.getCampusFromInfo(campus);
        CampusController cc = null;
        try {
            cc = new CampusController(c);
            return world.getPersonWithLogin(person).login(cc);
        } catch (ArgumentIsNullException e) {
            if (e.getMessage().equals("The given Campus was null")) {
                Logger.getLogger(WorldController.class.getName()).log(Level.SEVERE, null, e);
                throw new Error("Login failed because of a null Campus.");
            } else {
                throw e;
            }
        }
    }

    /**
     * Returns the world so other controllers can access it.
     * @return World
     */
    World getWorld() {
        return world;
    }

    /**
     * Returns a set of all names of available factories.
     * @param clazz The type of factories that should be filtered
     * @return The names of the factories of type clazz.
     * @throws ArgumentIsNullException If clazz is null
     */
    protected Set<String> getAvailableFactories(Class<? extends Factory> clazz) throws ArgumentIsNullException {
        return world.getFactoriesOfType(clazz).keySet();
    }

    /**
     * Returns the current time of the Hospital
     * @return Time
     */
    @SystemAPI
    public Time getTime() {
        return world.getWorldTime().getTime();
    }

    /**
     * Returns the list of the descriptions of the preferences in the world
     * @return List of preferences
     */
    @SystemAPI
    public ListArgument<String> getPreferences() {
        List<Preference> prefs = world.getPreferences();
        List<String> prefString = new ArrayList<String>();
        for (Preference pref : prefs) {
            prefString.add(pref.getDescription());
        }
        try {
            return new ListArgument<String>("Enter the number of your preference", prefString);
        } catch (ArgumentIsNullException ex) {
            throw new Error("Argument was not null");
        }
    }

    /**
     * Constructs the standard world.
     * @return the WorldController associated with the created world
     */
    @SystemAPI
    public static WorldController getBasicWorld() {
        return BasicWorld.getBasicWorld();
    }

    /**
     * returns the campuses with enough food for new checkins
     * @return
     */
    @SystemAPI
    public List<CampusInfo> getCampusesWithFood() {
        List<CampusInfo> campusInfo = getCampuses();
        List<CampusInfo> result = new ArrayList<CampusInfo>();
        for (CampusInfo campusInfo2 : campusInfo) {
            Campus campusFromInfo = null;
            try {
                campusFromInfo = world.getCampusFromInfo(campusInfo2);
            } catch (ArgumentConstraintException e) {
                continue;
            }
            try {
                final FoodStock foodStockOfCampus = (FoodStock) campusFromInfo
                        .getWarehouse().getStock(Meal.MEAL);
                if (foodStockOfCampus.getPatientCapacity() >= 1) {
                    result.add(campusInfo2);
                }
            } catch (StockException e) {
            }
        }

        return result;

    }
}
