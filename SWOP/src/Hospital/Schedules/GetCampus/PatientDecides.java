package Hospital.Schedules.GetCampus;

import Hospital.Patient.Patient;
import Hospital.Schedules.CampusDecider;
import Hospital.World.Campus;

/**
 * The patient choses the campus when it is visited by a patient.
 */
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
