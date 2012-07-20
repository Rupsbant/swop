package Hospital.Exception.Scheduling;

/**
 * Thrown when an empty group of schedules is given to the scheduler.
 */
public class ScheduleGroupUnavailable extends SchedulingException {

    public ScheduleGroupUnavailable() {
    }

    public ScheduleGroupUnavailable(String str) {
        super(str);
    }
}
