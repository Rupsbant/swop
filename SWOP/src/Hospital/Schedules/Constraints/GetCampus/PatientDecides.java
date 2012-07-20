package Hospital.Schedules.Constraints.GetCampus;

import Hospital.Patient.Patient;
import Hospital.Schedules.Constraints.CampusDecider;
import Hospital.World.Campus;

public class PatientDecides extends CampusDecider {

    private Campus campus;

    @Override
    public Campus getCampus() {
        return this.campus;
    }

    @Override
    public void setPatient(Patient p) {
        this.campus = p.getCampus();
    }
}
