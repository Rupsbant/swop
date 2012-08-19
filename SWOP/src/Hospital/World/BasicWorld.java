package Hospital.World;

import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Machine.BloodAnalyzer;
import Hospital.Machine.Location;
import Hospital.Machine.SurgicalEquipment;
import Hospital.Machine.UltraSoundMachine;
import Hospital.Machine.XRayMachine;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.HospitalAdministrator;
import Hospital.People.Nurse;
import Hospital.Schedules.Constraints.Preference.ChangeLocationPreference;
import Hospital.Schedules.Constraints.Preference.Preference;
import Hospital.Schedules.Constraints.Preference.StayShiftPreference;
import Hospital.WareHouse.WarehouseManager;

/**
 * This object creates the world to be used.
 * It adds all factories and people needed at startup
 * and sets the startup time.
 */
public class BasicWorld {

    /**
     * Constructs the standard world.
     * @return the WorldController associated with the created world
     */
    public static WorldController getBasicWorld() {
        World w = getWorldForTesting();
        WorldController wc = null;
        try {
            wc = new WorldController(w);
        } catch (ArgumentIsNullException e) {
            System.out.println("World is null, should not happen!");
        }
        return wc;
    }

    /**
     * Constructs the empty standard world.
     * @return the WorldController associated with the created world
     */
    private static World getEmptyWorld() {
        World w = new World();

        w.addMachineFactory(new XRayMachine("Singleton ProtoType 1", null));
        w.addMachineFactory(new UltraSoundMachine("Singleton ProtoType 2", null));
        w.addMachineFactory(new BloodAnalyzer("Singleton ProtoType 3", null));
        w.addMachineFactory(new SurgicalEquipment("Singleton ProtoType 4", null));

        w.addPreference(new StayShiftPreference(null));
        w.addPreference(new ChangeLocationPreference(null));
        try {
            w.addSchedulable(new HospitalAdministrator("The Boss"));
        } catch (Exception ex) {
            System.out.println("Exception when creating empty World.");
        }
        return w;
    }

    /**
     * Creates a world used for testing the system
     * @return a world with several machines and staff already entered
     */
    public static World getWorldForTesting() {
        World w = getEmptyWorld();
        try {
            Preference pref = new ChangeLocationPreference(null);
            w.addCampus("North");
            w.addCampus("South");
            Campus campusNorth = w.getCampusFromInfo(new CampusInfo("North"));
            Campus campusSouth = w.getCampusFromInfo(new CampusInfo("South"));

            w.addSchedulable(new WarehouseManager("Warehouse Manager"));
            Doctor d = new Doctor("Doktoor");
            pref.makeThisAsPreference(d);
            w.addSchedulable(d);
            d = new Doctor("Janet Fraiser");
            pref.makeThisAsPreference(d);
            w.addSchedulable(d);
            d = new Doctor("Gregory House");
            pref.makeThisAsPreference(d);
            w.addSchedulable(d);
            
            Nurse nurse1 = new Nurse("Nurse Joy");
            Nurse nurse2 = new Nurse("Verpleegster");
            nurse1.setCampus(campusNorth);
            nurse2.setCampus(campusSouth);
            w.addSchedulable(nurse1);
            w.addSchedulable(nurse2);

            Patient p = new Patient("Ruben");
            Patient p2 = new Patient("Jeroen");
            w.addSchedulable(p);
            w.addSchedulable(p2);
            p.checkIn(w.getCampusFromInfo(w.getCampuses().get(0)));
            p2.checkIn(w.getCampusFromInfo(w.getCampuses().get(1)));

            w.addSchedulable(new XRayMachine("XRay 1", new Location("SOL N")).setCampus(campusSouth));
            w.addSchedulable(new UltraSoundMachine("UltraSoundMachine 1", new Location("SOL Z")).setCampus(campusSouth));
            w.addSchedulable(new BloodAnalyzer("BloodAnalyzer 1", new Location("SOL O")).setCampus(campusNorth));
            w.addSchedulable(new SurgicalEquipment("SurgicalEqiupment 1", new Location("SOL W")).setCampus(campusNorth));
        } catch (Exception ex) {
            System.out.println("Exception when creating basicworld.");
            ex.printStackTrace();
        }
        return w;
    }
}