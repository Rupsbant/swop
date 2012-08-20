package Hospital.WareHouse;

import Hospital.World.HasTime;
import Hospital.World.Time;

/**
 * An order from the stock-provider for a specific item
 */
public class Order implements StockChangeEvent {

    /**
     * the amount of items ordered
     */
    private int amount;
    /**
     * the time at which this batch of items expires
     */
    private Time expirationTime;
    /**
     * The time the shipment arrives
     */
    private Time arrival;
    /**
     * Boolean: whether the shipment has arrived
     */
    private boolean hasArrived;

    /**
     * Gets the amount of items in this order
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Constructor
     * @param amount the amount of items needed
     * @param arrival the time at which this order will arrive
     */
    public Order(int amount, Time arrival) {
        this.amount = amount;
        this.arrival = arrival;
    }

    /**
     * @return a string describing the amount of items in trhis order
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Order of amount: " + amount + ", arrives: " + arrival;
    }

    /**
     * Sets the time at which the items in this order expire
     * @param expirationTime the time to set
     */
    public void setExpirationTime(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * Gets the time at which the items in this order expire
     * @return the expiration date as Time-object
     */
    public Time getExpirationTime() {
        return expirationTime;
    }

    /**
     * Returns the arrival time.
     * @return the arrival time.
     */
    public boolean hasArrived() {
        return hasArrived;
    }

    /**
     * Updates this order to check whether is has arrived.
     * @param newTime The new Time in the system
     */
    void updateTime(Time newTime) {
        if (arrival.compareTo(newTime) <= 0) {
            setArrived();
        }
    }

    /**
     * Marks this order as received
     */
    private void setArrived() {
        hasArrived = true;
    }

    @Override
    public int stockChange(int old) {
        return old + amount;
    }

    @Override
    public int orderChange(int old) {
        return old - amount;
    }

    @Override
    public Time getTime() {
        return arrival;
    }

    @Override
    public int compareTo(HasTime o) {
        return arrival.compareTo(o);
    }
}
