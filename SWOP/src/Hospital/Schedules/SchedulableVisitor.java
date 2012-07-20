package Hospital.Schedules;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;

public class SchedulableVisitor {

    /**
     * (in)validates the constraint based on a given TimeFrame for a Doctor
     * @param n the Doctor-object
     */
    public void setDoctor(Doctor d) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Nurse
     * @param n the Nurse-object
     */
    public void setNurse(Nurse n) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Patient
     * @param p the Patient-object
     */
    public void setPatient(Patient p) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Schedulable
     * @param s the Schedulable-object
     */
    public void setSchedulable(Schedulable s) {
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Staffmember
     * @param s the Staffmember
     */
    public void setStaff(Staff s) {
    }

}
