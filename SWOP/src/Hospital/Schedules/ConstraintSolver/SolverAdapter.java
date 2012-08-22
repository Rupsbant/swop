package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.CampusDecider;
import Hospital.Schedules.DelayedTimeLength;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.TimeFrameConstraint;
import java.util.List;

/**
 * Een adapter voor de AppointmentConstraintSolver
 */
public class SolverAdapter {

    /**
     * The singleton
     */
    public static final SolverAdapter SINGLETON  = new SolverAdapter();

    private SolverAdapter() {
    }

    /**
     * Solves an Appointment problem
     * @param campus The campusDecides, to decides which campus
     * @param constraints The list of all constraints
     * @param groups the list of groups that must attend
     * @param delayed the delayed time object, contains the first time and the length
     * @return a AppointmentResult, a solved object
     * @throws SchedulingException If no solution can be found.
     */
    public AppointmentResult solve(CampusDecider campus, List<TimeFrameConstraint> constraints, List<ScheduleGroup> groups, DelayedTimeLength delayed) throws SchedulingException {
        AppointmentConstraintSolver solver = new JumpSolver();
        solver.setCampusDecider(campus);
        solver.setConstaints(constraints);
        solver.setScheduleGroups(groups);
        solver.setDelayedTimeLength(delayed);
        solver.solve();
        return new AppointmentResult(solver.getAttendees(), solver.getCampus(), solver.getChosenTime(), delayed.getLength());
    }
}
