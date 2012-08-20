package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.PriorityQueue;
import java.util.TreeMap;

class AppEndEvent extends AppEvent {

    private static final int CHANGE_DIRECTION = 1;
    private int plannedChange;

    public AppEndEvent(Time time, int plannedChange) {
        super(time);
        this.plannedChange = plannedChange;
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        int newCount = old - plannedChange;
        Integer nextCount = counter.get(newCount);
        nextCount = (nextCount == null ? 0 : nextCount);
        counter.put(newCount, nextCount+1);
        
        Time time = TimeUtils.getNextYear(getTime());
        events.add(new AppRemoveSetEvent(time, old));
        return newCount;
    }
}
