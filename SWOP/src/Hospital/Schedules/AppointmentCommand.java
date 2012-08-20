package Hospital.Schedules;

import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.Constraints.Priority.PriorityConstraint;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Scheduling.ScheduleGroupUnavailable;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Interfaces.Command;
import Hospital.Interfaces.NullCommand;
import Hospital.Schedules.ConstraintSolver.AppointmentResult;
import Hospital.Schedules.ConstraintSolver.SolverAdapter;
import Hospital.Utils;
import Hospital.World.World;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A command to create and schedule appointments
 */
public class AppointmentCommand implements Command {

    /**
     * The minimal delay before the appointment can be scheduled
     */
    private DelayedTimeLength td;
    /**
     * The created appointment
     */
    private Appointment appointment;
    /**
     * The Appointable object associated with the created appointment
     */
    private Appointable appointable;
    /**
     * The priority to create the appointment with
     */
    private Priority priority;
    /**
     * The next command to execute in chain form
     */
    private Command next = NullCommand.singleton;
    private List<ScheduleGroup> groups = new ArrayList<ScheduleGroup>();
    private final List<TimeFrameConstraint> constraints;

    /**
     * Constructor
     * @param world the world in which to schedule the appointment
     * @param coreSchedules the attendees which have to be specifically present (eg. a specific patient, as opposed to any nurse)
     * @param app the Appointable-object associated with the Appointment that will be created
     * @param priority the priority at which the appointment must be scheduled
     * @throws ArgumentIsNullException world, coreSchedules and/or app was null
     */
    public AppointmentCommand(World world, Appointable app, List<ScheduleGroup> coreSchedules, Priority priority) throws ArgumentIsNullException {
        if (app == null) {
            throw new ArgumentIsNullException("Appointable should not be null");
        }
        appointable = app;
        groups.addAll(app.getScheduleGroups());
        groups.addAll(coreSchedules);
        populateScheduleGroups(world, groups);
        constraints = app.getConstraints();
        constraints.add(new PriorityConstraint(priority));

        this.priority = priority;
        td = app.getDelayedTimeLength().setWorldTime(world.getWorldTime());
    }

    private void populateScheduleGroups(World world, List<ScheduleGroup> g) throws ArgumentIsNullException {
        List<MultiScheduleGroup> groups = Utils.filter(g, MultiScheduleGroup.class);
        for (MultiScheduleGroup m : groups) {
            try {
                m.setWorld(world);
            } catch (CannotChangeException ex) {
                throw new Error("This is the first time the world is set!");
            }
        }
    }

    /**
     * Executes the command by making and scheduling an appointment between the attendees given in the constructor
     * @see Hospital.Interfaces.Command#execute()
     */
    @Override
    public String execute() throws CannotDoException {
        if (appointment != null) {
            throw new CannotDoException("Appointment already planned");
        }
        try {
            AppointmentResult result = SolverAdapter.SINGLETON.solve(appointable.getCampusDecider(), constraints, groups, td);
            appointment = new Appointment(result, this, this.priority);

            Set<AppointmentCommand> preempted = getPreempted(appointment.getAttendees(), appointment);
            String s = undoPreempted(preempted);
            appointment.schedule();
            s += redoPreempted(preempted);

            appointable.setAppointment(appointment);
            s += next.execute();
            return s + appointment.toString();
        } catch (ScheduleGroupUnavailable ex) {
            throw new CannotDoException("Some ScheduleGroup had no available resources: " + ex.getMessage());
        } catch (SchedulingException ex) {
            throw new Error(ex);
            //throw new CannotDoException("All possible free resource-combinations throw schedulingException: " + ex.getMessage());
        }
    }

    /**
     * Removes the scheduled appointment from existence
     * @see Hospital.Interfaces.Command#undo()
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
     * @see Hospital.Interfaces.Command#isDone()
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

    /**
     * set a command to execute when the appointment is scheduled
     * @param next the next command to execute
     */
    public void setNext(Command next) {
        if (next == null) {
            next = NullCommand.singleton;
        }
        this.next = next;
    }

    private static String redoPreempted(Set<AppointmentCommand> preempted) {
        String s = "";
        for (AppointmentCommand appC : preempted) {
            //TODO: make this better!
            try {
                String temp = "";
                temp += "Redo: " + appC + "\n";
                temp += "\t" + appC.execute() + "\n";
                s += temp;
            } catch (CannotDoException ex) {
                s += "Error redoing: " + appC + "\n";
                s += "\t" + ex + "\n";
            }
        }
        return s;
    }

    private static String undoPreempted(Set<AppointmentCommand> preempted) {
        String s = "";
        for (AppointmentCommand appC : preempted) {
            //TODO: make this better!
            try {
                String temp;
                //temp = "Undo: " + appC + "\n";
                temp = appC.undo() + "\n";
                s += temp;
            } catch (CannotDoException ex) {
                s += "Error redoing: " + appC + "\n";
                s += "\t" + ex + "\n";
            }
        }
        return s;
    }

    private static Set<AppointmentCommand> getPreempted(List<Schedule> chosenSchedules, Appointment chosenTimeFrame) {
        Set<AppointmentCommand> preempted = new HashSet<AppointmentCommand>();
        for (Schedule s : chosenSchedules) {
            for (Appointment app : s.getCollidingAppointments(chosenTimeFrame)) {
                preempted.add(app.getAppCommand());
            }
        }
        return preempted;
    }
}
