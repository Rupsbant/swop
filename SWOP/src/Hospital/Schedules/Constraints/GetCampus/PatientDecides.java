package Hospital.Schedules.Constraints.GetCampus;

import Hospital.Patient.Patient;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.World.Campus;

public class PatientDecides extends GetCampusConstraint {

    private Campus campus;

    @Override
    public Campus getCampus() {
        return this.campus;
    }

    @Override
    public void setPatient(Patient p) {
        p.getCampus();
    }
}
