package Hospital.MedicalTest;

import Hospital.Controllers.MedicalTestInfo;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Factory.Result;
import Hospital.Machine.Machine;
import Hospital.People.Nurse;
import Hospital.Schedules.Appointable;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.Implementation.NurseAppointmentBackToBackConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Schedules.TimeFrameDelay;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the common elements of all types of medical tests
 */
public abstract class MedicalTest implements Result, Appointable {

    /**
     * The appointment associated with this test
     */
    private Appointment appointment;
    /**
     * Indicates whether this test already has a result entered
     */
    private boolean resultEntered = false;

    /**
     * Returns the Appointment for this MedicalTest.
     * @return Appointment
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Sets the Appointment of this MedicalTest
     * @param appointment Appointment to set.
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Returns whether the results were entered.
     * @return true if they were entered.
     */
    public boolean isResultEntered() {
        return resultEntered;
    }

    /**
     * Sets whether this test has had it's result already answered
     * @param res a boolean indicating whether a result was entered
     */
    public void setResultEntered(boolean res) {
        this.resultEntered = res;
    }

    /**
     * Gets the details of this test
     * @return a detailed string containing information about the test, its appointment and its result
     */
    public String advancedString() {
        return toString() + "\n"
                + getResultString() + "\n"
                + getAppointment().toString();
    }

    /**
     * Gets an object representing this medical test
     * @return a MedicalTestInfo object
     */
    public MedicalTestInfo getInfo() {
        return new MedicalTestInfo(this);
    }

    /**
     * Returns a representation of this MedicalTest.
     * @return Description
     */
    @Override
    public abstract String toString();

    /**
     * Returns a description of the results of this MedicalTest.
     * @return Description
     */
    public abstract String getResultString();

    /**
     * Returns the constraints this MedicalTest must satisfy for the Patient.
     * @return new NurseAppointmentBackTobackConstraint, the nurse determines the location.
     */
    public TimeFrameConstraint getConstraints() {
        return new NurseAppointmentBackToBackConstraint();
    }

    /**
     * Gets the minimal time between now and the moment the test is allowed to be scheduled
     * @return in case of medical tests the default time is 60 minutes
     */
    int getTimeDiff() {
        return 60;
    }

    /**
     * Gets the time it takes to perform this test  
     * @return length of the test
     */
    abstract int getLength();

    /**
     * Returns the resources of the hospital that are needed for this MedicalTest.
     * These include normally: a nurse and a machine.
     * @return A list of Groups to search from the world.
     */
    public List<MultiScheduleGroup> getScheduleGroups() {
        try {
            List<MultiScheduleGroup> out = new ArrayList<MultiScheduleGroup>();
            out.add(new MultiScheduleGroup(Nurse.class));
            if (getRequiredMachine() != null) {
                out.add(new MultiScheduleGroup(getRequiredMachine()));
            }
            return out;
        } catch (ArgumentIsNullException ex) {
            throw new Error("Classes can't be null");
        }
    }

    /**
     * Gets the type of machine needed to perform this test
     * @return the class of the required machine
     */
    abstract<S extends Machine> Class<S> getRequiredMachine();

    /**
     * Returns a TimeFrameDelay object that waits the needed time during scheduling based on the current time.
     * @return Returns a new TimeFrameDelay that makes the basic TimeFrame for scheduling
     */
    public TimeFrameDelay getTimeFrameDelay() {
        try {
            return new TimeFrameDelay(getTimeDiff(), getLength());
        } catch (ArgumentConstraintException ex) {
            throw new Error("Length of medicalTest was not valid somewhere, check code");
        }
    }

    /**
     * @see Hospital.Schedules.Appointable#getNeededItems()
     */
    public String[] getNeededItems() {
        return new String[0];
    }
}
