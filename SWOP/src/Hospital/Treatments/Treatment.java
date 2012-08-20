package Hospital.Treatments;

import Hospital.Controllers.TreatmentInfo;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;
import Hospital.Interfaces.Command;
import Hospital.Interfaces.NeedsItems;
import Hospital.Interfaces.Result;
import Hospital.People.Nurse;
import Hospital.Schedules.Appointable;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.CampusDecider;
import Hospital.Schedules.GetCampus.NurseDecides;
import Hospital.Schedules.Constraints.Implementation.NurseAppointmentBackToBackConstraint;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Constraints.Warehouse.ItemConstraint;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Schedules.DelayedTimeLength;
import Hospital.WareHouse.ItemReservationCommand;
import Hospital.WareHouse.ItemInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<TimeFrameConstraint> getConstraints() {
        NurseAppointmentBackToBackConstraint nurseConstraint = new NurseAppointmentBackToBackConstraint();
        List<TimeFrameConstraint> out = new ArrayList<TimeFrameConstraint>();
        out.add(nurseConstraint);
        out.add(new ItemConstraint(this));
        return out;
    }

    /**
     * The Nurse is unmovable and decides where the appointment is.
     * @return new NurseDecides();
     */
    public CampusDecider getCampusDecider() {
        return new NurseDecides();
    }

    /**
     * @return the group of Nurses
     * @see Hospital.Schedules.Appointable#getScheduleGroups()
     */
    public List<MultiScheduleGroup> getScheduleGroups() {
        try {
            return Collections.singletonList(new MultiScheduleGroup(Nurse.class));
        } catch (ArgumentIsNullException ex) {
            throw new Error("Classes can't be null");
        }
    }

    /**
     * @return a TimeFrameDelay with a delay of 60 and the length specified this.getLength()
     * @see Hospital.Schedules.Appointable#getTimeFrameDelay()
     */
    public DelayedTimeLength getDelayedTimeLength() {
        try {
            return new DelayedTimeLength(60, getLength());
        } catch (ArgumentConstraintException ex) {
            throw new Error("Illegal length in code");
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
            throw new Error(ex);
        } catch (CannotDoException ex) {
            throw new Error("Command can only be done if appointment was scheduled!!!");
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
                return "Not scheduled yet, not unscheduled.\n";
            }
        } catch (NotDoneException ex) {
            throw new Error(ex);
        }
    }
}
