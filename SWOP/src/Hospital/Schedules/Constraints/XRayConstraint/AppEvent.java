package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.PriorityQueue;
import java.util.TreeMap;

abstract class AppEvent implements HasTime {

    private Time time;

    public AppEvent(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public int compareTo(HasTime o) {
        return time.compareTo(o.getTime());
    }

    abstract int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events);
}
