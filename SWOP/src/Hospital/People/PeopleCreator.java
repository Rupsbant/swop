package Hospital.People;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Schedules.Constraints.Preference.ChangeLocationPreference;
import Hospital.WareHouse.WarehouseManager;
import Hospital.World.Campus;
import Hospital.World.CampusInfo;
import Hospital.World.World;

public class PeopleCreator {

    public static final PeopleCreator SINGLETON = new PeopleCreator();

    private PeopleCreator() {
    }

    public String makeStaff(StaffRole role, World w, String name, CampusInfo info) throws SchedulableAlreadyExistsException, CannotChangeException, InvalidArgumentException {
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

    private Nurse makeNurse(World w, String name, CampusInfo info) throws InvalidArgumentException, CannotChangeException {
        Nurse nurse = new Nurse(name);
        Campus campus = w.getCampusFromInfo(info);
        nurse.setCampus(campus);
        return nurse;
    }

    private WarehouseManager makeWarehouseManager(World w, String name, CampusInfo info) throws InvalidArgumentException, CannotChangeException {
        WarehouseManager manager = new WarehouseManager(name);
        Campus campus = w.getCampusFromInfo(info);
        manager.setCampus(campus);
        return manager;

    }
}
