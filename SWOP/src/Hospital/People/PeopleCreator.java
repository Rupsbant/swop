package Hospital.People;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Constraints.Preference.ChangeLocationPreference;
import Hospital.WareHouse.WarehouseManager;
import Hospital.World.Campus;
import Hospital.World.CampusInfo;
import Hospital.World.World;

public class PeopleCreator {

    public static final PeopleCreator SINGLETON = new PeopleCreator();

    private PeopleCreator() {
    }

    public String makeDoctor(World w, String name) throws ArgumentConstraintException, ArgumentIsNullException, SchedulableAlreadyExistsException {
        Doctor doctor = new Doctor(name);
        doctor.setPreference(new ChangeLocationPreference(doctor));
        w.addSchedulable(doctor);
        return "Created new: " + doctor;
    }

    public String makeNurse(World w, String name, CampusInfo info) throws ArgumentConstraintException, ArgumentIsNullException, CannotChangeException, SchedulableAlreadyExistsException {
        Nurse nurse = new Nurse(name);
        Campus campus = w.getCampusFromInfo(info);
        nurse.setCampus(campus);
        w.addSchedulable(nurse);
        return "Created new: " + nurse;
    }

    public String makeWarehouseManager(World w, String name, CampusInfo info) throws ArgumentConstraintException, ArgumentIsNullException, CannotChangeException, SchedulableAlreadyExistsException {
        WarehouseManager manager = new WarehouseManager(name);
        Campus campus = w.getCampusFromInfo(info);
        manager.setCampus(campus);
        w.addSchedulable(manager);
        return "Created new: " + manager;

    }
}
