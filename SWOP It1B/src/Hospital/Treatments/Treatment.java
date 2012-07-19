package Hospital.Treatments;

import Hospital.Controllers.TreatmentInfo;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;
import Hospital.Factory.Command;
import Hospital.Factory.NeedsItems;
import Hospital.Factory.Result;
import Hospital.People.Nurse;
import Hospital.Schedules.Appointable;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.Implementation.NurseAppointmentBackToBackConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Constraints.Warehouse.ItemConstraint;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Schedules.TimeFrameDelay;
import Hospital.WareHouse.ItemReservationCommand;
import Hospital.WareHouse.ItemInfo;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides common functionality and an interface for the different kinds of treatment provided in the hospital.
 */
public abstract class Treatment implements Result, Appointable, NeedsItems {

    /**
     * The appointment associated with this treatment
     */
    private Appointment appointment;
    /**
     * The command to execute after the approval of the diagnosis.
     */
    private Command delayedCommand;
    /**
     * Indicates whether the result for this Treatment has been entered
     */
    private boolean resultEntered;
    /**
     * The command that reserves the items to use.
     */
    private ItemReservationCommand itemReservationCommand;

    /**
     * @see Hospital.Schedules.Appointable#getAppointment()
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * @see Hospital.Schedules.Appointable#setAppointment(Hospital.Schedules.Appointment)
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Returns a wrapper to display information about the Treatment.
     * @return new TreatmentInfo(this);
     */
    public TreatmentInfo getInfo() {
        return new TreatmentInfo(this);
    }

    /**
     * Gives a description of the Treatment.
     * @return description
     */
    @Override
    public abstract String toString();

    /**
     * Returns the length a Treatment appointment takes.
     * @return length of TimeFrame to make.
     */
    public abstract int getLength();

    /**
     * @see Hospital.Schedules.Appointable#getConstraints()
     */
    public TimeFrameConstraint getConstraints() {
        NurseAppointmentBackToBackConstraint getC = new NurseAppointmentBackToBackConstraint();
        getC.addConstraintList(new ItemConstraint(getC, this));
        return getC;
    }

    /**
     * @return the group of Nurses
     * @see Hospital.Schedules.Appointable#getScheduleGroups()
     */
    public List<MultiScheduleGroup> getScheduleGroups() {
        try {
            return Collections.singletonList(new MultiScheduleGroup(Nurse.class));
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Classes can't be null");
        }
    }

    /**
     * @return a TimeFrameDelay with a delay of 60 and the length specified this.getLength()
     * @see Hospital.Schedules.Appointable#getTimeFrameDelay()
     */
    public TimeFrameDelay getTimeFrameDelay() {
        try {
            return new TimeFrameDelay(60, getLength());
        } catch (ArgumentConstraintException ex) {
            throw new RuntimeException("Illegal length in code");
        }
    }

    /**
     * Returns true if the results can be entered.
     * @return true if the results can be entered.
     */
    public boolean canEnterResults() {
        return delayedCommand.isDone();
    }

    /**
     * @see Hospital.Factory.Result#isResultEntered()
     */
    public boolean isResultEntered() {
        return resultEntered;
    }

    /**
     * Mark the Treatment as having a result entered
     */
    public void setResultEntered() {
        try {
            itemReservationCommand.use();
        } catch (NotDoneException ex) {
            Logger.getLogger(Treatment.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (CannotDoException ex) {
            throw new RuntimeException("Command can only be done if appointment was scheduled!!!");
        }
        resultEntered = true;
    }

    public void setItemReservationCommand(ItemReservationCommand itemReservationCommand) {
        this.itemReservationCommand = itemReservationCommand;
    }

    /**
     * Sets the command for delayed creation of the rest of the Treatment, this includes itemReservation and Scheduling.
     * @param delayedCommand the command to set
     */
    void setDelayedCommand(Command delayedCommand) {
        this.delayedCommand = delayedCommand;
    }

    /**
     * Returns a list of all needed items for this treatment.
     * @return List of names of items
     */
    public ItemInfo[] getNeededItems() {
        return new ItemInfo[0];
    }

    /**
     * Schedules this treatment
     * @return a string detailing the scheduling of this treatment
     * @throws CannotDoException the treatment could no be scheduled
     */
    public String schedule() throws CannotDoException {
        return delayedCommand.execute();
    }

    /**
     * Remove the scheduling for this treatment
     * @throws CannotDoException the treatment could not be unscheduled
     */
    public String unSchedule() throws CannotDoException {
        try {
            if (delayedCommand.isDone()) {
                return delayedCommand.undo();
            } else {
                return "";
            }
        } catch (NotDoneException ex) {
            Logger.getLogger(Treatment.class.getName()).log(Level.SEVERE, "Does not happen", ex);
            return "";
        }
    }
}
