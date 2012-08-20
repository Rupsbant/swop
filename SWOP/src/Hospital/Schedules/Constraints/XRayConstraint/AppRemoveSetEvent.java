package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import java.util.PriorityQueue;
import java.util.TreeMap;

class AppRemoveSetEvent extends AppEvent {

    private int countToRemove;

    public AppRemoveSetEvent(Time time, int countToRemove) {
        super(time);
        this.countToRemove = countToRemove;
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        int count = counter.get(countToRemove) - 1;
        if (count == 0) {
            counter.remove(countToRemove);
        } else {
            counter.put(countToRemove, count);
        }
        return old;
    }
}
