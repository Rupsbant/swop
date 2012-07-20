package Hospital.Schedules.Constraints;

import Hospital.World.Campus;

/**
 * Returns the campus the appointment will be scheduled.
 * @author SWOP-12
 */
public interface GetCampusConstraint {
    /**
     * Returns the campus of the appointment when all attendees have visited.
     * @return
     */
    Campus getCampus();
}
