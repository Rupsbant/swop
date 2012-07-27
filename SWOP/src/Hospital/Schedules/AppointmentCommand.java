package Hospital.Schedules;

import Hospital.Argument.PriorityArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Schedules.ScheduleGroups.MultiScheduleGroup;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.Constraints.Priority.PriorityConstraint;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Scheduling.ScheduleGroupUnavailable;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Factory.Command;
import Hospital.Factory.NullCommand;
import Hospital.Schedules.ConstraintSolver.AppointmentConstraintSolver;
import Hospital.Schedules.ConstraintSolver.BruteForceSolver;
import Hospital.Utils;
import Hospital.World.World;
import Hospital.World.WorldTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A command to create and schedule appointments
 */
public class AppointmentCommand implements Command {

    public static PublicArgument[] getArguments() {
        return new PublicArgument[]{new PriorityArgument("Enter the priority of the appointment")};
    }

    private AppointmentConstraintSolver solver = new BruteForceSolver();
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
        
        List<ScheduleGroup> groups = new ArrayList<ScheduleGroup>();
        groups.addAll(app.getScheduleGroups());
        groups.addAll(coreSchedules);
        populateScheduleGroups(world, groups);
        solver.setScheduleGroups(groups);
        
        List<TimeFrameConstraint> temp  = app.getConstraints();
        temp.add(new PriorityConstraint(priority));
        solver.setConstaints(temp);
        solver.setCampusDecider(appointable.getCampusDecider());
        
        this.priority = priority;
        setTimeFrameDelay(app, world.getWorldTime());
    }

    private void populateScheduleGroups(World world, List<ScheduleGroup> g) throws ArgumentIsNullException, Error {
        List<MultiScheduleGroup> groups = Utils.filter(g, MultiScheduleGroup.class);
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
            solver.setFirstTimeFrame(td.getDelayedTimeFrame());
            solver.solve();
            List<Schedule> chosenSchedules = Schedule.getSchedules(solver.getAttendees());
            appointment = new Appointment(solver.getChosenTimeFrame(), chosenSchedules, this, solver.getCampus());
            
            Set<AppointmentCommand> preempted = getPreempted(appointment.getAttendees(), appointment.getTimeFrame());

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

    static String redoPreempted(Set<AppointmentCommand> preempted) {
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

    static String undoPreempted(Set<AppointmentCommand> preempted) {
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

    static Set<AppointmentCommand> getPreempted(List<Schedule> chosenSchedules, TimeFrame chosenTimeFrame) {
        Set<AppointmentCommand> preempted = new HashSet<AppointmentCommand>();
        for (Schedule s : chosenSchedules) {
            for (Appointment app : s.getCollidingAppointments(chosenTimeFrame)) {
                preempted.add(app.getAppCommand());
            }
        }
        return preempted;
    }
}
