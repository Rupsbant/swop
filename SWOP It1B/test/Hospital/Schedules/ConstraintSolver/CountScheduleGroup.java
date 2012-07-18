package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Schedulable;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import java.util.ArrayList;
import java.util.List;

public class CountScheduleGroup implements ScheduleGroup {

    List<Schedulable> schedulables = new ArrayList<Schedulable>();

    void addSchedulable(Schedulable schedulable) {
        schedulables.add(schedulable);
    }

    public List<Schedulable> getSchedulables() {
        return schedulables;
    }
}
