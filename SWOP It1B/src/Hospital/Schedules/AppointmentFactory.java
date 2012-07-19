package Hospital.Schedules;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Exception.Scheduling.ScheduleGroupUnavailable;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.ConstraintSolver.AppointmentConstraintSolver;
import Hospital.Schedules.ConstraintSolver.Solver;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.World.Campus;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

/**
 * Creates appointments
 */
public class AppointmentFactory {

    /**
     * Creates an appointment
     * @param tf the starting time and length of the appointment as TimeFrame
     * @param groups list of ScheduleGroups from which one of each group must attend the appointment
     * @return the created appointment
     * @throws SchedulingException one of the given schedules changed during the execution of this function
     * @throws ScheduleGroupUnavailable a ScheduleGroup a free schedule at the given TimeFrame was given to the function
     */
    static Appointment makeAppointment(TimeFrame tf, TimeFrameConstraint tfConstraints, List<ScheduleGroup> groups, AppointmentCommand appC) throws SchedulingException, ScheduleGroupUnavailable {
        AppointmentConstraintSolver solver = new Solver();
        solver.setConstaints(tfConstraints);
        solver.setFirstTimeFrame(tf);
        solver.setScheduleGroups(groups);
        solver.solve();
        Campus campus = solver.getCampus();
        List<Schedulable> chosen = solver.getAttendees();
        List<Schedule> chosenSchedules = Schedule.getSchedules(chosen);
        Appointment p = new Appointment(solver.getChosenTimeFrame(), chosenSchedules, appC, campus);
        return p;
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
