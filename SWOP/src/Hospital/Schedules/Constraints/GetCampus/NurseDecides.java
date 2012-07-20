package Hospital.Schedules.Constraints.GetCampus;

import Hospital.People.Nurse;
import Hospital.Schedules.CampusDecider;
import Hospital.World.Campus;

public class NurseDecides extends CampusDecider {
    private Campus campus;

    @Override
    public Campus getCampus() {
        return this.campus;
    }

    @Override
    public void setNurse(Nurse n) {
        this.campus = n.getCampus();
    }
}
