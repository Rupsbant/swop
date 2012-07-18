package Hospital.WareHouse;

import java.util.ArrayList;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Warehouse.OrderUnavailableException;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * A list of orders
 */
public class OrderList {

    /**
     * The storage for the orders in this object
     */
    private ArrayList<Order> orders;

    /**
     * Constructor
     */
    public OrderList() {
        orders = new ArrayList<Order>();
    }

    /**
     * Adds an order to the list
     * @param amount the amount of items in the order
     * @param current the current time
     * @return the created Order-object that was added to the list
     * @throws ArgumentConstraintException amount was zero or less
     */
    public Order placeOrder(int amount, Time current) throws ArgumentConstraintException {
        if(!(amount>0)) throw new ArgumentConstraintException("Please enter an amount larger than 0 to place an order.");
        Time delivery = TimeUtils.getStartOfDay(current);
        delivery = delivery.getDiffTime(0, 0, 2, 6, 0);
        Order order = new Order(amount, delivery);
        orders.add(order);
        return order;
    }

    /**
     * Gets the list of orders
     * @return an ArrayList containing all the orders in this object
     */
    public ArrayList<Order> getOrders() {
        return (ArrayList<Order>) orders.clone();
    }

    /**
     * Gets the list of arrived orders
     * @return the list of orders that have already arrived
     */
    public ArrayList<Order> getArrivedOrders() {
        ArrayList<Order> out = new ArrayList<Order>();
        for (Order o : orders) {
            if (o.hasArrived()) {
                out.add(o);
            }
        }
        return out;
    }

    /**
     * Removes an order from this list
     * @param order the order to remove
     * @throws OrderUnavailableException the order was not part of this object
     */
    public void remove(Order order) throws OrderUnavailableException {
        if (!orders.remove(order)) {
            throw new OrderUnavailableException("The Order doesn't exist in this orderlist.");
        }
    }

    /**
     * Get the total amount of items from all orders in this object combined
     * @return the total amount of items
     */
    int totalItemsOrdered() {
        int out = 0;
        for (Order o : orders) {
            out += o.getAmount();
        }
        return out;
    }

    /**
     * Updates this orderlist to the given time
     * @param newTime the new time
     */
    void updateTime(Time newTime) {
        for (Order o : orders) {
            o.updateTime(newTime);
        }
    }
}
