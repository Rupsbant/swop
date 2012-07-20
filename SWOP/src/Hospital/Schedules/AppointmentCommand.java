package Hospital.Schedules;

import Hospital.Argument.PriorityArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Scheduling.ScheduleGroupUnavailable;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Factory.Command;
import Hospital.Factory.NullCommand;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.Constraints.Priority.PriorityConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Utils;
import Hospital.World.World;
import Hospital.World.WorldTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A command to create and schedule appointments
 */
public class AppointmentCommand implements Command {

    public static PublicArgument[] getArguments() {
        return new PublicArgument[]{new PriorityArgument("Enter the priority of the appointment")};
    }
    /**
     * The groups from which to pick attendees for the appointment
     */
    private List<ScheduleGroup> groups = new ArrayList<ScheduleGroup>();
    /**
     * The minimal delay before the appointment can be scheduled
     */
    private TimeFrameDelay td;
    /**
     * The created appointment
     */
    private Appointment appointment;
    /**
     * The Appointable object associated with the created appointment
     */
    private Appointable appointable;
    /**
     * The constraints to be placed on the appointment
     */
    private TimeFrameConstraint tfConstraints;
    /**
     * The priority to create the appointment with
     */
    private Priority priority;
    /**
     * The next command to execute in chain form
     */
    private Command next = NullCommand.singleton;

    /**
     * Constructor
     * @param world the world in which to schedule the appointment
     * @param coreSchedules the attendees which have to be specifically present (eg. a specific patient, as opposed to any nurse)
     * @param app the Appointable-object associated with the Appointment that will be created
     * @throws ArgumentIsNullException world, coreSchedules and/or app was null
     */
    public AppointmentCommand(World world, Appointable app, List<ScheduleGroup> coreSchedules, Priority priority) throws ArgumentIsNullException {
        setAppointable(app);
        this.groups.addAll(app.getScheduleGroups());
        this.groups.addAll(coreSchedules);
        this.tfConstraints = app.getConstraints();
        this.tfConstraints.addConstraintList(new PriorityConstraint(priority));
        this.priority = priority;
        populateScheduleGroups(world);
        setTimeFrameDelay(app, world.getWorldTime());
    }

    private void populateScheduleGroups(World world) throws ArgumentIsNullException, Error {
        List<MultiScheduleGroup> groups = Utils.filter(this.groups, MultiScheduleGroup.class);
        for (MultiScheduleGroup m : groups) {
            try {
                m.setWorld(world);
            } catch (CannotChangeException ex) {
                throw new Error("This is the first time the world is set!");
            }
        }
    }

    private void setAppointable(Appointable app) throws ArgumentIsNullException {
        if (app == null) {
            throw new ArgumentIsNullException("Appointable should not be null");
        }
        appointable = app;
    }

    private void setTimeFrameDelay(Appointable app, WorldTime wt) throws Error {
        td = app.getTimeFrameDelay();
        try {
            td.setWorldTime(wt);
        } catch (CannotChangeException ex) {
            throw new Error("This was the first time World was set after creation");
        }
    }

    /**
     * Executes the command by making and scheduling an appointment between the attendees given in the constructor
     * @see Hospital.Factory.Command#execute()
     */
    public String execute() throws CannotDoException {
        if (appointment != null) {
            throw new CannotDoException("Appointment already planned");
        }
        try {
            appointment = AppointmentFactory.makeAppointment(td.getDelayedTimeFrame(), tfConstraints, groups, this);
            Set<AppointmentCommand> preempted = AppointmentFactory.getPreempted(appointment.getAttendees(), appointment.getTimeFrame());

            String s = AppointmentFactory.undoPreempted(preempted);
            appointment.schedule();
            s += AppointmentFactory.redoPreempted(preempted);

            appointable.setAppointment(appointment);
            s += next.execute();
            return s + appointment.toString();
        } catch (ScheduleGroupUnavailable ex) {
            throw new CannotDoException("Some ScheduleGroup had no available resources: " + ex.getMessage());
        } catch (SchedulingException ex) {
            throw new CannotDoException("All possible free resource-combinations throw schedulingException: " + ex.getMessage());
        }
    }

    /**
     * @see Hospital.Factory.Command#undo()
     */
    public String undo() throws CannotDoException {
        if (appointment == null) {
            throw new CannotDoException("Not yet done!");
        }
        appointment.remove();
        appointable.setAppointment(null);
        appointment = null;
        return "Undone:\n" + this.toString();
    }

    /**
     * @see Hospital.Factory.Command#isDone()
     */
    public boolean isDone() {
        return appointment != null;
    }

    /**
     * @return "AppointmentCommand"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AppointmentCommand of " + appointable;
    }

    protected Priority getPriority() {
        return priority;
    }

    protected Appointment getAppointment() {
        return appointment;
    }

    public Command getNext() {
        return next;
    }

    public void setNext(Command next) {
        if (next == null) {
            next = NullCommand.singleton;
        }
        this.next = next;
    }
}
