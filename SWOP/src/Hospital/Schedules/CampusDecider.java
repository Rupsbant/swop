package Hospital.Schedules;

import Hospital.Schedules.SchedulableVisitor;
import Hospital.World.Campus;

/**
 * Returns the campus the appointment will be scheduled.
 * @author SWOP-12
 */
public abstract class CampusDecider extends SchedulableVisitor {
    /**
     * Returns the campus of the appointment when all attendees have visited.
     * @return
     */
    public abstract Campus getCampus();
}
