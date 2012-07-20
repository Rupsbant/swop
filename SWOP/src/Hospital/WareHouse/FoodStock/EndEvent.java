package Hospital.WareHouse.FoodStock;

import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.PriorityQueue;

public class EndEvent implements Event {

    private Time t;

    public EndEvent(Time t) {
        this.t = t;
    }

    public boolean doWork(PriorityQueue<Event> pq) {
        return true;
    }

    public Time getTime() {
        return t;
    }

    public int compareTo(HasTime o) {
        return t.compareTo(o);
    }
}
