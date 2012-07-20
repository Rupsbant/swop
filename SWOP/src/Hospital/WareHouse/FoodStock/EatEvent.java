package Hospital.WareHouse.FoodStock;

import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.PriorityQueue;

public class EatEvent implements Event {

    private FoodStock stock;
    private Time t;

    public EatEvent(FoodStock stock, Time t) {
        this.stock = stock;
        this.t = t;
    }

    public boolean doWork(PriorityQueue<Event> pq) {
        stock.patientsEat();
        return false;
    }

    public Time getTime() {
        return t;
    }

    public int compareTo(HasTime o) {
        return t.compareTo(o);
    }
}
