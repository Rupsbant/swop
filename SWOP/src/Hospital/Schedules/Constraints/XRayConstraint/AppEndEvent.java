package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AppEndEvent extends AppEvent {

    private static final int CHANGE_DIRECTION = -1;
    private int plannedChange;
    private int doneCount;

    public AppEndEvent(Time time, int plannedChange, int doneCount) {
        super(time);
        this.plannedChange = plannedChange;
        this.doneCount = doneCount;
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        int newCount = old + plannedChange;
        Integer count = counter.get(doneCount);
        if (count == null) {
            count = 0;
        }
        counter.put(doneCount, count + CHANGE_DIRECTION);
        return newCount;
    }
}
