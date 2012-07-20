package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.CampusDecider;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import java.util.List;

public interface AppointmentConstraintSolver {

    void setFirstTimeFrame(TimeFrame tf);

    void setScheduleGroups(List<ScheduleGroup> list);

    /**
     * Linked list of all constraints.
     * @param tfConstraints
     */
    void setConstaints(List<TimeFrameConstraint> tfConstraints);

    AppointmentConstraintSolver solve() throws SchedulingException;

    Campus getCampus();

    List<Schedulable> getAttendees();

    TimeFrame getChosenTimeFrame();

    void setCampusDecider(CampusDecider campusDecider);

}
