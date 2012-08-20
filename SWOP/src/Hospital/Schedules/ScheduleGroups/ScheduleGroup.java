package Hospital.Schedules.ScheduleGroups;

import Hospital.Schedules.Schedulable;
import java.util.List;

/**
 * An interface for a group of schedules
 */
public interface ScheduleGroup {

    /**
     * Returns a list of schedules that are available during the given TimeFrame.
     * @return List of Schedules in this group
     */
    public List<Schedulable> getSchedulables();
}
