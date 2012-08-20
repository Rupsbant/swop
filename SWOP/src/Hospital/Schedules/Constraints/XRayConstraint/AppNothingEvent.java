package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import java.util.PriorityQueue;
import java.util.TreeMap;

class AppNothingEvent extends AppEvent {

    public AppNothingEvent(Time time) {
        super(time);
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        return old;
    }
}
