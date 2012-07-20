package Hospital.World;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotFindException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Factory.Factory;
import Hospital.Schedules.Schedulable;
import Hospital.Patient.Patient;
import Hospital.People.LoginInfo;
import Hospital.People.Person;
import Hospital.People.Staff;
import Hospital.People.Unmovable;
import Hospital.Schedules.Constraints.Preference.Preference;
import Hospital.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The hospital-world where most data resides.
 * Data that isn't in here can be found trough data that is.
 */
public class World {

    /**
     * a list of all objects with a schedule in this world
     */
    private ArrayList<Schedulable> resources;
    /**
     * the factories in this world (for creating machines, diagnoses, etc...)
     */
    private Map<String, Factory> factories;
    /**
     * this worldTime
     */
    private WorldTime worldTime;
    /**
     * a list of all campuses in this world
     */
    private ArrayList<Campus> campuses;
    /**
     * a list of all possible preferences
     */
    private ArrayList<Preference> preferences;

    /**
     * Default constructor
     */
    public World() {
        resources = new ArrayList<Schedulable>();
        factories = new HashMap<String, Factory>();
        worldTime = new WorldTime();
        campuses = new ArrayList<Campus>();
        preferences = new ArrayList<Preference>();
    }

    /**
     * Adds this Schedulable to the world.
     * @param app The Schedulable to be added to the world.
     * @throws ArgumentIsNullException If app is null
     * @throws SchedulableAlreadyExistsException If Schedulable already exists
     */
    public void addSchedulable(Schedulable app) throws ArgumentIsNullException, SchedulableAlreadyExistsException {
        if (app == null) {
            throw new ArgumentIsNullException("Can't add null Schedulable.");
        }
        if (resources.contains(app)) {
            throw new SchedulableAlreadyExistsException("Schedulable already in list.");
        }
        resources.add(app);
    }

    /**
     * Returns the Person
     * @param person LoginInfo describing a PersonWithLogin
     * @return PersonWithLogin of the searched user.
     * @throws NoPersonWithNameAndRoleException the described person was not found
     * @throws ArgumentIsNullException the given LoginInfo was null
     */
    public Staff getPersonWithLogin(LoginInfo person) throws NoPersonWithNameAndRoleException, ArgumentIsNullException {
        if (person == null) {
            throw new ArgumentIsNullException("Person is null.");
        }
        List<Staff> temp = getResourceOfClass(Staff.class);
        for (Staff p : temp) {
            if (!p.getName().equals(person.getName())) {
                continue;
            }
            if (!p.getRole().equals(person.getRole())) {
                continue;
            }
            return p;
        }
        throw new NoPersonWithNameAndRoleException();
    }

    /**
     * @param ci a CampusInfo-object representing a campus
     * @return the Campus-objeft corresponding to the given CampusInfo
     * @throws ArgumentConstraintException There was no campus that corresponds to the given CanmpusInfo-object in this world
     */
    public Campus getCampusFromInfo(CampusInfo ci) throws ArgumentConstraintException {
        if (ci == null) {
            throw new ArgumentConstraintException("Null-Campus not found.");
        }
        for (Campus c : campuses) {
            if (c.getName().equals(ci.getName())) {
                return c;
            }
        }
        throw new ArgumentConstraintException("Campus not found.");
    }

    /**
     * This method gives back a list of all staffmembers that can log in.
     * @return List of descriptions of staffmembers
     */
    public List<LoginInfo> getLogins() {
        List<Staff> temp = getResourceOfClass(Staff.class);
        List<LoginInfo> out = new ArrayList<LoginInfo>();
        for (Staff p : temp) {
            out.add(p.getLoginInfo());
        }
        return out;
    }

    /**
     * Gets a list of campuses in this world
     * @return List with a CampusInfo for every campus in this world
     */
    public List<CampusInfo> getCampuses() {
        ArrayList<CampusInfo> temp = new ArrayList<CampusInfo>();
        for (Campus c : campuses) {
            temp.add(c.getCampusInfo());
        }
        return temp;
    }

