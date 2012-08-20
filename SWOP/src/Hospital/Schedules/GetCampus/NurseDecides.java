package Hospital.Schedules.GetCampus;

import Hospital.People.Nurse;
import Hospital.Schedules.CampusDecider;
import Hospital.World.Campus;

/**
 * Visiting nurses choose the campus.
 */
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
