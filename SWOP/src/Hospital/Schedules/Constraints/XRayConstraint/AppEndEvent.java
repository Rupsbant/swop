package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AppEndEvent extends AppEvent {

    public AppEndEvent(Time time, int plannedChange) {
        super(time, plannedChange);
    }

    protected AppEndEvent(Time time, int planndedChange, int changeCount) {
        super(time, planndedChange, changeCount);
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        final int eventOut = super.doEvent(old, counter, events);

        return eventOut;
    }
}
