package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.CampusDecider;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.DelayedTimeLength;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.List;

public interface AppointmentConstraintSolver {

    void setTimeDelay(DelayedTimeLength tf);

    void setScheduleGroups(List<ScheduleGroup> list);

    void setCampusDecider(CampusDecider campusDecider);

    /**
     * Linked list of all constraints.
     * @param tfConstraints
     */
    void setConstaints(List<TimeFrameConstraint> tfConstraints);

    AppointmentConstraintSolver solve() throws SchedulingException;

    Campus getCampus();

    List<Schedulable> getAttendees();

    Time getChosenTime();

}
