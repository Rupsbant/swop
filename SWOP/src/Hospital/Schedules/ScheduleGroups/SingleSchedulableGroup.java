package Hospital.Schedules.ScheduleGroups;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Schedules.Schedulable;
import java.util.Collections;
import java.util.List;

/**
 * A ScheduleGroup holding only one schedulable
 */
public class SingleSchedulableGroup implements ScheduleGroup {
    /**
     * The represented Schedulable
     */
    private Schedulable sched;

    /**
     * Constructor
     * @param p the Schedulable to represent
     * @throws ArgumentIsNullException p was null
     */
    public SingleSchedulableGroup(Schedulable p) throws ArgumentIsNullException {
        if (p == null) {
            throw new ArgumentIsNullException("Patient was null");
        }
        this.sched = p;
    }

    /**
     * @return the represented schedule as a list with one element
     */
    @Override
    public List<Schedulable> getSchedulables() {
        return Collections.singletonList(sched);
    }
}
