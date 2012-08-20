package Hospital.People;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Schedules.Constraints.Preference.ChangeLocationPreference;
import Hospital.WareHouse.WarehouseManager;
import Hospital.World.Campus;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**
 * This class creates staffmembers and introduces them safely in the world
 * @author Rupsbant
 */
public class PeopleCreator {

    /**
     * The singleton
     */
    public static final PeopleCreator SINGLETON = new PeopleCreator();

    private PeopleCreator() {
    }

    /**
     * Creates a new staffmember of the given role
     * @param role
     * @param w The world to introduce the nem staffmember in
     * @param name the name of the staffmember
     * @param info the campus to member is stationed at, ingored for doctors
     * @return a description of the created staffmember
     * @throws SchedulableAlreadyExistsException
     * @throws InvalidArgumentException if some parameter was not as expected
     */
    public String makeStaff(StaffRole role, World w, String name, CampusInfo info) throws SchedulableAlreadyExistsException, InvalidArgumentException {
        Staff made = null;
        switch (role) {
            case Doctor:
                made = makeDoctor(w, name);
                break;
            case Nurse:
                made = makeNurse(w, name, info);
                break;
            case WarehouseManager:
                made = makeWarehouseManager(w, name, info);
                break;
            default:
                throw new ArgumentConstraintException("This Role cannot be created");
        }
        w.addSchedulable(made);
        return "Created new: " + made;
    }

    private Doctor makeDoctor(World w, String name) throws InvalidArgumentException, SchedulableAlreadyExistsException {
        Doctor doctor = new Doctor(name);
        doctor.setPreference(new ChangeLocationPreference(doctor));
        return doctor;
    }

    private Nurse makeNurse(World w, String name, CampusInfo info) throws InvalidArgumentException {
        Nurse nurse = new Nurse(name);
        Campus campus = w.getCampusFromInfo(info);
        try {
            nurse.setCampus(campus);
        } catch (CannotChangeException ex) {
            throw new Error("There's a first time for everything");
        }
        return nurse;
    }

    private WarehouseManager makeWarehouseManager(World w, String name, CampusInfo info) throws InvalidArgumentException {
        WarehouseManager manager = new WarehouseManager(name);
        Campus campus = w.getCampusFromInfo(info);
        try {
            manager.setCampus(campus);
        } catch (CannotChangeException ex) {
            throw new Error("There's a first time for everything");
        }
        return manager;

    }
}
