package Hospital.WareHouse.FoodStock;

import Hospital.World.HasTime;
import Hospital.World.Time;
import java.util.PriorityQueue;

public class RestockEvent implements Event {

    private FoodStock stock;
    private Time t;

    public RestockEvent(FoodStock stock, Time t) {
        this.stock = stock;
        this.t = t;
    }

    public boolean doWork(PriorityQueue<Event> pq) {
        stock.restock();
        //t is always 23:59
        pq.add(new EatEvent(stock, t.getDiffTime(0, 0, 0, 8, 1)));
        pq.add(new EatEvent(stock, t.getDiffTime(0, 0, 0, 12, 1)));
        pq.add(new EatEvent(stock, t.getDiffTime(0, 0, 0, 18, 1)));
        pq.add(new RestockEvent(stock, t.getDiffTime(0, 0, 1, 0, 0)));
        return false;
    }

    public Time getTime() {
        return t;
    }

    public int compareTo(HasTime o) {
        return t.compareTo(o);
    }

}
