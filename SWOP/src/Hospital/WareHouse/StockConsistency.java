package Hospital.WareHouse;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.WareHouse.OrderPlacers.OrderPlacer;
import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockConsistency {

    public static boolean evaluateEvents(PriorityQueue<StockChangeEvent> events, int items, int order, int maxStock, OrderPlacer o) {
        try {
            while (!events.isEmpty()) {
                StockChangeEvent e = events.poll();
                items = e.stockChange(items);
                order = e.orderChange(order);
                if (items < 0) {
                    return false;
                }
                int orderAmount = o.checkStock(items, order, maxStock);
                if (orderAmount != 0) {
                    order += orderAmount;
                    events.add(new Order(orderAmount, getNewOrderArrivalTime(e.getTime())));
                }
            }
        } catch (ArgumentConstraintException ex) {
            Logger.getLogger(StockConsistency.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private static Time getNewOrderArrivalTime(Time in) {
        return TimeUtils.addDay(TimeUtils.addDay(TimeUtils.copyDay(in, new Time(0, 0, 0, 6, 0))));

    }
}
