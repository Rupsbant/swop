package Hospital.WareHouse;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.WareHouse.OrderPlacers.OrderPlacer;
import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.PriorityQueue;

/**
 * A static class to simulate the time and check if all reservations are valid and no overstock or understock happens
 * @author Rupsbant
 */
public class StockConsistency {

    /**
     * Calculates the simulated future given the current knownledge of appointments
     * @param events The events that change the stock
     * @param items the current number of items in stock
     * @param order the current number of orders
     * @param maxStock the maximum stock
     * @param o the orderplacer for this stock
     * @return false if errors happen
     */
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
            throw new Error(ex);
        }
        return true;
    }

    private static Time getNewOrderArrivalTime(Time in) {
        return TimeUtils.addDay(TimeUtils.addDay(TimeUtils.copyDay(in, new Time(0, 0, 0, 6, 0))));

    }
}
