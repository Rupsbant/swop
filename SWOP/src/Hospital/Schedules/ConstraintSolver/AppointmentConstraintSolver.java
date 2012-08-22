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

/**
 * A interface for finding good appointments
 * @author Rupsbant
 */
public interface AppointmentConstraintSolver {

    void setDelayedTimeLength(DelayedTimeLength tf);

    void setScheduleGroups(List<ScheduleGroup> list);

    void setCampusDecider(CampusDecider campusDecider);

    void setConstaints(List<TimeFrameConstraint> tfConstraints);

    /**
     * Solves the problem with the given parameters by the set methods
     * @throws SchedulingException
     */
    void solve() throws SchedulingException;

    Campus getCampus();

    List<Schedulable> getAttendees();

    Time getChosenTime();

}