    /**
     * Gets all resources of the given class
     * @param clazz the class to search
     * @return a List containing all resources in this world of the given class
     */
    public <S extends Schedulable> List<S> getResourceOfClass(Class<S> clazz) {
        return Utils.filter(resources, clazz);
    }

    /**
     * Gets all resources of the given class on the given campus
     * In the case of patients, patients that were last checked in on the given campus, but are now discharged, are also included.
     * @param clazz the class to search
     * @param campus the campus to filter by
     * @return a List containing all resources in this campus of the given class
     */
    public <S extends Schedulable> List<S> getResourceOfClass(Class<S> clazz, Campus campus) {
        List<S> beforeCampusFilter = Utils.filter(resources, clazz);
        List<S> afterCampusFilter = new ArrayList<S>();
        for (S resource : beforeCampusFilter) {
            if (resource instanceof Unmovable) {
                if (campus.equals(((Unmovable) resource).getCampus())) {
                    afterCampusFilter.add(resource);
                }
            }
            if (resource instanceof Patient) {
                if (campus.equals(((Patient) resource).getCampus())) {
                    afterCampusFilter.add(resource);
                }
            }
        }
        return afterCampusFilter;
    }

    /**
     * Gets a specific person in this world
     * @param clazz the class of the person
     * @param name the name of the person
     * @return the person of the given class in this world with the given name
     * @throws NoPersonWithNameAndRoleException the person was not found
     */
    public <S extends Person> S getPersonByName(Class<S> clazz, String name) throws NoPersonWithNameAndRoleException {
        List<S> temp = Utils.filter(resources, clazz);
        for (S person : temp) {
            if (person.getName().equals(name)) {
                return person;
            }
        }
        throw new NoPersonWithNameAndRoleException();
    }

    /**
     * Gets all factories of the given type in this world
     * @param clazz the class to search for
     * @return a Map containing the factories of type clazz
     * @throws ArgumentIsNullException the given class was null
     */
    public <T extends Factory> Map<String, T> getFactoriesOfType(Class<T> clazz) throws ArgumentIsNullException {
        if (clazz == null) {
            throw new ArgumentIsNullException();
        }
        return Utils.filter(factories, clazz);
    }

    /**
     * Gets a factory of the given class with a certain name
     * @param clazz the class of the factory
     * @param name the name of the factory
     * @return the factory of class <i>clazz</i> and with name <i>name</i>
     * @throws ArgumentIsNullException the given class was null
     * @throws NotAFactoryException the factory was not found
     */
    public <T extends Factory> T getFactory(Class<T> clazz, String name) throws ArgumentIsNullException, NotAFactoryException {
        T factory = this.getFactoriesOfType(clazz).get(name);
        if (factory == null) {
            throw new NotAFactoryException();
        }
        return factory;
    }

    /**
     * Adds a new factory to this world
     * @param f the factory to add
     */
    public void addFactory(Factory f) {
        factories.put(f.getName(), f);
    }

    public WorldTime getWorldTime() {
        return worldTime;
    }

    public void setWorldTime(WorldTime worldTime) {
        this.worldTime = worldTime;
    }

    /**
     * Adds a new campus to the list of available campuses
     * @param newCampus name of the campus to be added
     * @throws InvalidArgumentException newCampus was null or already in the list
     */
    public void addCampus(String newCampus) throws InvalidArgumentException {
        if (newCampus == null) {
            throw new ArgumentIsNullException("newCampus is null");
        }
        for (Campus c : campuses) {
            if (c.getName().equals(newCampus)) {
                throw new ArgumentConstraintException("This campus already exists.");
            }
        }
        try {
            campuses.add(new Campus(newCampus, this));
        } catch (IllegalArgumentException iae) {
            throw new ArgumentConstraintException(iae.getMessage());
        }
    }

    /**
     * This method adds a new preference to the world
     * @param pref : The preference
     */
    public void addPreference(Preference pref){
        preferences.add(pref);
    }

    public List<Preference> getPreferences() {
        return new ArrayList<Preference>(preferences);
    }

    public Preference getPreference(String answer) throws CannotFindException {
        for(Preference p : preferences){
            if(p.getDescription().equals(answer)){
                return p;
            }
        }
        throw new CannotFindException("Preference");
    }


}
