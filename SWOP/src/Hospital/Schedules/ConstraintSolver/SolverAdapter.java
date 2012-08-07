package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.CampusDecider;
import Hospital.Schedules.DelayedTimeLength;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.TimeFrameConstraint;
import java.util.List;

public class SolverAdapter {

    public static final SolverAdapter SINGLETON  = new SolverAdapter();

    private SolverAdapter() {
    }

    public AppointmentResult solve(CampusDecider campus, List<TimeFrameConstraint> constraints, List<ScheduleGroup> groups, DelayedTimeLength delayed) throws SchedulingException {
        AppointmentConstraintSolver solver = new JumpSolver();
        solver.setCampusDecider(campus);
        solver.setConstaints(constraints);
        solver.setScheduleGroups(groups);
        solver.setTimeDelay(delayed);
        solver.solve();
        return new AppointmentResult(solver.getAttendees(), solver.getCampus(), solver.getChosenTime(), delayed.getLength());
    }
}
