package Hospital.Schedules.ScheduleGroups;

import Hospital.Schedules.Schedulable;
import java.util.List;

/**
 * An interface for a group of schedules
 *
 */
public interface ScheduleGroup {

    /**
     * Returns a list of schedules that are available during the given TimeFrame.
     * @param tf TimeFrame to filter with.
     * @return List of free Schedules
     */
    public List<Schedulable> getSchedulables();
}
