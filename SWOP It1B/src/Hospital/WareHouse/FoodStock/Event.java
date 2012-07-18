package Hospital.WareHouse.FoodStock;

import Hospital.World.HasTime;
import java.util.PriorityQueue;

public interface Event extends HasTime {
    boolean doWork(PriorityQueue<Event> pq);
}
