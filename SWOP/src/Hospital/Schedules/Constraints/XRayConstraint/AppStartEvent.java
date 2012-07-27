package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AppStartEvent extends AppEvent {

    public AppStartEvent(Time time, int plannedChange) {
        super(time, plannedChange);
    }

    private AppStartEvent(Time time, int planndedChange, int changeCount) {
        super(time, planndedChange, changeCount);
    }

    @Override
    int doEvent(int old, TreeMap<Integer, Integer> counter, PriorityQueue<AppEvent> events) {
        int eventOut = super.doEvent(old, counter, events);
        Time appOver = TimeUtils.getNextYear(getTime());
        appOver = TimeUtils.getStartOfDay(appOver).getDiffTime(0, 0, 0, 23, 59);
        events.add(new AppEndEvent(appOver, -getPlannedChange(), -1));
        return eventOut;
    }
}
