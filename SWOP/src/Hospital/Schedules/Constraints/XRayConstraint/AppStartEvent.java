package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AppStartEvent extends AppEvent {
    private final static int CHANGE_DIRECTION = 1;
    private int plannedChange;

    public AppStartEvent(Time time, int plannedChange) {
        super(time);
        this.plannedChange = plannedChange;
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) { 
        int newCount = old + plannedChange;
        Integer count = counter.get(newCount);
        if (count == null) {
            count = 0;
        }
        counter.put(newCount, count + CHANGE_DIRECTION);
        
        Time appOver = TimeUtils.getNextYear(getTime());
        appOver = TimeUtils.getStartOfDay(appOver).getDiffTime(0, 0, 0, 23, 59);
        events.add(new AppEndEvent(appOver, -plannedChange, newCount));
        return newCount;
    }
}
