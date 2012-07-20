package Hospital.Schedules.Constraints;

import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.People.Staff;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.TimeFrame;

public interface TimeFrameConstraint {

    /**
     * Check whether all Constraints in the list are accepted.
     * @return true if valid.
     */
    Boolean acceptAll();

    /**
     * Unsets all Constraints.
     */
    void resetAll();

    /**
     * Adds a constraintList to the end of this list.
     * @param toAdd constraintlist to add.
     * @return this, for chaining.
     */
    TimeFrameConstraint addConstraintList(TimeFrameConstraint toAdd);

    /**
     * Sets the TimeFrame of the appointment that is being made.
     * @param tf, The TimeFrame during which the appointment happens.
     * @return this
     */
    TimeFrameConstraint setTimeFrame(TimeFrame tf);

    /**
     * Sets the Doctor d that is present during the appointment.
     * @param d Doctor that will be present.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameDoctor(Doctor d);

    /**
     * Sets the Nurse n that is present during the appointment.
     * @param n Nurse that will be present.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameNurse(Nurse n);

    /**
     * Sets the Patient p that is present during the appointment.
     * @param p Patient that will be present.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFramePatient(Patient p);

    /**
     * Sets the Schedulable s that is present during the appointment.
     * @param s Schedulable that will be present.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameSchedulable(Schedulable s);

    /**
     * Sets the Staff s that is present during the appointment.
     * @param s Staff that will be present.
     * @return this, for chaining.
     */
    TimeFrameConstraint setValidTimeFrameStaff(Staff s);

}
