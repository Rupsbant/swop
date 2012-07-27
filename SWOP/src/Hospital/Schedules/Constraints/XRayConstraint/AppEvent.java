package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AppEvent implements HasTime {

    private Time time;
    private int plannedChange;
    private int changeCount;

    public AppEvent(Time time, int plannedChange) {
        this.time = time;
        this.plannedChange = plannedChange;
        this.changeCount = 1;
    }

    AppEvent(Time time, int planndedChange, int changeCount) {
        this(time, planndedChange);
        this.changeCount = changeCount;
    }

    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        int newC = old + plannedChange;
        Integer count = counter.get(old);
        if (count == null) {
            count = 0;
        }
        counter.put(old, count + getChangeCount());
        return newC;
    }

    public Time getTime() {
        return time;
    }

    public int compareTo(HasTime o) {
        return time.compareTo(o.getTime());
    }

    public int getPlannedChange() {
        return plannedChange;
    }

    public int getChangeCount() {
        return changeCount;
    }
}
