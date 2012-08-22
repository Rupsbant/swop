package Hospital.WareHouse;

import Hospital.World.HasTime;
import Hospital.World.Time;

public class ExpirationEvent implements StockChangeEvent {

    Time t;
    int items = 1;

    public ExpirationEvent(Time t) {
        this.t = t;
    }

    @Override
    public int stockChange(int old) {
        return old-items;
    }

    @Override
    public int orderChange(int old) {
        return old;
    }

    @Override
    public Time getTime() {
        return t;
    }

    @Override
    public int compareTo(HasTime o) {
        return t.compareTo(o);
    }
}
